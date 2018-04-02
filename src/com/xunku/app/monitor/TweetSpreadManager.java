package com.xunku.app.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.db.AccountDB;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.SortKeyMan;
import com.xunku.app.helpers.SourceHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.AppAuth;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.Result;
import com.xunku.app.result.SpreadResult;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.spread.RetweetLevelResult;
import com.xunku.app.result.spread.RetweetStatisResult;
import com.xunku.app.stores.MSpreadStore;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;

/**
 * 微博监测库管理器，负责传播分析
 * 
 * @author wujian
 * @created on Jun 18, 2014 6:51:02 PM
 */
public class TweetSpreadManager extends MonitorManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(TweetSpreadManager.class);

	protected static TweetSpreadManager _manager;

	public synchronized static TweetSpreadManager getInstance(AppContext context) {
		if (_manager == null) {
			_manager = new TweetSpreadManager(context);
		}
		return _manager;
	}

	private TweetSpreadManager(AppContext context) {
		super(context);
	}

	public Map<String, Float> viewHotwords(int monitorid) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorid);
		MSpreadStore db = this.getSpreadDB(monitor);
		return db.queryHotWord();
	}

	/**
	 * 理解将已经分析的状态设置为false，JOB会再次分析这个传播
	 * 
	 * @param monitorId
	 */
	public void anaylsisSpreadNow(int monitorId) {
		MWeiBoDao dao = new MWeiBoDaoImpl();
		dao.updateMWeiboById(monitorId, false);
	}

	public void landing(WeiboMonitor monitor, User user) {
		String tid = monitor.getTid();
		Platform platform = monitor.getPlatform();
		Result<ITweet> post = this._context.getMOfficialManager()
				.tweetGetByTid(tid, platform, user);

		if (post.getErrCode() == 0) {
			this.landing(monitor, user, post.getData());
		} else {
			LOG.info(post.getMessage());
		}
		new MWeiBoDaoImpl().updateMWeiboById(monitor.getMonitorId(), true);
	}

	public void landing(int monitorId, User user, ITweet post) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);
		if (monitor != null) {
			this.landing(monitor, user, post);
		}
	}

	public static void main(String[] args) {

		AppContext context = AppContext.getInstance();
		context.init();

		User user = new UserDaoImpl().queryAdmin(10);

		WeiboMonitor c = new MWeiBoDaoImpl().queryMWeiboById(155);

		context.getMWeiboManager().landing(c, user);
	}

	/**
	 * 数据落地
	 * 
	 * @param monitorId
	 * @param user
	 * @param post
	 */
	public void landing(WeiboMonitor monitor, User user, ITweet post) {

		LOG.info("传播分析->监测对象:{}的数据开始落地！", monitor.getTid());

		if (monitor == null) {
			LOG.info("监测对象为空，无法落地数据，请检查后重试！");
			return;
		}

		MSpreadStore db = this.getSpreadDB(monitor);
		if (db == null) {
			LOG.info("没找到要落地的监测项！");
			return;
		}

		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				monitor.getPlatform());

		if (auth == null) {
			LOG.info("没找到当前用户可用的授权，无法落地数据！");
			return;
		}

		AccountDB accountdb = this.getAccountManager().getDB(
				monitor.getPlatform());

		if (post != null) {
			accountdb.updateTweetAccount(post);
		}
		monitor.landing(accountdb, db, auth, post);
	}

	/**
	 * 返回监测对象年龄比例
	 * 
	 * @param monitor
	 *            监测对象
	 * @param isComment
	 *            传true返回回复趋势，传false返回转发趋势
	 */
	public Map<String, Integer> viewAge(int monitorId, boolean isComment) {
		// 系统暂时不做年龄分析
		// 如果要做步骤如下：
		// 1、添加Weibo_Age_Count表
		// 2、在入库是注册年龄处理器
		// 3、查询Weibo_Age_Count表即可获得年龄统计信息
		// 4、年龄统计信息如何分组是一个问题
		// Map<年龄分组,分组里的人数>

		return null;
	}

	/**
	 * 返回监测对象的粉丝比例
	 * 
	 * @param monitor
	 *            监测对象
	 * @param type
	 *            转发/回复
	 */
	public int[] viewFansHistogram(int monitorId, PostType type) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);
		MSpreadStore db = this.getSpreadDB(monitor);
		return db.queryFansCount(type);
	}

	/**
	 * 返回转发态度统计结果[1-100，100-1000,1000-10000,...]数组里面一共5个元素对应每个等级
	 * 
	 * @param monitorId
	 * @return
	 */
	public double[] viewRetweetAttitude(int monitorId) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);
		MSpreadStore db = this.getSpreadDB(monitor);
		return db.queryRetweetWithText();
	}

	/**
	 * 返回粉丝的性别比例
	 * 
	 * @param monitor
	 *            监测对象
	 * @param isComment
	 *            传true返回回复趋势，传false返回转发趋势
	 */
	public GenderResult viewGender(int monitorId, PostType type) {

		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);

		MSpreadStore db = this.getSpreadDB(monitor);

		Map<GenderEnum, Integer> map = db.queryGender(type);
		GenderResult result = new GenderResult();
		if (map.containsKey(GenderEnum.Famale)) {
			result.setFemales(map.get(GenderEnum.Famale));
		}
		if (map.containsKey(GenderEnum.Male))
			result.setMales(map.get(GenderEnum.Male));
		if (map.containsKey(GenderEnum.Unknow))
			result.setOthers(map.get(GenderEnum.Unknow));
		return result;
	}

	/**
	 * 返回粉丝的区域分布
	 * 
	 * @param monitor
	 *            监测对象
	 * @param isComment
	 *            传true返回回复趋势，传false返回转发趋势
	 */
	public Map<String, Integer> viewLocation(int monitorId, PostType type) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);

		MSpreadStore db = this.getSpreadDB(monitor);
		Map<String, Integer> result = new HashMap<String, Integer>();
		Map<String, Integer> cities = db.queryCity(type, monitor.getPlatform());

		// 转换后显示用
		for (Map.Entry<String, Integer> e : cities.entrySet()) {
			result.put(e.getKey(), e.getValue());
		}

		return result;
	}

	/**
	 * 返回回复/转发数
	 * 
	 * @param monitor
	 *            监测对象
	 * @param isComment
	 *            传true返回回复趋势，传false返回转发趋势
	 */
	public int viewPersonNum(int monitorId, PostType type) {

		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);
		MSpreadStore db = this.getSpreadDB(monitor);

		return db.count(type);
	}

	/**
	 * 返回大号回复/转发
	 * 
	 * @param monitor
	 *            监测对象
	 * @param isComment
	 *            传true返回回复趋势，传false返回转发趋势
	 */
	public void viewSuperMan(int monitorId, PostType type) {

	}

	/**
	 * 分析来源
	 * 
	 * @param monitor
	 * @param isComment
	 */
	public Map<String, Integer> viewFromsNum(int monitorId, PostType type) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);

		MSpreadStore db = this.getSpreadDB(monitor);

		Map<Integer, Integer> formSet = db.queryFrom(type);

		Map<String, Integer> result = new HashMap<String, Integer>();

		for (Map.Entry<Integer, Integer> e : formSet.entrySet()) {
			result.put(SourceHelper.getSource(e.getKey()).getName(), e
					.getValue());
		}
		return result;
	}

	/**
	 * 认证比例
	 * 
	 * @param monitor
	 * @param isComment
	 * @return
	 */
	public VipResult viewVipNum(int monitorId, PostType type) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);

		MSpreadStore db = this.getSpreadDB(monitor);

		return db.queryVip(type);
	}

	/**
	 * 返回该对象的转发层级
	 * 
	 * @param monitor
	 * @param isComment
	 */
	public List<RetweetLevelResult> viewRetweetLevel(int monitorId) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);

		MSpreadStore db = this.getSpreadDB(monitor);

		int reposts = monitor.getReposts();

		return db.queryRetweetLevel(monitor.getPlatform(), reposts);

	}

	/**
	 * 返回监测对象的趋势分析结果
	 * 
	 * @param monitor
	 *            监测对象
	 * @param isComment
	 *            传true返回回复趋势，传false返回转发趋势
	 * @return
	 */
	public Map<Long, Integer> viewTimeTrend(int monitorId, PostType type) {

		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);

		MSpreadStore db = this.getSpreadDB(monitor);

		return db.queryTrend(type);

	}

	/**
	 * 获得已传播的关键用户
	 * 
	 * @param tid
	 * @param platform
	 */
	public List<RetweetStatisResult> viewKeyMrDissGet(int monitorId,
			SortKeyMan sort) {
		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);
		List<RetweetStatisResult> result = null;
		if (sort == SortKeyMan.Retweet) {
			result = this.getSpreadDB(monitor).querySpreadedKeyManByNums();
		} else if (sort == SortKeyMan.Fans) {
			result = this.getSpreadDB(monitor).querySpreadedKeyManByFans();
		}

		return result;
	}

	/**
	 * 获得未传播的关键用户
	 * 
	 * @param tid
	 * @param platform
	 */
	public List<RetweetStatisResult> viewKeyMrUnDissGet(int monitorId,
			SortKeyMan sort) {

		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);

		if (sort == SortKeyMan.Vip) {
			return this.getSpreadDB(monitor).queryUnspreadKeyMan(true);
		} else {
			return this.getSpreadDB(monitor).queryUnspreadKeyMan(false);
		}
	}

	/**
	 * 获得监测微博的转发列表
	 * 
	 * @param tid
	 * @param platform
	 * @return
	 */
	public Pagefile<ITweet> retweetsGet(WeiboMonitor monitor, Pager pager) {
		MSpreadStore db = getMonitorDB(monitor);
		return db.retweetsGet(monitor.getTid(), monitor.getPlatform(), pager);
	}

	/**
	 * 获得监测微博的评论列表
	 * 
	 * @param tid
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> commentsGet(WeiboMonitor monitor, Pager pager) {
		MSpreadStore db = getMonitorDB(monitor);
		return db.commentsGet(monitor.getTid(), monitor.getPlatform(), pager);
	}

	/**
	 * 根据url监测推特的信息，实时数据
	 * 
	 * @param url
	 * @return
	 */
	public Result<ITweet> tweetGetOnline(String url, User user) {

		return this._context.getMOfficialManager().tweetGetByUrl(url, user);
	}

	/**
	 * 通过监测项获得检测的微博
	 * 
	 * @param monitorid
	 * @return
	 */
	public ITweet tweetGetByMonitorID(int monitorId) {

		WeiboMonitor monitor = this.getWeiboMonitor(monitorId);
		MSpreadStore db = this.getMonitorDB(monitor);

		// 这是个愚蠢的解决方法，有时间要检查到底为什么存储里面的转发和评论会被清零

		ITweet tweet = monitor.tweetGet(db);

		return tweet;
	}

	/**
	 * 立即分析
	 * 
	 * @param posts
	 *            要分析的微博
	 * @param anew
	 *            是否重新分析
	 * @return
	 */
	public SpreadResult getResult(User user, int monitorId) {
		SpreadResult result = new SpreadResult();

		// 查询数据库组装传播分析结果

		return result;
	}

	// ========================================================================

	/**
	 * 获得当前监视项的存储
	 * 
	 * @param tid
	 * @param platform
	 * @return
	 */
	private MSpreadStore getMonitorDB(WeiboMonitor monitor) {
		// 得到监视项对应的DB
		MSpreadStore db = (MSpreadStore) monitor.getStore(_context);
		return db;
	}

	private MSpreadStore getSpreadDB(IMonitor monitor) {
		return (MSpreadStore) monitor.getStore(_context);
	}

	private WeiboMonitor getWeiboMonitor(int monitorId) {
		MWeiBoDao dao = new MWeiBoDaoImpl();
		WeiboMonitor m = dao.queryMWeiboById(monitorId);
		return m;
	}
}
