package com.xunku.app.handlers.spreads;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.result.spread.RetweetStatisResult;
import com.xunku.app.stores.MSpreadStore;
import com.xunku.app.stores.TweetStore;

/**
 * 转发，关键用户分析\转发层级分析
 * <p>
 * 每个检测对象的转发统计表保持在TOP-100的数据量
 * 
 * @author wujian
 * @created on Jul 23, 2014 9:43:57 AM
 */
public class SRetweetHandler implements IPostHandler {

	
	/**
	 * 传播分析 - 关键用户分析
	 */
	public static final String SQL_SPREAD_RETWEET = "spreads/spread.retweet.sql";

	
	private final int SIZE = 10;// 粉丝取TOP几

	private List<RetweetStatisResult> top_Spread_Followers;
	private List<RetweetStatisResult> top_Spread_Retweets;
	private List<RetweetStatisResult> top_Unspread_Retweets;

	private RetweetStatisResult findRetweetMin() {
		if (top_Spread_Retweets.size() == 0) {
			return null;
		}

		RetweetStatisResult result = top_Spread_Retweets.get(0);
		for (RetweetStatisResult r : top_Spread_Retweets) {
			if (result.getRetweets() < r.getRetweets()) {
				result = r;
			}
		}
		return result;
	}

	private RetweetStatisResult findFollowerMin() {
		if (top_Spread_Followers.size() == 0) {
			return null;
		}

		RetweetStatisResult result = top_Spread_Followers.get(0);
		for (RetweetStatisResult r : top_Spread_Followers) {
			if (result.getFollowes() < r.getFollowes()) {
				result = r;
			}
		}
		return result;
	}

	private RetweetStatisResult findUnFollowerMin() {
		if (top_Unspread_Retweets.size() == 0) {
			return null;
		}

		RetweetStatisResult result = top_Unspread_Retweets.get(0);
		for (RetweetStatisResult r : top_Unspread_Retweets) {
			if (result.getFollowes() < r.getFollowes()) {
				result = r;
			}
		}
		return result;
	}

	private RetweetStatisResult createResult(IAccount author, ITweet post) {
		RetweetStatisResult result = new RetweetStatisResult();
		result.setFollowes(author.getFollowers());
		result.setLevel(post.getLayer());
		result.setLocation(author.getLocation());
		result.setName(author.getName());
		result.setRetweets(post.getReposts());
		result.setRetweetTime(post.getCreated());
		return result;
	}

	@Override
	public void initialize(TweetStore db) {

		// 从数据库里读取粉丝数
		MSpreadStore mdb = (MSpreadStore) db;

		this.top_Spread_Followers = mdb.querySpreadedKeyManByFans();

		this.top_Spread_Retweets = mdb.querySpreadedKeyManByNums();

		this.top_Unspread_Retweets = mdb.queryUnspreadKeyMan(false);

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		// 关键用户只统计转发的微博
		if (pr == PutResult.Add && post.getType() == PostType.Repost) {
			// 获得转发作者
			IAccount author = post.getAuthor();

			if (author != null) {

				int sid = storeDB.getMonitorID();
				Pooling pool = storeDB.getPool();
				// 当前转发统计结果
				RetweetStatisResult current = this.createResult(author, post);

				if (post.getReposts() != 0) {
					// 扩散 - 按粉丝
					putSpreadFollowers(post, author, sid, pool, current);

					// 扩散 - 按转发
					putSpreadRetweet(post, author, sid, pool, current);
				} else {// 未扩散
					// 当前的结果
					putUnspread(post, author, sid, pool, current);
				}

			}
		}

	}

	private void putSpreadFollowers(ITweet post, IAccount author, int sid,
			Pooling pool, RetweetStatisResult current) {
		if (this.top_Spread_Followers.size() <= SIZE) {
			// 没达到TOP10直接插入
			this.sys_inc_Spread_Retweet_Fans_Statis(sid, pool, post, author);
			this.top_Spread_Followers.add(current);
		} else {
			RetweetStatisResult min = this.findFollowerMin();
			if (min != null) {
				if (min.getFollowes() > min.getFollowes()) {
					this.sys_inc_Spread_Retweet_Fans_Statis(sid, pool, post,
							author);
					min = current;
				}
			}
		}
	}

