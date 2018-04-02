package com.xunku.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.controller.AccountController;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.manager.AuthTimestampManager;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;
import com.xunku.app.stores.MCustomStore;
import com.xunku.app.stores.TweetStore;
import com.xunku.constant.ApiCST;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 模拟爬虫
 * 
 * @author wujian
 * @created on Sep 24, 2014 5:04:19 PM
 */
public class Fetcher {

	private final static String FUN_HOME_TIMELINE = "home.timeline";
	private final static String FUN_USER_TIMELINE = "user.timeline";
	private final static String FUN_MENTIONS_TWEET = "mentions.tweets";
	private final static String FUN_MENTIONS_COMMENT = "mentions.comments";
	private final static String FUN_COMENTS_TOME = "comments.to.me";
	private final static String FUN_COMMENTS_BYME = "comments.by.me";
	private final static String FUN_COMMENTS_TIMELINE = "comments.timeline";
	private static final Logger LOG = LoggerFactory.getLogger(Fetcher.class);

	private int getPagerCount(long total, int pageSize) {
		int it = (int) total;
		return it / pageSize;
	}

	private void _moreResult(String KEY, int total, MCustomStore db,
			AppAuth auth, String funName) throws ApiException {

		int pageCnt = (total / ApiCST.COUNT_COMMENT_SINA) + 1;
		if (pageCnt > 10) {
			pageCnt = 10;
		}
		Pager pager = new Pager();
		pager.setPageSize(ApiCST.COUNT_COMMENT_SINA);
		for (int i = 2; i < pageCnt; i++) {
			pager.setPageIndex(i);
			// java不支持函数作为参数传递这里现在这么弄，有好方案再说
			Pagefile<ITweet> mores = null;
			if (funName.equals(FUN_USER_TIMELINE)) {
				mores = auth.tweetsUserTimeline(pager);
			} else if (funName.equals(FUN_HOME_TIMELINE)) {
				mores = auth.tweetsHomeTimeline(pager);
			} else if (funName.equals(FUN_MENTIONS_TWEET)) {
				mores = auth.tweetsGetByMention(pager, PostType.Creative);
			} else if (funName.equals(FUN_MENTIONS_COMMENT)) {
				mores = auth.tweetsGetByMention(pager, PostType.Comment);
			} else if (funName.equals(FUN_COMENTS_TOME)) {
				mores = auth.commentToMe(pager);
			} else if (funName.equals(FUN_COMMENTS_BYME)) {
				mores = auth.commentByMe(pager);
			} else if (funName.equals(FUN_COMMENTS_TIMELINE)) {
				mores = auth.commentsTimeline(pager);
			}
			LOG.info("{}正在获取:[" + funName + "]的第{}页数据.", auth.getAccount()
					.getName(), pager.getPageIndex());
			if (mores != null && mores.getRows() != null
					&& mores.getRows().size() != 0) {
				this._put(KEY, db, mores, auth);
			}
		}
	}

	private void _put(String Key, ITweetStore db, Pagefile<ITweet> wrapper,
			AppAuth auth) {
		if (wrapper != null) {
			List<ITweet> posts = wrapper.getRows();
			for (ITweet post : posts) {
				db.put(post);
			}
			db.flushHandlers();
			AuthTimestampManager.getInstance().putTimestamp(auth.getToken(),
					Key, System.currentTimeMillis());

		} else {
			LOG.info("落地{0}数据失败!", Key);
		}
	}

