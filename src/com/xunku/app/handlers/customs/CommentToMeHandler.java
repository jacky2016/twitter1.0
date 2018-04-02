package com.xunku.app.handlers.customs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.Utility;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

/**
 * 评论给我的处理器
 * <p>
 * 
 * @author wujian
 * @created on Aug 22, 2014 11:08:31 AM
 */
public class CommentToMeHandler implements IPostHandler {

	String currentUID;

	public CommentToMeHandler(String cuid) {
		this.currentUID = cuid;
	}

	@Override
	public void initialize(TweetStore db) {
	}

	@Override
	public void processPost(ITweet tweet, PutResult pr, TweetStore db) {
		if (pr == PutResult.Add) {

		}

	}

	public void put(ITweet post, String me,Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_comments_tome(?,?)}");
			cstmt.setString(1, post.getTid());
			cstmt.setString(2, me);
			cstmt.setInt(3, Utility.getPlatform(post.getPlatform()));
			cstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
