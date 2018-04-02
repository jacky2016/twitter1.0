package com.xunku.daoImpl.event;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jolbox.bonecp.BoneCP;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.app.result.BeTrendResult;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.RetweetResult;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.event.MEventUserRegResult;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.event.DataAnalysisDao;
import com.xunku.dto.KeyPointDTO;
import com.xunku.utils.DatabaseUtils;

public class DataAnalysisDaoImpl implements DataAnalysisDao {

	@Override
	public Map<String, Integer> queryTrendAnalyze(BoneCP dataSource,
			Date startTime, Date endTime, String table) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String start = sdf.format(startTime);
			String end = sdf.format(endTime);
			conn = dataSource.getConnection();
			String sql = "select COUNT(id) as count,created from " + table
					+ " where created between ? and ? group by created";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, start);
			pstmt.setString(2, end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("created"), rs.getInt("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return map;
	}

	@Override
	public RetweetResult queryOriginalReposts(int eid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RetweetResult result = null;
		try {
			conn = pool.getConnection();
			String sql = "SELECT * FROM Event_Retweet_Statis WHERE eventid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = new RetweetResult();
				result.setTweetCnt(rs.getInt("tweetCnt"));
				result.setRetweetCnt(rs.getInt("retweetCnt"));
				result.setCommentCnt(rs.getInt("commentCnt"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public GenderResult querySexScale(int eid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		GenderResult result = null;
		try {
			conn = pool.getConnection();
			String sql = "select males,females,unsex from Event_Grender_Statis where eventid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = new GenderResult();
				result.setMales(rs.getInt("males"));
				result.setFemales(rs.getInt("females"));
				result.setOthers(rs.getInt("unsex"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public VipResult queryVIPScale(int eid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		VipResult result = null;
		try {
			conn = pool.getConnection();
			String sql = "select vip,unvip from Event_Vip_Statis where eventid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = new VipResult();
				result.setVipCnt(rs.getInt("vip"));
				result.setNoVipCnt(rs.getInt("unvip"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public MEventUserRegResult queryEventReg(int eid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MEventUserRegResult result = null;
		try {
			conn = pool.getConnection();
			String sql = "select * from Event_Reg_Statis where eventid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = new MEventUserRegResult();
				result.setHalfYears(rs.getInt("halfYears"));
				result.setOneYears(rs.getInt("oneYears"));
				result.setTwoYears(rs.getInt("twoYears"));
				result.setMoreYears(rs.getInt("moreYears"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> queryEventLocal(int eid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			conn = pool.getConnection();
			String sql = "select * from Event_Local_Statis where eventid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Cache cache = CacheManager
						.getCacheInfo(PortalCST.AppSourceCacheKey);
				Map<Integer, String> sourceMap = (Map<Integer, String>) cache
						.getValue();
				map.put(sourceMap.get(rs.getInt("city")), rs.getInt("value"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> queryEventSource(int eid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			conn = pool.getConnection();
			String sql = "select * from Event_Source_Statis where eventid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Cache cache = CacheManager
						.getCacheInfo(PortalCST.AppSourceCacheKey);
				Map<Integer, String> sourceMap = (Map<Integer, String>) cache
						.getValue();
				map.put(sourceMap.get(rs.getInt("sourceid")), rs
						.getInt("value"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return map;
	}

	@Override
	public Map<String, Integer> queryEventKeyUser(int eid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			conn = pool.getConnection();
			String sql = "SELECT TOP 10 * FROM Event_KeyUse_Statis WHERE eventId=? ORDER BY followers DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("ucode"), rs.getInt("followers"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return map;
	}

	private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
			Connection conn) {
		this.CloseStatus(null, rs, null, pstmt, conn);
	}

	private void CloseStatus(ResultSet rs2, ResultSet rs,
			CallableStatement cstmt, PreparedStatement pstmt, Connection conn) {

		try {
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
	public Map<String, Integer> queryHotWord(int eventid, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			conn = pool.getConnection();
			String sql = "SELECT top 10 * FROM Event_HotWord_Statis WHERE eventid=? ORDER BY count DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("keyword"), rs.getInt("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return map;
	}

	@Override
	public List<KeyPointDTO> queryKeyPoint(int eventid, Pooling pool,
			long start, long end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<KeyPointDTO> list = new ArrayList<KeyPointDTO>();
		try {
			conn = pool.getConnection();
			String sql = "SELECT TOP 5 * FROM Event_KeyPoint_Statis WHERE eventid=? and created<=? and created>=? ORDER BY reposts DESC";
			//String sql = "SELECT TOP 5 * FROM Event_KeyPoint_Statis WHERE eventid=? ORDER BY reposts DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventid);
			// 统计关键观点不设置时间
			pstmt.setLong(2, end);
			pstmt.setLong(3, start);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				KeyPointDTO dto = new KeyPointDTO();
				dto.setTid(rs.getString("tid"));
				dto.setReposts(rs.getInt("reposts"));
				dto.setPlatform(Utility.getPlatform(rs.getInt("platform")));
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
	public Map<Long, List<String>> queryTrendDetail(Date start, Date end,
			Query query, Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(query.toSQL());
			rs = pstmt.executeQuery();
			Map<Long, List<String>> result = new HashMap<Long, List<String>>();
			if (rs.next()) {
				long timezone = rs.getDate("timezone").getTime();
				if (!result.containsKey(timezone)) {
					result.put(timezone, new ArrayList<String>());
				}
				result.get(timezone).add(rs.getString("tid"));
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

	@Override
	public Map<Long, Integer> queryTrend(Date start, Date end, Query query,
			Pooling pool) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(query.toSQL());
			rs = pstmt.executeQuery();
			Map<Long, Integer> result = new HashMap<Long, Integer>();
			if (rs.next()) {
				long timezone = rs.getDate("timezone").getTime();
				if (!result.containsKey(timezone)) {
					result.put(timezone, rs.getInt("cnt"));
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

	@Override
	public BeTrendResult queryBeTrend(Date date, String ucode, Platform platform) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Base_Account_BehaviorTrends WHERE day_key=? and ucode=? and platform=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, new java.sql.Date(date.getTime()));
			pstmt.setString(2, ucode);
			pstmt.setInt(3, Utility.getPlatform(platform));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				BeTrendResult btr = new BeTrendResult();
				btr.setUcode(rs.getString("ucode"));
				btr.setBi_followers_count(rs.getInt("bi_followers_count"));
				btr.setComments_count(rs.getInt("comments_count"));
				btr
						.setDaren_followers_count(rs
								.getInt("daren_followers_count"));
				btr.setFollowers_count(rs.getInt("followers_count"));
				btr.setFriends_count(rs.getInt("friends_count"));
				btr.setReceive_comments_count(rs
						.getInt("receive_comments_count"));
				btr.setRepost_count(rs.getInt("repost_count"));
				btr.setReposted_count(rs.getInt("reposted_count"));
				btr.setStatuses_count(rs.getInt("statuses_count"));
				btr.setV_followers_count(rs.getInt("v_followers_count"));
				btr.setV_friends_count(rs.getInt("v_friends_count"));
				return btr;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return null;
	}

	@Override
	public void insertBeTrend(Date date, String ucode, Platform platform,
			BeTrendResult trend) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Base_Account_BehaviorTrends([ucode],[platform],[day_key],"
					+ "[followers_count],[v_followers_count],"
					+ "[daren_followers_count],[friends_count],"
					+ "[v_friends_count],[receive_comments_count],"
					+ "[statuses_count],[bi_followers_count],"
					+ "[repost_count],[reposted_count],[comments_count])"
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, ucode);
			ps.setInt(2, Utility.getPlatform(platform));
			ps.setDate(3, new java.sql.Date(date.getTime()));
			ps.setInt(4, trend.getFollowers_count());
			ps.setInt(5, trend.getV_followers_count());
			ps.setInt(6, trend.getDaren_followers_count());
			ps.setInt(7, trend.getFriends_count());
			ps.setInt(8, trend.getV_friends_count());
			ps.setInt(9, trend.getReceive_comments_count());
			ps.setInt(10, trend.getStatuses_count());
			ps.setInt(11, trend.getBi_followers_count());
			ps.setInt(12, trend.getRepost_count());
			ps.setInt(13, trend.getReposted_count());
			ps.setInt(14, trend.getComments_count());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, ps, conn);
		}

	}
}
