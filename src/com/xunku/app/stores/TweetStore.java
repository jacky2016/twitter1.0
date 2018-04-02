package com.xunku.app.stores;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.Utility;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.handlers.AccountPostHandler;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.app.monitor.IMonitor;
import com.xunku.constant.SQLCST;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 微博库
 * <p>
 * 该对象管理一个微博存储库SQL数据库
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:54:02 PM
 */
public class TweetStore implements ITweetStore {

	protected static Logger LOG = LoggerFactory.getLogger(TweetStore.class);

	public TweetStore(IMonitor monitor, Pooling pooled, AccountManager accDB) {
		this._pool = pooled;
		this._accountDb = accDB;
		this._monitor = monitor;
		this.init();

		// 帖子处理器列表
		this._postHandlers = new ArrayList<IPostHandler>();

		this._postHandlers.add(new AccountPostHandler());
	}

	/**
	 * 注册一个帖子处理程序
	 * 
	 * @param handler
	 */
	public void RegHandler(IPostHandler handler) {
		if (!this.containsHandler(handler)) {
			// 如果已经初始化了pool则注册handler否则不注册
			if (this._pool.isInitialized(handler)) {
				this._postHandlers.add(handler);
			}
			handler.initialize(this);
		}
	}

	/**
	 * 更新转发数
	 * 
	 * @param platform
	 * @param tid
	 * @param cnt
	 */
	public void updateRetweetCnt(Platform platform, String tid, int cnt) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this._pool.getConnection();
			String sql = "UPDATE " + this.getMonitor().getTableName()
					+ " SET reposts=? WHERE tid=? and platform=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cnt);
			pstmt.setString(2, tid);
			pstmt.setInt(3, Utility.getPlatform(platform));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	/**
	 * 获得转发列表的最大SinceID
	 * 
	 * @return
	 */
	public String queryRetweetSinceID(String ucode) {
		String cmdText = "select top 1 * from "
				+ this.getMonitor().getTableName()
				+ " where type = 2 and ucode ='" + ucode
				+ "' order by created desc";
		String result = this.executeLastTid(cmdText);
		LOG.info("获得微博转发的 SinceID is " + result);
		return result;
	}

	/**
	 * 获得评论列表的最大SinceID
	 * 
	 * @return
	 */
	public String queryCommentSinceID(String ucode) {
		String cmdText = "select top 1 * from "
				+ this.getMonitor().getTableName()
				+ " where type = 3 and ucode ='" + ucode
				+ "' order by created desc";
		String result = this.executeLastTid(cmdText);
		LOG.info("获得微博评论的 SinceID is " + result);
		return result;
	}

	/**
	 * 更新评论数
	 * 
	 * @param platform
	 * @param tid
	 * @param cnt
	 */
	public void updateCommentCnt(Platform platform, String tid, int cnt) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this._pool.getConnection();
			String sql = "UPDATE " + this.getMonitor().getTableName()
					+ " SET comments=? WHERE tid=? and platform=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cnt);
			pstmt.setString(2, tid);
			pstmt.setInt(3, Utility.getPlatform(platform));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	public Pagefile<ITweet> queryComments(String tid, Platform platform,
			Pager pager) {
		Query query = new Query();
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setSortField("created");
		query.setTableName(this.getMonitor().getTableName());
		query.setWhere(" where type =3 and source='" + tid + "' and platform="
				+ Utility.getPlatform(platform));
		return this.executePostsQueryWithPager(query);
	}

	public Pagefile<ITweet> queryRetweets(String tid, Platform platform,
			Pager pager) {
		Query query = new Query();
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setSortField("created");
		query.setTableName(this.getMonitor().getTableName());
		query.setWhere(" where type =2 and source='" + tid + "' and platform="
				+ Utility.getPlatform(platform));
		return this.executePostsQueryWithPager(query);
	}

	/**
	 * 获得一个Long类型的比较器
	 * 
	 * @return
	 */
	protected Comparator<Long> getLongComparator() {
		return new Comparator<Long>() {

			@Override
			public int compare(Long o1, Long o2) {
				return (int) (o1 - o2);
			}
		};
	}

