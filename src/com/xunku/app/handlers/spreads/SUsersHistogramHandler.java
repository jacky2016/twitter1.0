package com.xunku.app.handlers.spreads;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.Utility;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class SUsersHistogramHandler implements IPostHandler {

	/**
	 * 传播分析 - 用户分析 - 转发/评论用户粉丝柱柱状图 不带详细信息的
	 */
	public static final String SQL_SPREAD_USER_HISTOGRAM = "spreads/spread.users.histogram.sql";
	

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			IAccount author = post.getAuthor();
			if (author != null) {
				int v1 = 0;
				int v2 = 0;
				int v3 = 0;
				int v4 = 0;
				int v5 = 0;
				Pooling pool = storeDB.getPool();
				this.put(storeDB.getMonitorID(), post.getType(), v1, v2, v3,
						v4, v5, pool);
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

	private void put(int spreadid, PostType type, int v1, int v2, int v3,
			int v4, int v5, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Users_Histogram_Statis(?,?,?,?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setInt(2, Utility.getPostType(type));
			cstmt.setInt(3, v1);
			cstmt.setInt(4, v2);
			cstmt.setInt(5, v3);
			cstmt.setInt(6, v4);
			cstmt.setInt(7, v5);
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
