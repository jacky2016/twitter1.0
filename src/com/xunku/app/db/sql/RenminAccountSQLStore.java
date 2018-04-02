package com.xunku.app.db.sql;

import java.sql.ResultSet;
import java.util.List;

import com.xunku.app.db.cache.AccountRedisDB;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.Pooling;

public class RenminAccountSQLStore extends AccountSQLStore {

	public RenminAccountSQLStore(Pooling pool, Platform platform) {
		super(pool, platform);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public IAccount getAccountByUcode(String ucode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount getAccountByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PutResult updateAccount(IAccount acc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putFollowings(String ucode, List<String> followings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putFollowers(String ucode, List<String> followers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFollowerCache(AccountRedisDB cache, String ucode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillUsersCache(AccountRedisDB cache) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected IAccount readAccount(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putFollower(String ucode, String follower) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putFollowing(String ucode, String following) {
		// TODO Auto-generated method stub
		
	}

	
}
