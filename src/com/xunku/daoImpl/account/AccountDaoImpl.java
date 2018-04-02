package com.xunku.daoImpl.account;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.Pooling;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.constant.PortalCST;
import com.xunku.dao.account.AccountDao;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class AccountDaoImpl implements AccountDao {

	private AccountMonitor fillAccountMonitor(ResultSet rs) throws SQLException {
		AccountMonitor ma = new AccountMonitor();
		ma.setId(rs.getInt("id"));
		ma.setUserid(rs.getInt("userId"));
		ma.setNick(rs.getString("nick"));
		ma.setUid(rs.getString("uid"));
		ma.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		ma.setOriginal(rs.getFloat("original"));
		ma.setRepostsperday(rs.getFloat("repostsperday"));
		ma.setCommentperday(rs.getFloat("commentperday"));
		ma.setDbserver(rs.getString("poolname"));
		ma.setWeibos(rs.getInt("weibos"));
		ma.setFans(rs.getInt("fans"));
		ma.setFriends(rs.getInt("friends"));
		return ma;
	}

	@Override
	public void updateMonitorCnt(int id, int weibos, int fans, int friends) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("update Account_list set weibos=?,fans=?,friends=? where id = ?");
			cstmt.setInt(1, weibos);
			cstmt.setInt(2, fans);
			cstmt.setInt(3, friends);
			cstmt.setInt(4, id);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public Pagefile<AccountMonitor> queryAccountList(Pager pager) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<AccountMonitor> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "View_Account_List");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, " where 1=1 ");
			cstmt.setString(6, "id");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<AccountMonitor>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.fillAccountMonitor(rs));
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

	@Override
	public Pagefile<AccountMonitor> queryAccountList(Pager dto, int userid,
			Platform platform) {

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<AccountMonitor> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "vi_AccountList_queryAccountList");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.getPageSize());
			cstmt.setInt(4, dto.getPageIndex());
			cstmt.setString(5,
					" where customID=(select CustomID from Base_Users where id="
							+ userid + ") and platform= "
							+ Utility.getPlatform(platform));
			cstmt.setString(6, "id");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<AccountMonitor>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.fillAccountMonitor(rs));
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
	public boolean insert(AccountMonitor acc) {
		Connection conn = null;
		CallableStatement cstmt = null;
		boolean isInsert = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_AccountList_insert(?,?,?,?,?,?)}");
			cstmt.setInt(1, acc.getUserid());
			cstmt.setString(2, acc.getNick());
			cstmt.setString(3, acc.getUid());
			cstmt.setInt(4, acc.getCustomId());
			cstmt.setInt(5, Utility.getPlatform(acc.getPlatform()));
			cstmt.registerOutParameter(6, java.sql.Types.INTEGER);//取得返回值

			cstmt.execute();
			int accountid = cstmt.getInt(6);
			// int flag = cstmt.executeUpdate();
			if (accountid > 0) {
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
	public boolean checkIsExsit(int customid, String name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExsit = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select id from Account_List as a inner join Account_Custom as b on a.id = b.accountid and b.CustomID=? and a.nick=?";
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
	public boolean checkIsAccount(int customid,int userid, String name){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExsit = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "  select id from vi_AccountList_queryAccountList where customid = ? and userId = ? and nick = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			pstmt.setInt(2, userid);
			pstmt.setString(3, name);
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
	public void deleteByEId(int id) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sp_Account_deleteById(?)}");
			cstmt.setInt(1, id);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public int getPeriodAccountCount(int userid, Platform platform) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from vi_AccountList_queryAccountList where customID=(select CustomID from Base_Users where id="
					+ userid
					+ ") and platform="
					+ Utility.getPlatform(platform);
			pstmt = conn.prepareStatement(sql);
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
	public AccountWarn queryAccountById(int accid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountWarn acc = new AccountWarn();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Office_Account_Warning where accid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, accid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				acc.setId(rs.getInt("id"));
				acc.setAccid(rs.getInt("accid"));
				acc.setGroupName(rs.getString("groupName"));
				acc.setKeyword(rs.getString("keyword"));
				acc.setReceiver(rs.getString("receiver"));
				acc.setType(rs.getString("type"));
				acc.setCustomid(rs.getInt("customid"));
				acc.setHappen(rs.getBoolean("happen"));// 是否发生了关键字预警
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
		return acc;
	}

	@Override
	public void updateRealtime(Pooling pool, int id, long timezone, int fans) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_MAccount_Fans_Statis(?,?,?)}");
			cstmt.setInt(1, id);
			cstmt.setDate(2, new java.sql.Date(timezone));
			cstmt.setInt(3, fans);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
		}
	}

	@Override
	public void updateRealtime(Pooling pool, int id, long timezone, int weibos,
			int retweets) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_MAccount_Trend_Statis(?,?,?,?)}");
			cstmt.setInt(1, id);
			cstmt.setDate(2, new java.sql.Date(timezone));
			cstmt.setInt(3, weibos);
			cstmt.setInt(4, retweets);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
		}

	}

	@Override
	public AccountMonitor queryAccountMonitorById(int accid) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AccountMonitor acc = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String cmdText = "select * from View_Account_List where id=?";
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, accid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				acc = this.fillAccountMonitor(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return acc;
	}

	@Override
	public void clear(long time) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("delete Base_Crawler_Task_List where created <?");
			cstmt.setLong(1, time);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
		}

	}

}
