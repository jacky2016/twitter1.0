package com.xunku.daoImpl.monitor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.constant.PortalCST;
import com.xunku.dao.monitor.MAccountDao;
import com.xunku.dto.PagerDTO;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class MAccountDaoImpl implements MAccountDao {

	@Override
	public AccountMonitor queryMAccountByID(int monitorId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountMonitor am = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT a.*,b.name as dbname FROM Account_List a JOIN Base_DBServer b ON a.dbserver=b.id where a.id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, monitorId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				am = this.getAccountMonitor(rs);
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
		return am;
	}

	private AccountMonitor getAccountMonitor(ResultSet rs) throws SQLException {
		AccountMonitor ma = new AccountMonitor();
		ma.setId(rs.getInt("id"));
		ma.setUserid(rs.getInt("userId"));
		ma.setNick(rs.getString("nick"));
		ma.setUid(rs.getString("uid"));
		// ma.setTail(rs.getString("tail"));
		ma.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		// ma.setStatus(rs.getInt("status"));
		ma.setOriginal(rs.getFloat("original"));
		ma.setRepostsperday(rs.getFloat("repostsperday"));
		ma.setCommentperday(rs.getFloat("commentperday"));
		ma.setDbserver(rs.getString("dbname"));
		return ma;
	}

	@Override
	public boolean deleteByMid(int mid) {
		Connection conn = null;
		CallableStatement cstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sp_MAccount_deleteByMid(?)}");
			cstmt.setInt(1, mid);
			isDelete = cstmt.execute();
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
		return isDelete;
	}

	@Override
	public void insert(AccountMonitor ma) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_MAccount_insert(?,?,?,?,?,?,?,?,?)}");
			cstmt.setInt(1, ma.getUserid());
			cstmt.setString(2, ma.getNick());
			cstmt.setString(3, ma.getUid());
			cstmt.setInt(4, Utility.getPlatform(ma.getPlatform()));
			cstmt.setInt(5, ma.getStatus());
			cstmt.setFloat(6, ma.getOriginal());
			cstmt.setFloat(7, ma.getRepostsperday());
			cstmt.setFloat(8, ma.getCommentperday());
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
	public Pagefile<AccountMonitor> queryPagefile(PagerDTO dto,
			Platform platform) {
		String whereSql = null;
		switch (platform) {
		case Sina:
			whereSql = "where platform=1";
			break;
		case Tencent:
			whereSql = "where platform=2";
			break;
		case Renmin:
			whereSql = "where platform=5";
			break;
		default:
			whereSql = "where platform=1";
			break;
		}
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<AccountMonitor> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Account_List");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "id");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<AccountMonitor>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				AccountMonitor ma = new AccountMonitor();
				ma.setId(rs.getInt("id"));
				ma.setUserid(rs.getInt("userId"));
				ma.setNick(rs.getString("nick"));
				ma.setUid(rs.getString("uid"));
				ma.setTail(rs.getString("tail"));
				ma.setPlatform(Utility.getPlatform(rs.getInt("platform")));
				ma.setStatus(rs.getInt("status"));
				ma.setOriginal(rs.getFloat("original"));
				ma.setRepostsperday(rs.getFloat("repostsperday"));
				ma.setCommentperday(rs.getFloat("commentperday"));
				pagefile.getRows().add(ma);
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
	public List<AccountMonitor> queryMAccountList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AccountMonitor> list = new ArrayList<AccountMonitor>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT a.*,b.name as dbname FROM Account_List a JOIN Base_DBServer b ON a.dbserver=b.id";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getAccountMonitor(rs));
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
	public List<AccountMonitor> queryAll(int status) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AccountMonitor> list = new ArrayList<AccountMonitor>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT a.*,b.name as dbname FROM Account_List a JOIN Base_DBServer b ON a.dbserver=b.id";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getAccountMonitor(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return list;
	}

}
