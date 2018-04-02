package com.xunku.app.manager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.xunku.app.handlers.customs.MentionPostHandler;
import com.xunku.app.handlers.customs.NavieCountHandler;
import com.xunku.app.handlers.customs.RepostOrganizationHandler;
import com.xunku.app.handlers.events.EGrenderHandler;
import com.xunku.app.handlers.events.EHotWordHandler;
import com.xunku.app.handlers.events.EKeyPointHandler;
import com.xunku.app.handlers.events.EKeyUseHandler;
import com.xunku.app.handlers.events.ELocalHandler;
import com.xunku.app.handlers.events.ERegHandler;
import com.xunku.app.handlers.events.ERetweetHandler;
import com.xunku.app.handlers.events.ESourceHandler;
import com.xunku.app.handlers.events.ETrendHandler;
import com.xunku.app.handlers.events.EVipHandler;
import com.xunku.app.handlers.maccounts.RealtimeHandler;
import com.xunku.app.handlers.spreads.SHotWordHandler;
import com.xunku.app.handlers.spreads.SRetweetAttitudeHandler;
import com.xunku.app.handlers.spreads.SRetweetHandler;
import com.xunku.app.handlers.spreads.SRetweetLevelHandler;
import com.xunku.app.handlers.spreads.STrendHandler;
import com.xunku.app.handlers.spreads.SUserFansHandler;
import com.xunku.app.handlers.spreads.SUserVipHandler;
import com.xunku.app.handlers.spreads.SUserXFromHandler;
import com.xunku.app.handlers.spreads.SUsersAgeHandler;
import com.xunku.app.handlers.spreads.SUsersCityHanlder;
import com.xunku.app.handlers.spreads.SUsersGenderHandler;
import com.xunku.app.handlers.spreads.SUsersHistogramDetailHandler;
import com.xunku.app.handlers.spreads.SUsersHistogramHandler;
import com.xunku.app.handlers.spreads.SUsersLocationHandler;
import com.xunku.app.handlers.spreads.SUsersTagHandler;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.model.Pooling;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.DBServerDao;
import com.xunku.daoImpl.base.DBServerDaoImpl;
import com.xunku.pojo.base.ConnectionInfo;
import com.xunku.utils.PropertiesUtils;

/**
 * 连接池管理器
 * 
 * @author wujian
 * @created on Sep 2, 2014 5:42:07 PM
 */
public class PoolManager {

	final String LOG_POOL_NAME = "weibolog";
	final String USER_POOL_NAME = "userAccountAdress";
	final String BASE_POOL_NAME = "base";
	Map<String, Pooling> _pools;

	public PoolManager() {
		this._pools = new HashMap<String, Pooling>();

		// 初始化base
		this.loadPool(BASE_POOL_NAME);
		// weibolog
		this.loadPool(LOG_POOL_NAME);
		this.loadPool(USER_POOL_NAME);
	}

	public void initPools() {
		if (this.getPooling(BASE_POOL_NAME) != null) {
			this.loadPools();
		}
	}

	/**
	 * 获得指定名称的连接池
	 * 
	 * @param name
	 * @return
	 */
	public Pooling getPooling(String name) {
		return this._pools.get(name);
	}

	/**
	 * 获得Base连接池
	 * 
	 * @return
	 */
	public Pooling getBasePooling() {
		return this.getPooling(BASE_POOL_NAME);
	}

	/**
	 * 获得微博用户连接池
	 * 
	 * @return
	 */
	public Pooling getUserPooling() {
		return this.getPooling(USER_POOL_NAME);
	}

	/**
	 * 获得微博日志连接池
	 * 
	 * @return
	 */
	public Pooling getLogPooling() {
		return this.getPooling(LOG_POOL_NAME);
	}

