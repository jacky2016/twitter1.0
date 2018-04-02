package com.xunku.daoImpl.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xunku.constant.PortalCST;
import com.xunku.dao.log.LogsDao;
import com.xunku.pojo.log.LogsEntity;
import com.xunku.utils.DatabaseUtils;

// user: sunao 保存日志方法
public class LogsImpl implements LogsDao {

	@Override
	public void insertLogs(LogsEntity log) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_LOG_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("INSERT INTO logs(ModuleCode,ModuleClass,UserID,UserName,CustomID,CustomName,State,LogContent,IP,ActionName) VALUES(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, log.getModuleCode());
			pstmt.setString(2, log.getModuleClass());
			pstmt.setInt(3, log.getUserID());
			pstmt.setString(4, log.getUserName());
			pstmt.setInt(5, log.getCustomID());
			pstmt.setString(6, log.getCustomName());
			pstmt.setInt(7, log.getState());
			pstmt.setString(8, log.getLogsContent());
			pstmt.setString(9, log.getIp());
			pstmt.setString(10, log.getActionName());
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
