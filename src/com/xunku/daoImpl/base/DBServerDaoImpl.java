package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.DBServerDao;
import com.xunku.utils.DatabaseUtils;

public class DBServerDaoImpl implements DBServerDao {

	@Override
	public Map<String, String> queryAll() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Base_DBServer";
			pstmt = conn.prepareStatement(sql);
			Map<String,String> list = new HashMap<String,String>();
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String poolString = rs.getString("server");
				list.put(name, poolString);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
	}

	@Override
	public String queryByName(String name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Base_DBServer where name =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String poolString = rs.getString("server");
				return poolString;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
	}

}
