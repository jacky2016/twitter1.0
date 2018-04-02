package com.xunku.app.monitor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Fetcher;
import com.xunku.app.Utility;
import com.xunku.app.db.AccountDB;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.MonitorLandStatus;
import com.xunku.app.enums.Platform;
import com.xunku.app.handlers.spreads.SRetweetAttitudeHandler;
import com.xunku.app.handlers.spreads.SRetweetHandler;
import com.xunku.app.handlers.spreads.SRetweetLevelHandler;
import com.xunku.app.handlers.spreads.STrendHandler;
import com.xunku.app.handlers.spreads.SUserFansHandler;
import com.xunku.app.handlers.spreads.SUserVipHandler;
import com.xunku.app.handlers.spreads.SUserXFromHandler;
import com.xunku.app.handlers.spreads.SUsersCityHanlder;
import com.xunku.app.handlers.spreads.SUsersGenderHandler;
import com.xunku.app.handlers.spreads.SUsersHistogramHandler;
import com.xunku.app.handlers.spreads.SUsersLocationHandler;
import com.xunku.app.handlers.spreads.SUsersTagHandler;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.interfaces.MonitorType;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.MSpreadStore;
import com.xunku.app.stores.TweetStore;
import com.xunku.constant.PortalCST;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;

//微博监测by wanghui
/**
 * M=Monitor
 * <p>
 * 微博监测对象Monitor Weibo
 * 
 * @author wujian
 * @created on Jun 9, 2014 2:43:32 PM
 */
public class WeiboMonitor implements IMonitor {

	private static final Logger LOG = LoggerFactory
			.getLogger(WeiboMonitor.class);

	public WeiboMonitor() {
		this.landStatus = MonitorLandStatus.finished;
	}

	MonitorLandStatus landStatus;

	public String getTaskToken(AppAccount acc, int crawlType) {
		// 2+2+2+16+13+id
		return MonitorType.WeiboMonitor
				+ Utility.getPlatform(this.getPlatform()) + crawlType + "_"
				+ this.getMonitorId() + "_" + DateHelper.getTimezonHour();
	}

	@Override
	public int getMonitorType() {
		return 4;
	}

	ITweetStore store;

	@Override
	public ITweetStore getStore(AppContext context) {
		if (store == null) {
			Pooling pool = context.getPooling(this.getPooling(), true);
			AccountManager manager = AccountManager.getInstance(context);
			store = new MSpreadStore(this, pool, manager);

			store.RegHandler(new SRetweetHandler());
			store.RegHandler(new SRetweetLevelHandler());
			store.RegHandler(new STrendHandler());
			store.RegHandler(new SUsersCityHanlder());
			store.RegHandler(new SUsersLocationHandler());
			store.RegHandler(new SUsersGenderHandler());
			store.RegHandler(new SUsersHistogramHandler());
			store.RegHandler(new SUsersTagHandler());
			store.RegHandler(new SUserVipHandler());
			store.RegHandler(new SUserXFromHandler());
			store.RegHandler(new SRetweetAttitudeHandler());
			store.RegHandler(new SUserFansHandler());
		}
		return store;
	}

	@Override
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * 通过post获得监测项
	 * 
	 * @param post
	 * @param userid
	 * @return
	 */
	public static WeiboMonitor create(ITweet post, int customid) {
		WeiboMonitor m = new WeiboMonitor();
		m.comments = post.getComments();
		m.anayTime = new Date();
		m.platform = post.getPlatform();
		m.reposts = post.getReposts();
		m.text = post.getText();
		m.tid = post.getTid();
		if (post.getAuthor() != null) {
			m.ucode = post.getAuthor().getUcode();
		}
		m.url = Utility.getPostUrl(post);
		m.customid = customid;
		m.publishTime = new Date(post.getCreated());

		m.analysised = false;
		m.tries = 0;
		// 创建一个新的存储并且插入当前的记录
		// m.getStore(AppContext.getInstance()).put(post);
		return m;
	}

	public ITweet tweetGet(TweetStore db) {

		if (db == null)
			return null;
		ITweet post = db.executePostQuery(tid, platform);

		return post;
	}

	// 重新分析这里不应该指定SinceID，否则无法获得全面的数据，导致统计不准确
	public void landing(AccountDB accDB, ITweetStore store, AppAuth auth,
			ITweet post) {

		if (this.landStatus == MonitorLandStatus.landing) {
			LOG.info("当前监测项正在落地数据，现在无法再次落地，请稍候再试！");
			return;
		}

		this.landStatus = MonitorLandStatus.landing;

		if (store == null) {
			LOG.info("没有监测项的存储，无法落地！");
			this.landStatus = MonitorLandStatus.finished;
			return;
		}

		MSpreadStore db = (MSpreadStore) store;

		// ITweet post = auth.PostGet(this.getTid());

		if (post == null) {
			LOG.info("无法获得监测项的微博信息<微博可能不存在>，无法落地该监测项！");
			this.landStatus = MonitorLandStatus.finished;
			return;
		}

		if (post != null) {

			try {
				LOG.info("开始获得{}的转发列表！", post.getTid());
				new Fetcher().fetchRetweetList(db, post, auth);
			} catch (ApiException ex) {
				LOG.error("爬取转发列表失败", ex);
			}

			try {
				LOG.info("开始获得{}的评论列表！", post.getTid());
				new Fetcher().fetchCommentList(db, post, auth);
			} catch (ApiException ex) {
				LOG.error("爬取评论列表失败", ex);
			}

			// 这里如果是评论获得的原始贴的回复数和转发数都是0，所以会更新post的数据
			this.reposts = post.getReposts();
			this.comments = post.getComments();

			MWeiBoDao dao = new MWeiBoDaoImpl();
			dao.insert(this);
			db.put(post);
		}

		this.timestamp = System.currentTimeMillis();

		// 更新分析状态
		new MWeiBoDaoImpl().updateMWeiboById(this.id, true);
		this.landStatus = MonitorLandStatus.finished;

	}

	@Override
	public String getToken() {
		return PortalCST.MONITOR_WEIBO_PERFIX + this.getMonitorId();
	}

	private long timestamp;

	private int id;
	private String tid; // 微博id
	private int customid; // 客户id
	private String url; // url
	private String ucode; // ucode
	private String text; // 微博内容
	private int reposts; // 转发数
	private int comments; // 评论数
	private Date anayTime;
	private Platform platform; // 平台 1新浪2腾讯5人民
	private String dbserver; // server
	private Date publishTime;// 发布时间

	int poolId;
	/**
	 * 监测微博的ID
	 */
	@Override
	public int getMonitorId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getReposts() {
		return reposts;
	}

	public void setReposts(int reposts) {
		this.reposts = reposts;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public Date getAnayTime() {
		return anayTime;
	}

	public void setAnayTime(Date anayTime) {
		this.anayTime = anayTime;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getPooling() {
		return dbserver;
	}

	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}

	public String getTableName() {
		return PortalCST.MONITOR_WEIBO_PERFIX + this.id;
	}

	public void setTable(String table) {
	}

	public int getCustomid() {
		return customid;
	}

	public void setCustomid(int customid) {
		this.customid = customid;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	boolean analysised;

	public void setAnalysised(boolean analysised) {
		this.analysised = analysised;
	}

	// true: 正在分析
	public boolean isAnalysised() {
		return this.analysised;
	}

	int tries;

	public int getTries() {
		return tries;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}

	public int getPoolId() {
		return poolId;
	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}

}
