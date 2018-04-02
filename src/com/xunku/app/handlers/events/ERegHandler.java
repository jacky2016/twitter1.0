package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class ERegHandler implements IPostHandler {

	/**
	 * 注册时间分析【事件用户分析】
	 */
	public static final String SQL_EVENT_REG = "event/event.reg.sql";
	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			Date date = new Date(post.getAuthor().getCreated());
			int days = DateHelper.beforeDays(date, new Date()) / 365;
			if (days <= 0.5) {
				this.put(pool, eventid, 1, 0, 0, 0);
			} else if (days > 0.5 && days < 1) {
				this.put(pool, eventid, 0, 1, 0, 0);
			} else if (days > 1 && days < 2) {
				this.put(pool, eventid, 0, 0, 1, 0);
			} else {
				this.put(pool, eventid, 0, 0, 0, 1);
			}

		}
	}

	private void put(Pooling pool, int eventid, int halfYears, int oneYears,
			int twoYears, int moreYears) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Event_Reg_Statis(?,?,?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setInt(2, halfYears);
			cstmt.setInt(3, oneYears);
			cstmt.setInt(4, twoYears);
			cstmt.setInt(5, moreYears);
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
