package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class ERetweetHandler implements IPostHandler {

	/**
	 * 原创/转发分析【事件用户分析】
	 */
	public static final String SQL_EVENT_RETWEET = "event/event.retweet.sql";

	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			switch (post.getType()) {
			case Creative:
				this.put(pool, eventid, 1, 0, 0);
				break;
			case Repost:
				this.put(pool, eventid, 0, 1, 0);
				break;
			case Comment:
				this.put(pool, eventid, 0, 0, 1);
				break;
			default:
				break;
			}
		}
	}

	private void put(Pooling pool, int eventid, int tweet, int retweet,
			int comment) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Event_Retweet_Statis(?,?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setInt(2, tweet);
			cstmt.setInt(3, retweet);
			cstmt.setInt(4, comment);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void initialize(TweetStore db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
