package com.xunku.app.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.handlers.follower.FollowersNumHandler;
import com.xunku.app.handlers.follower.FollowesTopNHandler;
import com.xunku.app.handlers.follower.GenderHandler;
import com.xunku.app.handlers.follower.LocalHandler;
import com.xunku.app.handlers.follower.VipHandler;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IFollowerHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.app.monitor.IMonitor;
import com.xunku.app.result.FansResult;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.MAccountTrend;
import com.xunku.app.result.VipResult;
import com.xunku.dao.account.AccountDao;
import com.xunku.daoImpl.account.AccountDaoImpl;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

public class MAccountStore extends TweetStore {

	public MAccountStore(IMonitor monitor, Pooling pooling, AccountManager accDB) {
		super(monitor, pooling, accDB);
	}

	public List<ITweet> queryWarning(String[] wordList, int sinceId) {

		String cmdText = "select * from " + this.getMonitor().getTableName()
				+ " where id>" + sinceId;
		StringBuilder sb = new StringBuilder();
		if (wordList != null && wordList.length > 0) {
			sb.append(" and (text like '%");
			sb.append(wordList[0]);
			sb.append("%'");

			for (int i = 1; i < wordList.length; i++) {
				sb.append(" or text like '%");
				sb.append(wordList[i]);
				sb.append("%'");
			}
			sb.append(")");
		}
		cmdText += sb.toString();

		return this.executePostsQuery(cmdText);
	}

	public static void main(String[] args) {
		AppContext context = AppContext.getInstance();
		context.init();

		AccountDao dao = new AccountDaoImpl();
		Pager pager = Pager.createPager(1, 10);
		Pagefile<AccountMonitor> list = dao.queryAccountList(pager);

		Date end = new Date();
		Date start = DateHelper.addDays(end, -10);

		System.out.println("start:" + start);
		System.out.println("end:" + end);

		for (AccountMonitor m : list.getRows()) {
			MAccountStore store = (MAccountStore) m.getStore(context);

			FansResult r1 = store.queryFans(context);
			System.out.println(r1);

			List<ITweet> tweets = store.queryHotTweetsTop10();
			System.out.println(tweets);

			int[] rt = store.queryRealtime(start, end);
			System.out.println(rt);

			MAccountTrend trend = store.queryTrend(start, end);
			System.out.println(trend);

		}

	}

	private int queryFans(Date start, Date end) {

		String cmdText = "select * from MAccount_Fans_Statis where mid ="
				+ this.getMonitorID()
				+ " and timezone>=? and timezone<=? order by timezone desc";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setDate(1, new java.sql.Date(start.getTime()));
			pstmt.setDate(2, new java.sql.Date(end.getTime()));
			rs = pstmt.executeQuery();
			int fFans = 0;
			int lFans = 0;
			while (rs.next()) {
				if (fFans == 0) {
					// 得到最新的
					fFans = rs.getInt("fans");
				}
				// 得到最旧的
				lFans = rs.getInt("fans");
			}
			return fFans - lFans;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return 0;

	}

	public FansResult queryFans(AppContext context) {
		AccountManager manager = context.getAccountManager();
		FansResult result = new FansResult();

		VipHandler vip = new VipHandler();
		GenderHandler gender = new GenderHandler();
		FollowersNumHandler followers = new FollowersNumHandler();
		LocalHandler local = new LocalHandler();
		FollowesTopNHandler topNfollowers = new FollowesTopNHandler();

		List<IFollowerHandler> handlers = new ArrayList<IFollowerHandler>();
		handlers.add(vip);
		handlers.add(gender);
		handlers.add(followers);
		handlers.add(local);
		handlers.add(topNfollowers);

		AccountMonitor monitor = (AccountMonitor) this.getMonitor();
		Platform platform = monitor.getPlatform();
		String ucode = monitor.getUid();

		manager.getDB(platform).processFollowers(ucode, platform, handlers);
		VipResult vr = new VipResult();
		vr.setVipCnt(vip.getVipCnt());
		vr.setNoVipCnt(vip.getNoVipCnt());
		result.setVip(vr);

		GenderResult gr = new GenderResult();
		gr.setFemales(gender.getFemale());
		gr.setMales(gender.getMale());
		gr.setOthers(gender.getUnknow());

		result.setGrender(gr);

		result.setLocations(local.getLocals());
		result.setFollowersNums(new int[] { followers.getCnt100(),
				followers.getCnt1000(), followers.getCnt1W(),
				followers.getCnt10W(), followers.getCnt100W() });

		result.setSupermans(topNfollowers.getTopN());

		return result;
	}

