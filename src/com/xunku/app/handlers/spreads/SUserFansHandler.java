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

/**
 * 用户分析-用户粉丝数统计
 * 
 * @author wujian
 * @created on Sep 23, 2014 3:22:00 PM
 */
public class SUserFansHandler implements IPostHandler {

	/**
	 * 传播分析 - 用户分析 - 粉丝分析
	 */
	public static final String SQL_SPREAD_USER_FANS = "spreads/spread.users.fans.sql";
	
	@Override
	public void initialize(TweetStore db) {

	}
	
	public SUserFansHandler(){
		this.comment_buffer = new buffer();
		this.retweet_buffer = new buffer();
	}

	class buffer {
		int c100;
		int c1000;
		int c1w;
		int c10w;
		int c100w;

		public void clear() {
			c100 = 0;
			c1000 = 0;
			c1w = 0;
			c10w = 0;
			c100w = 0;
		}
	}

	buffer retweet_buffer;
	buffer comment_buffer;

	@Override
	public void processPost(ITweet tweet, PutResult pr, TweetStore db) {
		if (pr == PutResult.Add) {
			IAccount author = tweet.getAuthor();
			if (author != null) {
				int cnt = author.getFollowers();
				if (tweet.getType() == PostType.Comment) {
					countFans(cnt, this.comment_buffer);
				}
				if (tweet.getType() == PostType.Repost) {
					countFans(cnt, this.retweet_buffer);
				}
			}
		}
	}

	private void countFans(int cnt, buffer buffer) {
		if (cnt > 0 && cnt <= 100) {
			buffer.c100++;
		} else if (cnt > 100 && cnt <= 1000) {
			buffer.c1000++;
		} else if (cnt > 1000 && cnt <= 10000) {
			buffer.c1w++;
		} else if (cnt > 10000 && cnt <= 100000) {
			buffer.c10w++;
		} else if (cnt > 100000) {
			buffer.c100w++;
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void flush(TweetStore db) {

		Pooling pool = db.getPool();
		int sid = db.getMonitorID();

		this.put(sid, Utility.getPostType(PostType.Comment), pool,
				this.comment_buffer);
		this.comment_buffer.clear();
		
		this.put(sid, Utility.getPostType(PostType.Repost), pool,
				this.retweet_buffer);
		this.retweet_buffer.clear();

	}

	private void put(int spreadid, int type, Pooling pool, buffer buffer) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Users_Fans_Statis(?,?,?,?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setInt(2, type);
			cstmt.setInt(3, buffer.c100);
			cstmt.setInt(4, buffer.c1000);
			cstmt.setInt(5, buffer.c1w);
			cstmt.setInt(6, buffer.c10w);
			cstmt.setInt(7, buffer.c100w);

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

}
