package com.xunku.app.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IRefreshStrategy;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.manager.AuthTimestampManager;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.Pooling;
import com.xunku.app.result.Result;
import com.xunku.app.strategy.DefaultAppAccountStrategy;
import com.xunku.constant.ApiCST;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;
import com.xunku.utils.PropertiesUtils;

/**
 * 博主库，这个库的交互涉及到redis和SQL
 * <p>
 * 博主信息管理器，博主的唯一标识是ucode
 * <p>
 * 如果是新浪的：从API中获取数据时，如果这个帐号有个性化域名则ucode=个性化域名<br>
 * 如果没有个性化域名则ucode=uid<br>
 * 从讯库获取数据时，ucode保存的就是抓取时的内容，uid则从url里面分析出来。
 * <p>
 * 如果是腾讯的：从API获得时ucode保存的是name，uid保存的是openid 如果从讯库获得，ucode保存的是name，uid为空
 * 
 * @author wujian
 * @created on Jul 29, 2014 4:43:43 PM
 */
public class AccountManager {

	private static Logger LOG = LoggerFactory.getLogger(AccountManager.class);
	static AccountManager _manager;

	public synchronized static AccountManager getInstance(AppContext context) {
		if (_manager == null) {
			_manager = new AccountManager();

			_manager.init(context);

		}
		return _manager;
	}

