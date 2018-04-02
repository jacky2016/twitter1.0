package com.xunku.app;

import com.google.gson.Gson;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.manager.CustomManager;
import com.xunku.app.manager.FavoriteManager;
import com.xunku.app.manager.PoolManager;
import com.xunku.app.manager.TaskManager;
import com.xunku.app.manager.UserSessionManager;
import com.xunku.app.manager.WarningManager;
import com.xunku.app.model.Pooling;
import com.xunku.app.monitor.TweetAccountManager;
import com.xunku.app.monitor.TweetEventManager;
import com.xunku.app.monitor.TweetOfficalManager;
import com.xunku.app.monitor.TweetSpreadManager;
import com.xunku.app.result.Result;
import com.xunku.pojo.base.User;

/**
 * 应用上下文对象
 * <p>
 * 1、负责管理调用API的帐号和应用
 * <p>
 * 2、负责维护SQL数据库和Redis数据库
 * <p>
 * 3、负责维护业务SQL数据库
 * <p>
 * 4、对外提供微博帐号服务
 * <p>
 * 5、对外提供微博服务
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:49:27 PM
 */
public class AppContext {

	/**
	 * 获得应用服务器的唯一实例
	 * 
	 * @return
	 */
	public synchronized static AppContext getInstance() {
		if (_manager == null) {
			_manager = new AppContext();

		}
		return _manager;
	}

	// ======================== public ==========================

	/**
	 * 获得默认库xk_weibo的连接池
	 * 
	 * @return
	 */
	public Pooling getPoolingHome() {
		return this._poolManager.getBasePooling();
	}

	/**
	 * 根据连接池串获得一个连接池对象
	 * 
	 * @param poolString
	 * @return
	 */
	public Pooling getPooling(String name, boolean initHandler) {
		return this._poolManager.getPooling(name);
	}

	/**
	 * 获得微博帐号库
	 * 
	 * @return
	 */
	public AccountManager getAccountManager() {
		return this._accountManager;
	}

	// ========================================================

	public static void main(String[] args) {

		AppContext server = AppContext.getInstance();
		server.init();

		Result<IAccount> account = server._accountManager
				.accountGetByUcodeOnline("1628951200", Platform.Sina,
						new User(), null);
		System.out.print(new Gson().toJson(account));

	}

	// ======================= private =======================

	private static AppContext _manager = null;

	/**
	 * 博主库列表
	 */
	private AccountManager _accountManager;
	private MQController _mqController;

	private TweetOfficalManager _myPostDBManager;
	private TweetEventManager _eventDBManager;
	private TweetAccountManager _accountDBManager;
	private TweetSpreadManager _weiboDBManager;
	private CustomManager _customManager;
	private TaskManager _taskManager;
	private FavoriteManager _favoriteManager;
	private UserSessionManager _sessionManager;
	PoolManager _poolManager;
	WarningManager _warningManager;

	private AppContext() {
	}

	public void init() {

		this._poolManager = new PoolManager();
		this._poolManager.initPools();

		this._warningManager = new WarningManager();

		this._customManager = CustomManager.getInstance();

		this._taskManager = TaskManager.getInstance();

		this._sessionManager = UserSessionManager.getInstance();

		this._accountManager = AccountManager.getInstance(this);

		this._myPostDBManager = TweetOfficalManager.getInstance(this);

		this._eventDBManager = TweetEventManager.getInstance(this);

		this._accountDBManager = TweetAccountManager.getInstance(this);

		this._weiboDBManager = TweetSpreadManager.getInstance(this);

		this._favoriteManager = FavoriteManager.getInstance();

		this._mqController = MQController.getInstance();

	}

	/**
	 * 获得基于客户的授权管理器，通过该管理器获得的所有数据均为在线数据
	 * 
	 * @return
	 */
	public CustomManager getCustomManager() {
		return this._customManager;
	}

	/**
	 * 获得官微/认证用户的管理器
	 * 
	 * @return
	 */
	public TweetOfficalManager getMOfficialManager() {
		return this._myPostDBManager;
	}

	/**
	 * 获得事件监测管理器
	 * 
	 * @return
	 */
	public TweetEventManager getMEventManager() {
		return this._eventDBManager;
	}

	/**
	 * 获得帐号监测管理器
	 * 
	 * @return
	 */
	public TweetAccountManager getMAccountManager() {
		return this._accountDBManager;
	}

	/**
	 * 获得传播分析管理器
	 * 
	 * @return
	 */
	public TweetSpreadManager getMWeiboManager() {
		return this._weiboDBManager;
	}

	/**
	 * 获得应用程序管理器
	 * 
	 * @return
	 */
	public CustomManager getAppliationManager() {
		return this._customManager;
	}

	/**
	 * 获得消息队列控制器
	 * 
	 * @return
	 */
	public MQController getMQ() {
		return this._mqController;
	}

	/**
	 * 获得收藏夹管理器（收集器）
	 * 
	 * @return
	 */
	public FavoriteManager getFavManager() {
		return this._favoriteManager;
	}

	/**
	 * 获得当前会话管理器
	 * 
	 * @return
	 */
	public UserSessionManager getSessionManager() {
		return this._sessionManager;
	}

	/**
	 * 获得任务管理器
	 * 
	 * @return
	 */
	public TaskManager getTaskManager() {
		return this._taskManager;
	}

	/**
	 * 获得连接池管理器
	 * 
	 * @return
	 */
	public PoolManager getPoolManager() {
		return this._poolManager;
	}

	// ================================
}
