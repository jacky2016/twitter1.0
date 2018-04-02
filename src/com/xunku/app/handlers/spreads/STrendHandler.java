package com.xunku.app.handlers.spreads;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.xunku.app.Utility;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

/**
 * 传播分析--趋势分析
 * 
 * @author wujian
 * @created on Jul 22, 2014 4:06:20 PM
 */
public class STrendHandler implements IPostHandler {
	/**
	 * 传播分析-趋势分析初始化脚本
	 */
	public static final String SQL_SPREAD_TREND = "spreads/spread.trend.sql";

	
	@Override
	public void initialize(TweetStore db) {
		// TODO 这里需要预制数据
		
		
	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		// 只统计新入库的
		if (pr == PutResult.Add) {
			Pooling pool = storeDB.getPool();
			long timezone = DateHelper.getTimezoneHour(post.getCreated());
			this.put(storeDB.getMonitorID(), timezone, post.getType(), pool);
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	// =======================================================

	private void put(int spreadid, long timezone, PostType type, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Trend_Statis(?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setTimestamp(2, new Timestamp(timezone));
			cstmt.setInt(3, Utility.getPostType(type));
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
