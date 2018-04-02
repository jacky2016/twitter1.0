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
import com.xunku.app.model.accounts.AccountSina;

/**
 * 新浪SQL存储
 * 
 * @author wujian
 * @created on Jul 29, 2014 4:24:09 PM
 */
public class SinaAccountSQLStore extends AccountSQLStore {

	public SinaAccountSQLStore(Pooling pool, Platform platform) {
		super(pool, platform);
	}

	private final String followingTable = "s_followings";
	private final String followerTable = "s_followers";
	private final String userTable = "s_users";

	@Override
	protected IAccount readAccount(ResultSet rs) {
		try {
			AccountSina acc = new AccountSina();
			acc.setUid(rs.getLong("uid"));
			acc.setCity(AccountFactory.createCity(rs.getInt("province"), rs
					.getInt("city"), Platform.Sina));
			acc.setCreatedAt(rs.getLong("created_at"));
			acc.setDescription(rs.getString("description"));
			acc.setDomain(rs.getString("domain"));
			acc.setFollowers(rs.getInt("followers_count"));
			acc.setFriends(rs.getInt("friends_count"));
			int gender = rs.getInt("gender");
			if (gender == 1) {
				acc.setGender(GenderEnum.Male);
			} else if (gender == 2) {
				acc.setGender(GenderEnum.Famale);
			} else {
				acc.setGender(GenderEnum.Unknow);
			}
			acc.setHead(rs.getString("profile_image_url"));
			acc.setLargeHead(rs.getString("avatar_large"));
			acc.setLocation(rs.getString("location"));
			acc.setName(rs.getString("name"));
			acc.setRLevel(0);
			acc.setScreenName(rs.getString("screen_name"));
			acc.setStatuses(rs.getInt("statuses_count"));
			acc.setTags(rs.getString("tags"));
			acc.setTimestamp(System.currentTimeMillis());
			acc.setUcode(acc.getUid());
			acc.setUrl(rs.getString("url"));
			acc.setVerified(rs.getBoolean("verified"));
			acc.setXunku(false);
			return acc;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public PutResult updateAccount(IAccount account) {
		Connection conn = null;
		CallableStatement cs = null;
		try {
			if (account.getPlatform() == Platform.Sina) {
				AccountSina acc = (AccountSina) account;
				conn = this._pool.getConnection();
				cs = conn
						.prepareCall("{call sys_put_s_user(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				cs.setString(1, acc.getUcode());
				cs.setString(2, acc.getDisplayName());
				cs.setString(3, acc.getName());
				int gender = 3;
				if (acc.getGender() == GenderEnum.Famale) {
					gender = 2;
				} else if (acc.getGender() == GenderEnum.Male) {
					gender = 1;
				}
				cs.setInt(4, gender);
				cs.setLong(5, acc.getCreated());
				cs.setInt(6, acc.getFollowers());
				cs.setInt(7, acc.getFriends());
				cs.setInt(8, acc.getWeibos());
				cs.setString(9, acc.getHead());
				cs.setBoolean(10, acc.isVerified());
				if (acc.getCity() == null) {
					cs.setInt(11, 0);
				} else {
					cs.setInt(11, acc.getCity().getCode());
				}
				cs.setInt(12, acc.getProvince());
				cs.setString(13, acc.getLocation());
				cs.setString(14, acc.getDescription());
				cs.setLong(15, acc.getTimestamp());
				cs.setInt(16, acc.getLevel());
				cs.setInt(17, acc.getFavourites());
				cs.setString(18, acc.getProfileUrl());
				cs.setString(19, acc.getDomain());
				cs.setString(20, acc.getWeihao());
				cs.setString(21, acc.getUrl());
				cs.setString(22, Utility.getTags(acc.getTags()));
				cs.setString(23, acc.getLargeHead());
				cs.setBoolean(24, acc.isXunku());
				cs.registerOutParameter(25, java.sql.Types.INTEGER);

				cs.execute();

				int result = cs.getInt(25);
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
	public IAccount getAccountByUcode(String uid) {
		if (!Utility.isNumber(uid)) {
			return null;
		}
		Long luid = Long.parseLong(uid);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			String sql = "SELECT * FROM " + this.userTable + " WHERE uid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, luid);
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
	public void putFollower(String ucode, String follower) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			pstmt = conn.prepareStatement("if not exists(select uid from "
					+ this.followerTable
					+ " where uid=? and follower_uid=?)INSERT INTO "
					+ this.followerTable
					+ " (uid,follower_uid,timestamp)VALUES(?,?,?)");
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
	public void putFollowing(String ucode, String follower) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			pstmt = conn.prepareStatement("if not exists(select uid from "
					+ this.followingTable
					+ " where uid=? and following_uid=?)INSERT INTO "
					+ this.followingTable
					+ " (uid,following_uid,timestamp)VALUES(?,?,?)");
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
	public void putFollowings(String ucode, List<String> followings) {
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
	public IAccount getAccountByName(String name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			String sql = "SELECT * FROM " + this.userTable
					+ " WHERE screen_name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
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

}
