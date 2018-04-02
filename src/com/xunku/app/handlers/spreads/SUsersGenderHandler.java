package com.xunku.app.handlers.spreads;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.Utility;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

/**
 * 传播分析-粉丝性别统计
 * 
 * @author wujian
 * @created on Jul 22, 2014 5:12:14 PM
 */
public class SUsersGenderHandler implements IPostHandler {
	/**
	 * 传播分析 - 用户分析- 性别分析
	 */
	public static final String SQL_SPREAD_GENDER = "spreads/spread.users.gender.sql";
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(SUsersGenderHandler.class);

	@Override
	public void initialize(TweetStore db) {
		Pooling pool = db.getPool();

		if (!DbHelper.dbHasData(
				pool,
				"select * from Spread_Users_Gender_Statis where sid = "
						+ db.getMonitorID())) {

			LOG.info("预制数据");

		}
	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {

		if (pr == PutResult.Add) {
			Pooling pool = storeDB.getPool();
			IAccount author = post.getAuthor();
			if (author != null) {
				int famales = 0;
				int males = 0;
				int unknows = 0;
				if (author.getGender() == GenderEnum.Male) {
					males = 1;
				} else if (author.getGender() == GenderEnum.Famale) {
					famales = 1;
				} else {
					unknows = 1;
				}
				this.put(storeDB.getMonitorID(),
						Utility.getPostType(post.getType()), famales, males,
						unknows, pool);
			} else {
				LOG.info("[{}]平台的微博[{}],没有作者，无法统计性别!", post.getPlatform(),
						post.getTid());
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	// =======================================================

	private void put(int spreadid, int type, int famales, int males,
			int unknows, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Users_Gender_Statis(?,?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setInt(2, type);
			cstmt.setInt(3, famales);
			cstmt.setInt(4, males);
			cstmt.setInt(5, unknows);
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
