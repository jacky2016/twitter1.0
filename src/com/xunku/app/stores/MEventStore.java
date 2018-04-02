package com.xunku.app.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xunku.app.Utility;
import com.xunku.app.db.AccountDB;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.monitor.IMonitor;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.RetweetResult;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.event.MEventContentResult;
import com.xunku.app.result.event.MEventUserRegResult;
import com.xunku.constant.FiltrateEnum;
import com.xunku.constant.TimeSortEnum;
import com.xunku.dao.event.DataAnalysisDao;
import com.xunku.daoImpl.event.DataAnalysisDaoImpl;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 事件监控库
 * 
 * @author wujian
 * @created on Jun 17, 2014 4:40:12 PM
 */
public class MEventStore extends TweetStore {

	private DataAnalysisDao _dataAnalysisDAO = new DataAnalysisDaoImpl();

	public MEventStore(IMonitor monitor, Pooling pool, AccountManager accDB) {
		super(monitor, pool, accDB);
	}

	public RetweetResult queryOriginalReposts(EventMonitor event) {
		RetweetResult result = new RetweetResult();
		result.setCommentCnt(this.getCountByType(3, event.getStartTime(), event
				.getEndTime()));
		result.setRetweetCnt(this.getCountByType(2, event.getStartTime(), event
				.getEndTime()));
		result.setTweetCnt(this.getCountByType(1, event.getStartTime(), event
				.getEndTime()));
		return result;
	}

	/**
	 * 功能描述<查询性别比例>
	 * 
	 * @author wanghui
	 * @param void
	 * @return GenderResult
	 * @version twitter 1.0
	 * @date Jul 23, 201411:28:10 AM
	 */
	public GenderResult querySexScale() {
		return _dataAnalysisDAO.querySexScale(this.getMonitorID(), this
				.getPool());
	}

	/**
	 * 功能描述<查询认证用户比例>
	 * 
	 * @author wanghui
	 * @param void
	 * @return VipResult
	 * @version twitter 1.0
	 * @date Jul 23, 201411:28:35 AM
	 */
	public VipResult queryVIPScale() {
		return _dataAnalysisDAO.queryVIPScale(this.getMonitorID(), this
				.getPool());
	}
	

	/**
	 * 功能描述<查询来源分布>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,Integer>
	 * @version twitter 1.0
	 * @date Jul 23, 201411:29:25 AM
	 */
	public Map<String, Integer> queryEventSource() {
		return _dataAnalysisDAO.queryEventSource(this.getMonitorID(), this
				.getPool());
	}

	/**
	 * 功能描述<查询注册时间分布>
	 * 
	 * @author wanghui
	 * @param void
	 * @return MEventUserRegResult
	 * @version twitter 1.0
	 * @date Jul 23, 201411:29:59 AM
	 */
	public MEventUserRegResult queryEventReg() {
		return _dataAnalysisDAO.queryEventReg(this.getMonitorID(), this
				.getPool());
	}

	/**
	 * 功能描述<查询地域分布>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,Integer>
	 * @version twitter 1.0
	 * @date Jul 23, 201411:30:27 AM
	 */
	public Map<String, Integer> queryEventLocal() {
		return _dataAnalysisDAO.queryEventLocal(this.getMonitorID(), this
				.getPool());
	}

