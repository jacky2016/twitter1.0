package com.xunku.app.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xunku.app.Utility;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.helpers.LocationHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.KV;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.app.monitor.IMonitor;
import com.xunku.app.result.Result;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.spread.RetweetLevelResult;
import com.xunku.app.result.spread.RetweetStatisResult;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 传播分析存储
 * 
 * @author wujian
 * @created on Jul 16, 2014 5:18:57 PM
 */
public class MSpreadStore extends TweetStore {

	public MSpreadStore(IMonitor monitor, Pooling pooled, AccountManager accDB) {
		super(monitor, pooled, accDB);
	}

	public List<ITweet> queryAll() {
		String cmdText = "select * from " + this.getMonitor().getTableName();

		return this.executePostsQuery(cmdText);
	}
	
	/**
	 * 功能描述<查询top10的热门关键词>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,Integer>
	 * @version twitter 1.0
	 * @date Jul 23, 20145:52:02 PM
	 */
	public Map<String, Float> queryHotWord() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Float> map = new HashMap<String, Float>();
		try {
			conn = this.getPool().getConnection();
			String sql = "SELECT top 20 * FROM Spread_HotWord_Statis WHERE mid=? ORDER BY weight DESC,created desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, this.getMonitorID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("keyword"), rs.getFloat("weight"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return map;
	}

	/**
	 * 获得指定的一条微博
	 * 
	 * @param tid
	 * @param platform
	 * @return
	 */
	public ITweet getTweet(String tid, Platform platform) {
		return this.executePostQuery(tid, platform);
	}

	

