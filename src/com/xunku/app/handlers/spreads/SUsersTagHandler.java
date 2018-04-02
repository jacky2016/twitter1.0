package com.xunku.app.handlers.spreads;

import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;

/**
 * 用户标签分析
 * 
 * @author wujian
 * @created on Jul 23, 2014 9:42:28 AM
 */
public class SUsersTagHandler implements IPostHandler {

	/**
	 * 传播分析 - 用户热门标签
	 */
	public static final String SQL_SPREAD_USER_HOT_TAGS = "spreads/spread.users.tags.sql";

	
	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			IAccount author = post.getAuthor();
			if (author != null) {
				if (author.getTags() != null) {
					for (String tag : author.getTags()) {
						DbHelper.incSpreadStatis(
								"sys_inc_Spread_Users_Tag_Statis", storeDB
										.getMonitorID(), post.getType(), tag,
								storeDB.getPool());
					}
				}

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
