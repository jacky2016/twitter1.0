package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xunku.app.Utility;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.TermDao;
import com.xunku.pojo.base.Term;
import com.xunku.utils.DatabaseUtils;

public class TermDaoImpl implements TermDao {

	@Override
	public boolean deleteByToken(String token) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Base_Terms WHERE token=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, token);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isDelete;
	}

	@Override
	public boolean insert(Term t) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isInsert = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "INSERT INTO Base_Terms (token,term,platform) VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getToken());
			pstmt.setString(2, t.getTerm());
			pstmt.setInt(3, Utility.getPlatform(t.getPlatform()));
			isInsert = pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isInsert;
	}

}
