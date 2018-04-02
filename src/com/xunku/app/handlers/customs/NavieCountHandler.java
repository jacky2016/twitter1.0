package com.xunku.app.handlers.customs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;
import com.xunku.pojo.base.Organization;

/**
 * 网评员统计，处理器负责更新【考核管理-网评员统计】
 * 
 * @author wanghui
 * 
 */
public class NavieCountHandler implements IPostHandler {

	/**
	 * 网评员统计初始化脚本【考核管理】
	 */
	public static final String SQL_NAVIECOUNT = "custom/naviecount.sql";

	@Override
	public void initialize(TweetStore db) {

	}

	private List<Organization> navieList = null;
	private int customid;

	public NavieCountHandler(List<Organization> navieList) {
		this.navieList = navieList;
	}

	public int getCustomid() {
		return this.customid;
	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (post != null && this.navieList != null) {
			for (Organization na : this.navieList) {
				if (na.getUid().equals(post.getUid())
						&& (post.getSource() != null)) {
					// System.out.println("网评员[" + na.getName() + "]统计入库");
					this.put(storeDB, na.getId(), post);
				}
			}
		}
	}

	private void put(TweetStore storeDB, int navieid, ITweet post) {
		Pooling pool = storeDB.getPool();
		String tid = post.getTid();
		Platform platform = post.getPlatform();
		String uid = post.getSource().getUid();
		PostType type = post.getType();
		long created = post.getCreated();
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Office_naviecunt_Statis(?,?,?,?,?,?,?)}");
			cstmt.setInt(1, storeDB.getMonitorID());
			cstmt.setInt(2, navieid);
			cstmt.setString(3, tid);
			cstmt.setInt(4, Utility.getPlatform(platform));
			cstmt.setString(5, uid);
			cstmt.setInt(6, Utility.getPostType(type));
			cstmt.setLong(7, created);
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
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub

	}

}
