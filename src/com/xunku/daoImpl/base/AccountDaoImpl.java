package com.xunku.daoImpl.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.constant.AccountStatus;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AccountDao;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.base.WeiboAccountDTO;
import com.xunku.dto.sysManager.AccountVO;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;
import com.xunku.utils.StringUtils;

public class AccountDaoImpl implements AccountDao {

	@Override
	public long remainAuth(int accid) {
		String cmdText = "select expires_in,authTime from Base_Account_Auths where accountId = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int hour = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, accid);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				Date date = rs.getTimestamp(2);
				int seconds = rs.getInt(1);
				return this.getDistanceDays(date, seconds);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return hour;
	}

	private long getDistanceDays(Date authDate, int seconds) {
		long authTime = authDate.getTime();
		// 过期的时间
		long expireTime = authTime + seconds * 1000;
		// 现在时间和过期时间还差多久？
		return DateHelper.getDistanceDays(System.currentTimeMillis(),
				expireTime);
	}

	@Override
	public boolean checkAccountBind(String uid, Platform platform) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("select id from Base_Accounts where uid=? and platform=?");
			pstmt.setString(1, uid);
			pstmt.setInt(2, Utility.getPlatform(platform));
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

	@Override
	public List<AccountInfo> queryById(int[] cId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AccountInfo> list = null;
		try {
			String ids = StringUtils.arrayToString(cId);
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("select * from Base_Accounts a join Base_Account_Auths b on a.id=b.accountId where a.id in ("
							+ ids + ")");
			rs = pstmt.executeQuery();
			list = new ArrayList<AccountInfo>();
			while (rs.next()) {
				AccountInfo info = this.getAccountInfo(rs);
				info.setAuthTime(DateHelper.getSqlTime(rs
						.getTimestamp("authTime")));
				info.setExpiresin(rs.getInt("expires_in"));
				list.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	private AccountInfo getAccountInfo(ResultSet rs) throws SQLException {
		AccountInfo info = new AccountInfo();
		info.setCreator(rs.getInt("creator"));
		info.setId(rs.getInt("id"));
		info.setCustomId(rs.getInt("customId"));
		info.setName(rs.getString("name"));
		info.setUcode(rs.getString("ucode"));
		info.setUid(rs.getString("uid"));
		info.setVerfiy(rs.getBoolean("verify"));
		info.setLoadedFullFans(rs.getBoolean("fansloaded"));
		info.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		return info;
	}

	@Override
	public Pagefile<WeiboAccountDTO> queryByUserID(PagerDTO dto, int userId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<WeiboAccountDTO> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "vi_Account_queryByUserID");
			cstmt.setString(2, "id,ucode,uid,name,platform,expired,customId");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt.setString(5,
					"where customId = (select CustomID from Base_Users where ID="
							+ userId + ")");
			cstmt.setString(6, "id");
			cstmt.setInt(7, 0);
			cstmt.execute();
			rs = cstmt.getResultSet();
			pagefile = new Pagefile<WeiboAccountDTO>();
			while (rs.next()) {
				WeiboAccountDTO wa = new WeiboAccountDTO();
				wa.setId(rs.getInt("id"));
				wa.setName(rs.getString("uid"));
				wa.setNickName(rs.getString("name"));
				String startTime = DateUtils
						.nowDateFormat("yyyy-MM-dd HH:mm:ss");
				String endTime = rs.getString("expired");
				int day = DateUtils.beforDays(startTime, endTime);
				if (day > 0) {
					wa.setStatus(AccountStatus.NOEXPIRES);
					wa.setDay(day);
				} else {
					wa.setStatus(AccountStatus.EXPIRES);
					wa.setDay(0);
				}
				pagefile.getRows().add(wa);
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				if (rs2.next()) {
					pagefile.setRealcount(rs2.getInt("RecordCount"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs2, rs, cstmt, conn);
		}
		return pagefile;
	}

	@Override
	public void insert(AccountInfo wa, AccountAuths auth, int userId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_BaseAccount_insert(?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, wa.getUcode());
			cstmt.setString(2, wa.getUid());
			cstmt.setInt(3, wa.getCustomId());
			cstmt.setInt(4, wa.getCreator());
			cstmt.setString(5, wa.getName());
			cstmt.setInt(6, userId);
			cstmt.setInt(7, auth.getPlatform());
			cstmt.setString(8, auth.getToken());
			cstmt.setInt(9, auth.getAppId());
			cstmt.setInt(10, auth.getExpiresin());
			cstmt.setTimestamp(11, DateHelper.getSqlTime(auth.getAuthTime()));
			cstmt.setBoolean(12, wa.isVerify());
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public boolean deleteById(int aid,String wbname) {
		Connection conn = null;
		CallableStatement cstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sp_BaseAccount_deleteById(?,?)}");
			cstmt.setInt(1, aid);
			cstmt.setString(2, wbname);
			int flag = cstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
		return isDelete;
	}

	@Override
	public List<AccountVO> queryAccountByUid(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AccountVO> list = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			//String sql = "SELECT dbo.Base_Users.ID,dbo.Base_Accounts.uid,dbo.Base_Accounts.name, dbo.Base_Accounts.platform,dbo.Base_Account_Auths.expires_in, dbo.Base_Account_Auths.authTime FROM dbo.Base_Users  INNER JOIN dbo.My_Account ON dbo.Base_Users.ID = dbo.My_Account.userId INNER JOIN dbo.Base_Accounts ON dbo.My_Account.accountId = dbo.Base_Accounts.id INNER JOIN dbo.Base_Account_Auths ON dbo.Base_Accounts.uid = dbo.Base_Account_Auths.uid where dbo.Base_Users.ID=?";
			String sql = "SELECT dbo.Base_Users.ID,dbo.Base_Accounts.uid,dbo.Base_Accounts.name, dbo.Base_Accounts.platform,dbo.Base_Account_Auths.expires_in, dbo.Base_Account_Auths.authTime FROM dbo.Base_Users INNER JOIN dbo.Base_Accounts ON dbo.Base_Users.CustomID = dbo.Base_Accounts.customId INNER JOIN dbo.Base_Account_Auths ON dbo.Base_Accounts.uid = dbo.Base_Account_Auths.uid where dbo.Base_Users.ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			list = new ArrayList<AccountVO>();
			while (rs.next()) {
				AccountVO avo = new AccountVO();
				avo.setId(rs.getInt("id"));
				avo.setUid(rs.getString("uid"));
				avo.setName(rs.getString("name"));
				avo.setType(rs.getInt("platform"));
				avo.setAuthTime(DateHelper.getSqlTime(rs
						.getTimestamp("authTime")));
				avo.setExpiresin(rs.getInt("expires_in"));
				list.add(avo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	@Override
	public AccountInfo queryByUid(String uid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountInfo info = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT id,ucode,uid,customId,creator,name,platform,verify,fansloaded  FROM Base_Accounts WHERE uid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				info = this.getAccountInfo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return info;
	}

	@Override
	public Pagefile<AccountInfo> queryPagefile(PagerDTO dto, int userId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<AccountInfo> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Base_Accounts");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt.setString(5,
					"where customId=(SELECT CustomID FROM Base_Users WHERE ID="
							+ userId + ")");
			cstmt.setString(6, "id");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<AccountInfo>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.getAccountInfo(rs));
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

	@Override
	public boolean checkAccountExist(String uid, int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT id FROM Base_Accounts WHERE uid=? AND creator=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			pstmt.setInt(2, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("id") > 0) {
					isExists = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isExists;
	}

	private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
			Connection conn) {
		this.CloseStatus(null, null, rs, null, pstmt, conn);
	}

	private void CloseStatus(CallableStatement cstmt, Connection conn) {
		this.CloseStatus(null, null, cstmt, conn);
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
	public List<AccountInfo> queryByCustomId(int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AccountInfo> infoList = new ArrayList<AccountInfo>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Base_Accounts WHERE customId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				infoList.add(this.getAccountInfo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return infoList;
	}

	@Override
	public AccountInfo queryById(int accountId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountInfo info = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("SELECT id,ucode,uid,customId,creator,name,platform,verify FROM Base_Accounts WHERE ID=?");
			pstmt.setInt(1, accountId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				info = this.getAccountInfo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return info;
	}

	@Override
	public List<AccountInfo> queryByUserId(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AccountInfo> infoList = new ArrayList<AccountInfo>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from vi_User_Account where ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				infoList.add(this.getAccountInfo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return infoList;
	}

	@Override
	public int queryCountByCustomid(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(a.id) as count from Base_Accounts a inner join Base_Account_Auths b on a.id=b.accountId where a.customId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return count;
	}

	@Override
	public void updateFansLoaded(int accid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Base_Accounts set fansloaded=1 where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accid);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}

	}
}
