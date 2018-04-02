package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class EKeyUseHandler implements IPostHandler {

	/**
	 * 关键用户分析【事件用户分析】
	 */
	public static final String SQL_EVENT_KEYUSE = "event/event.keyuse.sql";
	
	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			String ucode = post.getAuthor().getUcode();
			int followers = post.getAuthor().getFollowers();
			this.put(pool, eventid, ucode, followers);
		}
	}

	private void put(Pooling pool, int eventid, String ucode, int followers) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Event_KeyUse_Statis(?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setString(2, ucode);
			cstmt.setInt(3, followers);
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

	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
