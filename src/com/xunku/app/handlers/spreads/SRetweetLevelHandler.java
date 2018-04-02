package com.xunku.app.handlers.spreads;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class SRetweetLevelHandler implements IPostHandler {

	/**
	 * 传播分析 - 转发层级分析
	 */
	public static final String SQL_SPREAD_RETWEET_LEVEL = "spreads/spread.retweet.level.sql";

	
	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore db) {
		if (post.getType() == PostType.Repost) {
			IAccount acc = post.getAuthor();
			if (acc != null) {
				this.sys_inc_Spread_Retweet_Level_Statis(db.getMonitorID(), db
						.getPool(), post, acc);
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	// =======================================================

	private void sys_inc_Spread_Retweet_Level_Statis(int spreadid,
			Pooling pool, ITweet post, IAccount author) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字

			String tid = post.getTid();
			int retweets = post.getReposts();
			int followers = author.getFollowers();
			boolean vip = author.isVerified();
			int level = post.getLayer();
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Retweet_Level_Statis(?,?,?,?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setString(2, tid);
			cstmt.setInt(3, retweets);
			cstmt.setString(4, author.getUcode());
			cstmt.setInt(5, followers);
			cstmt.setBoolean(6, vip);
			cstmt.setInt(7, level);
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
