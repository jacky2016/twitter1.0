package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xunku.constant.PortalCST;
import com.xunku.dao.base.LogDao;
import com.xunku.pojo.base.Log;
import com.xunku.utils.DatabaseUtils;

public class LogDaoImpl implements LogDao {

	@Override
	public void insertBaseLog(Log log) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("INSERT INTO Base_Logs(Type,ModuleID,ClassName,Action,Method,Description) VALUES(?,?,?,?,?,?)");
			pstmt.setInt(1, log.getType());
			pstmt.setInt(2, log.getModuleID());
			pstmt.setString(3, log.getClassName());
			pstmt.setString(4, log.getAction());
			pstmt.setString(5, log.getMethod());
			pstmt.setString(6, log.getDescription());
			pstmt.execute();
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
	}

}
