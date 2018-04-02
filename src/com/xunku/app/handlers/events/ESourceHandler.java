package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.TweetFrom;
import com.xunku.app.stores.TweetStore;

public class ESourceHandler implements IPostHandler {

	/**
	 * 来源分布【事件用户分析】
	 */
	public static final String SQL_EVENT_SOURCE = "event/event.source.sql";
	
	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			TweetFrom frm = post.getFrom();
			if (frm != null) {
				this.put(pool, eventid, frm.getId(), 1);
			}
		}
	}

	private void put(Pooling pool, int eventid, int sourceid, int value) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Event_Source_Statis(?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setInt(2, sourceid);
			cstmt.setInt(3, value);
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
