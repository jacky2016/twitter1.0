package com.xunku.app.stores;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xunku.app.Utility;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.FilterAccountType;
import com.xunku.app.enums.FilterCommentOrient;
import com.xunku.app.enums.FilterPostType;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.app.monitor.IMonitor;
import com.xunku.app.params.MentionQueryParam;
import com.xunku.app.result.AccountTrendResult;
import com.xunku.app.result.MentionResult;
import com.xunku.app.result.NavieCountResult;
import com.xunku.app.result.NavieResult;
import com.xunku.app.result.PublishCountResult;
import com.xunku.app.result.RepostOrganizationResult;
import com.xunku.app.result.WeiboTextResult;
import com.xunku.constant.WeiboType;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Mention;
import com.xunku.utils.Pagefile;

/**
 * 官方微博库，基于客户管理
 * 
 * @author wujian
 * @created on Jun 17, 2014 4:39:00 PM
 */
public class MCustomStore extends TweetStore {

	public MCustomStore(IMonitor monitor, Pooling ds, AccountManager accDB) {
		super(monitor, ds, accDB);
	}

	/**
	 * 获得指定时间范围，机构转发和评论某个作者的总数
	 * 
	 * @param orgUid
	 * @param start
	 * @param end
	 * @param authorUid
	 * @return
	 */
	public int[] queryOrganization(String orgid, Date start, Date end,
			String authorUid) {
		int[] result = new int[2];

		result[0] = this.queryNaviecunt(orgid, start, end, authorUid, 2);
		result[1] = this.queryNaviecunt(orgid, start, end, authorUid, 3);

		return result;

	}