	/**
	 * 获得转发列表
	 * 
	 * @param tid
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> retweetsGet(String tid, Platform platform,
			Pager pager) {
		return this.executePostsQueryWithPager(this.buildRetweetsQuery(tid,
				platform, pager));
	}

	/**
	 * 获得评论列表
	 * 
	 * @param tid
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> commentsGet(String tid, Platform platform,
			Pager pager) {
		return this.executePostsQueryWithPager(buildCommentsQuery(tid,
				platform, pager));
	}

	/**
	 * 查询来源
	 * 
	 * @param type
	 * @return
	 */
	public Map<Integer, Integer> queryFrom(PostType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			// 这个存储表里面都是当前检测对象的数据，只过滤类型即可
			String sql = "select top 8 * from Spread_Users_From_Statis where sid="
					+ this.getMonitorID();
			if (type != PostType.All) {
				sql = sql + " and type=" + Utility.getPostType(type);
			}
			sql += " order by value desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Map<Integer, Integer> result = new HashMap<Integer, Integer>();
			while (rs.next()) {
				result.put(rs.getInt("xfrom"), rs.getInt("value"));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return null;
	}

	public void clearFrom(PostType type) {
		String cmdText = "delete from Spread_Users_Vip_Statis where sid="
				+ this.getMonitorID() + " and type="
				+ Utility.getPostType(type);

		this.clear(cmdText);
	}

	/**
	 * 认证比例
	 * 
	 * @param type
	 * @return
	 */
	public VipResult queryVip(PostType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			// 这个存储表里面都是当前检测对象的数据，只过滤类型即可

			String cmdText = "select vips,novips from Spread_Users_Vip_Statis where sid="
					+ this.getMonitorID();

			if (type == PostType.All) {
				cmdText += " and (type =2 or type=3)";
			}
			if (type == PostType.Repost) {
				cmdText += " and type = 2";
			}
			if (type == PostType.Comment) {
				cmdText += " and type = 3";
			}

			pstmt = conn.prepareStatement(cmdText);
			rs = pstmt.executeQuery();
			VipResult result = new VipResult();
			int vips = 0;
			int novips = 0;
			while (rs.next()) {
				vips += rs.getInt("vips");
				novips += rs.getInt("novips");
			}
			result.setVipCnt(vips);
			result.setNoVipCnt(novips);
			return result;
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
		return null;
	}

	public void clearVip(PostType type) {
		String cmdText = "delete from Spread_Users_Vip_Statis where sid="
				+ this.getMonitorID() + " and type="
				+ Utility.getPostType(type);
		this.clear(cmdText);
	}

	/**
	 * 获得已经扩散的关键用户按转发排序
	 * 
	 * @return
	 */
	public List<RetweetStatisResult> querySpreadedKeyManByNums() {
		String sql = "select top 10 * from Spread_Retweet_Nums_Statis where sid="
				+ this.getMonitorID() + " order by retweets desc";
		return this.querySpread(sql, false);
	}

	public void clearKeyManNums() {
		String cmdText = "delete from Spread_Retweet_Nums_Statis where sid="
				+ this.getMonitorID();
		this.clear(cmdText);
	}

	/**
	 * 获得已经扩散的关键用户按粉丝排序
	 * 
	 * @param sort
	 */
	public List<RetweetStatisResult> querySpreadedKeyManByFans() {
		String sql = "select top 10 * from Spread_Retweet_Fans_Statis where sid="
				+ this.getMonitorID() + " order by followers desc";
		return this.querySpread(sql, false);
	}

	public void clearKeyManFans() {
		String cmdText = "delete from Spread_Retweet_Fans_Statis where sid="
				+ this.getMonitorID();
		this.clear(cmdText);
	}

	/**
	 * 获得未扩散的关键用户
	 * 
	 * @param isOnlyVip
	 *            是否只显示认证用户
	 * @return
	 */
	public List<RetweetStatisResult> queryUnspreadKeyMan(boolean isOnlyVip) {
		String sql = "select top 10 * from Spread_Retweet_Unspread_Statis where sid="
				+ this.getMonitorID()
				+ (isOnlyVip ? " and vip=1 " : "")
				+ "  order by followers desc";
		return this.querySpread(sql, false);
	}

	public void clearKeyManUN() {
		String cmdText = "delete from Spread_Retweet_Unspread_Statis where sid="
				+ this.getMonitorID();
		this.clear(cmdText);
	}

	/**
	 * 获得当前监测对象的转发层级分析
	 * 
	 * @return
	 */
	public List<RetweetLevelResult> queryRetweetLevel(Platform platform,
			int reposts) {

		List<RetweetLevelResult> levels = this.getLevels();

		int sumRepost = 0;

		for (RetweetLevelResult r : levels) {
			Map<String, Integer> map = this.getLevelDetails(r.getLevel());
			for (Map.Entry<String, Integer> e : map.entrySet()) {
				Result<IAccount> acc = this.getAccountManager()
						.accountGetByUcode(e.getKey(), platform);
				if (acc.getErrCode() == 0) {
					r.getSuperMans().add(
							new KV<IAccount, Integer>(acc.getData(), e
									.getValue()));
				}
			}
			Collections.sort(r.getSuperMans(),
					new Comparator<KV<IAccount, Integer>>() {
						@Override
						public int compare(KV<IAccount, Integer> o1,
								KV<IAccount, Integer> o2) {
							return o2.getValue() - o1.getValue();
						}
					});

			sumRepost += r.getTweets();
		}

		// 补齐API得不到的数据放到第一层里
		if (sumRepost < reposts) {
			int offset = reposts - sumRepost;
			if (levels.size() >= 1) {
				RetweetLevelResult r = levels.get(0);
				r.setTweets(r.getTweets() + offset);
			}
		}

		return levels;
	}

	public void clearRetweetLevel() {
		String cmdText = "delete from Spread_Retweet_Level_Statis where sid="
				+ this.getMonitorID();
		this.clear(cmdText);
	}

	/**
	 * 获得该监视项指定类型微博的数量
	 * 
	 * @param monitorId
	 * @param type
	 * @return
	 */
	public int count(PostType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			// 这个存储表里面都是当前检测对象的数据，只过滤类型即可

			String sql = "select count(tid) as cnt from "
					+ this.getMonitor().getTableName();
			if (type != PostType.All) {
				sql = sql + " where type='" + Utility.getPostType(type) + "'";
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int result = 0;
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return 0;
	}

	public int[] queryFansCount(PostType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			String sql = "select * from Spread_Users_Fans_Statis where sid="
					+ this.getMonitorID();
			if (type != PostType.All) {
				sql = sql + " AND type=" + Utility.getPostType(type);
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int[] result = new int[5];
			if (rs.next()) {
				result[0] = rs.getInt("c100");
				result[1] = rs.getInt("c1000");
				result[2] = rs.getInt("c1w");
				result[3] = rs.getInt("c10w");
				result[4] = rs.getInt("c100w");
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return null;

	}

	public void clearFansCount() {
		String cmdText = "delete from Spread_Users_Fans_Statis where sid="
				+ this.getMonitorID();

		this.clear(cmdText);
	}

	/**
	 * 获得该监视项指定类型微博作者的城市信息
	 * 
	 * @param monitorId
	 * @param type
	 * @return
	 */
	public Map<String, Integer> queryCity(PostType type, Platform platform) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			String sql = "select * from Spread_Users_City_Statis where sid="
					+ this.getMonitorID();
			if (type != PostType.All) {
				sql = sql + " AND type=" + Utility.getPostType(type);
			}
			sql += " order by value desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Map<Integer, Integer> result = new HashMap<Integer, Integer>();
			while (rs.next()) {
				int p = rs.getInt("province");
				int v = rs.getInt("value");
				if (!result.containsKey(p)) {
					result.put(p, v);
				} else {
					result.put(p, result.get(p) + v);
				}
			}
			Map<String, Integer> r = new HashMap<String, Integer>();
			for (Map.Entry<Integer, Integer> e : result.entrySet()) {
				r.put(LocationHelper.getProvinceName(e.getKey(), platform)
						.getName(), e.getValue());
			}
			return r;
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
		return null;
	}

	public void clearCity(PostType type) {
		String cmdText = "delete from Spread_Users_City_Statis where type='"
				+ Utility.getPostType(type) + "' AND sid="
				+ this.getMonitorID();

		this.clear(cmdText);
	}

	/**
	 * 返回用户粉丝柱状图数据
	 * 
	 * @param monitorId
	 * @return
	 */
	public int[] queryHistogram(PostType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			pstmt = conn
					.prepareStatement("select * from Spread_Users_Histogram_Statis where type='"
							+ Utility.getPostType(type)
							+ "' AND sid="
							+ this.getMonitorID());
			rs = pstmt.executeQuery();
			int[] result = new int[5];
			if (rs.next()) {
				result[0] = rs.getInt("v1");
				result[1] = rs.getInt("v2");
				result[2] = rs.getInt("v3");
				result[3] = rs.getInt("v4");
				result[4] = rs.getInt("v5");
			}
			return result;
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
		return null;
	}

	public void clearHistogram(PostType type) {
		String cmdText = "delete * from Spread_Users_Histogram_Statis where type='"
				+ Utility.getPostType(type)
				+ "' AND sid="
				+ this.getMonitorID();
		this.clear(cmdText);
	}

	/**
	 * 获得该监视项的性别比例统计
	 * 
	 * @param monitorId
	 * @return
	 */
	public Map<GenderEnum, Integer> queryGender(PostType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			String sql = "select * from Spread_Users_Gender_Statis where sid="
					+ this.getMonitorID();
			if (type != PostType.All) {
				sql = sql + " AND type =" + Utility.getPostType(type);
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Map<GenderEnum, Integer> result = new HashMap<GenderEnum, Integer>();
			int famales = 0;
			int males = 0;
			int unknows = 0;
			while (rs.next()) {
				famales += rs.getInt("famales");
				males += rs.getInt("males");
				unknows += rs.getInt("unknows");
			}
			result.put(GenderEnum.Famale, famales);
			result.put(GenderEnum.Male, males);
			result.put(GenderEnum.Unknow, unknows);
			return result;
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
		return null;
	}

	public void clearGender(PostType type) {
		String cmdText = "select * from Spread_Users_Gender_Statis where sid="
				+ this.getMonitorID();
		this.clear(cmdText);
	}

	/**
	 * 查询当前事件的趋势分析结果，不含详细信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<Long, Integer> queryTrend(PostType type) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			String sql = "select * from Spread_Trend_Statis WHERE sid="
					+ this.getMonitorID();
			if (type != PostType.All) {
				sql = sql + " AND type=" + Utility.getPostType(type);
			}
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Map<Long, Integer> result = new TreeMap<Long, Integer>(this
					.getLongComparator());
			while (rs.next()) {
				long timezone = DateHelper.getTimezoneHour(rs
						.getTimestamp("timezone"));
				System.out.println(new Date(timezone));
				if (!result.containsKey(timezone)) {
					result.put(timezone, rs.getInt("value"));
				}
			}
			return result;
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
		return null;
	}

	public void clearTrend(PostType type) {
		String cmdText = "delete from Spread_Trend_Statis WHERE type='"
				+ Utility.getPostType(type) + "' AND sid="
				+ this.getMonitorID();
		this.clear(cmdText);
	}

	/**
	 * 转发态度
	 * <p>
	 * 按粉丝数分组显示转发是否写转发内容了
	 */
	public double[] queryRetweetWithText() {
		// 返回转发总数
		// 在这个总数基础上再返回写
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double[] result = new double[2];
		try {
			conn = this.getPool().getConnection();
			String sql = "select * from Spread_Retweet_Attitude_Statis where sid="
					+ this.getMonitorID();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int att = 0;
			int noatt = 0;
			if (rs.next()) {
				att = rs.getInt("attitude_cnt");
				noatt = rs.getInt("no_attitude_cnt");
			}
			result[0] = att;
			result[1] = noatt;
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
		return result;
	}

	/**
	 * 清除所有的分析数据
	 */
	public void clearAll() {
		this.clearCity(PostType.Repost);
		this.clearCity(PostType.Comment);

		this.clearFrom(PostType.Repost);
		this.clearFrom(PostType.Comment);

		this.clearGender(PostType.Repost);
		this.clearGender(PostType.Comment);

		this.clearHistogram(PostType.Repost);
		this.clearHistogram(PostType.Comment);

		this.clearKeyManFans();
		this.clearKeyManNums();
		this.clearKeyManUN();

		this.clearRetweetLevel();
		this.clearTrend(PostType.Repost);
		this.clearTrend(PostType.Comment);

		this.clearVip(PostType.Repost);
		this.clearVip(PostType.Comment);

	}

	// ===========================================================================

	private Query buildCommentsQuery(String tid, Platform platform, Pager pager) {
		Query query = new Query();
		query.setAsc(true);
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setSortField("created");
		query.setTableName(this.getMonitor().getTableName());
		query.setWhere(" where type = 3 and source='" + tid
				+ "' and platform =" + Utility.getPlatform(platform));
		return query;
	}

	private Query buildRetweetsQuery(String tid, Platform platform, Pager pager) {
		Query query = new Query();
		query.setAsc(true);
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setSortField("created");
		query.setTableName(this.getMonitor().getTableName());
		query.setWhere(" where type = 2 and source='" + tid
				+ "' and platform =" + Utility.getPlatform(platform));
		return query;
	}

	private Map<String, Integer> getLevelDetails(int level) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			// level
			String sql = "select top 10 * from Spread_Retweet_Level_Statis where level ="
					+ level
					+ " and sid = "
					+ this.getMonitorID()
					+ " order by retweets desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			Map<String, Integer> list = new HashMap<String, Integer>();
			while (rs.next()) {
				list.put(rs.getString("ucode"), rs.getInt("retweets"));
			}
			return list;
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
		return null;
	}

	private List<RetweetLevelResult> getLevels() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			// level
			pstmt = conn
					.prepareStatement("select level, COUNT(tid) num,SUM(followers) cnt  from Spread_Retweet_Level_Statis where sid="
							+ this.getMonitorID() + " group by level");
			rs = pstmt.executeQuery();
			List<RetweetLevelResult> list = new ArrayList<RetweetLevelResult>();
			while (rs.next()) {
				RetweetLevelResult r = new RetweetLevelResult();
				r.setLevel(rs.getInt("level"));
				r.setTweets(rs.getInt("num"));
				r.setFollowers(rs.getInt("cnt"));
				list.add(r);
			}
			return list;
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
		return null;
	}

	private RetweetStatisResult readKeyUserResult(ResultSet rs)
			throws SQLException {
		RetweetStatisResult result = new RetweetStatisResult();
		result.setFollowes(rs.getInt("followers"));
		result.setLocation(rs.getString("location"));
		result.setName(rs.getString("name"));
		result.setRetweets(rs.getInt("retweets"));
		result.setRetweetTime(rs.getTimestamp("created").getTime());
		result.setVip(rs.getBoolean("vip"));
		return result;
	}

	private List<RetweetStatisResult> querySpread(String sql, boolean isLevel) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<RetweetStatisResult> result = new ArrayList<RetweetStatisResult>();
			while (rs.next()) {
				result.add(this.readKeyUserResult(rs));
			}
			return result;
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
		return null;
	}

}
