package com.xunku.daoImpl.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.model.people.PUser;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AccountAuthsDao;
import com.xunku.dto.sysManager.PeopleDTO;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;
import com.xunku.utils.StringUtils;

public class AccountAuthsDaoImpl implements AccountAuthsDao {

	@Override
	public List<AccountAuths> queryAuthByAccountId(int accountid, int appid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		List<AccountAuths> auths = new ArrayList<AccountAuths>();
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from vi_AccountAuths_queryPagefile where accountId=? and appid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accountid);
			pstmt.setInt(2, appid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				auths.add(this.getAccountAuths(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return auths;
	}

	private AccountAuths getAccountAuths(ResultSet rs) throws SQLException {
		AccountAuths auths = new AccountAuths();
		auths.setId(rs.getInt("id"));
		auths.setAccountId(rs.getInt("accountId"));
		auths.setUcode(rs.getString("ucode"));
		auths.setUid(rs.getString("uid"));
		auths.setCustomId(rs.getInt("CustomID"));
		auths.setName(rs.getString("name"));
		auths.setPlatform(rs.getInt("platform"));
		auths.setAppId(rs.getInt("appId"));
		auths.setToken(rs.getString("token"));
		auths.setExpiresin(rs.getInt("expires_in"));
		auths.setAuthTime((java.util.Date) rs.getTimestamp("authTime"));
		auths.setCustomId(rs.getInt("customId"));
		return auths;
	}

	@Override
	public List<AccountAuths> queryAuthsByAccountIds(int[] accountids, int appid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		List<AccountAuths> authList = new ArrayList<AccountAuths>();
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String aids = StringUtils.arrayToString(accountids);
			String sql = "select * from vi_AccountAuths_queryPagefile where accountId in ("
					+ aids + ") and appid=" + appid;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				authList.add(this.getAccountAuths(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return authList;
	}

	@Override
	public boolean insert(AccountAuths auth) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isInsert = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "INSERT INTO Base_Account_Auths(accountId,uid,platform,token,appId,expires_in,authTime) values (?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, auth.getAccountId());
			pstmt.setString(2, auth.getUid());
			pstmt.setInt(3, auth.getPlatform());
			pstmt.setString(4, auth.getToken());
			pstmt.setInt(5, auth.getAppId());
			pstmt.setInt(6, auth.getExpiresin());
			pstmt.setTimestamp(7, DateHelper.getSqlTime(auth.getAuthTime()));
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isInsert = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isInsert;
	}

	@Override
	public boolean update(AccountAuths auth) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Base_Account_Auths SET uid=?,platform=?,token=?,authTime=? WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, auth.getUid());
			pstmt.setInt(2, auth.getPlatform());
			pstmt.setString(3, auth.getToken());
			pstmt.setTimestamp(4, DateHelper.getSqlTime(auth.getAuthTime()));
			pstmt.setInt(6, auth.getId());
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isUpdate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isUpdate;
	}

	@Override
	public boolean deleteById(int authId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Base_Account_Auths WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authId);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isDelete;
	}

	@Override
	public Pagefile<AccountAuths> queryPagefile(Pager dto, int userId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<AccountAuths> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Base_Accounts");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.getPageSize());
			cstmt.setInt(4, dto.getPageIndex());
			cstmt.setString(5,
					"where customId=(SELECT CustomID FROM Base_Users WHERE ID="
							+ userId + ")");
			cstmt.setString(6, "id");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<AccountAuths>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				AccountAuths auths = new AccountAuths();
				auths.setId(rs.getInt("id"));
				auths.setUcode(rs.getString("ucode"));
				auths.setUid(rs.getString("uid"));
				auths.setCustomId(rs.getInt("CustomID"));
				auths.setName(rs.getString("name"));
				auths.setPlatform(rs.getInt("platform"));
				AccountAuths auth = queryAccountAuthsById(conn, rs.getInt("id"));
				if (auth != null) {
					auths.setAppId(auth.getAppId());
					auths.setToken(auth.getToken());
					auths.setExpiresin(auth.getExpiresin());
					auths.setAuthTime(auth.getAuthTime());
				}
				pagefile.getRows().add(auths);
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
			this.CloseStatus(rs2, rs, cstmt, conn);
		}
		return pagefile;
	}

	private AccountAuths queryAccountAuthsById(Connection conn, int aid) {
		String sql = "SELECT token,appId,expires_in,authTime FROM Base_Account_Auths WHERE accountId=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountAuths auths = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, aid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				auths = new AccountAuths();
				auths.setToken(rs.getString("token"));
				auths.setAppId(rs.getInt("appId"));
				auths.setExpiresin(rs.getInt("expires_in"));
				auths.setAuthTime((java.util.Date) rs.getTimestamp("authTime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return auths;
	}

	@Override
	public boolean insertPeople(PeopleDTO people) {
		Connection conn = null;
		CallableStatement cstmt = null;
		boolean isInsert = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_BaseAccount_insertPeople(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setInt(1, people.getCustomId());
			cstmt.setInt(2, people.getUserid());
			cstmt.setString(3, people.getUsername());
			cstmt.setString(4, people.getPassword());
			cstmt.setLong(5, people.getUser().getPuserId());
			cstmt.setString(6, people.getUser().getNickName());
			cstmt.setString(7, people.getUser().getLocation());
			cstmt.setInt(8, Utility.getSexform(people.getUser().getGender()));
			cstmt.setString(9, people.getUser().getHeadUrl());
			cstmt.setString(10, people.getUser().getPersonUrl());
			int flag = cstmt.executeUpdate();
			if (flag > 0) {
				isInsert = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
		return isInsert;
	}

	@Override
	public boolean checkAccountBind(String name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("select id from Account_People where username=?");
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return false;

	}

	private void CloseStatus(PreparedStatement pstmt, Connection conn) {
		this.CloseStatus(null, pstmt, conn);
	}

	private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
			Connection conn) {
		this.CloseStatus(null, null, rs, null, pstmt, conn);
	}

	private void CloseStatus(ResultSet rs2, ResultSet rs,
			CallableStatement cstmt, Connection conn) {
		this.CloseStatus(null, rs2, rs, cstmt, null, conn);
	}

	private void CloseStatus(ResultSet rs3, ResultSet rs2, ResultSet rs,
			CallableStatement cstmt, PreparedStatement pstmt, Connection conn) {

		try {
			if (rs3 != null) {
				rs3.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
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

	@Override
	public PUser queryPeopleUser(long peopleId) {
		String sql = "SELECT * FROM Account_People WHERE peopleid=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		PUser user = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, peopleId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new PUser();
				user.setGender(rs.getInt("sex"));
				user.setHeadUrl(rs.getString("headUrl"));
				user.setLocation(rs.getString("location"));
				user.setLoginName(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setNickName(rs.getString("nickName"));
				user.setUserId(rs.getInt("peopleid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return user;
	}
}
