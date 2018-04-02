package com.xunku.daoImpl.office;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IFiller;
import com.xunku.app.model.Query;
import com.xunku.constant.PortalCST;
import com.xunku.dao.office.AccountWarnDao;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class AccountWarnDaoImpl implements AccountWarnDao, IFiller<AccountWarn> {

	@Override
	public boolean deleteById(int awid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "delete Office_Account_Warning where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, awid);
			int flag = pstmt.executeUpdate();
			;
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
	public int insert(AccountWarn aw) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int awid = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Office_Account_Warning (accid,groupName,keyword,receiver,type,customid) values (?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, aw.getAccid());
			pstmt.setString(2, aw.getGroupName());
			pstmt.setString(3, aw.getKeyword());
			pstmt.setString(4, aw.getReceiver());
			pstmt.setString(5, aw.getType());
			pstmt.setInt(6, aw.getCustomid());
			pstmt.execute();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				awid = rs.getInt(1);
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
		return awid;
	}

	@Override
	public List<AccountWarn> queryAccountWarnList(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AccountWarn> list = new ArrayList<AccountWarn>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Office_Account_Warning where customid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.fill(rs));
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
	public boolean update(AccountWarn aw) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Account_Warning set groupName=?,keyword=?,receiver=?,type=?,customid=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aw.getGroupName());
			pstmt.setString(2, aw.getKeyword());
			pstmt.setString(3, aw.getReceiver());
			pstmt.setString(4, aw.getType());
			pstmt.setInt(5, aw.getCustomid());
			pstmt.setInt(6, aw.getId());
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

	@Override
	public AccountWarn queryAccountWarnById(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountWarn warn = new AccountWarn();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Office_Account_Warning where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				warn = this.fill(rs);
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
		return warn;
	}

	@Override
	public Pagefile<AccountWarn> queryAccountWarnList(int customid, Pager pager) {
		Query query = new Query();
		query.setTableName("Office_Account_Warning");
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setSortField("created");
		query.setWhere(" where customid = " + customid);
		return DbHelper.executePager(query, this);
	}

	@Override
	public AccountWarn fill(ResultSet rs) throws SQLException {
		AccountWarn aw = new AccountWarn();
		aw.setId(rs.getInt("id"));
		aw.setAccid(rs.getInt("accid"));
		aw.setCustomid(rs.getInt("customid"));
		aw.setCreated(new Date(rs.getTimestamp("created").getTime()));
		aw.setGroupName(rs.getString("groupName"));
		aw.setKeyword(rs.getString("keyword"));
		aw.setReceiver(rs.getString("receiver"));
		aw.setType(rs.getString("type"));
		aw.setRunning(rs.getBoolean("isrunning"));
		aw.setSinceid(rs.getInt("sinceid"));
		aw.setHappen(rs.getBoolean("happen"));//是否发生了关键字预警
		return aw;
	}

	@Override
	public void changeRunning(int accountid, int customid, boolean running) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Account_Warning set isrunning=? where accid=? and customid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, running);
			pstmt.setInt(2, accountid);
			pstmt.setInt(3, customid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void updateSinceId(int warnid, int sinceId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Account_Warning set sinceid=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sinceId);
			pstmt.setInt(2, warnid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
		
	}

}
