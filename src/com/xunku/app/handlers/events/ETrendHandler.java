package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class ETrendHandler implements IPostHandler {

	
	/**
	 * 事件趋势初始化脚本
	 */
	public static final String SQL_EVENT_TREND = "event/event.trend.sql";
	
	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		// 只对新入库的微博做处理
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			long timezone = DateHelper.getTimezoneHour(post.getCreated());
			this.put(eventid, timezone, post.getTid(), pool);
		}

	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	// =======================================================

	private void put(int eventid, long timezone, String tid, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Event_Trend_Statis(?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setTimestamp(2, new Timestamp(timezone));
			cstmt.setString(3, tid);
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
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
