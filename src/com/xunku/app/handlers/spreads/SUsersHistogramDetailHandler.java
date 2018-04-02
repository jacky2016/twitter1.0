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
 * 传播分析 - 粉丝分析 - 评论/转发粉丝比例分析
 * 
 * @author wujian
 * @created on Jul 22, 2014 6:04:06 PM
 */
public class SUsersHistogramDetailHandler implements IPostHandler {

	/**
	 * 传播分析 - 用户分析 - 转发/评论用户粉丝数柱状图 带详细信息的
	 */
	public static final String SQL_SPREAD_USER_HISTOGRAM_DETAIL = "spreads/spread.users.histogram.detail.sql";
	
	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {

		Pooling pool = storeDB.getPool();
		IAccount author = post.getAuthor();
		if (author != null) {
			this.put(storeDB.getMonitorID(), post.getType(), author.getUcode(),
					author.getFollowers(), pool);
		}

	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	// =======================================================

	private void put(int spreadid, PostType type, String uid, int followers,
			Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Spread_Users_Histogram_Detail_Statis(?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setInt(2, Utility.getPostType(type));
			cstmt.setString(3, uid);
			cstmt.setInt(4, followers);
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
	public void initialize(TweetStore db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
