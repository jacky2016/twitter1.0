package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.Utility;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class EKeyPointHandler implements IPostHandler {

	/**
	 * 热门关键观点【事件用户分析】
	 */
	public static final String SQL_EVENT_KEYPOINT = "event/event.keypoint.sql";

	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			int platform = Utility.getPlatform(storeDB.getMonitor()
					.getPlatform());
			this.put(pool, eventid, post.getTid(), post.getReposts(), platform,
					post.getCreated());
		}
	}

	private void put(Pooling pool, int eventid, String tid, int reposts,
			int platform, long created) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Event_KeyPoint_Statis(?,?,?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setLong(2, Long.parseLong(tid));
			cstmt.setInt(3, platform);
			cstmt.setInt(4, reposts);
			cstmt.setLong(5, created);
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
