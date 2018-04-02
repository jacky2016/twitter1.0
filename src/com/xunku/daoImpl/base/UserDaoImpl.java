package com.xunku.daoImpl.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.model.ClientLoginTimes;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.UserDao;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.pushservices.PushUserDTO;
import com.xunku.pojo.base.CustomRole;
import com.xunku.pojo.base.User;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class UserDaoImpl implements UserDao {

	@Override
	public void insert(User user) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_BaseUser_insert(?,?,?,?,?,?,?,?)}");
			cstmt.setInt(1, user.getCustomID());
			cstmt.setString(2, user.getUserName());
			cstmt.setString(3, user.getNickName());
			cstmt.setString(4, Utility.MD5(user.getToken()));
			cstmt.setString(5, user.getEmail());
			cstmt.setString(6, user.getTel());
			cstmt.setBoolean(7, user.isAdmin());
			cstmt.setInt(8, user.getRole().getId());
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	private User getUser(ResultSet rs) throws SQLException {
		User ud = new User();
		ud.setId(rs.getInt("ID"));
		ud.setCustomID(rs.getInt("CustomID"));
		ud.setUserName(rs.getString("UserName"));
		ud.setNickName(rs.getString("NickName"));
		ud.setToken(rs.getString("passwd"));
		ud.setEmail(rs.getString("Email"));
		ud.setTel(rs.getString("Tele"));
		ud.setAdmin(rs.getBoolean("IsAdmin"));
		ud.setCheckid(rs.getInt("checkid"));
		return ud;
	}

	@Override
	public Pagefile<User> queryByAll(PagerDTO dto, int uid) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<User> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "vi_BaseUser_queryByAll");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt
					.setString(
							5,
							"where IsEnabled=1 and IsAdmin=0 and CustomID = (select CustomID from Base_Users where ID="
									+ uid + ")");
			cstmt.setString(6, "ID");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<User>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				User user = this.getUser(rs);
				CustomRole role = new CustomRole();
				role.setId(rs.getInt("CustomRoleId"));
				role.setName(rs.getString("Name"));
				user.setRole(role);
				pagefile.getRows().add(user);
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
	public boolean deleteByID(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Base_Users SET IsEnabled=? WHERE ID=? "
						+"delete from Task_SubscriberContact where ContactID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, false);
			pstmt.setInt(2, id);
			pstmt.setInt(3, id);
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
	public void updateByID(User user) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_BaseUser_updateByID(?,?,?,?,?)}");
			cstmt.setString(1, user.getNickName());
			cstmt.setString(2, user.getEmail());
			cstmt.setString(3, user.getTel());
			cstmt.setInt(4, user.getId());
			cstmt.setInt(5, user.getRole().getId());
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public List<PushUserDTO> queryUserByUID(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<PushUserDTO> list = new ArrayList<PushUserDTO>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select ID,NickName,Email from Base_Users where IsEnabled=1 and CustomID = (select CustomID from Base_Users where ID=?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PushUserDTO dto = new PushUserDTO();
				dto.setId(rs.getInt("ID"));
				dto.setUseName(rs.getString("NickName"));
				dto.setEmail(rs.getString("Email"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	@Override
	public void updateByUid(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = null;
			if (user.getToken() == null || user.getToken().length() == 0) {
				sql = "UPDATE Base_Users SET Email=?,Tele=? WHERE ID=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, user.getEmail());
				pstmt.setString(2, user.getTel());
				pstmt.setInt(3, user.getId());
				pstmt.executeUpdate();
			} else {
				sql = "UPDATE Base_Users SET Email=?,Tele=?,passwd=? WHERE ID=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, user.getEmail());
				pstmt.setString(2, user.getTel());
				pstmt.setString(3, Utility.MD5(user.getToken()));
				pstmt.setInt(4, user.getId());
				pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public User queryAdmin(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Base_Users where CustomID = ? and IsAdmin =1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = this.getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return user;
	}

	@Override
	public User queryByUid(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Base_Users where ID =? and IsEnabled=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = this.getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return user;
	}

	@Override
	public User queryByUserid(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = new User();
		;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select a.ID,a.CustomID,a.UserName,a.NickName,a.Email,a.Tele,a.IsAdmin,a.CreateTime,a.IsEnabled,a.passwd,a.checkid,b.UserId,b.CustomRoleId from Base_Users as a join Base_CustomRole_User as b on a.ID = b.UserId where a.ID=? and a.IsEnabled=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				user.setId(rs.getInt("ID"));
				user.setCustomID(rs.getInt("CustomID"));
				user.setUserName(rs.getString("UserName"));
				user.setNickName(rs.getString("NickName"));
				user.setToken(rs.getString("passwd"));
				user.setEmail(rs.getString("Email"));
				user.setTel(rs.getString("Tele"));
				user.setAdmin(rs.getBoolean("IsAdmin"));
				user.setCheckid(rs.getInt("checkid"));
				CustomRole crole = new CustomRole();
				crole.setId(rs.getInt("UserId"));
				crole.setCustomId(rs.getInt("CustomRoleId"));
				user.setRole(crole);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return user;
	}

	@Override
	public boolean checkPWD(int uid, String token) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isTrue = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from Base_Users where ID=? and passwd=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setString(2, Utility.MD5(token));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("count") > 0) {
					isTrue = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			isTrue = false;
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isTrue;
	}

	@Override
	public User getUserByName(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Base_Users WHERE UserName=? and IsEnabled=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = this.getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return user;
	}

	private void CloseStatus(PreparedStatement pstmt, Connection conn) {
		this.CloseStatus(null, pstmt, conn);
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
	public User queryUserByIsAdmin(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Base_Users where IsAdmin=1 and CustomID = (select CustomID from Base_Users where ID=?)  and IsEnabled=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = this.getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return user;
	}

	@Override
	public boolean checkIsExsit(String name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExsit = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select ID from Base_Users where UserName=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isExsit = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isExsit;
	}

	@Override
	public List<User> queryUserByCid(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<User> list = new ArrayList<User>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Base_Users where CustomID = ?  and IsEnabled=1";
			pstmt = conn.prepareStatement(sql);System.out.println(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getUser(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	@Override
	public boolean resetPWD(int userid, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean reset = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Base_Users SET passwd=? WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Utility.MD5(password));
			pstmt.setInt(2, userid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				reset = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return reset;
	}

	@Override
	public boolean updateApproved(int userid, int checkid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean reset = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Base_Users SET checkid=? WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, checkid);
			pstmt.setInt(2, userid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				reset = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return reset;
	}

	@Override
	public int getUserByCustomid(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from Base_Users where CustomID=? and IsEnabled=1 and IsAdmin = 0";
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

	/*
	 * @Override public User getUserByTid(String tid, String uid, int platform) {
	 * Connection conn = null; PreparedStatement pstmt = null; ResultSet rs =
	 * null; User user = null; try { conn =
	 * DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	 * String sql = "select * from Base_Users where ID = " + "(select userid
	 * from My_Sending where id = " + "(select sid from My_Sender where tid=?
	 * and uid=? and platform=?))"; pstmt = conn.prepareStatement(sql);
	 * pstmt.setString(1, tid); pstmt.setString(2, uid); pstmt.setInt(3,
	 * platform); rs = pstmt.executeQuery(); while (rs.next()) { user =
	 * this.getUser(rs); } } catch (SQLException e) { e.printStackTrace(); }
	 * finally { this.CloseStatus(rs, pstmt, conn); } return user; }
	 */

	@Override
	public ClientLoginTimes getLoginTimes(String clientId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ClientLoginTimes times = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select clientid,times,expired from Base_Client_Login_Times where clientid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, clientId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				times = new ClientLoginTimes();
				times.setClientId(clientId);
				times.setExpired(rs.getLong("expired"));
				times.setTimes(rs.getInt("times"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return times;
	}

	@Override
	public void addLoginTimes(String clientId, long expired) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Base_Client_Login_Times (times,clientid,expired,timestamp)values(1,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, clientId);
			pstmt.setLong(2, expired);
			pstmt.setLong(3, System.currentTimeMillis());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void incLoginTimes(String clientId, long expired) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Base_Client_Login_Times set times=times+1,expired=?,timestamp=? where clientid=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, expired);
			pstmt.setLong(2, System.currentTimeMillis());
			pstmt.setString(3, clientId);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void clearLoginTimes(String clientId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Base_Client_Login_Times set times =0 where clientid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, clientId);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}

	}

	@Override
	public User queryByid(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Base_Users where ID =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = this.getUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return user;
	}
}
