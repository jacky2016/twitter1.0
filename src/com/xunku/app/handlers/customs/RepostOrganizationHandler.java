package com.xunku.app.handlers.customs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;
import com.xunku.pojo.base.Organization;

/**
 * 转发机构，处理器负责更新【考核管理-微博内容统计】
 * 
 * @author wanghui
 * 
 */
public class RepostOrganizationHandler implements IPostHandler {

	/**
	 * 转发机构统计初始化脚本【考核管理】
	 */
	public static final String SQL_REPOSTORGANIZATION = "custom/RepostOrganization.sql";

	@Override
	public void initialize(TweetStore db) {

	}

	private List<Organization> list = null;

	public RepostOrganizationHandler(List<Organization> list) {
		this.list = list;

	}

	private int isOrg(String ucode) {
		int result = -1;
		for (Organization org : this.list) {
			if (org.getUid().equals(ucode)) {
				result = org.getId();
			}
		}
		return result;
	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		Pooling pool = storeDB.getPool();
		IAccount acc = post.getAuthor();
		if (post != null && post.getSource() != null && acc != null) {

			int orgid = this.isOrg(acc.getUcode());
			if (orgid != -1 && post.getSource() != null) {
				this.put(pool, orgid, post, acc);
			}
		}
	}

	private void put(Pooling pool, int orgid, ITweet post, IAccount acc) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_put_Office_Organization_Statis(?,?,?,?,?,?,?)}");
			cstmt.setInt(1, orgid);
			cstmt.setString(2, post.getTid());
			cstmt.setString(3, post.getOriginalTweet().getTid());
			cstmt.setString(4, acc.getName());
			cstmt.setInt(5, Utility.getPlatform((post.getPlatform())));
			cstmt.setString(6, acc.getUcode());
			cstmt.setInt(7, Utility.getPostType(post.getType()));
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
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
