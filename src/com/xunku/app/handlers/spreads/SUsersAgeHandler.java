package com.xunku.app.handlers.spreads;

import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;

/**
 * 由于用户没有保存年龄，这里不能统计，该处理器暂时没用
 * 
 * @author wujian
 * @created on Jul 22, 2014 5:59:28 PM
 */
public class SUsersAgeHandler implements IPostHandler {
	
	/**
	 * 传播分析 - 用户分析 - 年龄分析
	 */
	public static final String SQL_SPREAD_USER_AGE = "spreads/spread.users.age.sql";

	
	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			// Pooling pool = storeDB.getPool();
			// Account author = post.getAuthor();
			// if (author != null) {
			//
			// } else {
			// LOG.info("[{}]平台的微博[{}],没有作者，无法统计年龄!", post.getPlatform(), post
			// .getTid());
			// }
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

	// =======================================================

	// private void put(int spreadid, int age, Pooling pool) {
	// Connection conn = null;
	// CallableStatement cstmt = null;
	// try {
	// // 事件是平台相关的，所以这里不需要平台关键字
	// conn = pool.getConnection();
	// cstmt = conn
	// .prepareCall("{call sys_put_Spread_Fans_Age_Statis(?,?)}");
	// cstmt.setInt(1, spreadid);
	// cstmt.setInt(2, age);
	// cstmt.execute();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (cstmt != null) {
	// cstmt.close();
	// }
	// if (conn != null) {
	// conn.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
}
