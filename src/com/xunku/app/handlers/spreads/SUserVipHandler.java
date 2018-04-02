package com.xunku.app.handlers.spreads;

import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;

/**
 * 用户认证分析
 * 
 * @author wujian
 * @created on Jul 22, 2014 7:58:50 PM
 */
public class SUserVipHandler implements IPostHandler {

	/**
	 * 传播分析 - 用户分析 - 认证分析
	 */
	public static final String SQL_SPREAD_USER_VIP = "spreads/spread.users.vip.sql";
	

	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			IAccount author = post.getAuthor();
			if (author != null) {
				DbHelper.incSpreadVipStatis(storeDB.getMonitorID(), post
						.getType(), author.isVerified(), storeDB.getPool());
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
