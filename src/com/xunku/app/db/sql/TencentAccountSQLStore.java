package com.xunku.app.db.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.db.cache.AccountRedisDB;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.accounts.AccountFactory;
import com.xunku.app.model.accounts.AccountTencent;

/**
 * 腾讯帐号SQL存储
 * 
 * @author wujian
 * @created on Jul 29, 2014 4:23:58 PM
 */
public class TencentAccountSQLStore extends AccountSQLStore {

	final String followingTable = "t_followings";
	final String followerTable = "t_followers";
	final String userTable = "t_users";

	public TencentAccountSQLStore(Pooling pool, Platform platform) {
		super(pool, platform);
	}

	@Override
	public IAccount getAccountByUcode(String ucode) {

		// 这里的ucode指的是腾讯的name
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			String sql = "SELECT * FROM " + this.userTable + " WHERE name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ucode);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				IAccount acc = this.readAccount(rs);
				return acc;
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
	}

	@Override
	public IAccount getAccountByName(String nick) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			String sql = "SELECT * FROM " + this.userTable + " WHERE nick = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				IAccount acc = this.readAccount(rs);
				return acc;
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
	}

	@Override
	public PutResult updateAccount(IAccount account) {
		Connection conn = null;
		CallableStatement cs = null;

		try {
			if (account.getPlatform() == Platform.Tencent) {
				AccountTencent acc = (AccountTencent) account;
				if (Utility.isNullOrEmpty(acc.getUcode())) {
					return PutResult.Nothing;
				}
				conn = this._pool.getConnection();
				cs = conn
						.prepareCall("{call sys_put_t_user(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

				cs.setString(1, acc.getUcode());
				cs.setString(2, acc.getOpenId());
				cs.setInt(3, 0);
				cs.setInt(4, 0);
				cs.setInt(5, 0);
				cs.setInt(6, acc.getFollowers());
				cs.setInt(7, 0);
				cs.setString(8, acc.getHead());
				cs.setString(9, acc.getHomePage());
				cs.setInt(10, acc.getFriends());

				cs.setString(11, acc.getDescription());
				cs.setBoolean(12, acc.isVerified());
				cs.setString(13, acc.getLocation());
				cs.setInt(14, 0);
				cs.setLong(15, acc.getCreated());
				int gender = 3;
				if (acc.getGender() == GenderEnum.Famale) {
					gender = 2;
				} else if (acc.getGender() == GenderEnum.Male) {
					gender = 1;
				}
				cs.setInt(16, gender);

				cs.setString(17, null);
				cs.setInt(18, acc.getWeibos());
				cs.setString(19, null);// @verifyinfo
				cs.setString(20, null);// @exp
				cs.setInt(21, 0);// @level
				cs.setInt(22, 10);//
				cs.setLong(23, System.currentTimeMillis());
				cs.setBoolean(24, acc.isXunku());// isXunku
				cs.setString(25, acc.getDisplayName());// @nick

				cs.registerOutParameter(26, java.sql.Types.INTEGER);

				cs.execute();

				int result = cs.getInt(26);
				PutResult pR = PutResult.Nothing;
				if (result == 1) {
					pR = PutResult.Add;
				} else if (result == 2) {
					pR = PutResult.Set;
				}
				return pR;
			}
		} catch (SQLException e) {
			System.out.println("更新微博用户出错:"+e.getMessage());
			//e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cs, null);
		}
		return PutResult.Nothing;
	}

	@Override
	public void putFollowings(String ucode, List<String> followings) {
		// 有优化的余地，可批量更新
		for (String follower : followings) {
			this.putFollowing(ucode, follower);
		}

	}

	@Override
	public void putFollowers(String ucode, List<String> followers) {
		for (String follower : followers) {
			this.putFollower(ucode, follower);
		}

	}

	@Override
	public void setFollowerCache(AccountRedisDB cache, String ucode) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			pstmt = conn.prepareStatement("SELECT follower,timestamp FROM "
					+ this.followerTable
					+ " WHERE ucode=? order by timestamp desc");
			pstmt.setString(1, ucode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cache.putFollower(ucode, _platform, rs.getString(1), rs
						.getLong("timestamp"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}

	}

	@Override
	public void fillUsersCache(AccountRedisDB cache) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM " + this.userTable);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				IAccount acc = this.readAccount(rs);
				cache.addAccount(acc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
	}

	@Override
	protected IAccount readAccount(ResultSet rs) {
		try {
			AccountTencent acc = new AccountTencent();
			acc.setOpenId(rs.getString("openid"));
			acc.setCity(AccountFactory.createCity(rs.getInt("province_code"),
					rs.getInt("city_code"), Platform.Tencent));
			acc.setRegTime(rs.getLong("regtime"));
			acc.setIntroduction(rs.getString("introduction"));
			acc.setFollowers(rs.getInt("fansnum"));
			acc.setFollowings(rs.getInt("idolnum"));
			int gender = rs.getInt("sex");
			if (gender == 1) {
				acc.setGender(GenderEnum.Male);
			} else if (gender == 2) {
				acc.setGender(GenderEnum.Famale);
			} else {
				acc.setGender(GenderEnum.Unknow);
			}
			acc.setHead(rs.getString("head"));
			acc.setLocation(rs.getString("location"));
			acc.setName(rs.getString("name"));
			acc.setTweets(rs.getInt("tweetnum"));
			acc.setTimestamp(System.currentTimeMillis());
			acc.setUcode(acc.getName());
			acc.setHomePage(rs.getString("homepage"));
			acc.setVerified(rs.getBoolean("isvip"));
			acc.setNick(rs.getString("nick"));
			acc.setXunku(true);// 都是讯库来的
			return acc;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void putFollower(String ucode, String follower) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			pstmt = conn.prepareStatement("if not exists(select name from "
					+ this.followerTable
					+ " where name=? and follower_name=?)INSERT INTO "
					+ this.followerTable
					+ " (name,follower_name,timestamp)VALUES(?,?,?)");
			pstmt.setString(1, ucode);
			pstmt.setString(2, follower);
			pstmt.setString(3, ucode);
			pstmt.setString(4, follower);
			pstmt.setLong(5, System.currentTimeMillis());
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}

	}

	@Override
	public void putFollowing(String ucode, String following) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			pstmt = conn.prepareStatement("if not exists(select name from "
					+ this.followingTable
					+ " where name=? and following_name=?)INSERT INTO "
					+ this.followingTable
					+ " (name,following_name,timestamp)VALUES(?,?,?)");
			pstmt.setString(1, ucode);
			pstmt.setString(2, following);
			pstmt.setString(3, ucode);
			pstmt.setString(4, following);
			pstmt.setLong(5, System.currentTimeMillis());
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}

	}

}