	protected Comparator<Integer> getInegerComparator() {
		return new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}

		};
	}

	/**
	 * 获得当前库的表名，这个表名不可能重复
	 * 
	 * @return
	 */
	public IMonitor getMonitor() {
		return this._monitor;
	}

	/**
	 * 获得当前关联的帐号库
	 * 
	 * @return
	 */
	public AccountManager getAccountManager() {
		return this._accountDb;
	}

	/**
	 * 获得当前存储的连接对象池
	 * 
	 * @return
	 */
	public Pooling getPool() {
		return this._pool;
	}

	/**
	 * 业务ID是指每个业务表对应的检测对象ID
	 * <p>
	 * 如果是Event则这里指的是这个event对应的eventID
	 * <p>
	 * 如果是Account则这里指的是这个Account对应的AccountID
	 * <p>
	 * 如果是Weibo则这里指的是这个Weibo对应的WeiboID
	 * <p>
	 * 如果是官微返回的是CustomID
	 * 
	 * @return
	 */
	public int getMonitorID() {
		return this._monitor.getMonitorId();
	}

	/**
	 * 通过查询获得一个Post
	 * 
	 * @param query
	 * @return
	 */
	public ITweet executePostQuery(String tid, Platform platform) {

		Query query = buildPostByTidQuery(tid, platform);

		List<ITweet> posts = this.executePostsQuery(query);
		if (posts.size() == 1) {
			return posts.get(0);
		}
		return null;
	}

	/**
	 * 通过Query获得不带分页的Post集合，该方法直接执行query.toSql方法
	 * 
	 * @param query
	 * @return
	 */
	public List<ITweet> executePostsQuery(Query query) {
		List<ITweet> posts = this.getPostsByQuery(query);
		return posts;
	}

	public List<ITweet> executePostsQuery(String cmdText) {
		List<ITweet> posts = this.getPostsByQuery(cmdText);
		return posts;
	}

	/**
	 * 在微博库上执行一个无返回的查询，适合更新删除操作
	 * 
	 * @param query
	 */
	public int executeNonQuery(String cmdText) {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			cs = conn.prepareCall(cmdText);
			return cs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cs, rs);
		}
		return 0;
	}

	/**
	 * @param cmdText
	 * @return
	 */
	public int count(String cmdText) {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this._pool.getConnection();
			cs = conn.prepareCall(cmdText);
			rs = cs.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cs, rs);
		}
		return result;
	}

	/**
	 * 获得该存储上最大的记录
	 * 
	 * @return
	 */
	public int executeMaxId() {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this._pool.getConnection();
			cs = conn.prepareCall("select max(id) from "
					+ this.getMonitor().getTableName());
			cs.execute();
			rs = cs.getResultSet();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		} finally {
			Utility.closeConnection(conn, cs, rs);
		}
	}

	public String executeLastTid(String cmdText) {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String result = "1";
		try {
			conn = this._pool.getConnection();
			cs = conn.prepareCall(cmdText);
			cs.execute();
			rs = cs.getResultSet();
			if (rs.next()) {
				result = rs.getString("tid");
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		} finally {
			Utility.closeConnection(conn, cs, rs);
		}
	}

	public Date executeLastCT() {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		Date result = null;
		try {
			String cmdText = "select top 1 CT from "
					+ this.getMonitor().getTableName() + " order by CT desc";
			conn = this._pool.getConnection();
			cs = conn.prepareCall(cmdText);
			cs.execute();
			rs = cs.getResultSet();
			if (rs.next()) {
				result = rs.getTimestamp(1);
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cs, rs);
		}
		return result;
	}

	/**
	 * 通过Query获得带分页的Post集合
	 * 
	 * @param query
	 * @return
	 */
	public Pagefile<ITweet> executePostsQueryWithPager(Query query) {

		Pagefile<ITweet> result = getPostsByQueryWithPage(query);
		// ITweet org = result.getRows().get(0).getOriginalTweet();
		// ITweet source = result.getRows().get(0).getSource();

		/*
		 * for (ITweet post : result.getRows()) { this.buildPost(post); }
		 */
		return result;
	}

	public ITweet getITweet(String table, String tid, Platform platform) {
		ITweet post = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			String sql = "select * from " + table + " where tid=" + tid
					+ " and platform=" + Utility.getPlatform(platform);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				post = this.readPost(rs);
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
		return post;
	}

	/**
	 * 删除指定的tid帖子，该删除是真删除，操作需谨慎
	 * 
	 * @param tid
	 *            帖子唯一编号
	 */
	public void postDeleteByTid(String tid, Platform platform) {
		this.deletePostByTidFromDB(tid, platform);
	}

	/**
	 * 从数据库中获得指定tid的帖子信息
	 * 
	 * @param tid
	 *            帖子编号
	 * @param platform
	 *            平台信息
	 * @return
	 */
	public ITweet postGetByTid(String tid, Platform platform) {
		ITweet p = this.getPostByTid(tid, platform);
		// this.buildPost(p);
		return p;
	}

	@Override
	public void flushHandlers() {
		for (IPostHandler handler : this._postHandlers) {
			handler.flush(this);
		}
	}

	/**
	 * 向当前的微博库中追加帖子
	 * <p>
	 * 如果当前库中有则更新库中的数据否则插入新的数据
	 * 
	 * @param post
	 */
	public PutResult put(ITweet post) {
		try {
			if (post != null) {

				// 回复帖子不更新原始贴
				// 新浪回复里面的Status的状态不对，这里只同步转发的状态
				if (post.getOriginalTweet() != null
						&& post.getType() != PostType.Comment) {
					// 同步一下原始微博
					this.put(post.getOriginalTweet());
				}
				// 如果有引用贴，则同步一下引用贴
				if (post.getSource() != null
						&& post.getType() != PostType.Comment) {
					this.put(post.getSource());
				}
				// 同步本帖
				return this._put(post);
			}
			return PutResult.Nothing;
		} catch (Exception ex) {
			ex.printStackTrace();
			return PutResult.Nothing;
		}
	}

	public void processHandlers(ITweet post, PutResult pr) {
		for (IPostHandler hander : this._postHandlers) {
			if (pr != PutResult.Nothing) {
				hander.processPost(post, pr, this);
			}
		}
	}

	protected void clear(String cmdText) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getPool().getConnection();
			pstmt = conn.prepareStatement(cmdText);
			pstmt.execute();

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

	/**
	 * 向当前的微博库中追加帖子
	 * <p>
	 * 如果当前库中有则更新库中的数据否则插入新的数据
	 * 
	 * @param posts
	 */
	public void puts(List<ITweet> posts) {
		if (posts != null) {
			for (ITweet post : posts) {
				this.put(post);
			}
			this.flushHandlers();
		}

	}

	/**
	 * 向当前的微博库中追加帖子
	 * <p>
	 * 如果当前库中有则更新库中的数据否则插入新的数据
	 * 
	 * @param post
	 */
	private PutResult _put(ITweet post) {

		if (Utility.isNullOrEmpty(post.getUid())) {
			LOG.info("post的uid为空" + post);
			return PutResult.Nothing;
		}

		if (Utility.isNullOrEmpty(post.getTid())) {
			LOG.info("post的tid为空" + post);
			return PutResult.Nothing;
		}

		// TODO 此方法有待验证...
		// if (post.getAuthor() != null) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = this._pool.getConnection();
			cstmt = conn.prepareCall("{call " + this.getPutProcName()
					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, post.getUcode());
			cstmt.setInt(2, Utility.getPlatform(post.getPlatform()));
			cstmt.setString(3, post.getTid());
			cstmt.setInt(4, Utility.getPostType(post.getType()));
			cstmt.setString(5, post.getText());
			cstmt.setLong(6, post.getCreated());
			if (post.getFrom() == null) {
				cstmt.setInt(7, 0);
			} else {
				cstmt.setInt(7, post.getFrom().getId());
			}
			cstmt.setString(8, post.getUcode());
			cstmt.setInt(9, post.getReposts());
			cstmt.setInt(10, post.getComments());
			if (post.getSource() != null) {
				cstmt.setString(11, post.getSource().getTid());
			} else {
				cstmt.setString(11, null);
			}
			cstmt.setString(12, Utility.getImageList(post.getImages()));
			// cstmt.setString(13, post.getUrl());
			cstmt.setString(13, null);
			cstmt.setInt(14, post.getLayer());
			cstmt.setString(15, post.getHomeTimeline());

			if (post.getOriginalTweet() != null) {
				cstmt.setString(16, post.getOriginalTweet().getTid());
			} else {
				cstmt.setString(16, null);
			}

			cstmt.registerOutParameter(17, java.sql.Types.INTEGER);

			cstmt.execute();

			int result = cstmt.getInt(17);
			// 管道处理器开始处理post对象
			PutResult pR = PutResult.Nothing;
			if (result == 1) {
				pR = PutResult.Add;
			} else if (result == 2) {
				pR = PutResult.Set;
			}
			this.processHandlers(post, pR);
			return pR;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, null);
		}
		return PutResult.Nothing;

	}

	// ======================= 私有方法 ========================

	private Query buildPostByTidQuery(String tid, Platform platform) {

		Query query = new Query();
		query.setAsc(true);
		query.setFields("*");
		query.setSortField("created");
		query.setTableName(this.getMonitor().getTableName());
		query.setWhere(" where tid='" + tid + "' and platform ="
				+ Utility.getPlatform(platform));
		return query;
	}

	private ITweet getPostByTid(String tid, Platform platform) {

		// 查询数据库获得最新的Post对象
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ITweet p = null;
		try {
			conn = this._pool.getConnection();
			String sql = "SELECT * FROM " + this._monitor.getTableName()
					+ " WHERE tid=? AND platform=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tid);
			pstmt.setInt(2, Utility.getPlatform(platform));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				p = this.readPost(rs);
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
		return p;

	}

	public void deletePostByTidFromDB(String tid, Platform platform) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this._pool.getConnection();
			String sql = "DELETE FROM " + this._monitor.getTableName()
					+ " WHERE tid=? and platform=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tid);
			pstmt.setInt(2, Utility.getPlatform(platform));
			pstmt.execute();
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
	}

	public void init() {
		// 如果表不存在则创建
		try {
			String table = DbHelper.getTemplateTable();
			String proce = DbHelper.getTemplateProce();

			String tableName = this._monitor.getTableName();
			String sqlCreateTable = table.replace(SQLCST.TEMPLATE_TABLE_NAME,
					tableName);
			DbHelper.dbCreateSQLObject(this._pool, tableName, sqlCreateTable);

			String procName = SQLCST.PROC_PUT_PREFIX + tableName;
			String sqlCreateProc = proce.replace(SQLCST.TEMPLATE_TABLE_NAME,
					tableName);
			DbHelper.dbCreateSQLObject(this._pool, procName, sqlCreateProc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<ITweet> getPostsByQuery(String cmdText) {
		Connection conn = null;
		CallableStatement cstmt = null;
		List<ITweet> result = new ArrayList<ITweet>();
		ResultSet rs = null;
		try {
			conn = this._pool.getConnection();
			cstmt = conn.prepareCall(cmdText);
			cstmt.execute();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				result.add(this.readPost(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, rs);
		}
		return result;
	}

	private List<ITweet> getPostsByQuery(Query query) {
		return this.getPostsByQuery(query.toSQL());
	}

	private Pagefile<ITweet> getPostsByQueryWithPage(Query query) {

		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<ITweet> pagefile = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = this._pool.getConnection();
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

	protected ITweet readPost(ResultSet rs) {
		ITweet tweet = TweetFactory.createTweet(rs, this._accountDb);

		tweet.setStore(this);
		return tweet;
	}

	private boolean containsHandler(IPostHandler handler) {

		for (IPostHandler h : this._postHandlers) {
			if (h.getName().equals(handler.getName())) {
				return true;
			}
		}

		return false;
	}

	private String getPutProcName() {
		return SQLCST.PROC_PUT_PREFIX + this._monitor.getTableName();
	}

	// 保存微博数据的表名称
	private IMonitor _monitor;

	private Pooling _pool;

	private List<IPostHandler> _postHandlers;

	private AccountManager _accountDb;

}
