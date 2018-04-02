package com.xunku.app.monitor;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.enums.MonitorLandStatus;
import com.xunku.app.interfaces.IRefreshStrategy;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.FansResult;
import com.xunku.app.result.MAccountTrend;
import com.xunku.app.stores.MAccountStore;
import com.xunku.app.strategy.DefaultMonitorStrategy;
import com.xunku.dao.monitor.MAccountDao;
import com.xunku.daoImpl.monitor.MAccountDaoImpl;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;

/**
 * 监测帐号管理器，负责帐号分析
 * 
 * @author wujian
 * @created on Jun 18, 2014 6:20:57 PM
 */
public class TweetAccountManager extends MonitorManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(TweetAccountManager.class);

	public MonitorLandStatus landing(int monitorId, User user) {
		LOG.info("帐号分析->监测对象:{}的数据开始落地！", monitorId);

		AccountMonitor monitor = this.getMAccountMonitor(monitorId);
		monitor.landing(user);
		return MonitorLandStatus.finished;
	}

	protected static TweetAccountManager _manager;

	public synchronized static TweetAccountManager getInstance(
			AppContext context) {
		if (_manager == null) {
			_manager = new TweetAccountManager(context);
		}
		return _manager;
	}

	IRefreshStrategy<AccountMonitor> _strategy = new DefaultMonitorStrategy<AccountMonitor>();

	private TweetAccountManager(AppContext context) {
		super(context);
	}

	private MAccountStore getMAccountDB(IMonitor monitor) {
		return (MAccountStore) super.getDB(monitor);
	}

	public AccountMonitor getMAccountMonitor(int monitorId) {
		MAccountDao dao = new MAccountDaoImpl();
		IMonitor monitor = dao.queryMAccountByID(monitorId);
		AccountMonitor m = (AccountMonitor) monitor;
		return m;
	}

	private MAccountStore getStore(int id) {
		AccountMonitor monitor = this.getMAccountMonitor(id);

		MAccountStore store = (MAccountStore) monitor.getStore(_context);

		return store;
	}

	public MAccountTrend viewTrend(int id, Date start, Date end) {
		return this.getStore(id).queryTrend(start, end);
	}

	public MAccountTrend viewTrendToday(int id, Date start, Date end,
			int radiovalue) {
		return this.getStore(id).queryTrendToday(start, end, radiovalue);
	}

	public int[] viewRealtime(int id, Date start, Date end) {

		return this.getStore(id).queryRealtime(start, end);
	}

	public FansResult viewFans(int id) {

		return this.getStore(id).queryFans(_context);
	}

	public List<ITweet> viewHotTweets(int id) {
		return this.getStore(id).queryHotTweetsTop10();
	}

	/**
	 * 获得监测帐号的微博列表
	 * 
	 * @param account
	 * @param startDate
	 * @param endDate
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> TweetsGetByMAccount(AccountMonitor account,
			Date startDate, Date endDate, Pager pager) {

		MAccountStore db = this.getMAccountDB(account);

		return db.TweetsGetByMAccount(startDate, endDate, pager);
	}

	/**
	 * 获得指定帐号的微博信息 此方法在对应的数据表里没有数据的时候调用
	 * <p>
	 * 该方法可以提供给UI刷新使用，UI调用可以用该方法刷新一下对应的数据表
	 * <p>
	 * 如果有数据，则以后的数据都通过JOB由讯库提供
	 * 
	 * @param uid
	 * @param paltform
	 * @return
	 */
	public Pagefile<ITweet> TweetsGetByMAccount4RT(AccountMonitor account,
			Date startDate, Date endDate, Pager pager, User user) {

		// 先调用落地方法
		this.landing(account.getMonitorId(), user);

		// 返回结果
		return this.TweetsGetByMAccount(account, startDate, endDate, pager);

	}

	/**
	 * 获得指定范围内的微博详情
	 * 
	 * @param accoundid
	 * @param start
	 * @param end
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> tweetsRealtimeDetail(String text, int accoundid,
			Date start, Date end, Pager pager) {

		AccountMonitor account = this.getMAccountMonitor(accoundid);

		MAccountStore db = this.getMAccountDB(account);

		return db.queryRealtimeDetail(text, start, end, pager);
	}
}
