package com.xunku.app.monitor;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.db.AccountDB;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.handlers.AccountPostHandler;
import com.xunku.app.handlers.events.EGrenderHandler;
import com.xunku.app.handlers.events.EKeyPointHandler;
import com.xunku.app.handlers.events.EKeyUseHandler;
import com.xunku.app.handlers.events.ELocalHandler;
import com.xunku.app.handlers.events.ERegHandler;
import com.xunku.app.handlers.events.ERetweetHandler;
import com.xunku.app.handlers.events.ESourceHandler;
import com.xunku.app.handlers.events.ETrendHandler;
import com.xunku.app.handlers.events.EVipHandler;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.accounts.AccountFactory;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.app.stores.MEventStore;
import com.xunku.constant.ApiCST;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.TermDao;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.base.TermDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.dto.task.TaskTwitterVO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.service.TaskSearchService;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;

//
/**
 * 监控事件对象,描述事件基本信息
 * 
 * @author wujian
 * @created on Jun 9, 2014 2:45:24 PM
 */
public class EventMonitor implements IMonitor {

	/**
	 * 最多抓取的数量10000
	 */
	static final int MAX_FETCH_COUNT = 10000;

	@Override
	public int getMonitorType() {
		return 3;
	}

	ITweetStore store;

	@Override
	public ITweetStore getStore(AppContext context) {
		if (this.store == null) {
			Pooling pool = context.getPooling(this.getPooling(), true);
			AccountManager manager = AccountManager.getInstance(context);
			store = new MEventStore(this, pool, manager);
			// 注册博主处理器
			store.RegHandler(new AccountPostHandler());
			store.RegHandler(new EGrenderHandler());
			// weiboDB.RegHandler(new EHotWordHandler());
			store.RegHandler(new EKeyPointHandler());
			store.RegHandler(new EKeyUseHandler());
			store.RegHandler(new ELocalHandler());
			store.RegHandler(new ERegHandler());
			store.RegHandler(new ERetweetHandler());
			store.RegHandler(new ESourceHandler());
			store.RegHandler(new EVipHandler());
			store.RegHandler(new ETrendHandler());
		}
		return store;
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(EventMonitor.class);

	@Override
	public long getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String getToken() {
		return PortalCST.MONITOR_EVENT_PERFIX + this.getMonitorId();
	}

	public void copy(EventMonitor monitor) {
		this.endTime = monitor.endTime;
		this.startTime = monitor.startTime;
		this.name = monitor.name;
		this.keywords = monitor.keywords;
		this.notkeywords = monitor.notkeywords;
	}

	public void landing(AccountDB accDB, ITweetStore db, User user) {
		EventDao _eventDAO = new EventDaoImpl();
		TermDao _termDAO = new TermDaoImpl();
		Date now = new Date();
		Date endTime = this.getEndTime();

		// 清理关键观点统计表
		MEventStore store = (MEventStore) db;
		this.arrangeStatis(store);
		if (endTime.compareTo(now) > 0) {
			this.fetch(db);
			_eventDAO.updateFetchTime(this.getMonitorId(), DateUtils
					.format(now));
		} else {
			// 删除关键字，不删除事件，事件需要显示已过期
			_termDAO.deleteByToken(this.getToken());
		}

		this.timestamp = System.currentTimeMillis();
	}

	public void arrangeStatis(MEventStore store) {
		store.arrangeKeyPoint();
		store.arrangeKeyUser();
	}

	private void fetch(ITweetStore db) {
		TaskSearchService _searchService = new TaskSearchService();
		Date fetchTime = this.getFetchTime();
		if (this.getFetchTime() == null) {
			this.setFetchTime(this.getStartTime());
		}
		fetchTime = this.getFetchTime();
		Date endTime = new Date();
		try {
			Pager pager = new Pager();
			pager.setPageIndex(1);
			pager.setPageSize(ApiCST.COUNT_WEIBO_XUNKU);

			Pagefile<TaskTwitterVO> pagefile = _searchService.tweetEventSearch(this
					.getQuery(), fetchTime, endTime, pager, Utility
					.getPlatform(this.getPlatform()));

			if (pagefile != null) {

				int total = pagefile.getRealcount();
				if (total > MAX_FETCH_COUNT) {
					total = MAX_FETCH_COUNT;
					LOG.info("由于本次抓取超过了最大抓取量[" + MAX_FETCH_COUNT + "]，所以只收集["
							+ MAX_FETCH_COUNT + "]条数据.");
				}

				int pages = total / pager.getPageSize() + 1;
				LOG
						.info("本次抓取事件[" + this.getName() + "]从["
								+ this.fetchTime + "]到[" + endTime
								+ "]区间的数据，总计[" + pagefile.getRealcount()
								+ "]条数据，分[" + pages + "]页抓取.");
				this.fillPost(pagefile.getRows(), db);
				for (int i = 2; i <= pages; i++) {
					LOG.info("正在抓取第" + i + "页...");
					pager.setPageIndex(i);
					pager.setPageSize(ApiCST.COUNT_WEIBO_XUNKU);
					pagefile = _searchService.tweetEventSearch(this.getQuery(),
							fetchTime, endTime, pager, Utility.getPlatform(this
									.getPlatform()));
					if (pagefile != null) {
						this.fillPost(pagefile.getRows(), db);
					}
				}
			} else {
				LOG.info("本次抓取事件[" + this.getName() + "]没有数据");
			}

		} catch (Exception e) {
			LOG.error("抓取事件数据出错", e);
		}

	}

	private void fillPost(List<TaskTwitterVO> rows, ITweetStore db) {
		for (TaskTwitterVO vo : rows) {
			IAccount author = AccountFactory.createAccount(vo);
			ITweet post = TweetFactory.createTweet(vo);
			post.setAuthor(author);
			db.put(post);
		}
		db.flushHandlers();
	}

	private long timestamp;
	private int id;
	private String name; // 事件名称
	private String keywords; // 关键词
	private String notkeywords;// 不包含关键词
	private int location; // 地域
	private Date startTime; // 开始时间
	private Date endTime; // 结束时间
	private int creator; // 创建者
	private int customID; // 客户
	private Platform platform; // 平台类型：1新浪2腾讯5人民
	private String dbserver; // server连接串
	private Date fetchTime;// 抓取时间modity by wanghui
	// private String table; //表名
	private String prov; // 用于地点修改省直辖市
	private String city; // 用于地点修改的市区

	// 查询串
	public String getQuery() {
		String[] key = keywords.split(",");
		String[] nokey = notkeywords.split(",");
		String query = null;
		if (!Utility.isNullOrEmpty(keywords)) {
			query = this.joinMetaQuery(key, false);
		}
		if (!Utility.isNullOrEmpty(notkeywords)) {
			query = query + " AND " + this.joinMetaQuery(nokey, true);
		}

		return query;
	}

	private String joinMetaQuery(String[] keys, boolean isNot) {
		StringBuilder sb = new StringBuilder();
		if (keys.length > 0) {
			sb.append(Utility.getMetaQuery(keys[0], isNot));
		}
		for (int i = 1; i < keys.length; i++) {
			sb.append(" AND ");
			sb.append(Utility.getMetaQuery(keys[i], isNot));
		}
		return sb.toString();
	}

	/**
	 * 事件ID
	 */
	public int getMonitorId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public int getCustomID() {
		return customID;
	}

	public void setCustomID(int customID) {
		this.customID = customID;
	}

	public String getNotkeywords() {
		return notkeywords;
	}

	public void setNotkeywords(String notkeywords) {
		this.notkeywords = notkeywords;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPooling() {
		return dbserver;
	}

	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}

	public String getTableName() {
		return PortalCST.MONITOR_EVENT_PERFIX + this.id;
	}

	String table;

	public void setTable(String table) {
		this.table = table;
	}

	public Date getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(Date fetchTime) {
		this.fetchTime = fetchTime;
	}

	int pool_id;

	public int getPoolId() {
		return this.pool_id;
	}

	public void setPoolId(int poolId) {
		this.pool_id = poolId;
	}

}
