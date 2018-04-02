package com.xunku.app.db.sql;

import java.sql.ResultSet;
import java.util.List;

import com.xunku.app.db.cache.AccountRedisDB;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.Pooling;

/**
 * 帐号SQL存储
 * 
 * @author wujian
 * @created on Jul 29, 2014 4:12:28 PM
 */
public abstract class AccountSQLStore {

	Pooling _pool;
	Platform _platform;

	public AccountSQLStore(Pooling pool, Platform platform) {
		this._pool = pool;
		this._platform = platform;
	}

	public abstract IAccount getAccountByUcode(String ucode);

	public abstract IAccount getAccountByName(String name);

	/**
	 * 更新数据库，该方法应该是由消息队列处理器来调用
	 * 
	 * @param acc
	 * @param platform
	 * @return
	 */
	public abstract PutResult updateAccount(IAccount acc);

	public abstract void putFollowings(String ucode, List<String> followings);

	public abstract void putFollowing(String ucode, String following);

	public abstract void putFollowers(String ucode, List<String> followers);

	public abstract void putFollower(String ucode, String follower);

	/**
	 * 填充粉丝cache
	 * 
	 * @param cache
	 */
	public abstract void setFollowerCache(AccountRedisDB cache, String ucode);

	/**
	 * 填充帐号cache
	 * 
	 * @param cache
	 */
	public abstract void fillUsersCache(AccountRedisDB cache);

	protected abstract IAccount readAccount(ResultSet rs);

}