	/**
	 * 爬取官微我发出的评论和评论我的微博
	 * 
	 * @param db
	 * @param auth
	 * @throws ApiException
	 */
	public void fetchComments(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		if (auth != null) {
			if (auth.getPlatform() == Platform.Sina) {
				String sinceId = db.queryCommentSinceID(auth.getAccount()
						.getUcode());
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, sinceId);
				Pagefile<ITweet> comments = auth.commentsTimeline(pager);
				this._put(ApiCST.KEY_COMMENTS, db, comments, auth);
				if (!onlyFirstPage) {
					this._moreResult(ApiCST.KEY_COMMENTS, comments
							.getRealcount(), db, auth, FUN_COMMENTS_TIMELINE);
				}
				LOG.info("落地提到我评论的时间线" + comments.getRows().size() + "条数据");
			}
		}
	}

	/**
	 * 爬取发给我的评论
	 * 
	 * @param db
	 * @param auth
	 * @param onlyFirstPage
	 * @throws ApiException
	 */
	public void fetchCommentToMe(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		if (auth != null) {
			if (auth.getPlatform() == Platform.Sina) {
				String sinceId = db.queryCommentToMeSinceID(auth.getAccount()
						.getUcode());
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, sinceId);
				Pagefile<ITweet> comments = auth.commentToMe(pager);
				this._put(ApiCST.KEY_COMMENT_TOME, db, comments, auth);
				if (!onlyFirstPage) {
					this._moreResult(ApiCST.KEY_COMMENT_TOME, comments
							.getRealcount(), db, auth, FUN_COMENTS_TOME);
				}
				LOG.info("落地发给我的评论" + comments.getRows().size() + "条数据");
			}
		}
	}

	/**
	 * 爬取提到我的评论
	 * 
	 * @param db
	 * @param auth
	 * @param onlyFirstPage
	 * @throws ApiException
	 */
	public void fetchMentionsByComment(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		if (auth != null) {
			if (auth.getPlatform() == Platform.Sina) {
				String sinceId = db.queryCommentMentionSinceID(auth
						.getAccount().getUcode());
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, sinceId);
				Pagefile<ITweet> tweets = auth.tweetsGetByMention(pager,
						PostType.Comment);
				this._put(ApiCST.KEY_MENTION_COMMENT, db, tweets, auth);
				if (!onlyFirstPage) {
					this._moreResult(ApiCST.KEY_MENTION_COMMENT, tweets
							.getRealcount(), db, auth, FUN_MENTIONS_COMMENT);
				}
				LOG.info("落地提到我的评论" + tweets.getRows().size() + "条数据");
			}
		}
	}

	/**
	 * 爬取提到我的微博
	 * 
	 * @param db
	 * @param auth
	 * @param onlyFirstPage
	 * @throws ApiException
	 */
	public void fetchMentionsByTweet(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		if (auth != null) {
			if (auth.getPlatform() == Platform.Sina) {
				String sinceId = db.queryTweetMentionSinceID(auth.getAccount()
						.getUcode());
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, sinceId);

				Pagefile<ITweet> posts = auth.tweetsGetByMention(pager,
						PostType.Creative);
				this._put(ApiCST.KEY_MENTION_TWEET, db, posts, auth);

				if (!onlyFirstPage) {
					this._moreResult(ApiCST.KEY_MENTION_TWEET, posts
							.getRealcount(), db, auth, FUN_MENTIONS_TWEET);
				}
				LOG.info("落地提到我的微博" + posts.getRows().size() + "条数据");
			}
		}
	}

	/**
	 * 爬取官微最新发布的微博内容
	 * 
	 * @param db
	 * @param auth
	 * @throws ApiException
	 */
	public void fetchHomePosts(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		if (auth != null) {
			if (auth.getPlatform() == Platform.Sina) {

				String sinceId = db.queryHomelineSinceID(auth.getAccount()
						.getUcode());
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, sinceId);
				Pagefile<ITweet> tweets = auth.tweetsHomeTimeline(pager);

				this._put(ApiCST.KEY_USERHOMETIMELINE, db, tweets, auth);

				if (!onlyFirstPage) {
					// 落地其他页的数据
					this._moreResult(ApiCST.KEY_USERHOMETIMELINE, tweets
							.getRealcount(), db, auth, FUN_HOME_TIMELINE);
				}

				/*
				sinceId = db.queryHomelineSinceID(auth.getAccount().getUcode());
				pager = Pager.BuildSinaPager(1, ApiCST.COUNT_COMMENT_SINA,
						sinceId);
				Pagefile<ITweet> userline = auth.tweetsUserTimeline(pager);
				this._put(ApiCST.KEY_USERHOMETIMELINE, db, userline, auth);

				if (!onlyFirstPage) {
					// 落地其它页的数据
					this._moreResult(ApiCST.KEY_USERHOMETIMELINE, userline
							.getRealcount(), db, auth, FUN_HOME_TIMELINE);
				}
				*/
			}
		}
	}

	public void fetchUserPosts(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		if (auth != null) {
			if (auth.getPlatform() == Platform.Sina) {

				String sinceId = db.queryUserlineSinceID(auth.getAccount()
						.getUcode());
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, sinceId);
				Pagefile<ITweet> tweets = auth.tweetsUserTimeline(pager);

				this._put(ApiCST.KEY_USERTIMELINE, db, tweets, auth);

				if (!onlyFirstPage) {
					// 落地其他页的数据
					if (tweets.getRows().size() >= ApiCST.COUNT_COMMENT_SINA) {
						this._moreResult(ApiCST.KEY_USERTIMELINE, tweets
								.getRealcount(), db, auth, FUN_USER_TIMELINE);
					}
					
				}
			}
		}
	}

	/**
	 * 落地官微@到我的最新微博内容
	 * 
	 * @param db
	 * @param auth
	 * @throws ApiException
	 */
	public void fetchMentions(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		this.fetchMentionsByComment(db, auth, onlyFirstPage);
		this.fetchMentionsByTweet(db, auth, onlyFirstPage);
	}

	/**
	 * 爬取我评论的微博
	 * 
	 * @param db
	 * @param auth
	 * @param onlyFirstPage
	 * @throws ApiException
	 */
	public void fetchCommentByMe(MCustomStore db, AppAuth auth,
			boolean onlyFirstPage) throws ApiException {
		if (auth != null) {
			if (auth.getPlatform() == Platform.Sina) {
				String sinceId = db.queryCommentSinceID(auth.getAccount()
						.getUcode());
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, sinceId);
				Pagefile<ITweet> comments = auth.commentByMe(pager);
				this._put(ApiCST.KEY_COMMENT_BYME, db, comments, auth);
				if (!onlyFirstPage) {
					this._moreResult(ApiCST.KEY_COMMENT_BYME, comments
							.getRealcount(), db, auth, FUN_COMMENTS_BYME);
				}
				LOG.info("落地我发布的评论" + comments.getRows().size() + "条数据");
			}
		}
	}

	/**
	 * 落地我的粉丝
	 * 
	 * @param _store
	 * @param auth
	 */
	public void fetchFollowerList(AppAuth auth, AppAccount acc,
			AccountManager manager) {

		try {
			// 第一次取全集2000条
			if (!acc.isLoadedFullFans()) {
				// 尝试装载2000条
				if (auth != null) {
					if (auth.getPlatform() == Platform.Sina) {
						int sum = 0;
						Pagefile<IAccount> followers = auth.followersGet(0);
						sum = followers.getRows().size();
						manager.followerCreate(acc.getUcode(), acc
								.getPlatform(), followers.getRows());
						while (true) {

							if (followers.getNext_cursor() == 0) {
								break;
							}

							if (followers.getNext_cursor() == followers
									.getRealcount()) {
								break;
							}

							if (sum == 2000) {
								break;
							}
							followers = auth.followersGet(followers
									.getNext_cursor());
							sum += followers.getRows().size();
							manager.followerCreate(acc.getUcode(), acc
									.getPlatform(), followers.getRows());
						}
						AccountController ctl = new AccountController();
						ctl.updateFansLoaded(acc.getAccountId());
					}
				}
			} else {
				// 装载前200条
				if (auth.getPlatform() == Platform.Sina) {
					Pagefile<IAccount> followers = auth.followersGet(0);
					manager.followerCreate(acc.getUcode(), acc.getPlatform(),
							followers.getRows());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 抓取指定监测微博的转发列表
	 * <p>
	 * 消息队列指令：FetchRetweets(tid,platform)
	 * 
	 * @param tid
	 * @param platform
	 * @throws ApiException
	 */
	public void fetchRetweetList(TweetStore db, ITweet post, AppAuth auth)
			throws ApiException {

		String tid = post.getTid();
		Platform platform = post.getPlatform();

		if (db != null) {
			Pager pager = null;
			if (platform == Platform.Sina) {
				String since_id = db.queryRetweetSinceID(post.getUcode());
				pager = Pager.BuildSinaPager(0, ApiCST.COUNT_REPOST_SINA,
						since_id);

			} else if (platform == Platform.Tencent) {
				// 下面这两个值应该是从数据库里面得到的
				String pagetime = "0";
				String tweeterid = "0";
				pager = Pager.BuildTencentPager(0, ApiCST.COUNT_REPOST_TENCENT,
						pagetime, tweeterid);

			}

			if (auth == null) {
				LOG.info("无授权******************");
				return;
			}
			pager.setPageIndex(1);
			Pagefile<ITweet> wrapper = auth.retweetGet(tid, pager);

			int pageCount = this.getPagerCount(wrapper.getRealcount(),
					ApiCST.COUNT_REPOST_SINA);
			db.puts(wrapper.getRows());
			if (pageCount > 10) {
				pageCount = 10;
				LOG.info("接口只允许取2000条转发，这里只取2000条，超过的没法取！");
			}
			// 新浪2000、腾讯1000
			for (int i = 2; i <= pageCount; i++) {
				pager.setPageIndex(i);
				db.puts(auth.retweetGet(tid, pager).getRows());
			}
		}
	}

	/**
	 * 抓取指定监测微博的评论列表
	 * <p>
	 * 消息队列指令：FetchComments(tid,platform)
	 * 
	 * @param tid
	 * @param platform
	 * @throws ApiException
	 */
	public void fetchCommentList(TweetStore db, ITweet post, AppAuth auth)
			throws ApiException {

		String tid = post.getTid();
		Platform platform = post.getPlatform();

		if (db != null) {
			if (auth == null) {
				LOG.info("无授权************");
				return;
			}
			String since_id = db.queryCommentSinceID(post.getUcode());
			if (platform == Platform.Sina) {
				Pager pager = Pager.BuildSinaPager(1,
						ApiCST.COUNT_COMMENT_SINA, since_id);
				pager.setPageIndex(1);
				Pagefile<ITweet> wrapper = auth.commentsGet(tid, pager);
				db.puts(wrapper.getRows());
				// 总页码
				int pageCount = this.getPagerCount(wrapper.getRealcount(),
						ApiCST.COUNT_COMMENT_SINA);
				if (pageCount > 10) {
					pageCount = 10;
					LOG.info("接口只允许取2000条评论，这里只取2000条，超过的没法取！");
				}
				// 从第二页开始取如果有
				for (int i = 2; i <= pageCount; i++) {
					pager.setPageIndex(i);
					wrapper = auth.commentsGet(tid, pager);
					db.puts(wrapper.getRows());
				}
			}

		}
	}
}