	private int queryNaviecunt(String orgid, Date start, Date end,
			String authorUid, int type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getPool().getConnection();
			// String cmdText = "select COUNT(*) from Office_naviecunt_Statis
			// where navieid = ? and uid = ? and type=? and created>=? and
			// created<=?";

			String cmdText = "select count(*) from "
					+ this.getMonitor().getTableName() + " where ucode = '"
					+ orgid + "' and created<=" + end.getTime()
					+ " and created>=" + start.getTime() + " and type =" + type;
			pstmt = conn.prepareStatement(cmdText);
			/*
			 * pstmt.setInt(1, orgid); pstmt.setString(2, authorUid);
			 * pstmt.setInt(3, type); pstmt.setLong(4, start.getTime());
			 * pstmt.setLong(5, end.getTime());
			 */
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
	 * 获得指定用户Userline的最大SinceID
	 * 
	 * @param uid
	 * @return
	 */
	public String queryUserlineSinceID(String uid) {
		String cmdText = "select top 1 * from "
				+ this.getMonitor().getTableName() + " where ucode='" + uid
				+ "' and homeid is null order by created desc";
		String result = this.executeLastTid(cmdText);
		LOG.info("UserTimeline SinceID is " + result);
		return result;
	}

	/**
	 * 获得指定用户Homeline的最大SinceID
	 * 
	 * @param uid
	 * @return
	 */
	public String queryHomelineSinceID(String uid) {
		String cmdText = "select top 1 * from "
				+ this.getMonitor().getTableName() + " where homeid ='" + uid
				+ "' order by created desc";
		String result = this.executeLastTid(cmdText);
		LOG.info("HomeTimeline SinceID is " + result);
		return result;
	}

	/**
	 * 获得我评论的最大SinceID
	 * 
	 * @return
	 */
	public String queryCommentByMeSinceID(String uid) {
		return queryCommentSinceID(uid);
	}

	/**
	 * 获得给我评论的最大SinceID
	 * 
	 * @return
	 */
	public String queryCommentToMeSinceID(String uid) {
		String cmdText = "select a.* from " + this.getMonitor().getTableName()
				+ " a inner join " + this.getMonitor().getTableName()
				+ " b on a.source = b.tid where a.ucode = '" + uid
				+ "' and b.type=3 order by a.created desc";
		String result = this.executeLastTid(cmdText);
		LOG.info("评论我的 SinceID is " + result);
		return result;
	}

	/**
	 * 获得评论里面提到我的最大SinceID
	 * 
	 * @param uid
	 * @return
	 */
	public String queryCommentMentionSinceID(String uid) {
		String cmdText = "select * from My_Mentions where uid ='" + uid
				+ "' and type = 3 order by id desc";
		String result = this.executeLastTid(cmdText);
		LOG.info("获得评论提到我的 SinceID is " + result);
		return result;
	}

	/**
	 * 获得微博里提到我的最大SinceID
	 * 
	 * @param uid
	 * @return
	 */
	public String queryTweetMentionSinceID(String uid) {
		// 包括转发和原创
		String cmdText = "select * from My_Mentions where uid ='" + uid
				+ "' and (type = 1 or type =2)  order by id desc";
		String result = this.executeLastTid(cmdText);
		LOG.info("获得微博里提到我的 SinceID is " + result);
		return result;
	}

	/**
	 * 获得指定帐号发布的微博
	 * 
	 * @param uid
	 * @return
	 */
	public List<ITweet> queryTweetsByUid(String uid, long than, int topN,
			PostType type) {
		StringBuilder cmdText = new StringBuilder();
		cmdText.append("select ");
		if (topN > 0) {
			cmdText.append(" top " + topN);
			cmdText.append(" ");
		}
		cmdText.append("* from " + this.getMonitor().getTableName());
		cmdText.append(" where ucode ='");
		cmdText.append(uid);
		cmdText.append("'");
		cmdText.append(" and created>=" + than);
		if (type != PostType.All) {
			cmdText.append(" and type = ");
			cmdText.append(Utility.getPostType(type));
		}

		return this.executePostsQuery(cmdText.toString());

	}

	/**
	 * 功能描述<微博内容统计【考核管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<ITweet>
	 * @version twitter 1.0
	 * @date Aug 20, 20145:18:22 PM
	 */
	public Pagefile<WeiboTextResult> queryTweetCountDetail(Pager pager,
			long start, long end, String uid, List<String> uids) {
		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setTableName(this.getMonitor().getTableName());
		query.setSortField("created");
		query.setAsc(true);
		query.setWhere(" where created<=" + end + " and created>=" + start
				+ " and uid='" + uid + "' and type=1");

		Pagefile<WeiboTextResult> result = this.executeWeiboTextQueryWithPager(
				query, uid);

		if (result != null) {
			for (WeiboTextResult r : result.getRows()) {
				// 获得网评员的uid，看看有几个...
				if (uids != null || uids.size() != 0) {
					r
							.setOrgrans(this.queryOrgCnt(start, end,
									r.getTid(), uids));
				}
			}
		}

		return result;
	}

	private int queryOrgCnt(long start, long end, String tid, List<String> orgs) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> uids = new ArrayList<String>();
		try {
			conn = this.getPool().getConnection();
			String cmdText = "select * from "
					+ this.getMonitor().getTableName() + " where source=" + tid
					+ " and type =2";
			pstmt = conn.prepareStatement(cmdText);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String ucode = rs.getString("ucode");
				if (orgs.contains(ucode)) {
					// if (!uids.contains(ucode)) {
					uids.add(ucode);
					// }
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return uids.size();
	}

	// 微博内容统计【考核管理】
	public Pagefile<WeiboTextResult> executeWeiboTextQueryWithPager(
			Query query, String uid) {
		Pagefile<WeiboTextResult> pagefile = new Pagefile<WeiboTextResult>();
		Pagefile<ITweet> result = this.getPostsByQueryWithPage(query);
		for (ITweet post : result.getRows()) {
			WeiboTextResult wtr = new WeiboTextResult();
			wtr.setTid(post.getTid());
			wtr.setText(post.getText());
			wtr.setComments(post.getComments());
			wtr.setReposts(post.getReposts());
			wtr.setOrgrans(this.getPostROCount(this.getMonitorID(), post
					.getTid()));
			wtr.setCreated(new Date(post.getCreated()));
			pagefile.getRows().add(wtr);
		}
		pagefile.setRealcount(result.getRealcount());
		return pagefile;
	}

	/**
	 * 获得转发机构的转发数
	 * 
	 * @param customid
	 * @param sourceid
	 * @return
	 */
	private int getPostROCount(int customid, String sourceid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getPool().getConnection();
			String sql = "select count(orgid) from Office_Organization_Statis where sourceid=? group by orgid";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sourceid);
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

	private Pagefile<ITweet> getPostsByQueryWithPage(Query query) {

		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<ITweet> pagefile = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = this.getPool().getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, query.getTableName());
			cstmt.setString(2, query.getFields());
			cstmt.setInt(3, query.getPageSize());
			cstmt.setInt(4, query.getPageIndex());
			cstmt.setString(5, query.getWhere());
			cstmt.setString(6, query.getSortField());
			cstmt.setInt(7, query.getSort());
			cstmt.execute();
			pagefile = new Pagefile<ITweet>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.readPost(rs));
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

	/**
	 * 功能描述<转发机构反查列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<RepostOrganizationResult>
	 * @version twitter 1.0
	 * @date Aug 21, 20143:40:41 PM
	 */
	public Pagefile<RepostOrganizationResult> queryRODetail(Pager pager,
			String tid, List<Integer> orgids) {
		// 1、获得微博的编号
		// 2、组装微博对象
		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setSortField("tid");
		query.setTableName("Office_Organization_Statis");

		StringBuilder sb = new StringBuilder();
		if (orgids != null) {

			if (orgids.size() > 0) {
				sb.append(" and (orgid= ");
				sb.append(orgids.get(0));
			}
			for (int i = 1; i < orgids.size(); i++) {
				sb.append(" or orgid = ");
				sb.append(orgids.get(i));
			}
			if (orgids.size() > 0) {
				sb.append(")");
			}
		}
		query.setWhere("where sourceid=" + tid + " and type = 2"
				+ sb.toString());
		return this.getROByQueryWithPage(query);
	}

	private Pagefile<RepostOrganizationResult> getROByQueryWithPage(Query query) {
		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<RepostOrganizationResult> pagefile = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = this.getPool().getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, query.getTableName());
			cstmt.setString(2, query.getFields());
			cstmt.setInt(3, query.getPageSize());
			cstmt.setInt(4, query.getPageIndex());
			cstmt.setString(5, query.getWhere());
			cstmt.setString(6, query.getSortField());
			cstmt.setInt(7, query.getSort());
			cstmt.execute();
			pagefile = new Pagefile<RepostOrganizationResult>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				RepostOrganizationResult result = new RepostOrganizationResult();
				result.setName(rs.getString("name"));
				result.setPlatform(Utility.getPlatform(rs.getInt("platform")));
				result.setUid(rs.getString("uid"));
				pagefile.getRows().add(result);
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

	/**
	 * 功能描述<网评员统计【考核管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<NavieCountResult>
	 * @version twitter 1.0
	 * @date Aug 21, 20142:07:56 PM
	 */
	public Pagefile<NavieCountResult> queryNavieCount(Pager pager, long start,
			long end) {
		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("navieid,customid");
		query.setTableName("Office_naviecunt_Statis");
		query.setSortField("navieid");
		query
				.setWhere(" where navieid in (select navieid from Office_naviecunt_Statis where customid="
						+ this.getMonitorID()
						+ " and created>="
						+ start
						+ " and created<="
						+ end
						+ " and type=2) group by navieid,customid");
		return this.getNavieByQueryWithPage(query);
	}

	private Pagefile<NavieCountResult> getNavieByQueryWithPage(Query query) {
		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<NavieCountResult> pagefile = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = this.getPool().getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, query.getTableName());
			cstmt.setString(2, query.getFields());
			cstmt.setInt(3, query.getPageSize());
			cstmt.setInt(4, query.getPageIndex());
			cstmt.setString(5, query.getWhere());
			cstmt.setString(6, query.getSortField());
			cstmt.setInt(7, query.getSort());
			cstmt.execute();
			pagefile = new Pagefile<NavieCountResult>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				NavieCountResult result = new NavieCountResult();
				int customid = rs.getInt("customid");
				int navieid = rs.getInt("navieid");
				result.setCustomid(customid);
				result.setNavieid(navieid);
				result.setUids(this.getNavieUids(conn, customid, navieid));
				result.setTotal(this
						.getNavieWeiboCount(conn, customid, navieid));
				pagefile.getRows().add(result);
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				while (rs2.next()) {
					pagefile.setRealcount(pagefile.getRows().size());
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

	/**
	 * 功能描述<获取当前客户下的网评员账号的个数>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Aug 21, 201411:37:47 AM
	 */
	private int getNavieUids(Connection conn, int customid, int navieid) {
		PreparedStatement pstmt = null;
		String sql = "select customid,navieid,uid from Office_naviecunt_Statis where customid=? and navieid=? and type<>1 group by customid,navieid,uid";
		List<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			pstmt.setInt(2, navieid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("uid"));
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list.size();
	}

	private int getNavieWeiboCount(Connection conn, int customid, int navieid) {
		PreparedStatement pstmt = null;
		String sql = "select customid,navieid,tid from Office_naviecunt_Statis where customid=? and navieid=? and type<>1 group by customid,navieid,tid";
		List<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			pstmt.setInt(2, navieid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("tid"));
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list.size();
	}

	/**
	 * 功能描述<网评员账号的个数的反查列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<NavieResult>
	 * @version twitter 1.0
	 * @date Aug 21, 20144:29:40 PM
	 */
	public Pagefile<NavieResult> queryNavieResult(Pager pager, int navieid) {
		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setTableName("Office_naviecunt_Statis");
		query.setSortField("created");
		query.setWhere(" where customid=" + this.getMonitorID()
				+ " and navieid=" + navieid);
		return this.getNavieResultByQueryWithPage(query);
	}

	public Pagefile<ITweet> queryNavieRCResult(Pager pager, int navieid,
			long start, long end, WeiboType type) {
		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setTableName("Office_naviecunt_Statis");
		query.setSortField("created");
		query.setAsc(true);
		if (type == WeiboType.CommentPost) {
			query.setWhere(" where customid=" + this.getMonitorID()
					+ " and navieid=" + navieid + " and type=3");
		} else {
			query.setWhere(" where customid=" + this.getMonitorID()
					+ " and navieid=" + navieid + " and type=2");
		}
		return this.executePostsQueryPager(query);
	}

	public Pagefile<ITweet> executePostsQueryPager(Query query) {
		Pagefile<ITweet> postPage = new Pagefile<ITweet>();
		Pagefile<NavieResult> pagefile = getNavieResultByQueryWithPage(query);
		for (NavieResult nr : pagefile.getRows()) {
			postPage.getRows().add(
					this.postGetByTid(nr.getTid(), nr.getPlatform()));
		}
		postPage.setRealcount(postPage.getRows().size());
		return postPage;
	}

	private Pagefile<NavieResult> getNavieResultByQueryWithPage(Query query) {
		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<NavieResult> pagefile = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = this.getPool().getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, query.getTableName());
			cstmt.setString(2, query.getFields());
			cstmt.setInt(3, query.getPageSize());
			cstmt.setInt(4, query.getPageIndex());
			cstmt.setString(5, query.getWhere());
			cstmt.setString(6, query.getSortField());
			cstmt.setInt(7, query.getSort());
			cstmt.execute();
			pagefile = new Pagefile<NavieResult>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				NavieResult result = new NavieResult();
				result.setCustomid(rs.getInt("customid"));
				result.setNavieid(rs.getInt("navieid"));
				result.setUid(rs.getString("uid"));
				result.setPlatform(Utility.getPlatform(rs.getInt("platform")));
				result.setTid(rs.getString("tid"));
				result.setType(Utility.getPostType(rs.getInt("type")));
				pagefile.getRows().add(result);
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				if (rs2.next()) {
					pagefile.setRealcount(pagefile.getRows().size());
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

	/*
	 * 删除网评员下的账号
	 */
	public boolean deleteNavieRecord(int navieid, String uid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = this.getPool().getConnection();
			String sql = "delete from Office_naviecunt_Statis where customid="
					+ this.getMonitorID() + " and navieid=" + navieid
					+ " and uid=" + uid;
			pstmt = conn.prepareStatement(sql);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		return isDelete;
	}

	public int queryOrganWeibos(String uid, long start, long end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int weibos = 0;
		try {
			conn = this.getPool().getConnection();
			String sql = "select count(id) as count from "
					+ this.getMonitor().getTableName() + " where uid='" + uid
					+ "' and created >=" + start + " and created <=" + end;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				weibos = rs.getInt("count");
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
		return weibos;
	}

	public int queryMyMentions(String uid, long start, long end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int weibos = 0;
		try {
			conn = this.getPool().getConnection();
			String sql = "select count(id) as count from My_Mentions where uid='"
					+ uid
					+ "' and created >="
					+ start
					+ " and created <="
					+ end;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				weibos = rs.getInt("count");
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
		return weibos;
	}

	public Pagefile<ITweet> queryOrganITweets(Pager pager, String uid,
			long start, long end) {
		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setTableName(this.getMonitor().getTableName());
		query.setSortField("created");
		query.setAsc(true);
		query.setWhere(" where created<=" + end + " and created>=" + start
				+ " and uid='" + uid + "'");
		return this.getPostsByQueryWithPage(query);
	}

	public Pagefile<Mention> queryMentionITweet(Pager pager, String uid,
			long start, long end) {
		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setTableName(this.getMonitor().getTableName());
		query.setSortField("created");
		query.setAsc(true);
		query.setWhere(" where created<=" + end + " and created>=" + start
				+ " and uid='" + uid + "'");
		return this.getMentionQueryWithPage(query);
	}

	private Pagefile<Mention> getMentionQueryWithPage(Query query) {
		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<Mention> pagefile = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = this.getPool().getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, query.getTableName());
			cstmt.setString(2, query.getFields());
			cstmt.setInt(3, query.getPageSize());
			cstmt.setInt(4, query.getPageIndex());
			cstmt.setString(5, query.getWhere());
			cstmt.setString(6, query.getSortField());
			cstmt.setInt(7, query.getSort());
			cstmt.execute();
			pagefile = new Pagefile<Mention>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				Mention m = new Mention();
				m.setId(rs.getInt("id"));
				m.setTid(rs.getString("tid"));
				m.setUid(rs.getString("uid"));
				m.setPlatform(rs.getInt("platform"));
				m.setText(rs.getString("text"));
				m.setType(rs.getInt("type"));
				m.setAuthor(rs.getString("author"));
				m.setCreated(rs.getLong("created"));
				pagefile.getRows().add(m);
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

	/**
	 * 功能描述<通过tid和platform获取微博>
	 * 
	 * @author wanghui
	 * @param void
	 * @return ITweet
	 * @version twitter 1.0
	 * @date Aug 25, 20142:19:52 PM
	 */
	public ITweet queryITweetByTid(String tid, Platform platform) {

		return this.postGetByTid(tid, platform);
	}

	/**
	 * 获得当前用户的评论列表
	 * 
	 * @param user
	 * @param uid
	 * @param platform
	 * @param filter
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> commentsGetByAccount(User user, String uid,
			Platform platform, FilterCommentOrient filter, Pager pager) {
		Query query = this.buildMyCommentsQuery(uid, platform, filter, pager);
		return this.executePostsQueryWithPager(query);
	}

	/**
	 * 获得用户的微博转发列表
	 * 
	 * @param uid
	 * @param text
	 * @param pager
	 * @param type
	 * @param platform
	 * @return
	 */
	public Pagefile<ITweet> tweetsUserTimeline(String uid, String text,
			Pager pager, FilterPostType type, Platform platform) {

		Query q = this.buildUserTimelineQuery(uid, text, pager, type, platform);

		return this.executePostsQueryWithPager(q);
	}

	public Pagefile<ITweet> tweetsHomeTimeline(String uid, String text,
			Pager pager, FilterPostType type, Platform platform) {
		Query q = this.buildHomeTimelineQuery(uid, text, pager, type, platform);

		return this.executePostsQueryWithPager(q);
	}

	public void clearAll() {

		String cmdText = "delete from Base_Account_Trend";
		this.clear(cmdText);

	}

	/**
	 * 帐号粉丝趋势分析，该查询需要JOB支持灌数据才行
	 * 
	 * @param homePool
	 * @param uid
	 * @param platform
	 * @param start
	 * @param end
	 * @return
	 */
	public List<AccountTrendResult> queryTrendAccount(Pooling homePool,
			String uid, Platform platform, long start, long end) {

		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			String cmdText = "select * from Base_Account_Trend "
					+ "where uid=? and platform=? and created<=? and created>=?";
			conn = homePool.getConnection();
			cs = conn.prepareCall(cmdText);
			cs.setString(1, uid);
			cs.setInt(2, Utility.getPlatform(platform));
			cs.setTimestamp(3, new Timestamp(end));
			cs.setTimestamp(4, new Timestamp(start));
			cs.execute();
			List<AccountTrendResult> list = new ArrayList<AccountTrendResult>();
			rs = cs.getResultSet();
			while (rs.next()) {
				AccountTrendResult r = new AccountTrendResult();
				r.setCreated(rs.getTimestamp("created"));
				r.setFollowers(rs.getInt("followers"));
				r.setFriends(rs.getInt("friends"));
				r.setWeibos(rs.getInt("weibos"));
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
				if (cs != null) {
					cs.close();
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
	 * 返回转发趋势，返回的是我转发的趋势
	 * 
	 * @param uid
	 * @param platform
	 * @param start
	 * @param end
	 * @param _type
	 * @return
	 */
	public Map<Long, Integer> queryRetweetTrend(String uid, Platform platform,
			long start, long end) {

		// 我转发的，取这个时间段原创的微博，然后找这个原创微博的转发数趋势
		List<ITweet> retweets = this.queryPublish(uid, platform, start, end, 1);

		Map<Long, Integer> retweetTrend = new TreeMap<Long, Integer>(this
				.getLongComparator());

		for (ITweet post : retweets) {
			long key = DateHelper.getTimezoneHour(post.getCreated());
			int retweetsCnt = post.getReposts();
			if (retweetTrend.containsKey(key)) {
				retweetsCnt += retweetTrend.get(key);
			}
			retweetTrend.put(key, retweetsCnt);
		}

		return retweetTrend;
	}

	/**
	 * 返回评论趋势，我评论的趋势，不是我发的微博被评论的趋势...
	 * 
	 * @param uid
	 * @param platform
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<Long, Integer> queryCommentTrend(String uid, Platform platform,
			long start, long end) {

		Map<Long, Integer> commentTrend = new TreeMap<Long, Integer>(this
				.getLongComparator());
		// 我评论的，取这个时间段原创的微博，然后找这个原创微博的评论数趋势
		List<ITweet> comments = this.queryPublish(uid, platform, start, end, 1);
		for (ITweet post : comments) {
			long key = DateHelper.getTimezoneHour(post.getCreated());
			int commentCnt = post.getComments();
			if (commentTrend.containsKey(key)) {
				commentCnt += commentTrend.get(key);
				// commentCnt += post.getComments();
			}
			commentTrend.put(key, commentCnt);
		}
		return commentTrend;
	}

	/**
	 * 提到我的统计信息
	 * 
	 * @param uid
	 * @param platform
	 * @param _start
	 * @param end
	 * @param _type
	 * @return
	 */
	public Map<Long, Integer> queryMentionStatistics(MentionQueryParam param) {
		Query query = this.buildMentionIdsQuery(param, true);
		List<MentionResult> list = this.queryMetionIds(query);
		Map<Long, Integer> result = new TreeMap<Long, Integer>(this
				.getLongComparator());
		for (MentionResult r : list) {
			long time = DateHelper.getTimezoneHour(r.getCreated());
			int cnt = 1;
			if (result.containsKey(time)) {
				cnt = result.get(time);
				cnt++;
			}
			result.put(time, cnt);
		}
		return result;
	}

	/**
	 * 获得提到我的统计明细
	 * 
	 * @param uid
	 * @param platform
	 * @param _start
	 * @param end
	 * @param _type
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> queryMentionStatisticsDetail(MentionQueryParam param) {

		// 这个查询只得到了My_Mentions里面的数据
		Query query = this.buildMentionIdsQuery(param, true);

		List<MentionResult> tids = this.queryMetionIds(query);
		// 需要再执行一个查询找到Post
		Query q = this.buildMentionQuery(tids);
		q.setPageIndex(query.getPageIndex());
		q.setPageSize(query.getPageSize());
		return this.executePostsQueryWithPager(q);
	}

	/**
	 * 获得提到我的列表
	 * 
	 * @param user
	 * @param uid
	 * @param platform
	 * @param _start
	 * @param end
	 * @param text
	 * @param accFilter
	 * @param postFilter
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> tweetsMention(MentionQueryParam param) {

		Query query = this.buildMentionIdsQuery(param, false);

		List<MentionResult> tids = this.queryMetionIds(query);
		// 需要再执行一个查询找到Post
		Query q = this.buildMentionQuery(tids);
		q.setPageIndex(query.getPageIndex());
		q.setPageSize(query.getPageSize());
		return this.executePostsQueryWithPager(q);

	}

	/**
	 * 获得提到我的微博id集合
	 * 
	 * @param query
	 * @return
	 */
	public List<MentionResult> queryMetionIds(Query query) {
		Connection conn = null;
		CallableStatement cstmt = null;
		List<MentionResult> result = new ArrayList<MentionResult>();
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			cstmt = conn.prepareCall(query.toSQL());
			cstmt.execute();

			rs = cstmt.getResultSet();
			while (rs.next()) {
				MentionResult mr = new MentionResult();
				mr.setCreated(new Date(rs.getLong("created")));
				mr.setFriend(rs.getBoolean("friend"));
				mr.setTid(rs.getString("tid"));
				mr.setUid(rs.getString("uid"));
				mr.setAuthor(rs.getString("author"));
				mr.setVip(rs.getBoolean("vip"));
				result.add(mr);
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
		return result;
	}

	/**
	 * @param uid
	 * @param platform
	 * @param start
	 * @param end
	 * @param type
	 *            1、原创
	 *            <p>
	 *            2、转发
	 *            <p>
	 *            3、评论
	 *            <p>
	 *            4、原创和转发
	 *            <p>
	 *            5、转发和评论
	 *            <p>
	 *            6、原创和评论
	 * 
	 * @return
	 */
	private List<ITweet> queryPublish(String uid, Platform platform,
			long start, long end, int type) {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			String ct = "type = 1";
			switch (type) {
			case 1: {
				ct = "(type =1 )";
				break;
			}
			case 2: {
				ct = "(type =2)";
				break;
			}
			case 3: {
				ct = "(type =3)";
				break;
			}
			case 4: {
				ct = "(type =1 or type = 2)";
				break;
			}
			case 5: {
				ct = "(type =2 or type =3)";
				break;
			}
			case 6: {
				ct = "(type =1 or type = 3)";
				break;
			}
			}
			String sql = "select * from " + this.getMonitor().getTableName()
					+ " where created<=" + end + " and created>=" + start
					+ " and " + ct + " and uid='" + uid + "' and platform="
					+ Utility.getPlatform(platform) + " order by created desc";

			cs = conn.prepareCall(sql);
			cs.execute();

			rs = cs.getResultSet();
			List<ITweet> posts = new ArrayList<ITweet>();
			while (rs.next()) {
				posts.add(this.readPost(rs));
			}
			return posts;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
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
	 * 获得指定时间区间发布的统计信息--日期分布
	 * <p>
	 * Map<发布的小时数,本小时发布的数量>
	 * 
	 * @param start
	 * @param end
	 * @param isTweet
	 *            true 微博统计（原创和转发）
	 *            <p>
	 *            false 评论统计
	 * @return
	 */
	public Map<Long, PublishCountResult> queryPublishStatisticsByDate(
			String uid, Platform platform, long start, long end, int isTweet) {
		// 时间点分布
		List<ITweet> posts = this.queryPublish(uid, platform, start, end,
				isTweet);

		Map<Long, PublishCountResult> result = new TreeMap<Long, PublishCountResult>(
				this.getLongComparator());
		for (ITweet post : posts) {
			long time = DateHelper.getTimezoneHour(post.getCreated());
			PublishCountResult r;
			if (!result.containsKey(time)) {
				r = new PublishCountResult();
				result.put(time, r);
			}
			r = result.get(time);
			if (post.getType() == PostType.Creative) {
				r.setTweets(r.getTweets() + 1);
			} else if (post.getType() == PostType.Repost) {
				r.setRetweets(r.getRetweets() + 1);
			} else {
				r.setComments(r.getComments() + 1);
			}
		}
		return result;
	}

	/**
	 * 返回指定时间区间，指定类型的微博发布按天统计信息 -- 时间段分布
	 * <p>
	 * Map<发布的小时,该小时一共发布了多少>
	 * 
	 * @param start
	 * @param end
	 * @param type
	 * @return
	 */
	public Map<Integer, PublishCountResult> queryPublishStatisticsByDay(
			String uid, Platform platform, long start, long end, int isTweet) {
		// 时间段分布
		// 选择时间区间，然后统计同一个点发布的情况，此分布是要看某个时间段哪几个点发布微博比较活跃

		List<ITweet> posts = this.queryPublish(uid, platform, start, end,
				isTweet);

		new Date(start);
		new Date(end);

		// 这里只统计小时部分是一样的内容
		Map<Integer, PublishCountResult> result = new TreeMap<Integer, PublishCountResult>(
				this.getInegerComparator());
		for (ITweet post : posts) {
			int time = DateHelper.getTimezoneOnlyHour(post.getCreated());
			PublishCountResult r;
			if (!result.containsKey(time)) {
				r = new PublishCountResult();
				result.put(time, r);
			}
			r = result.get(time);
			if (post.getType() == PostType.Creative) {
				r.setTweets(r.getTweets() + 1);
			} else if (post.getType() == PostType.Repost) {
				r.setRetweets(r.getRetweets() + 1);
			} else {
				r.setComments(r.getComments() + 1);
			}
		}

		return result;
	}

	/**
	 * 获得发布统计明细
	 * 
	 * @param start
	 * @param end
	 * @param type
	 * @return
	 */
	public Pagefile<ITweet> queryPublishDetail(long start, long end,
			PostType type, Pager pager) {

		Query query = new Query();
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setFields("*");
		query.setTableName(this.getMonitor().getTableName());
		query.setWhere(" where created<='"
				+ DateHelper.formatDBTime(new Date(end)) + "' and created>='"
				+ DateHelper.formatDBTime(new Date(start)) + "' and type="
				+ Utility.getPostType(type));
		return this.executePostsQueryWithPager(query);
	}

	// ============================================================

	/**
	 * 创建我的评论查询
	 * 
	 * @param db
	 *            为哪个db创建查询
	 * @param uid
	 * @param platform
	 * @param filter
	 * @param pager
	 * @return
	 */
	private Query buildMyCommentsQuery(String uid, Platform platform,
			FilterCommentOrient filter, Pager pager) {
		Query query = new Query();
		String tableName = this.getMonitor().getTableName();
		query.setTableName(tableName);
		query.setAsc(true);
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setSortField("created");
		query.setAsc(true);
		StringBuilder sb = new StringBuilder();
		if (filter == FilterCommentOrient.By_Me) {
			sb.append(" where uid='" + uid + "' and type =3");
		} else {
			sb.append(" where source in (");
			sb.append("select tid from ");
			sb.append(tableName);
			sb.append(" where uid = '");
			sb.append(uid);
			sb.append("') and type = 3 and uid <> '");
			sb.append(uid);
			sb.append("'");
		}
		query.setWhere(sb.toString());
		return query;
	}

	private Query buildUserTimelineQuery(String ucode, String text,
			Pager pager, FilterPostType postFilter, Platform platform) {

		Query result = new Query();
		result.setTableName(this.getMonitor().getTableName());
		result.setFields("*");
		result.setPageIndex(pager.getPageIndex());
		result.setPageSize(pager.getPageSize());
		result.setSortField("created");
		result.setAsc(true);

		StringBuilder sb = new StringBuilder();
		sb.append(" where platform = ");
		sb.append(Utility.getPlatform(platform));
		sb.append(" ");

		// 只返回本人的微博信息
		sb.append(" and ucode ='" + ucode + "' ");
		if (postFilter != FilterPostType.All) {
			if (postFilter == FilterPostType.Creative) {
				sb.append(" and type = 1 ");
			} else if (postFilter == FilterPostType.Repost) {
				sb.append(" and type = 2 ");
			} else if (postFilter == FilterPostType.Image) {
				sb.append(" and images is not null ");
			}
		}

		sb.append("and text like '%");
		sb.append(DbHelper.nomalizeText(text));
		sb.append("%'");
		result.setWhere(sb.toString());
		return result;
	}

	private Query buildHomeTimelineQuery(String uid, String text, Pager pager,
			FilterPostType postFilter, Platform platform) {

		// 目前无法搜索到我转发的原创微博里面的关键字
		// 如果要实现该功能，则需要在post上添加sourceText
		// 或者为微博统一建立一个索引器，例如将所有的微博都注入到讯库里面去
		// 门户数据库只保存关系
		Query result = new Query();
		result.setTableName(this.getMonitor().getTableName());
		result.setFields("*");
		result.setPageIndex(pager.getPageIndex());
		result.setPageSize(pager.getPageSize());
		result.setSortField("created");
		result.setAsc(true);

		StringBuilder sb = new StringBuilder();

		if (Utility.isNullOrEmpty(text)) {
			sb.append(" where (uid = '");
			sb.append(uid);
			sb.append("' or homeid = '" + uid);
			sb.append("') and platform = ");
			sb.append(Utility.getPlatform(platform));
			sb.append(" ");

			if (postFilter != FilterPostType.All) {
				if (postFilter == FilterPostType.Creative) {
					sb.append(" and type = 1 ");
				} else if (postFilter == FilterPostType.Repost) {
					sb.append(" and type = 2 ");
				} else if (postFilter == FilterPostType.Image) {
					sb.append(" and images is not null ");
				}
			} else {
				sb.append(" and (type=1 or type =2) ");
			}
		} else {
			sb.append("where text like '%");
			sb.append(DbHelper.nomalizeText(text));
			sb.append("%'");
			if (postFilter != FilterPostType.All) {
				if (postFilter == FilterPostType.Creative) {
					sb.append(" and type = 1 ");
				} else if (postFilter == FilterPostType.Repost) {
					sb.append(" and type = 2 ");
				} else if (postFilter == FilterPostType.Image) {
					sb.append(" and images is not null ");
				}
			} else {
				sb.append(" and (type=1 or type =2) ");
			}
		}

		result.setWhere(sb.toString());
		return result;
	}

	private Query buildMentionQuery(List<MentionResult> mrs) {
		Query result = new Query();
		result.setTableName(this.getMonitor().getTableName());
		result.setFields("*");
		result.setPageIndex(1);
		result.setPageSize(20);
		result.setSortField("created");
		result.setAsc(true);

		StringBuilder sb = new StringBuilder();
		if (mrs.size() > 0)
			sb.append(mrs.get(0).getTid());
		for (int i = 1; i < mrs.size(); i++) {
			sb.append(",");
			sb.append(mrs.get(i).getTid());
		}
		if (sb.length() != 0) {
			result.setWhere(" WHERE tid in(" + sb.toString() + ")");
		} else {
			result.setWhere(" WHERE 1=0");
		}
		return result;
	}

	private Query buildMentionIdsQuery(MentionQueryParam param,
			boolean forStatistics) {
		Query result = new Query();

		String uid = param.getUid();
		Platform platform = param.getPlatform();

		PostType type = param.getType();
		long start = param.getStart();
		long end = param.getEnd();
		String text = param.getText();
		FilterAccountType accFilter = param.getAccFilter();
		FilterPostType postFilter = param.getPostFilter();
		Pager pager = param.getPager();

		result.setTableName("My_Mentions");
		result.setFields("*");
		if (pager != null) {
			result.setPageIndex(pager.getPageIndex());
			result.setPageSize(pager.getPageSize());
		}
		result.setSortField("created");
		result.setAsc(true);

		StringBuilder sb = new StringBuilder();
		sb.append(" where uid = '");
		sb.append(uid);
		sb.append("' and platform = ");
		sb.append(Utility.getPlatform(platform));
		sb.append(" and created<=");
		sb.append(end);
		sb.append(" and created >=");
		sb.append(start);
		sb.append(" ");
		if (!forStatistics) {
			if (type != null) {
				sb.append("and type=" + Utility.getPostType(type));
			} else {
				sb.append("and (type =1 or type =2)");
			}
		}
		sb.append(" ");

		if (accFilter != FilterAccountType.All) {
			if (accFilter == FilterAccountType.Friend) {
				sb.append(" and friend = 1 ");
			} else if (accFilter == FilterAccountType.Vip) {
				sb.append(" and vip = 1 ");
			} else if (accFilter == FilterAccountType.Navies) {
				sb.append(" and navies = 1 ");
			}
		}

		if (postFilter != FilterPostType.All) {
			if (postFilter == FilterPostType.Creative) {
				sb.append(" and type = 1 ");
			} else if (postFilter == FilterPostType.Repost) {
				sb.append(" and type = 2 ");
			} else if (postFilter == FilterPostType.Image) {
				sb.append(" and hasimg =1 ");
			}
		}

		if (!Utility.isNullOrEmpty(text)) {
			sb.append("and text like '%");
			sb.append(DbHelper.nomalizeText(text));
			sb.append("%'");
		}
		result.setWhere(sb.toString());

		return result;
	}
}