	private AccountMonitor getAccountMonitor() {
		AccountMonitor monitor = (AccountMonitor) this.getMonitor();
		return monitor;
	}

	public List<ITweet> queryHotTweetsTop10() {

		String cmdText = "select top 10 * from "
				+ this.getMonitor().getTableName() + " where ucode='"
				+ this.getAccountMonitor().getUid() + "' order by reposts desc";

		return this.executePostsQuery(cmdText);
	}

	/**
	 * 返回指定区间的实时监测数据，返回数组长度为3，分别是：新增微博、新增粉丝、新增转发
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private int[] queryRealWeibo(Date start, Date end) {

		String cmdText = "select count(*) from "
				+ this.getMonitor().getTableName() + " where created<="
				+ end.getTime() + " and created >=" + start.getTime()
				+ " and uid = '" + this.getAccountMonitor().getUid() + "'";
		int[] result = new int[2];
		result[0] = this.count(cmdText + " and type =1");
		result[1] = this.count(cmdText + " and type = 2");
		return result;
	}

	/**
	 * 获得实时监测结果
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public int[] queryRealtime(Date start, Date end) {
		int[] weibos = this.queryRealWeibo(start, end);

		int fans = this.queryFans(start, end);

		int[] result = new int[3];
		result[0] = weibos[0];
		result[1] = fans;
		result[2] = weibos[1];

		return result;
	}

	/**
	 * 获得指定时间范围的微博详情
	 * 
	 * @param start
	 * @param end
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> queryRealtimeDetail(String text, Date start,
			Date end, Pager pager) {
		Query query = new Query();
		query.setTableName(this.getMonitor().getTableName());
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setSortField("created");
		query.setAsc(true);
		String where = " where created<=" + end.getTime() + " and created>="
				+ start.getTime() + " and uid='"
				+ this.getAccountMonitor().getUid() + "'";
		if (!Utility.isNullOrEmpty(text)) {
			where += " and text like '%" + text + "%'";
		}
		query.setWhere(where);

		return this.executePostsQueryWithPager(query);

	}

	/**
	 * 获得指定区间的转发和原创趋势
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public MAccountTrend queryTrend(Date start, Date end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			String cmdText = "";
			String sql = "SELECT timezone, weibos,retweets FROM MAccount_Trend_Statis"
					+ " WHERE timezone >=? AND timezone <= ? AND mid=?"
					+ " order by timezone desc";
			conn = this.getPool().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, new java.sql.Date(start.getTime()));
			pstmt.setDate(2, new java.sql.Date(end.getTime()));
			pstmt.setInt(3, this.getMonitorID());
			rs = pstmt.executeQuery();
			MAccountTrend trend = new MAccountTrend();
			while (rs.next()) {
				trend.weibo.put(rs.getDate("timezone").getTime(), rs
						.getInt("weibos"));
				trend.retweet.put(rs.getDate("timezone").getTime(), rs
						.getInt("retweets"));
			}
			return trend;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return null;

	}

	/**
	 * 今天趋势分析图 radiovalue：1：微博数，2：转发数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public MAccountTrend queryTrendToday(Date start, Date end, int radiovalue) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			String sql = "SELECT COUNT(uid)as weibos,created FROM "
					+ this.getMonitor().getTableName()
					+ " where created>= ? and created<= ? and uid='"
					+ this.getAccountMonitor().getUid() + "' and type= ? "
					+ "group by uid,created order by created desc";

			conn = this.getPool().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, start.getTime());
			pstmt.setLong(2, end.getTime());
			pstmt.setInt(3, radiovalue);
			rs = pstmt.executeQuery();
			MAccountTrend trend = new MAccountTrend();
			while (rs.next()) {
				trend.weibo.put(rs.getLong("created"), rs.getInt("weibos"));
			}
			return trend;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return null;

	}

	/**
	 * 获得当前监测帐号的微博列表
	 * <p>
	 * 该帐号发布的微博信息
	 * 
	 * @param account
	 * @param startDate
	 * @param endDate
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> TweetsGetByMAccount(Date startDate, Date endDate,
			Pager pager) {

		Query query = this.BuildMAccountQuery(startDate, endDate, pager);
		return this.executePostsQueryWithPager(query);
	}

	private Query BuildMAccountQuery(Date startDate, Date endDate, Pager pager) {

		Query query = new Query();
		query.setAsc(true);
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setSortField("created");
		query.setTableName(this.getMonitor().getTableName());
		StringBuilder sb = new StringBuilder();
		sb.append(" where created >=");
		sb.append(startDate.getTime());
		sb.append(" and created <=");
		sb.append(endDate.getTime());
		query.setWhere(sb.toString());
		return query;
	}

}
