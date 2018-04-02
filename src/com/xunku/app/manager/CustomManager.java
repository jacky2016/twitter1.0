package com.xunku.app.manager;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAuthStrategy;
import com.xunku.app.model.App;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.people.PUser;
import com.xunku.app.strategy.AuthDefaultStrategy;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AccountAuthsDao;
import com.xunku.dao.base.AppDao;
import com.xunku.daoImpl.base.AccountAuthsDaoImpl;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.AppDaoImpl;
import com.xunku.daoImpl.base.CustomDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.User;
import com.xunku.utils.PropertiesUtils;

/**
 * 客户管理器，职责为：管理客户里面的用户，和客户下面的授权
 * 
 * @author wujian
 * @created on Jul 4, 2014 10:27:47 AM
 */
public class CustomManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(CustomManager.class);

	App sinaMainApp;
	App tencentMainApp;

	static CustomManager _manager;

	public synchronized static CustomManager getInstance() {
		if (_manager == null) {
			// 这里应该从配置文件里面读取策略和监听器
			_manager = new CustomManager();
		}
		return _manager;
	}

	// ========================= 客户的授权信息 ========================

	/**
	 * 通过默认策略在当前用户所在客户里面找一个授权
	 * 
	 * @param user
	 * @param platform
	 * @return
	 */
	public AppAuth getAuthByDefaultStrategy(User user, Platform platform) {
		Custom custom = this.getCustom(user.getCustomID());
		return this.getAuthByDefaultStrategy(custom, platform);
	}

	public AppAuth getAuthByDefaultStrategy(Custom custom, Platform platform) {
		IAuthStrategy strategy = new AuthDefaultStrategy(platform);
		return strategy.getAuth(custom.getAccounts());
	}

	/**
	 * 获得指定uid的授权信息
	 * 
	 * @param uid
	 * @param platform
	 * @return
	 */
	public AppAuth getAuthByUid(User user, String uid, Platform platform) {
		return this.getAuth(user, platform, new UidCompare(uid, platform));
	}

	public AppAuth getAuthByUser(Platform platform, User user) {
		IAuthStrategy s = new AuthDefaultStrategy(platform);
		return this.getAuthByStrategy(s, user);

	}

	public AppAuth getAuthByCustom(Platform platform, Custom custom) {
		IAuthStrategy s = new AuthDefaultStrategy(platform);
		return s.getAuth(custom.getAccounts());
	}

	/**
	 * 通过Ucode获得授权
	 * 
	 * @param user
	 * @param ucode
	 * @param platform
	 * @return
	 */
	public AppAuth getAuthByUcode(User user, String ucode, Platform platform) {
		return this.getAuth(user, platform, new UcodeCompare(ucode, platform));
	}

	public AppAuth getAuthByName(User user, String name, Platform platform) {
		return this.getAuth(user, platform, new NameCompare(name, platform));
	}

	public App getMainApp(Platform platform) {
		if (platform == Platform.Sina)
			return this.sinaMainApp;

		if (platform == Platform.Tencent)
			return this.tencentMainApp;

		return null;
	}

	public void addAccount(User user, AccountInfo info, AccountAuths auth) {
		Custom custom = this.getCustom(user.getCustomID());
		this.addAccount(info, auth, custom);
	}

	/**
	 * 通过客户编号获得客户对象
	 * 
	 * @param cid
	 * @return
	 */
	public Custom getCustom(int cid) {
		// 这里如果使用缓存的方式，则可能会出现同步的问题
		// 比如：客户添加了新的微博帐号，则无法立刻同步
		// 这么些貌似又有效率问题...
		Custom custom = new CustomDaoImpl().queryById(cid);
		this.initCustom(custom);
		return custom;
	}

	public User getAdmin(int customid) {
		return new UserDaoImpl().queryAdmin(customid);
	}

	public List<Custom> getCustoms() {
		List<Custom> customs = new CustomDaoImpl().queryAll();
		for (Custom custom : customs) {
			this.initCustom(custom);
		}
		return customs;
	}

	// ============================================

	private interface AccountCompare {
		boolean compare(AppAccount acc);
	}

	private class UcodeCompare implements AccountCompare {

		String _key;
		Platform _platform;

		public UcodeCompare(String key, Platform platfrom) {
			_key = key;
			_platform = platfrom;
		}

		@Override
		public boolean compare(AppAccount acc) {
			if (acc.getUcode().equals(_key) && acc.getPlatform() == _platform)
				return true;
			return false;
		}

	}

	private class NameCompare implements AccountCompare {

		String _key;
		Platform _platform;

		public NameCompare(String key, Platform platfrom) {
			_key = key;
			_platform = platfrom;
		}

		@Override
		public boolean compare(AppAccount acc) {
			if (acc.getName().equals(_key) && acc.getPlatform() == _platform)
				return true;
			return false;
		}

	}

	private class UidCompare implements AccountCompare {

		String _key;
		Platform _platform;

		public UidCompare(String key, Platform platfrom) {
			_key = key;
			_platform = platfrom;
		}

		@Override
		public boolean compare(AppAccount acc) {
			if (acc.getUid().equals(_key) && acc.getPlatform() == _platform)
				return true;
			return false;
		}

	}

	private AppAuth getAuthByStrategy(IAuthStrategy strategy, User user) {
		Custom custom = this.getCustom(user.getCustomID());
		return strategy.getAuth(custom.getAccounts());
	}

	private AppAuth getAuth(User user, Platform platform, AccountCompare compare) {
		List<AppAccount> accounts = this.getCustom(user.getCustomID())
				.getAccounts();

		AppAccount account = null;
		for (AppAccount acc : accounts) {
			if (compare.compare(acc)) {
				account = acc;
				break;
			}
		}
		if (account == null)
			return null;
		App app = this.getMainApp(platform);
		return account.getAuth(app.getId());
	}

	// ============================================================
	/**
	 * 添加一个客户对象，如果以前有则刷新之
	 * 
	 * @param c
	 */
	private void initCustom(Custom custom) {

		// 装载客户的绑定帐号
		List<AccountInfo> accList = new AccountDaoImpl().queryByCustomId(custom
				.getId());
		AccountAuthsDao dao = new AccountAuthsDaoImpl();

		for (AccountInfo info : accList) {
			if (info.getPlatform() != Platform.Renmin) {
				AppAccount acc = AppAccount.create(info);
				// 获得帐号的策略信息
				int appid = this.getMainApp(acc.getPlatform()).getId();
				// 装载帐号的授权信息
				List<AccountAuths> auths = dao.queryAuthByAccountId(acc
						.getAccountId(), appid);
				for (AccountAuths auth : auths) {
					this.addAccount(info, auth, custom);
				}
			} else {
				// 人民的要走另外一个逻辑
				PUser user = dao.queryPeopleUser(Long
						.parseLong(info.getUcode()));
				if (user != null) {
					AppAccount acc = AppAccount
							.createPeople(user, info.getId());
					custom.getAccounts().add(acc);
				}
			}
		}

	}

	private AppAccount findAccount(AccountInfo info, Custom custom) {
		for (AppAccount acc : custom.getAccounts()) {
			if (acc.getUid().equals(info.getUid())
					&& info.getPlatform().equals(acc.getPlatform())) {
				return acc;
			}
		}
		return null;
	}

	private void addAccount(AccountInfo info, AccountAuths auth, Custom custom) {
		AppAccount acc = this.findAccount(info, custom);
		if (acc == null) {
			acc = AppAccount.create(info);
			custom.getAccounts().add(acc);
		} else {
			acc.setName(info.getName());
		}
		// 重置授权信息
		if (auth != null) {
			AppAuth a = AppAuth.create(acc, auth);
			if (!a.expired()) {
				acc.AddAuth(a);
			} else {
				LOG.error("帐号[{}]在平台[{}]的应用[{}]上的授权[{}]已经过期，请尽快重新授权！", info
						.getName(), info.getPlatform(), auth.getAppId(), auth
						.getToken());
			}
		}
	}

	private CustomManager() {
		try {
			String appkey = PropertiesUtils.getString(
					PortalCST.CONFIG_FILE_NAME, PortalCST.SINA_APP_KEY_NAME)
					.trim();

			AppDao dao = new AppDaoImpl();
			this.sinaMainApp = dao.queryByAppkey(appkey);

			appkey = PropertiesUtils.getString(PortalCST.CONFIG_FILE_NAME,
					PortalCST.TENC_APP_KEY_NAME).trim();

			this.tencentMainApp = dao.queryByAppkey(appkey);

		} catch (IOException ex) {

		}

	}
}
