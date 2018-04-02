package com.xunku.app.handlers.spreads;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;
import com.xunku.constant.PortalCST;

/**
 * 
 * 用户转发是否评论统计
 * 
 * @author wujian
 * @created on Aug 1, 2014 3:54:02 PM
 */
public class SRetweetAttitudeHandler implements IPostHandler {

	/**
	 * 传播分析 - 转发态度分析
	 */
	public static final String SQL_SPREAD_RETWEET_ATTITUDE = "spreads/spread.retweet.attitude.sql";

	
	@Override
	public void initialize(TweetStore db) {

	}

	private boolean hasAttitude(String text) {
		if (text.contains("//")) {
			String reText = text.substring(0, text.indexOf("//"));
			for (String s : PortalCST.EMPTY_REPOST) {
				if (reText.equals(s)) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore db) {
		if (pr == PutResult.Add && post.getType() == PostType.Repost
				&& post.getAuthor() != null) {
			Connection conn = null;
			CallableStatement cstmt = null;
			try {
				int sid = db.getMonitorID();
				int cnt = 0;
				int notcnt = 0;
				if(this.hasAttitude(post.getText())){
					cnt = 1;
				}
				else{
					notcnt = 1;
				}
				conn = db.getPool().getConnection();
				cstmt = conn
						.prepareCall("{call sys_inc_Spread_Retweet_Attitude_Statis(?,?,?)}");
				cstmt.setInt(1, sid);
				cstmt.setInt(2, cnt);
				cstmt.setInt(3, notcnt);
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

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
