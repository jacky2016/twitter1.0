package com.xunku.daoImpl.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.OrganizationsDao;
import com.xunku.dto.PagerDTO;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class OrganizationsDaoImpl implements OrganizationsDao {

	@Override
	public boolean deleteById(int oid) { 
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Base_Organizations WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oid);
			isDelete = pstmt.execute();
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
	
	/**
	@Override
	public boolean deleteById(int oid) { 
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Base_Organizations WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oid);
			isDelete = pstmt.execute();
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
	 * */
	
	
	@Override
	public boolean insert(Organization o) {
		Connection conn = null;
		CallableStatement cstmt = null;
		boolean isInsert = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sp_Organization_insert(?,?,?,?)}");
			cstmt.setInt(1, o.getCustomID());
			cstmt.setString(2, o.getName());
			cstmt.setInt(3, o.getPlatform());
			cstmt.setString(4, o.getUid());
			int flag = cstmt.executeUpdate();
			if (flag > 0) {
				isInsert = true;
			}
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
		return isInsert;
	}

	@Override
	public Pagefile<Organization> queryPagefile(PagerDTO dto, int platform,
			int userId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<Organization> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Base_Organizations");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt
					.setString(
							5,
							"where platform="
									+ platform
									+ " and CustomID=(SELECT CustomID FROM Base_Users WHERE ID="
									+ userId + ")");
			cstmt.setString(6, "id");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<Organization>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.fill(rs));
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				if (rs2.next()) {
					pagefile.setRealcount(rs2.getInt("RecordCount"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs2 != null) {
					rs2.close();
				}
				if (rs != null) {
					rs.close();
				}
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
		return pagefile;
	}

	@Override
	public boolean update(Organization o) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Base_Organizations SET CustomID=?,Name=?,Platform=?,Uid=? WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, o.getCustomID());
			pstmt.setString(2, o.getName());
			pstmt.setInt(3, o.getPlatform());
			pstmt.setString(4, o.getUid());
			pstmt.setInt(5, o.getId());
			isUpdate = pstmt.execute();
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

	@Override
	public int queryMyMentions(String uid, int platform, String startTime,
			String endTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT COUNT(id) as count FROM My_Mentions WHERE uid=? AND platform=? AND created between ? AND ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			pstmt.setInt(2, platform);
			pstmt.setString(3, startTime);
			pstmt.setString(4, endTime);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;
	}

	@Override
	public int queryWeibos(String uid, int platform, String startTime,
			String endTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT COUNT(id) as count FROM My_Posts WHERE uid=? AND platform=? AND created between ? AND ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			pstmt.setInt(2, platform);
			pstmt.setString(3, startTime);
			pstmt.setString(4, endTime);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;
	}

	@Override
	public boolean checkIsExsit(int customid, String name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExsit = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select ID from Base_Organizations where CustomID=? and Name=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isExsit = true;
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
		return isExsit;
	}

	@Override
	public List<Organization> queryOrganizationList(int customid) {
		Connection conn = null;
		PreparedStatement cstmt = null;
		ResultSet rs = null;
		List<Organization> pagefile = new ArrayList<Organization>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareStatement("select * from Base_Organizations where customid =?");
			cstmt.setInt(1, customid);
			rs = cstmt.executeQuery();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.add(this.fill(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, rs);
		}
		return pagefile;
	}

	private Organization fill(ResultSet rs) throws SQLException {
		Organization org = new Organization();
		org.setId(rs.getInt("id"));
		org.setCustomID(rs.getInt("customid"));
		org.setName(rs.getString("name"));
		org.setPlatform(rs.getInt("Platform"));
		org.setUid(rs.getString("uid"));
		return org;
	}

	@Override
	public Pagefile<Organization> queryOrganizationList(int customid,
			Pager pager) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<Organization> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Base_Organizations");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, "where customid = " + customid);
			//cstmt.setString(6, "id");
			cstmt.setString(6, "Name");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<Organization>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.fill(rs));
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				if (rs2.next()) {
					pagefile.setRealcount(rs2.getInt("RecordCount"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, rs, rs2);

		}
		return pagefile;
	}

}