	private void putSpreadRetweet(ITweet post, IAccount author, int sid,
			Pooling pool, RetweetStatisResult current) {
		if (this.top_Spread_Retweets.size() <= SIZE) {
			// 没达到TOP10直接插入
			this.sys_inc_Spread_Retweet_Nums_Statis(sid, pool, post, author);
			this.top_Spread_Retweets.add(current);
		} else {
			RetweetStatisResult min = this.findRetweetMin();
			if (min != null) {
				if (current.getRetweets() > min.getRetweets()) {
					this.sys_inc_Spread_Retweet_Nums_Statis(sid, pool, post,
							author);
					// 把原来最小的换掉，数据库的清理交给JOB来处理
					min = current;
				}
			}
		}
	}

	private void putUnspread(ITweet post, IAccount author, int sid,
			Pooling pool, RetweetStatisResult current) {
		if (this.top_Unspread_Retweets.size() < SIZE) {
			this
					.sys_inc_Spread_Retweet_Unspread_Statis(sid, pool, post,
							author);
			this.top_Unspread_Retweets.add(current);
		} else {
			RetweetStatisResult min = this.findUnFollowerMin();
			if (min != null) {
				if (current.getFollowes() > min.getFollowes()) {
					this.sys_inc_Spread_Retweet_Unspread_Statis(sid, pool,
							post, author);
					this.top_Unspread_Retweets.add(current);
					// 把原来最小的换掉，数据库的清理交给JOB来处理
					min = current;
				}
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	// =======================================================

	private void sys_inc_Spread_Retweet_Nums_Statis(int spreadid, Pooling pool,
			ITweet post, IAccount author) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字

			String tid = post.getTid();
			long created = post.getCreated();
			int retweets = post.getReposts();
			String name = author.getName();
			String location = author.getLocation();
			int followers = author.getFollowers();
			boolean vip = author.isVerified();
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Retweet_Nums_Statis(?,?,?,?,?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setString(2, tid);
			cstmt.setTimestamp(3, new Timestamp(created));
			cstmt.setInt(4, retweets);
			cstmt.setString(5, name);
			cstmt.setString(6, location);
			cstmt.setInt(7, followers);
			cstmt.setBoolean(8, vip);
			cstmt.execute();
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
	}

	private void sys_inc_Spread_Retweet_Fans_Statis(int spreadid, Pooling pool,
			ITweet post, IAccount author) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			String tid = post.getTid();
			long created = post.getCreated();
			int retweets = post.getReposts();
			String name = author.getName();
			String location = author.getLocation();
			int followers = author.getFollowers();
			boolean vip = author.isVerified();
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Retweet_Fans_Statis(?,?,?,?,?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setString(2, tid);
			cstmt.setTimestamp(3, new Timestamp(created));
			cstmt.setInt(4, retweets);
			cstmt.setString(5, name);
			cstmt.setString(6, location);
			cstmt.setInt(7, followers);
			cstmt.setBoolean(8, vip);
			cstmt.execute();
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
	}

	private void sys_inc_Spread_Retweet_Unspread_Statis(int spreadid,
			Pooling pool, ITweet post, IAccount author) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			String tid = post.getTid();
			long created = post.getCreated();
			int retweets = post.getReposts();
			String name = author.getName();
			String location = author.getLocation();
			int followers = author.getFollowers();
			boolean vip = author.isVerified();
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Retweet_Unspread_Statis(?,?,?,?,?,?,?,?)}");
			cstmt.setInt(1, spreadid);
			cstmt.setString(2, tid);
			cstmt.setTimestamp(3, new Timestamp(created));
			cstmt.setInt(4, retweets);
			cstmt.setString(5, name);
			cstmt.setString(6, location);
			cstmt.setInt(7, followers);
			cstmt.setBoolean(8, vip);
			cstmt.execute();
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
	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