	private BoneCP createDataSource(String driver, String url, String username,
			String password) {
		try {
			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BoneCPConfig config = new BoneCPConfig();
		// config.setLazyInit(true);
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(50);
		// config.setConnectionTimeoutInMs(1000 * 10);// 10s
		// config.setIdleConnectionTestPeriodInSeconds(10);//10s测试一次链接状态
		// config.setCloseConnectionWatch(true);
		// config.setLogStatementsEnabled(true);
		config.setPartitionCount(4);
		try {
			BoneCP connectionPool = new BoneCP(config);

			return connectionPool;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // setup the connection pool

		return null;
	}

	private BoneCP createDataSource(ConnectionInfo info) {
		return createDataSource(info.getDriverName(), info
				.getConnectionString(), info.getUid(), info.getPassword());
	}

	private BoneCP createDataSource(String poolString) {
		ConnectionInfo info = ConnectionInfo.getConnInfo(poolString);
		return this.createDataSource(info);
	}

	/**
	 * 初始化连接池
	 * 
	 * @param pool
	 * @param isInitHandler
	 */
	private void initPool(Pooling pool, boolean isInitHandler) {
		// 在pool上初始化分页存储过程
		DbHelper.dbCreatePagerProc(pool);
		// 在pool上建立同义词实现跨库查询
		DbHelper.dbCreateSynonym(pool, this.getBasePooling());
		if (isInitHandler) {
			this.initHandler(pool);
		}

	}

	/**
	 * 装载Base连接池
	 */
	private void loadPool(String propName) {

		if (!this._pools.containsKey(propName)) {
			String poolString;
			try {
				poolString = PropertiesUtils.getString(
						PortalCST.CONFIG_FILE_NAME, propName);
				String urlsplits[] = poolString.split(",");
				String diver = urlsplits[0];
				String url = urlsplits[1];
				String username = urlsplits[2];
				String password = urlsplits[3];
				BoneCP datasource = this.createDataSource(diver, url, username,
						password);
				Pooling pooling = new Pooling(poolString, datasource);
				this.initPool(pooling, false);
				this._pools.put(propName, pooling);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 装载其它可用连接池
	 */
	private void loadPools() {
		DBServerDao dao = new DBServerDaoImpl();
		Map<String, String> poolStrings = dao.queryAll();
		for (Map.Entry<String, String> e : poolStrings.entrySet()) {
			if (!this._pools.containsKey(e.getKey())) {
				Pooling pooling = new Pooling(e.getKey(), this
						.createDataSource(e.getValue()));
				this.initPool(pooling, true);
				this._pools.put(e.getKey(), pooling);

			}
		}
	}

	/**
	 * 初始化连接池上的处理器逻辑
	 * 
	 * @param pool
	 */
	private void initHandler(Pooling pool) {
		try {
			// 网评员统计处理器
			pool.initHandler(NavieCountHandler.class.getName(),
					NavieCountHandler.SQL_NAVIECOUNT);
			// 转发机构统计处理器
			pool.initHandler(RepostOrganizationHandler.class.getName(),
					RepostOrganizationHandler.SQL_REPOSTORGANIZATION);
			// 提到我的处理器
			pool.initHandler(MentionPostHandler.class.getName(),
					MentionPostHandler.SQL_OFFICIAL_MENTION);

			// 事件性别处理器
			pool.initHandler(EGrenderHandler.class.getName(),
					EGrenderHandler.SQL_EVENT_GRENDER);
			// 事件热词处理器
			pool.initHandler(EHotWordHandler.class.getName(),
					EHotWordHandler.SQL_EVENT_HOTWORD);
			// 事件关键观点处理器
			pool.initHandler(EKeyPointHandler.class.getName(),
					EKeyPointHandler.SQL_EVENT_KEYPOINT);
			// 事件关键用户处理器
			pool.initHandler(EKeyUseHandler.class.getName(),
					EKeyUseHandler.SQL_EVENT_KEYUSE);
			// 事件用户地域分布处理器
			pool.initHandler(ELocalHandler.class.getName(),
					ELocalHandler.SQL_EVENT_LOCAL);
			// 事件用户注册时间分布处理器
			pool.initHandler(ERegHandler.class.getName(),
					ERegHandler.SQL_EVENT_REG);
			// 事件转发处理器
			pool.initHandler(ERetweetHandler.class.getName(),
					ERetweetHandler.SQL_EVENT_RETWEET);
			// 事件来源处理器
			pool.initHandler(ESourceHandler.class.getName(),
					ESourceHandler.SQL_EVENT_SOURCE);
			// 事件趋势处理器
			pool.initHandler(ETrendHandler.class.getName(),
					ETrendHandler.SQL_EVENT_TREND);
			// 事件用户认证处理器
			pool.initHandler(EVipHandler.class.getName(),
					EVipHandler.SQL_EVENT_VIP);

			// 传播分析转发观点处理器
			pool.initHandler(SRetweetAttitudeHandler.class.getName(),
					SRetweetAttitudeHandler.SQL_SPREAD_RETWEET_ATTITUDE);
			// 传播分析 - 转发分析
			pool.initHandler(SRetweetHandler.class.getName(),
					SRetweetHandler.SQL_SPREAD_RETWEET);
			// 传播分析 - 转发层级
			pool.initHandler(SRetweetLevelHandler.class.getName(),
					SRetweetLevelHandler.SQL_SPREAD_RETWEET_LEVEL);
			// 传播分析 - 趋势分析
			pool.initHandler(STrendHandler.class.getName(),
					STrendHandler.SQL_SPREAD_TREND);
			// 传播分析 - 用户分析 - 年龄分析
			pool.initHandler(SUsersAgeHandler.class.getName(),
					SUsersAgeHandler.SQL_SPREAD_USER_AGE);
			// 传播分析 - 用户分析 - 城市分析
			pool.initHandler(SUsersCityHanlder.class.getName(),
					SUsersCityHanlder.SQL_SPREAD_USER_CITY);
			// 传播分析 - 用户分析 - 性别分析
			pool.initHandler(SUsersGenderHandler.class.getName(),
					SUsersGenderHandler.SQL_SPREAD_GENDER);
			// 传播分析 - 用户分析 - 用户柱状图 详细信息
			pool
					.initHandler(
							SUsersHistogramDetailHandler.class.getName(),
							SUsersHistogramDetailHandler.SQL_SPREAD_USER_HISTOGRAM_DETAIL);
			// 传播分析 - 用户分析 - 用户柱状图 摘要信息
			pool.initHandler(SUsersHistogramHandler.class.getName(),
					SUsersHistogramHandler.SQL_SPREAD_USER_HISTOGRAM);
			// 传播分析 - 用户分析 - 地域分析
			pool.initHandler(SUsersLocationHandler.class.getName(),
					SUsersLocationHandler.SQL_SPREAD_USER_LOCATION);
			// 传播分析 - 用户分析 - 标签分析
			pool.initHandler(SUsersTagHandler.class.getName(),
					SUsersTagHandler.SQL_SPREAD_USER_HOT_TAGS);
			// 传播分析 - 用户分析 - 认证分析
			pool.initHandler(SUserVipHandler.class.getName(),
					SUserVipHandler.SQL_SPREAD_USER_VIP);
			// 传播分析 - 用户分析 - 来源分析
			pool.initHandler(SUserXFromHandler.class.getName(),
					SUserXFromHandler.SQL_SPREAD_USER_FROM);
			// 传播分析 - 用户分析 - 粉丝分析
			pool.initHandler(SUserFansHandler.class.getName(),
					SUserFansHandler.SQL_SPREAD_USER_FANS);
			// 传播分析热词处理器
			pool.initHandler(SHotWordHandler.class.getName(),
					SHotWordHandler.SQL_SPREAD_HOTWORD);

			// 帐号监测 - 实时监测
			pool.initHandler(RealtimeHandler.class.getName(),
					RealtimeHandler.initHandlerSQL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
