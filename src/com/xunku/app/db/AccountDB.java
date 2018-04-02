package com.xunku.app.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.MQController;
import com.xunku.app.Utility;
import com.xunku.app.db.cache.AccountRedisDB;
import com.xunku.app.db.sql.AccountSQLStore;
import com.xunku.app.db.sql.SQLStoreFactory;
import com.xunku.app.enums.CacheStatus;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IAccountHandler;
import com.xunku.app.interfaces.IFollowerHandler;
import com.xunku.app.interfaces.IRefreshStrategy;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.Pooling;
import com.xunku.app.strategy.DefaultAccountStrategy;
import com.xunku.constant.MQCommands;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;

/**
 * 帐号存储库，维护一个Cache和一个SQL 存储
 * 
 * @author wujian
 * @created on Jul 30, 2014 10:29:50 AM
 */
public class AccountDB {

	private static Logger LOG = LoggerFactory.getLogger(AccountDB.class);

	AccountSQLStore _sqlStore;

	AccountRedisDB _cache;

	Platform _platform;
	boolean _persistenceOK;
	Pooling _pool;

	CacheStatus _status = CacheStatus.offline;

	IRefreshStrategy<IAccount> _strategy = new DefaultAccountStrategy<IAccount>();

	public void setCacheStatus(CacheStatus status) {
		this._status = status;
	}

	List<IAccountHandler> _handlers;

	public AccountDB(Platform platform) {
		this._platform = platform;

		this._handlers = new ArrayList<IAccountHandler>();
	}

	/**
	 * 初始化持久层
	 */
	public void initPersistence(Pooling pool) {
		_pool = pool;
		this._sqlStore = new SQLStoreFactory()
				.createStore(pool, this._platform);

		this._persistenceOK = true;
	}

