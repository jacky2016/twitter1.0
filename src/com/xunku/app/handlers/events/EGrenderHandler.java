package com.xunku.app.handlers.events;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class EGrenderHandler implements IPostHandler {

	/**
	 * 性别比例初始化脚本【事件用户分析】
	 */
	public static final String SQL_EVENT_GRENDER = "event/event.grender.sql";
	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			int eventid = storeDB.getMonitorID();
			Pooling pool = storeDB.getPool();
			GenderEnum sex = post.getAuthor().getGender();
			if (sex == GenderEnum.Famale) {
				this.put(eventid, 1, 0, 0, pool);
			} else if (sex == GenderEnum.Male) {
				this.put(eventid, 0, 1, 0, pool);
			} else {
				this.put(eventid, 0, 0, 1, pool);
			}

		}
	}

	private void put(int eventid, int males, int females, int unsex,
			Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Event_Grender_Statis(?,?,?,?)}");
			cstmt.setInt(1, eventid);
			cstmt.setInt(2, males);
			cstmt.setInt(3, females);
			cstmt.setInt(4, unsex);
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
