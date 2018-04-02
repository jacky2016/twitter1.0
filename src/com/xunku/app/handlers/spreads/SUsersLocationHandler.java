package com.xunku.app.handlers.spreads;


import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;

/**
 * 区域分析
 * 
 * @author wujian
 * @created on Jul 23, 2014 9:45:36 AM
 */
public class SUsersLocationHandler implements IPostHandler {


	/**
	 * 传播分析 - 用户分析 - 地域分析
	 */
	public static final String SQL_SPREAD_USER_LOCATION = "spreads/spread.users.location.sql";

	
	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			IAccount author = post.getAuthor();
			if (author != null) {
				DbHelper.incSpreadStatis(
						"sys_inc_Spread_Users_Location_Statis", storeDB
								.getMonitorID(), post.getType(), author
								.getLocation(), storeDB.getPool());
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