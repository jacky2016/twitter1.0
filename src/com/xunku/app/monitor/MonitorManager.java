package com.xunku.app.monitor;

import com.xunku.app.AppContext;
import com.xunku.app.db.AccountManager;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.manager.CustomManager;

/**
 * 检测器管理器基类
 * 
 * @author wujian
 * @created on Jun 18, 2014 6:50:50 PM
 */
public abstract class MonitorManager {

	public MonitorManager(AppContext context) {
		this._context = context;
	}

	public ITweetStore getDB(IMonitor e) {
		return e.getStore(_context);

	}

	protected CustomManager getCustomManager() {
		return this._context.getCustomManager();
	}

	/**
	 * 获得帐号库
	 * 
	 * @return
	 */
	protected AccountManager getAccountManager() {
		return this._context.getAccountManager();
	}

	protected AppContext _context;
}