	/**
	 * 功能描述<查询top10的关键用户>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<Account,Integer>
	 * @version twitter 1.0
	 * @date Jul 23, 201411:31:28 AM
	 */
	public Map<IAccount, Integer> queryEventKeyUse(Platform platform) {
		Map<IAccount, Integer> useMap = new HashMap<IAccount, Integer>();
		Map<String, Integer> map = _dataAnalysisDAO.queryEventKeyUser(this
				.getMonitorID(), this.getPool());
		AccountDB db = this.getAccountManager().getDB(platform);
		for (String ucode : map.keySet()) {
			useMap.put(db.accountGetByUcode(ucode), map.get(ucode));
		}
		return useMap;
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
			String sql = "SELECT top 20 * FROM Event_HotWord_Statis WHERE mid=? ORDER BY weight DESC,created desc";
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
	 * 功能描述<查询关键观点>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<Post,Integer>
	 * @version twitter 1.0
	 * @date Jul 24, 201411:32:14 AM
	 */
	public Map<ITweet, Integer> queryKeyPoint(long start, long end) {
		/*
		 * Map<ITweet, Integer> map = new HashMap<ITweet, Integer>();
		 * EventMonitor monitor = (EventMonitor) this.getMonitor(); start =
		 * monitor.getStartTime().getTime(); end =
		 * monitor.getEndTime().getTime(); List<KeyPointDTO> list =
		 * _dataAnalysisDAO.queryKeyPoint(this .getMonitorID(), this.getPool(),
		 * start, end); for (KeyPointDTO dto : list) { ITweet post =
		 * this.postGetByTid(dto.getTid(), dto.getPlatform()); map.put(post,
		 * dto.getReposts()); }
		 */
		Map<ITweet, Integer> map = new HashMap<ITweet, Integer>();
		EventMonitor monitor = (EventMonitor) this.getMonitor();
		start = monitor.getStartTime().getTime();
		end = monitor.getEndTime().getTime();
		String cmdText = "select top 5 * from "
				+ this.getMonitor().getTableName() + " where created<=" + end
				+ " and created>=" + start + " order by reposts desc";
		List<ITweet> list = this.executePostsQuery(cmdText);
		for (ITweet t : list) {
			map.put(t, t.getReposts());
		}
		return map;
	}

	private List<String> getTop10(String tableName, String columnName,
			String orderColumnName) {
		String cmdText = "select top 10 " + columnName + " from " + tableName
				+ " where eventid = ? order by " + orderColumnName + " desc";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, this.getMonitorID());
			rs = pstmt.executeQuery();
			List<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString(columnName));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return null;
	}

	public void arrangeKeyUser() {
		List<String> top10 = this.getTop10("Event_KeyUse_Statis", "ucode",
				"followers");
		this.arrangeTop10(top10, "Event_KeyUse_Statis", "ucode");

	}

	private void arrangeTop10(List<String> top10, String tableName,
			String columnName) {
		if (top10 != null) {
			StringBuilder sb = new StringBuilder();
			if (top10.size() > 0) {
				sb.append(columnName + "<>'" + top10.get(0) + "'");
			}

			for (int i = 1; i < top10.size(); i++) {
				sb.append(" and " + columnName + "<>'" + top10.get(i) + "'");
			}
			if (top10.size() > 0) {
				String cmdText = "delete " + tableName + " where eventid="
						+ this.getMonitorID() + " and " + sb.toString();
				this.executeNonQuery(cmdText);
			}
		}
	}

	public void arrangeKeyPoint() {
		List<String> top10 = this.getTop10("Event_KeyPoint_Statis", "tid",
				"reposts");
		this.arrangeTop10(top10, "Event_KeyPoint_Statis", "tid");
	}

	/**
	 * 查询当前事件的趋势分析结果，含详细信息
	 * 
	 * @param query
	 * @return
	 */
	public Map<Long, List<String>> queryTrendDetail(Date start, Date end) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT timezone, tid as cnt  FROM Event_Trend_Statis"
					+ " WHERE timezone >=? AND timezone <= ? AND eventid=?";
			conn = this.getPool().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setTimestamp(1, new Timestamp(start.getTime()));
			ps.setTimestamp(2, new Timestamp(end.getTime()));
			ps.setInt(3, this.getMonitorID());
			rs = ps.executeQuery();
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
				if (ps != null) {
					ps.close();
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

	/**
	 * 查询当前事件的趋势分析结果，不含详细信息
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<Long, Integer> queryTrendByHour(long start, long end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT timezone, count(tid) as cnt  FROM Event_Trend_Statis"
					+ " WHERE timezone >=? AND timezone <= ? AND eventid=?"
					+ " group by timezone";
			conn = this.getPool().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(start));
			pstmt.setTimestamp(2, new Timestamp(end));
			pstmt.setInt(3, this.getMonitorID());
			rs = pstmt.executeQuery();
			Map<Long, Integer> result = new TreeMap<Long, Integer>();
			while (rs.next()) {
				long timezone = rs.getTimestamp("timezone").getTime();
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

	public Map<Long, Integer> queryTrendByDate(long start, long end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT timezone, count(tid) as cnt  FROM Event_Trend_Statis"
					+ " WHERE timezone >=? AND timezone <= ? AND eventid=?"
					+ " group by timezone";
			conn = this.getPool().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(start));
			pstmt.setTimestamp(2, new Timestamp(end));
			pstmt.setInt(3, this.getMonitorID());
			rs = pstmt.executeQuery();
			Map<Long, Integer> result = new TreeMap<Long, Integer>();
			while (rs.next()) {
				long timezone = rs.getDate("timezone").getTime();
				if (!result.containsKey(timezone)) {
					result.put(timezone, rs.getInt("cnt"));
				} else {
					result.put(timezone, result.get(timezone)
							+ rs.getInt("cnt"));
				}
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return null;
	}

	/**
	 * 查询本库微博
	 * 
	 * @param text
	 * @param startDate
	 * @param endDate
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> queryTweetGetByEvent(String text, Date startDate,
			Date endDate, FiltrateEnum filter, TimeSortEnum sort, Pager pager) {

		Query query = null;
		// 开始时间和结束时间相同，则返回全部的数据
		if (startDate.equals(endDate)) {
			query = this.buildMEventQuery(text, filter, sort, pager);
		} else {
			query = this.buildMEventQuery(text, startDate, endDate, filter,
					sort, pager);
		}

		if (query != null) {
			Pagefile<ITweet> result = this.executePostsQueryWithPager(query);
			return result;
		}
		return null;
	}

	public List<ITweet> queryNewestTopNTweets(int topN, Date lastCT) {

		String cmdText = "select top " + topN + " * from "
				+ this.getMonitor().getTableName() + " where CT>'"
				+ DateHelper.formatDBTime(lastCT) + "' order by created desc";
		return this.executePostsQuery(cmdText);
	}

	/**
	 * 获得事件里面转发和原创的数量，填充到result对应的字段上
	 * 
	 * @param result
	 */
	public void setReTweetInfo(MEventContentResult result) {
		this.executeRetweet(result);
	}

	/**
	 * 获得指定时间范围的数据量
	 * 
	 * @param _start
	 * @param end
	 * @return
	 */
	public int count(long startTime, long endTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getPool().getConnection();
			String sql = "SELECT count(tid) FROM "
					+ this.getMonitor().getTableName()
					+ " WHERE created>=? AND created<?" + this.getEventScale();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, startTime);
			pstmt.setLong(2, endTime);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	/**
	 * 获得当前监测表里一共有多少数据
	 * 
	 * @return
	 */
	public int count() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getPool().getConnection();
			EventMonitor monitor = (EventMonitor) this.getMonitor();
			String sql = "SELECT count(tid) FROM " + monitor.getTableName()
					+ " WHERE created<=" + monitor.getEndTime().getTime()
					+ " AND created>=" + monitor.getStartTime().getTime()
					+ this.getEventScale();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	// =====================================================

	private String getEventScale() {
		EventMonitor monitor = (EventMonitor) this.getMonitor();
		String scale = " and created>=" + monitor.getStartTime().getTime()
				+ " and created<=" + monitor.getEndTime().getTime();

		return scale;
	}

	private Query buildMEventQuery(String text, FiltrateEnum filter,
			TimeSortEnum sort, Pager pager) {

		Query query = new Query();
		query.setAsc(true);
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		if (sort == TimeSortEnum.Comment) {
			query.setSortField("comments");
		} else if (sort == TimeSortEnum.Transpond) {
			query.setSortField("reposts");
		} else {
			query.setSortField("created");
		}
		query.setTableName(this.getMonitor().getTableName());
		StringBuilder sb = new StringBuilder();

		sb.append(" WHERE ");
		if (filter == FiltrateEnum.Original) {
			sb.append("type = 1 ");
		} else if (filter == FiltrateEnum.Reference) {
			sb.append("type = 2 ");
		} else {
			sb.append("(type =1 or type = 2) ");
		}

		if (!Utility.isNullOrEmpty(text)) {
			sb.append(" and text like '%");
			sb.append(DbHelper.nomalizeText(text));
			sb.append("%'");
		}

		// add scale
		sb.append(this.getEventScale());

		query.setWhere(sb.toString());
		return query;

	}

	private Query buildMEventQuery(String text, Date startDate, Date endDate,
			FiltrateEnum filter, TimeSortEnum sort, Pager pager) {

		Query query = new Query();
		query.setAsc(true);
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		if (sort == TimeSortEnum.Comment) {
			query.setSortField("comments");
		} else if (sort == TimeSortEnum.Transpond) {
			query.setSortField("reposts");
		} else {
			query.setSortField("created");
		}
		query.setTableName(this.getMonitor().getTableName());
		StringBuilder sb = new StringBuilder();
		sb.append(" WHERE ");
		if (filter == FiltrateEnum.Original) {
			sb.append("type = 1 ");
		} else if (filter == FiltrateEnum.Reference) {
			sb.append("type = 2 ");
		} else {
			sb.append("(type =1 or type = 2) ");
		}

		if (!Utility.isNullOrEmpty(text)) {
			sb.append(" and text like '%");
			sb.append(DbHelper.nomalizeText(text));
			sb.append("%'");
		}

		sb.append(" and created >=");
		sb.append(startDate.getTime());
		sb.append(" and created <=");
		sb.append(endDate.getTime());

		sb.append(this.getEventScale());
		query.setWhere(sb.toString());
		return query;

	}

	private void executeRetweet(MEventContentResult result) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			String sql = "SELECT posts,reposts FROM Event_Count_An where eventid = ?"
					+ this.getMonitor().getTableName();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, this.getMonitorID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
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
	}

	private int getCountByType(int type, Date sTime, Date eTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getPool().getConnection();
			String sql = "select COUNT(tid) from "
					+ this.getMonitor().getTableName()
					+ " where type =? and created >= " + sTime.getTime()
					+ " and created <=" + eTime.getTime();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, type);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}
}
