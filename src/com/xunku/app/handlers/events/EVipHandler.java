package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class EVipHandler implements IPostHandler {

	/**
	 * 认证用户初始化脚本【事件用户分析】
	 */
	public static final String SQL_EVENT_VIP = "event/event.vip.sql";
	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			boolean isVerified = post.getAuthor().isVerified();
			if (isVerified) {
				this.put(pool, eventid, 1, 0);
			} else {
				this.put(pool, eventid, 0, 1);
			}
		}
	}

	private void put(Pooling pool, int eventid, int vip, int unvip) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn.prepareCall("{call sys_put_Event_Vip_Statis(?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setInt(2, vip);
			cstmt.setInt(3, unvip);
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
