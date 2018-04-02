package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.CrawlTaskDao;
import com.xunku.pojo.base.CrawlTask;
import com.xunku.utils.DatabaseUtils;

public class CrawlTaskDaoImpl implements CrawlTaskDao {

	@Override
	public boolean delete(int crawlTaskId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "delete from Base_Crawler_Task_List where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, crawlTaskId);
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
	public boolean insert(CrawlTask task) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isInsert = false;
		ResultSet res = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Base_Crawler_Task_List (token,created,homeurl,domainid,type,executed,acquired,customId,monitorId,monitorType) values (?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, task.getToken());
			pstmt.setLong(2, task.getCreated());
			pstmt.setString(3, task.getHomeUrl());
			pstmt.setInt(4, task.getDomainid());
			pstmt.setInt(5, task.getType());
			pstmt.setLong(6, task.getExecuted());
			pstmt.setLong(7, task.getAcquired());
			pstmt.setInt(8, task.getCustomId());
			pstmt.setInt(9, task.getMonitorid());
			pstmt.setInt(10, task.getMonitorType());

			pstmt.executeUpdate();// sql,
			res = pstmt.getGeneratedKeys();
			if (res.next()) {
				task.setId(res.getInt(1));
				isInsert = true;
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
		return isInsert;
	}

	@Override
	public CrawlTask queryById(int crawlTaskId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CrawlTask task = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Base_Crawler_Task_List where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, crawlTaskId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				task = this.getCrawlTask(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
		return task;
	}

	private CrawlTask getCrawlTask(ResultSet rs) throws SQLException {
		CrawlTask task = new CrawlTask();
		task.setId(rs.getInt("id"));
		task.setToken(rs.getString("token"));
		task.setCreated(rs.getLong("created"));
		task.setHomeUrl(rs.getString("homeurl"));
		task.setDomainid(rs.getInt("domainid"));
		task.setType(rs.getInt("type"));
		task.setExecuted(rs.getLong("executed"));
		task.setAcquired(rs.getLong("acquired"));
		task.setCustomId(rs.getInt("customId"));
		task.setMonitorid(rs.getInt("monitorId"));
		task.setMonitorType(rs.getInt("monitorType"));
		task.setResult(rs.getString("result"));
		return task;
	}

	@Override
	public List<CrawlTask> queryUnAcquiredTasks(int monitorid, int monitorType, long created,
			long expired) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CrawlTask> list = new ArrayList<CrawlTask>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			//String sql = "select * from Base_Crawler_Task_List where monitorid=? and monitorType=? and acquired=0 and created<=? and created>=?";
			String sql = "select * from Base_Crawler_Task_List where monitorid=? and monitorType=? and acquired=0 and created<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, monitorid);
			pstmt.setInt(2, monitorType);
			pstmt.setLong(3, created);// 返回提交超过超过10分钟
			//pstmt.setLong(4, expired);// 返回在超时时间以内的
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getCrawlTask(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return list;
	}

	@Override
	public List<CrawlTask> queryUnSubmitedTasks(int customId, int crawlType,
			int monitorType) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CrawlTask> list = new ArrayList<CrawlTask>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Base_Crawler_Task_List where customId=? and executed=0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customId);
			// pstmt.setInt(2, crawlType);
			// pstmt.setInt(3, monitorType);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getCrawlTask(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
		return list;
	}

	@Override
	public boolean update(CrawlTask task) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Base_Crawler_Task_List set token=?,created=?,homeurl=?,domainid=?,type=?,executed=?,acquired=?,customId=?,monitorId=?,"
					+ "monitorType=?,result=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, task.getToken());
			pstmt.setLong(2, task.getCreated());
			pstmt.setString(3, task.getHomeUrl());
			pstmt.setInt(4, task.getDomainid());
			pstmt.setInt(5, task.getType());
			pstmt.setLong(6, task.getExecuted());
			pstmt.setLong(7, task.getAcquired());
			pstmt.setInt(8, task.getCustomId());
			pstmt.setInt(9, task.getMonitorid());
			pstmt.setInt(10, task.getMonitorType());
			pstmt.setString(11, task.getResult());
			pstmt.setInt(12, task.getId());
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isUpdate = true;
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
		return isUpdate;
	}

}