	private void init(AppContext context) {

		Pooling pool = context.getPoolManager().getUserPooling();

		// cache可以共享
		try {
			int timeout = Integer.parseInt(PropertiesUtils.getString("config",
					"cache.redis.server.timeout"));
			String server = PropertiesUtils.getString("config",
					"cache.redis.server.host");
			int port = Integer.parseInt(PropertiesUtils.getString("config",
					"cache.redis.server.port"));

			this._sinaDB = new AccountDB(Platform.Sina);
			this._sinaDB.initPersistence(pool);
			this._sinaDB.initCache(server, port, timeout);

			this._tencentDB = new AccountDB(Platform.Tencent);
			this._tencentDB.initPersistence(pool);
			this._tencentDB.initCache(server, port, timeout);

			this._renminDB = new AccountDB(Platform.Renmin);
			this._renminDB.initPersistence(pool);
			this._renminDB.initCache(server, port, timeout);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	AccountDB _sinaDB;
	AccountDB _tencentDB;
	AccountDB _renminDB;

	public AccountDB getDB(Platform platform) {
		if (platform == Platform.Sina)
			return this._sinaDB;

		if (platform == Platform.Tencent)
			return this._tencentDB;

		if (platform == Platform.Renmin)
			return this._renminDB;

		return null;
	}

	/**
	 * 通过名称获得帐号信息
	 * 
	 * @param name
	 * @param platform
	 * @return
	 */
	public IAccount accountGetByName(String name, Platform platform) {
		return this.getDB(platform).accountGetByName(name);
	}

	/**
	 * 通过名字实时获得帐号信息
	 * 
	 * @param name
	 * @param platform
	 * @return
	 */
	public Result<IAccount> accountGetByNameOnline(String name,
			Platform platform, User user, AppAuth auth) {

		Result<IAccount> result = new Result<IAccount>();
		try {

			if (auth == null) {
				result.setError(500, "未找到授权信息，无法获得帐号。");
				return result;
			}

			IAccount acc = this.getDB(platform).accountGetByNameOnline(name,
					user, auth);
			result.setData(acc);
		} catch (ApiException e) {
			result.setError(e.getErrorCode(), e.getError());
			LOG.error("在线获得账号信息出错 by name", e);
		}

		return result;
	}

	/**
	 * 通过ucode获得帐号的离线数据
	 * 
	 * @param ucode
	 * @param platform
	 * @return
	 */
	public Result<IAccount> accountGetByUcode(String ucode, Platform platform) {

		Result<IAccount> result = new Result<IAccount>();
		IAccount acc = this.getDB(platform).accountGetByUcode(ucode);
		if (acc == null) {
			result.setError(500, "指定的帐号不存在!");
		}
		result.setData(acc);
		return result;
	}

	public IAccount accountGetByTweet(ITweet tweet) {

		Result<IAccount> rst = this.accountGetByUcode(tweet.getUcode(), tweet
				.getPlatform());

		if (rst.getErrCode() == 0) {
			return rst.getData();
		}
		return null;
	}

	/**
	 * 通过ucode获得帐号的在线数据，并且让数据落地
	 * 
	 * @param ucode
	 * @param platform
	 * @return
	 */
	public Result<IAccount> accountGetByUcodeOnline(String ucode,
			Platform platform, User user, AppAuth auth) {

		Result<IAccount> result = new Result<IAccount>();
		try {
			IRefreshStrategy<AppAuth> strategy = new DefaultAppAccountStrategy<AppAuth>();
			if (strategy.shouldImmediately(auth, ApiCST.KEY_ACCOUNT)
					&& auth != null) {
				IAccount acc = this.getDB(platform).accountGetByUcodeOnline(
						ucode, user, auth);
				result.setData(acc);
				AuthTimestampManager.getInstance().putTimestamp(
						auth.getToken(), ApiCST.KEY_ACCOUNT,
						System.currentTimeMillis());
			} else {
				return this.accountGetByUcode(ucode, platform);
			}
		} catch (ApiException e) {
			result.setError(e.getErrorCode(), e.getError());
			LOG.error("在线获得账号信息出错 by ucode --> " + e.getError());
		}

		return result;
	}

	public Result<IAccount> accountGetByUcodeOnline4Tip(String ucode,
			Platform platform, User user, AppAuth auth) {

		Result<IAccount> result = new Result<IAccount>();
		try {
			IAccount acc = this.getDB(platform).accountGetByUcodeOnline(ucode,
					user, auth);
			result.setData(acc);
		} catch (ApiException e) {
			result.setError(e.getErrorCode(), e.getError());
			LOG.error("在线获得账号信息出错 by ucode --> " + e.getError());
		}

		return result;
	}

	public Result<IAccount> accountGetByUcodeOnline(String ucode,
			Platform platform, AppAuth auth) {

		return this.accountGetByUcodeOnline(ucode, platform, null, auth);
	}

	public void updateTweetAccount(ITweet post) {
		if (post != null)
			this.getDB(post.getPlatform()).updateTweetAccount(post);
	}

	public void updateAccount(IAccount account) {
		if (account != null) {
			this.getDB(account.getPlatform()).updateAccount(account);
		}
	}

	public void updateAccounts(List<IAccount> accounts) {
		for (IAccount account : accounts) {
			this.updateAccount(account);
		}
	}

	// ==================================================

	/**
	 * 创建从讯库来的用户
	 * 
	 * @param as
	 */
	public void accountXKCreate(IAccount acc) {
		this.getDB(acc.getPlatform()).updateAccount(acc);

	}

	/**
	 * 创建ucode的粉丝关系
	 * 
	 * @param ucode
	 * @param platform
	 * @param followers
	 */
	public void followerCreate(String ucode, Platform platform,
			List<IAccount> followers) {
		// 是否要更新用户基础库？
		this.updateAccounts(followers);
		List<String> fs = new ArrayList<String>();
		for (IAccount acc : followers) {
			fs.add(acc.getUcode());
		}
		this.getDB(platform).appendFollowers(ucode, fs);
	}

	/**
	 * 创建ucode的关注关系
	 * 
	 * @param ucode
	 * @param platform
	 * @param followings
	 */
	public void followingCreate(String ucode, Platform platform,
			List<IAccount> followings) {
		this.updateAccounts(followings);

		List<String> fs = new ArrayList<String>();
		for (IAccount acc : followings) {
			fs.add(acc.getUcode());
		}
		this.getDB(platform).appendFollowers(ucode, fs);
	}

	/**
	 * 获得指定用户的粉丝列表
	 * 
	 * @param ucode
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Pagefile<IAccount> getFollowers(String ucode, Platform platform,
			Pager pager) {

		return this.getDB(platform).followersGet(ucode, pager);
	}

	public static void main(String[] args) {
	}

}
