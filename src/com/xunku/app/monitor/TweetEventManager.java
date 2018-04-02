package com.xunku.app.monitor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.db.AccountDB;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IRefreshStrategy;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.RetweetResult;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.event.MEventContentResult;
import com.xunku.app.result.event.MEventCountResult;
import com.xunku.app.result.event.MEventUserRegResult;
import com.xunku.app.result.event.MEventUserResult;
import com.xunku.app.stores.MEventStore;
import com.xunku.app.strategy.DefaultMonitorStrategy;
import com.xunku.constant.FiltrateEnum;
import com.xunku.constant.TimeSortEnum;
import com.xunku.dao.base.TermDao;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.base.TermDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.Term;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;

/**
 * 事件监测管理器，负责事件分析
 * 
 * @author wujian
 * @created on Jun 13, 2014 2:04:53 PM
 */
public class TweetEventManager extends MonitorManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(TweetEventManager.class);

	TermDao _termDAO = new TermDaoImpl();
	EventDao _eventDAO = new EventDaoImpl();

	IRefreshStrategy<AccountMonitor> _strategy = new DefaultMonitorStrategy<AccountMonitor>();

	protected static TweetEventManager _manager;

	public synchronized static TweetEventManager getInstance(AppContext context) {
		if (_manager == null) {
			_manager = new TweetEventManager(context);
		}
		return _manager;
	}

	private TweetEventManager(AppContext context) {
		super(context);
	}

	// ==========================================

	public void initTerms(IMonitor monitor) {
		EventMonitor event = (EventMonitor) monitor;

		// keywords一定有客户端保证
		String[] terms = event.getKeywords().split(",");

		// 先删除对应token的内容，然后再插入
		this._termDAO.deleteByToken(monitor.getToken());

		for (String term : terms) {
			Term t = new Term();
			t.setPlatform(event.getPlatform());
			t.setToken(event.getToken());
			t.setTerm(term);
			this._termDAO.insert(t);
		}
	}

	/**
	 * 获得当前事件监测对应的数据库
	 * 
	 * @param m
	 * @return
	 */
	public MEventStore getEventDB(IMonitor m) {
		return (MEventStore) this.getDB(m);
	}

	private EventMonitor getEventMonitor(int monitorID) {

		EventDao dao = new EventDaoImpl();

		IMonitor monitor = dao.queryEventByEId(monitorID);

		EventMonitor em = (EventMonitor) monitor;

		return em;

	}

	public void landing(int monitorID, User user) {
		LOG.info("事件分析->监测对象:{}的数据开始落地！", monitorID);
		EventMonitor event = _eventDAO.queryEventByEId(monitorID);
		this.getEventMonitor(monitorID);
		if (event == null) {
			return;
		}

		AccountDB db = this.getAccountManager().getDB(event.getPlatform());
		event.landing(db, this.getDB(event), null);

	}

	/**
	 * 通过事件获得事件对应的微博列表
	 * 
	 * @param event
	 * @param query
	 * @param startDate
	 * @param endDate
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> tweetsGetByMEvent(EventMonitor event, String text,
			Date startDate, Date endDate, FiltrateEnum filter,
			TimeSortEnum sort, Pager pager) {

		MEventStore db = this.getEventDB(event);

		return db.queryTweetGetByEvent(text, startDate, endDate, filter, sort,
				pager);
	}

	/**
	 * 获得指定事件的基本数据分析，今天、昨天、全部
	 * 
	 * @param event
	 * @return
	 */
	public Map<Integer, MEventCountResult> viewCounts(List<EventMonitor> events) {
		Map<Integer, MEventCountResult> map = new HashMap<Integer, MEventCountResult>();
		Calendar cs = Calendar.getInstance();
		Calendar ce = Calendar.getInstance();
		for (EventMonitor e : events) {
			MEventCountResult result = new MEventCountResult();
			MEventStore db = this.getEventDB(e);
			long st = DateHelper.formatTodayFirst();
			long et = DateHelper.formatTodayLast();
			result.setToday(db.count(st, et));
			cs.setTime(e.getStartTime());
			ce.setTime(e.getEndTime());
			// 如果只监测了一天，则昨天显示的是0
//			if (cs.get(Calendar.DAY_OF_YEAR) == ce.get(Calendar.DAY_OF_YEAR)
//					&& cs.get(Calendar.MONTH) == ce.get(Calendar.MONTH)
//					&& cs.get(Calendar.DATE) == ce.get(Calendar.DATE)) {
//				result.setYesterday(0);
//			} else {
				st = DateHelper.formatYesterdayFirst();
				et = DateHelper.formatYesterdayLast();
				result.setYesterday(db.count(st, et));
			//}
			st = e.getStartTime().getTime();
			et = e.getEndTime().getTime();
			result.setAll(db.count(st, et));
			map.put(e.getMonitorId(), result);
		}
		return map;
	}

	/**
	 * 趋势分析，如果开始时间和结束时间一样则返回当前的按小时趋势
	 * <p>
	 * 该统计包含时间区域内的帖子id供显示详细信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @param event
	 */
	public Map<Long, List<String>> viewTrendStatDetail(Date startTime,
			Date endTime, EventMonitor event) {

		MEventStore db = this.getEventDB(event);

		return db.queryTrendDetail(startTime, endTime);
	}

	/**
	 * 趋势分析，如果开始时间和结束时间一样则返回当前的按小时趋势
	 * <p>
	 * 该统计不包含详细信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @param event
	 * @return
	 */
	public Map<Long, Integer> viewTrendStatByHour(Date startTime, Date endTime,
			EventMonitor event) {

		MEventStore db = this.getEventDB(event);

		return db.queryTrendByHour(startTime.getTime(), endTime.getTime());
	}

	/**
	 * 趋势分析，按天分析
	 * 
	 * @param startTime
	 * @param endTime
	 * @param event
	 * @return
	 */
	public Map<Long, Integer> viewTrendStatByDate(Date startTime, Date endTime,
			EventMonitor event) {

		MEventStore db = this.getEventDB(event);

		return db.queryTrendByDate(startTime.getTime(), endTime.getTime());
	}

	/**
	 * 内容分析，获得事件的内容分析数据
	 * 
	 * @param event
	 * @return
	 */
	public MEventContentResult viewContentStat(EventMonitor event) {
		MEventContentResult result = new MEventContentResult();
		result.setHotwords(this.viewHotwords(event));
		result.setRetweetCnt(this.viewRetweetCnt(event));
		result.setSuperRetweet(this.viewSuperTweet(event));
		return result;
	}

	/**
	 * 返回转发和评论的比例，这里返回的是转发\原发\评论的的数量， 具体比例客户端计算 by wanghui
	 * 
	 * @return
	 */
	public RetweetResult viewRetweetCnt(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.queryOriginalReposts(event);
	}

	/**
	 * 返回热门关键词 by wanghui
	 * 
	 * @return
	 */
	public Map<String, Float> viewHotwords(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.queryHotWord();
	}

	/**
	 * 返回关键观点 by wanghui
	 * 
	 * @return
	 */
	public Map<ITweet, Integer> viewSuperTweet(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.queryKeyPoint(event.getStartTime().getTime(), event
				.getEndTime().getTime());
	}

	/**
	 * 
	 * 用户分析
	 * 
	 * @param event
	 * @return
	 */
	public MEventUserResult viewFansStat(EventMonitor event) {
		return null;
	}

	/**
	 * 性别分布 by wanghui
	 * 
	 * @return
	 */
	public GenderResult viewGrender(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.querySexScale();
	}

	/**
	 * 认证比例 by wanghui
	 * 
	 * @return
	 */
	public VipResult viewVip(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.queryVIPScale();
	}

	/**
	 * 来源分析 by wanghui
	 * 
	 * @return
	 */
	public Map<String, Integer> viewForms(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.queryEventSource();
	}

	/**
	 * 用户注册时间分析 by wanghui
	 * 
	 * @return
	 */
	public MEventUserRegResult viewReg(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.queryEventReg();
	}

	/**
	 * 用户区域分析 by wanghui
	 * 
	 * @return
	 */
	public Map<String, Integer> viewLocation(EventMonitor event) {
		MEventStore db = this.getEventDB(event);
		return db.queryEventLocal();
	}

	/**
	 * 关键用户分析 by wanghui
	 * 
	 * @return
	 */
	public Map<IAccount, Integer> viewSuperman(EventMonitor event) {

		MEventStore db = this.getEventDB(event);
		return db.queryEventKeyUse(event.getPlatform());
	}
}
