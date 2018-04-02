package com.xunku.app.handlers.spreads;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.Utility;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class SUsersCityHanlder implements IPostHandler {

	/**
	 * 传播分析 - 用户分析 - 城市分析
	 */
	public static final String SQL_SPREAD_USER_CITY = "spreads/spread.users.city.sql";

	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore store) {
		if (pr == PutResult.Add) {
			IAccount author = post.getAuthor();
			if (author != null) {
				int cityId = 0;
				if (author.getCity() != null) {
					cityId = author.getCity().getCode();
					this.put(store.getMonitorID(), Utility.getPostType(post
							.getType()), cityId,
							author.getCity().getProvince(), store.getPool());
				}
			}
		}

	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	private void put(int spreadid, int type, int cid, int pid, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Users_City_Statis(?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setInt(2, type);
			cstmt.setInt(3, cid);
			cstmt.setInt(4, pid);

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