	/**
	 * 初始化缓存
	 */
	public void initCache(String host, int port, int timeout) {

		this._cache = new AccountRedisDB(host, port, timeout);

		this._status = CacheStatus.loading;
		// 启动单独线程加载数据
		RedisAccountFillThread thread;
		try {
			thread = new RedisAccountFillThread(this);
			thread.setName("start " + this._platform + " redis load thread");
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void regHandler(IAccountHandler handler) {
		if (!this.containsHandler(handler)) {
			this._handlers.add(handler);
		}
	}

	public void updateTweetAccount(ITweet post) {
		if (post != null) {
			if (post.getAuthor() != null) {
				this.updateAccount(post.getAuthor());
			}

			if (post.getSource() != null) {
				if (post.getSource().getAuthor() != null) {
					this.updateAccount(post.getSource().getAuthor());
				}
			}
		}

	}

	public void updateAccount(IAccount acc) {

		if (acc != null) {

			// 跟新最后一次刷新时间
			acc.setTimestamp(System.currentTimeMillis());

			this.sendMsg(acc);

			// 这个方法由消息控制
			this._sqlStore.updateAccount(acc);

			this.processHandlers(acc);

			// 更新Cache
			if (this._cache.isReadly()) {
				this._cache.updateAccount(acc);
			}

		}
	}

	/**
	 * 通过名称获得帐号信息
	 * 
	 * @param name
	 * @param platform
	 * @return
	 */
	public IAccount accountGetByName(String name) {

		IAccount result = null;

		if (this._cache.isReadly()) {
			result = this._cache.getAccountByName(name, _platform);
		}

		if (result == null) {
			result = this._sqlStore.getAccountByName(name);
			if (result != null && this._cache.isReadly()) {
				this._cache.addAccount(result);
			}
		}
		return result;
	}

	/**
	 * 通过名字实时获得帐号信息
	 * 
	 * @param name
	 * @param platform
	 * @return
	 */
	public IAccount accountGetByNameOnline(String name, User user, AppAuth auth)
			throws ApiException {

		IAccount acc = this.accountGetByName(name);

		if (acc == null) {
			acc = this.accountGetByNameByAuth(auth, name);
		} else {
			if (this._strategy.shouldImmediately(acc, null)) {
				acc = this.accountGetByNameByAuth(auth, name);
			}
		}
		return acc;
	}

	private IAccount accountGetByNameByAuth(AppAuth auth, String name)
			throws ApiException {

		if (auth == null)
			return null;
		IAccount acc = auth.accountGetByName(name);

		if (acc != null) {
			this.updateAccount(acc);
		}

		return acc;
	}

	/**
	 * 通过ucode获得帐号的离线数据
	 * 
	 * @param ucode
	 * @param platform
	 * @return
	 */
	public IAccount accountGetByUcode(String ucode) {
		IAccount result = null;
		if (_cache.isReadly()) {
			result = this._cache.getAccount(ucode, this._platform);
		}

		if (result == null) {
			result = this._sqlStore.getAccountByUcode(ucode);
			if (result != null && this._cache.isReadly()) {
				this._cache.addAccount(result);
			}
		}
		return result;
	}

	/**
	 * 通过ucode获得帐号的在线数据，并且让数据落地
	 * 
	 * @param ucode
	 * @param platform
	 * @return
	 */
	public IAccount accountGetByUcodeOnline(String ucode, User user,
			AppAuth auth) throws ApiException {

		IAccount acc = accountGetByUcodeByAuth(auth, ucode);

		return acc;
	}

	private IAccount accountGetByUcodeByAuth(AppAuth auth, String ucode)
			throws ApiException {
		IAccount acc = null;
		if (auth.getPlatform() == Platform.Sina) {
			if (Utility.isNumber(ucode))
				acc = auth.accountGetByUid(ucode);
			else
				acc = auth.accountGetByDomain(ucode);
		}

		if (auth.getPlatform() == Platform.Tencent) {
			acc = auth.accountGetByName(ucode);
		}

		if (acc != null) {
			this.updateAccount(acc);
		}

		return acc;
	}

	public static void main(String[] args) {

		Set<String> set1 = new HashSet<String>();

		set1.add("a");
		set1.add("a");

		System.out.println(set1);

	}

	/**
	 * 看看follower是否是ucode的粉丝
	 * 
	 * @param ucode
	 * @param follower
	 * @param platform
	 * @return
	 */
	public boolean checkFollower(String ucode, String follower,
			Platform platform) {
		return this._cache.checkFollower(ucode, follower, platform);
	}

	/**
	 * 看看following是否关注了ucode
	 * 
	 * @param ucode
	 * @param following
	 * @param platform
	 * @return
	 */
	public boolean checkFolloing(String ucode, String following,
			Platform platform) {
		return this._cache.checkFollowing(ucode, following, platform);
	}

	/**
	 * 获得用户的粉丝列表
	 * 
	 * @param id
	 * @return
	 */
	public Pagefile<IAccount> followersGet(String ucode, Pager pager) {

		Set<String> followers = this._cache.getFollowers(ucode, _platform,
				pager);
		if (followers == null) {
			this._sqlStore.setFollowerCache(this._cache, ucode);
		}
		Pagefile<IAccount> result = new Pagefile<IAccount>();
		for (String follower : followers) {
			result.getRows().add(this.accountGetByUcode(follower));
		}
		result.setRealcount((int) pager.getRealCnt());
		return result;
	}

	/**
	 * 为ucode追加粉丝
	 * 
	 * @param ucode
	 * @param followers
	 */
	public void appendFollowers(String ucode, List<String> followers) {

		// if (this._status == CacheStatus.online) {
		// 创建粉丝关系
		this._cache.putFollowers(ucode, this._platform, followers);
		// 创建关注关系
		for (String follower : followers) {
			this._cache.putFollowing(follower, this._platform, ucode, System
					.currentTimeMillis());
		}

		this.sendFollowerMsg(ucode, followers);

		// } else {
		// this._sqlStore.putFollowers(ucode, followers);
		// }
	}

	/**
	 * 为ucode追加关注
	 * 
	 * @param ucode
	 * @param friends
	 */
	public void appendFolloweings(String ucode, List<String> friends) {

		if (this._status == CacheStatus.online) {
			// 创建关注关系
			this._cache.putFollowings(ucode, this._platform, friends);
			// 创建粉丝关系
			for (String follower : friends) {
				this._cache.putFollower(follower, _platform, ucode, System
						.currentTimeMillis());
			}
			this.sendFollowerMsg(ucode, friends);
		} else {
			this._sqlStore.putFollowers(ucode, friends);
		}
	}

	/**
	 * 获得用户的关注列表
	 * 
	 * @param ucode
	 * @param pager
	 * @return
	 */
	public Pagefile<IAccount> firendsGet(String ucode, Pager pager) {
		if (this._status == CacheStatus.online) {
			// 缓存已经准备就绪才能获得

		}
		return null;
	}

	public void loadAccountCache() {
		this._sqlStore.fillUsersCache(this._cache);

		// TODO 填充粉丝缓存
	}

	// 粉丝统计基于Cache
	public void processFollowers(String ucode, Platform platform,
			List<IFollowerHandler> handlers) {
		this._cache.processFollowers(ucode, platform, handlers);
	}

	// ==========================================================

	private boolean containsHandler(IAccountHandler handler) {

		for (IAccountHandler h : this._handlers) {
			if (h.getName().equals(handler.getName())) {
				return true;
			}
		}

		return false;
	}

	private void processHandlers(IAccount acc) {
		for (IAccountHandler hander : this._handlers) {
			hander.ProcessAccount(acc);
		}
	}

	/**
	 * 向消息队列中发送已经更改的帐号信息，每100个发一组
	 * 
	 * @param acc
	 */
	private void sendMsg(IAccount acc) {
		String key = acc.getPlatform() + acc.getUcode();
		try {
			this.getMQ().sendMsg(MQCommands.NAME_ACCOUNT, key, acc.ToJson());
		} catch (InterruptedException e) {
			LOG.error("消息发送失败！");
			e.printStackTrace();
		}
	}

	private void sendFollowerMsg(String ucode, List<String> followers) {
		this._sqlStore.putFollowers(ucode, followers);
		for (String uid : followers) {
			this._sqlStore.putFollowing(uid, ucode);
		}
	}

	public void sendFriendMsg(String ucode, List<String> friends) {
		this._sqlStore.putFollowings(ucode, friends);
		for (String uid : friends) {
			this._sqlStore.putFollower(uid, ucode);
		}
	}

	/**
	 * 获得当前应用服务器的消息队列控制器实例
	 * 
	 * @return
	 */
	private MQController getMQ() {
		return MQController.getInstance();
	}
}
