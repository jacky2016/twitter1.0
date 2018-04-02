package com.xunku.daoImpl.home;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.constant.PortalCST;
import com.xunku.dao.home.HomeFansDao;
import com.xunku.pojo.home.FansTrend;
import com.xunku.pojo.task.TaskCount;
import com.xunku.utils.DatabaseUtils;

public class HomeFansDaoImpl implements HomeFansDao {

	@Override
	public void delete(String ucode, Platform platform, Date created) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Home_Fans_Trend WHERE ucode=? AND platform=? AND created=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ucode);
			pstmt.setInt(2, Utility.getPlatform(platform));
			pstmt.setTimestamp(3, DateHelper.getSqlTime(created));
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void insert(FansTrend trend) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "INSERT INTO Home_Fans_Trend (ucode,platform,created,fans,weibos,friends,updated) VALUES (?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, trend.getUcode());
			pstmt.setInt(2, Utility.getPlatform(trend.getPlatform()));
			pstmt.setTimestamp(3, DateHelper.getSqlTime(trend.getCreated()));
			pstmt.setInt(4, trend.getFans());
			pstmt.setInt(5, trend.getFriends());
			pstmt.setInt(6, trend.getFriends());
			pstmt.setTimestamp(7, DateHelper.getSqlTime(trend.getUpdated()));
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public FansTrend queryFansTrend(String ucode, Platform platform,
			Date created) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FansTrend trend = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Home_Fans_Trend WHERE ucode=? AND platform=? AND created=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ucode);
			pstmt.setInt(2, Utility.getPlatform(platform));
			pstmt.setTimestamp(3, DateHelper.getSqlTime(created));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				trend = this.getFansTrend(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return trend;
	}

	private FansTrend getFansTrend(ResultSet rs) throws SQLException {
		FansTrend trend = new FansTrend();
		trend.setUcode(rs.getString("ucode"));
		trend.setCreated(DateHelper.getSqlTime(rs.getDate("created")));
		trend.setFans(rs.getInt("fans"));
		trend.setFriends(rs.getInt("friends"));
		trend.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		trend.setWeibos(rs.getInt("weibos"));
		return trend;
	}

	@Override
	public List<FansTrend> queryFansTrends(String ucode, Platform platform,
			Date start, Date end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<FansTrend> list = new ArrayList<FansTrend>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Home_Fans_Trend WHERE ucode=? AND platform=? AND created >= ? AND created<= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ucode);
			pstmt.setInt(2, Utility.getPlatform(platform));
			pstmt.setDate(3, new java.sql.Date(start.getTime()));
			pstmt.setDate(4, new java.sql.Date(end.getTime()));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getFansTrend(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return list;
	}

	@Override
	public void update(FansTrend trend) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPdate Home_Fans_Trend SET fans=?,weibos=?,friends=?,updated=? WHERE ucode=? AND platform=? AND created=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, trend.getFans());
			pstmt.setInt(2, trend.getWeibos());
			pstmt.setInt(3, trend.getFriends());
			pstmt.setTimestamp(4, DateHelper.getSqlTime(trend.getUpdated()));
			pstmt.setString(5, trend.getUcode());
			pstmt.setInt(6, Utility.getPlatform(trend.getPlatform()));
			pstmt.setTimestamp(7, DateHelper.getSqlTime(trend.getCreated()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void updateTaskStatis(int customid, long now, int sinaCnt,
			int qqCnt, int rmCnt) {
		// sys_update_homestatis
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sys_update_homestatis(?,?,?,?,?)}");
			cstmt.setInt(1, customid);
			cstmt.setDate(2, new java.sql.Date(now));
			cstmt.setInt(3, sinaCnt);
			cstmt.setInt(4, qqCnt);
			cstmt.setInt(5, rmCnt);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
		}
	}

	@Override
	public void updateFanTrend(String ucode, int platform, long now,
			int weibos, int fans, int friends) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sys_update_homefantrend(?,?,?,?,?,?)}");
			cstmt.setString(1, ucode);
			cstmt.setDate(2, new java.sql.Date(now));
			cstmt.setInt(3, platform);
			cstmt.setInt(4, weibos);
			cstmt.setInt(5, fans);
			cstmt.setInt(6, friends);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
		}

	}

	private TaskCount getTaskCount(ResultSet rs) throws SQLException {
		TaskCount count = new TaskCount();
		count.setCustomId(rs.getInt("customId"));
		count.setSina(rs.getInt("sina"));
		count.setTencent(rs.getInt("qq"));
		count.setRenmin(rs.getInt("renmin"));
		count.setCreated(DateHelper.getSqlTime(rs.getDate("created")));
		return count;
	}

	@Override
	public List<TaskCount> queryCountsByCustomID(int customId, Date start,
			Date end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TaskCount> list = new ArrayList<TaskCount>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Home_Statis WHERE customId=? AND created<= ? AND created>=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customId);
			pstmt.setDate(3, new java.sql.Date(start.getTime()));
			pstmt.setDate(2, new java.sql.Date(end.getTime()));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getTaskCount(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return list;
	}

	@Override
	public void updateCategories(int customid, int groupid, long now, int sina,
			int qq, int renmin) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sys_update_homeCategories(?,?,?,?,?,?)}");
			cstmt.setInt(1, customid);
			cstmt.setInt(2, groupid);
			cstmt.setDate(3, new java.sql.Date(now));
			cstmt.setInt(4, sina);
			cstmt.setInt(5, qq);
			cstmt.setInt(6, renmin);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
		}

	}

	@Override
	public Map<String, Integer> queryCategoriesByCustomID(int customId,
			Date start, Date end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select a.sina+a.qq+a.renmin as cnt,b.GroupName from Home_Categories a inner join Task_Groups b on a.groupid= b.ID WHERE a.customId=? AND a.created<=? AND a.created>=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customId);
			pstmt.setDate(3, new java.sql.Date(start.getTime()));
			pstmt.setDate(2, new java.sql.Date(end.getTime()));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int cnt = rs.getInt("cnt");
				if (cnt != 0) {
					result.put(rs.getString("GroupName"), cnt);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

}
