package com.xunku.app.monitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Fetcher;
import com.xunku.app.Utility;
import com.xunku.app.adapters.APIAdpaterFactory;
import com.xunku.app.controller.MessageController;
import com.xunku.app.controller.SendingController;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.FilterAccountType;
import com.xunku.app.enums.FilterCommentOrient;
import com.xunku.app.enums.FilterPostType;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.XKErrorCST;
import com.xunku.app.handlers.follower.FollowersNumHandler;
import com.xunku.app.handlers.follower.FollowesTopNHandler;
import com.xunku.app.handlers.follower.GenderHandler;
import com.xunku.app.handlers.follower.LocalHandler;
import com.xunku.app.handlers.follower.VipHandler;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IFollowerHandler;
import com.xunku.app.interfaces.IRefreshStrategy;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.ApiToken;
import com.xunku.app.model.ApiTokenInfo;
import com.xunku.app.model.App;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.TweetUrl;
import com.xunku.app.model.UploadImage;
import com.xunku.app.params.MentionQueryParam;
import com.xunku.app.parser.TweetUrlParserFactory;
import com.xunku.app.result.AccountTrendResult;
import com.xunku.app.result.BeTrendResult;
import com.xunku.app.result.FansResult;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.NavieCountResult;
import com.xunku.app.result.NavieResult;
import com.xunku.app.result.PublishCountResult;
import com.xunku.app.result.RepostOrganizationResult;
import com.xunku.app.result.Result;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.WeiboTextResult;
import com.xunku.app.stores.MCustomStore;
import com.xunku.app.strategy.DefaultAppAccountStrategy;
import com.xunku.constant.ApiCST;
import com.xunku.constant.WeiboType;
import com.xunku.dao.base.OrganizationsDao;
import com.xunku.dao.event.DataAnalysisDao;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.base.OrganizationsDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.event.DataAnalysisDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Mention;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.ImageUtil;
import com.xunku.utils.Pagefile;

/**
 * 官微管理器，负责官方微博分析
 * 
 * @author wujian
 * @created on Jun 18, 2014 6:21:17 PM
 */
public class TweetOfficalManager extends MonitorManager {

	protected static TweetOfficalManager _manager;

	public synchronized static TweetOfficalManager getInstance(
			AppContext context) {
		if (_manager == null) {
			_manager = new TweetOfficalManager(context);
		}
		return _manager;
	}

	public void landing(Custom custom, boolean onlyFirstPage) {
		LOG.info("官微监测->监测对象:{}的数据开始落地！", custom.getName());

		MCustomStore db = this.getStore(custom);

		CustomMonitor monitor = custom.getMonitor();

		try {
			monitor.landing(this.getAccountManager(), db, onlyFirstPage);

		} catch (ApiException e) {
			LOG.error("落地客户" + custom.getName() + "的数据时失败.", e);
		}
	}

	// ========================= 考核管理 ===========================
	/**
	 * 功能描述<微博内容统计【考核管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<ITweet>
	 * @version twitter 1.0
	 * @date Aug 20, 20145:18:22 PM
	 */
	public Pagefile<WeiboTextResult> queryTweetCountDetail(User user,
			Pager pager, long start, long end, String uid) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));

		// 得到这个客户下面的网评员
		OrganizationsDao dao = new OrganizationsDaoImpl();
		List<Organization> orgs = dao.queryOrganizationList(user.getCustomID());

		List<String> uids = new ArrayList<String>();
		for (Organization org : orgs) {
			uids.add(org.getUid());
		}
		return store.queryTweetCountDetail(pager, start, end, uid, uids);
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
	public Pagefile<RepostOrganizationResult> queryRODetail(User user,
			Pager pager, String tid) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));

		OrganizationsDao dao = new OrganizationsDaoImpl();
		List<Organization> orgs = dao.queryOrganizationList(user.getCustomID());

		List<Integer> orgids = new ArrayList<Integer>();
		for (Organization org : orgs) {
			orgids.add(org.getId());
		}

		return store.queryRODetail(pager, tid, orgids);
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
	public Pagefile<NavieCountResult> queryNavieCount(User user, Pager pager,
			long start, long end) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryNavieCount(pager, start, end);
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
	public Pagefile<NavieResult> queryNavieResult(User user, Pager pager,
			int navieid) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryNavieResult(pager, navieid);
	}

	/**
	 * 功能描述<网评员总转评到我的个数反查列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<ITweet>
	 * @version twitter 1.0
	 * @date Aug 21, 20146:24:01 PM
	 */
	public Pagefile<ITweet> queryNavieRCResult(User user, Pager pager,
			int navieid, long start, long end, WeiboType type) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryNavieRCResult(pager, navieid, start, end, type);
	}

	/**
	 * 功能描述<删除网评员的账号>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Sep 1, 20144:41:28 PM
	 */
	public boolean deleteNavieRecord(User user, int navieid, String uid) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.deleteNavieRecord(navieid, uid);
	}

	/**
	 * 功能描述<获取转发机构的微博数>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Sep 4, 20144:35:24 PM
	 */
	public int queryOrganWeibos(User user, String uid, long start, long end) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryOrganWeibos(uid, start, end);
	}

	public int queryMyMentions(User user, String uid, long start, long end) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryMyMentions(uid, start, end);
	}

	public Pagefile<ITweet> queryOrganITweets(Pager pager, User user,
			String uid, long start, long end) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryOrganITweets(pager, uid, start, end);
	}

	public Pagefile<Mention> queryMentionITweet(Pager pager, User user,
			String uid, long start, long end) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryMentionITweet(pager, uid, start, end);
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
	public ITweet queryITweetByTid(User user, String tid, Platform platform) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.queryITweetByTid(tid, platform);
	}

	// ======================== 微博服务 ==========================

	private void updateAccountOnline(AppAuth auth) {
		try {
			IAccount account = auth.accountGet();
			this._context.getAccountManager().updateAccount(account);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得指定帐号的非实时微博信息
	 * 
	 * @param uid
	 * @param query
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> TweetsHomeTimeline(User user, String uid,
			String text, Pager pager, FilterPostType postType, Platform platform) {

		MCustomStore db = this.getStore(this.getCutsomByUser(user));

		return db.tweetsHomeTimeline(uid, text, pager, postType, platform);
	}

	/**
	 * 实时获得指定帐号的微博信息
	 * 
	 * @param uid
	 * @param query
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> TweetsHomeTimelineOnline(User user, String uid,
			String query, Pager pager, FilterPostType postType,
			Platform platform, boolean onlyFirstPage) {

		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			LOG.info("未找到可调用的授权，只能返回[我的微博]的历史数据！");
			return this.TweetsHomeTimeline(user, uid, query, pager, postType,
					platform);
		}

		Fetcher fetcher = new Fetcher();

		if (this._strategy.shouldImmediately(auth, ApiCST.KEY_USERHOMETIMELINE)) {
			Custom custom = this.getCutsomByUser(user);
			MCustomStore db = this.getStore(custom);
			try {
				this.updateAccountOnline(auth);
				fetcher.fetchHomePosts(db, auth, onlyFirstPage);
			} catch (ApiException e) {
				LOG.error("同步我的首页时失败", e);
			}
		}
		return this.TweetsHomeTimeline(user, uid, query, pager, postType,
				platform);

	}

	// ========= 提到我的 =========

	/**
	 * 获得提到该帐号的微博信息
	 * 
	 * @param uid
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> TweetsGetByMention(User user, String uid,
			Platform platform, long start, long end, Pager pager) {

		MentionQueryParam param = new MentionQueryParam();
		param.setUser(user);
		param.setUid(uid);
		param.setPlatform(platform);
		param.setStart(start);
		param.setEnd(end);
		param.setPager(pager);
		param.setAccFilter(FilterAccountType.All);
		param.setPostFilter(FilterPostType.All);

		return this.tweetsGetByMention(param);
	}

	/**
	 * 实时获得提到该帐号的微博信息
	 * 
	 * @param uid
	 * @param platform
	 * @param text
	 * @param accFilter
	 * @param postFilter
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> TweetsGetByMentionOnline(User user, String uid,
			Platform platform, long start, long end, String text,
			FilterAccountType accFilter, FilterPostType postFilter,
			Pager pager, boolean onlyFirstPage) {

		MentionQueryParam param = new MentionQueryParam();
		param.setUser(user);
		param.setUid(uid);
		param.setPlatform(platform);
		param.setStart(start);
		param.setEnd(end);
		param.setText(text);
		param.setAccFilter(accFilter);
		param.setPostFilter(postFilter);
		param.setPager(pager);

		AppAuth auth = this.getCustomManager().getAuthByUid(param.getUser(),
				param.getUid(), param.getPlatform());

		if (auth == null) {
			LOG.info("未找到可调用的授权，只能返回[提到我的]的历史数据！");
			return this.tweetsGetByMention(param);
		}

		Custom custom = this.getCutsomByUser(param.getUser());
		MCustomStore db = this.getStore(custom);
		if (this._strategy.shouldImmediately(auth, ApiCST.KEY_MENTION_TWEET)) {
			try {
				new Fetcher().fetchMentionsByTweet(db, auth, onlyFirstPage);
			} catch (ApiException e) {
				LOG.error("同步提到我的微博时失败", e);
			}
		}
		if (this._strategy.shouldImmediately(auth, ApiCST.KEY_MENTION_COMMENT)) {
			try {
				new Fetcher().fetchMentionsByComment(db, auth, onlyFirstPage);
			} catch (ApiException e) {
				LOG.error("同步提到我的评论时失败", e);
			}
		}

		return this.tweetsGetByMention(param);
	}

	// =========== 我的评论 =============

	/**
	 * 获得我的评论
	 * 
	 * @param uid
	 * @param platform
	 * @param filter
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> commentsGetByAccount(User user, String uid,
			Platform platform, FilterCommentOrient filter, Pager pager) {
		MCustomStore store = this.getStore(this.getCutsomByUser(user));
		return store.commentsGetByAccount(user, uid, platform, filter, pager);
	}

	/**
	 * 实时获得我的评论
	 * 
	 * @param uid
	 * @param platform
	 * @param filter
	 * @param pager
	 * @return
	 */
	public Pagefile<ITweet> commentsGetByAccountOnline(User user, String uid,
			Platform platform, FilterCommentOrient filter, Pager pager,
			boolean onlyFirstPage) {

		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			LOG.info("未找到可调用的授权，只能返回[我的评论]的历史数据！");
			return this
					.commentsGetByAccount(user, uid, platform, filter, pager);
		}
		Custom custom = this.getCutsomByUser(user);
		MCustomStore db = this.getStore(custom);

		if (this._strategy.shouldImmediately(auth, ApiCST.KEY_COMMENT_BYME)
				&& filter == FilterCommentOrient.By_Me) {
			try {
				new Fetcher().fetchCommentByMe(db, auth, onlyFirstPage);
			} catch (ApiException e) {
				LOG.error("同步我发布的评论时失败", e);
			}
		}

		if (this._strategy.shouldImmediately(auth, ApiCST.KEY_COMMENT_TOME)
				&& filter == FilterCommentOrient.To_Me) {
			try {
				new Fetcher().fetchCommentToMe(db, auth, onlyFirstPage);
			} catch (ApiException e) {
				LOG.error("同步发给我的评论时失败", e);
			}
		}

		return this.commentsGetByAccount(user, uid, platform, filter, pager);

	}

	// ================== 查看评论和转发 ===============

	/**
	 * 用uid这个帐号查看platform平台上的tid的回复列表
	 * 
	 * @param uid
	 * @param tid
	 * @param platform
	 * @param pager
	 */
	public Result<Pagefile<ITweet>> viewCommentsOnline(String tid,
			Platform platform, Pager pager, User user) {

		Result<Pagefile<ITweet>> result = new Result<Pagefile<ITweet>>();

		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				platform);

		if (auth == null) {
			LOG.info("查看{}的回复列表时未找到授权", tid);
			result.setError(XKErrorCST.AUTH_NOTEXISTS_CODE, "未找到授权信息，无法查看回复列表");
			return result;
		}

		try {
			Pagefile<ITweet> comments = auth.commentsGet(tid, pager);
			if (comments != null) {
				result.setData(comments);
				Custom custom = this.getCutsomByUser(user);
				MCustomStore store = this.getStore(custom);
				if (store == null) {
					LOG.info("查看{}的回复列表是，无法获得对应的存储，数据无法落地，但可以查看", tid);
				} else {
					store.updateCommentCnt(platform, tid, comments
							.getRealcount());
					for (ITweet post : comments.getRows()) {
						store.put(post);
					}
					store.flushHandlers();
				}
			}
		} catch (ApiException e) {
			result.setError(e.getErrorCode(), e.getError());
			LOG.error("获得回复列表失败", e);
		}

		return result;

	}

	public Result<Pagefile<ITweet>> viewCommentsOnline(String url, Pager pager,
			User user) {
		TweetUrl turl = TweetUrlParserFactory.createTweetUrl(url);

		return this.viewCommentsOnline(turl.getTid(), turl.getPlatform(),
				pager, user);
	}

	public Result<Pagefile<ITweet>> viewRetweetsOnline(String url, Pager pager,
			User user) {
		TweetUrl turl = TweetUrlParserFactory.createTweetUrl(url);
		return this.viewRetweetsOnline(turl.getTid(), turl.getPlatform(),
				pager, user);
	}

	public Result<Pagefile<ITweet>> viewComments(String tid, Platform platform,
			Pager pager, User user) {
		Custom custom = this.getCutsomByUser(user);
		MCustomStore store = this.getStore(custom);

		Result<Pagefile<ITweet>> result = new Result<Pagefile<ITweet>>();

		Pagefile<ITweet> tweets = store.queryComments(tid, platform, pager);

		result.setData(tweets);

		return result;
	}

	public Result<Pagefile<ITweet>> viewComments(String uid, String url,
			Pager pager, User user) {
		TweetUrl turl = TweetUrlParserFactory.createTweetUrl(url);

		return this
				.viewComments(turl.getTid(), turl.getPlatform(), pager, user);

	}

	public Result<Pagefile<ITweet>> viewRetweets(String url, Pager pager,
			User user) {
		TweetUrl turl = TweetUrlParserFactory.createTweetUrl(url);
		return this
				.viewRetweets(turl.getTid(), turl.getPlatform(), pager, user);
	}

	public Result<Pagefile<ITweet>> viewRetweets(String tid, Platform platform,
			Pager pager, User user) {

		Custom custom = this.getCutsomByUser(user);
		MCustomStore store = this.getStore(custom);

		Result<Pagefile<ITweet>> result = new Result<Pagefile<ITweet>>();

		result.setData(store.queryRetweets(tid, platform, pager));

		return result;

	}

	/**
	 * 用uid查看platform上tid的转发列表
	 * 
	 * @param uid
	 * @param tid
	 * @param platform
	 * @param pager
	 */
	public Result<Pagefile<ITweet>> viewRetweetsOnline(String tid,
			Platform platform, Pager pager, User user) {

		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				platform);

		Result<Pagefile<ITweet>> result = new Result<Pagefile<ITweet>>();
		if (auth == null) {
			LOG.info("查看{}的转发列表时未找到授权", tid);
			result.setError(XKErrorCST.AUTH_NOTEXISTS_CODE, "未找到授权信息，无法查看转发列表");
			return result;
		}

		try {
			Pagefile<ITweet> tweets = auth.retweetGet(tid, pager);
			if (tweets != null) {
				result.setData(tweets);
				Custom custom = this.getCutsomByUser(user);
				MCustomStore store = this.getStore(custom);
				if (store == null) {
					LOG.info("查看{}的转发列表是，无法获得对应的存储，数据无法落地，但可以查看", tid);
				} else {
					store
							.updateRetweetCnt(platform, tid, tweets
									.getRealcount());
					for (ITweet post : tweets.getRows()) {
						store.put(post);
					}
					store.flushHandlers();
				}
			}
		} catch (ApiException e) {
			result.setError(e.getErrorCode(), e.getError());
			LOG.error("获得转发列表失败", e);
		}

		return result;
	}

	// ================== 分析数据 ===================
	/**
	 * 发布统计时间区间
	 * 
	 * @param start
	 * @param end
	 * @param unit
	 */
	public Map<Long, PublishCountResult> viewPublishByDate(String uid,
			Platform platform, long start, long end, int isTweet, User user) {

		Custom custom = this.getCutsomByUser(user);

		MCustomStore store = this.getStore(custom);

		return store.queryPublishStatisticsByDate(uid, platform, start, end,
				isTweet);
	}

	/**
	 * 发布统计时间段
	 * 
	 * @param start
	 * @param end
	 * @param type
	 * @param user
	 * @return
	 */
	public Map<Integer, PublishCountResult> viewPublishByDay(String uid,
			Platform platform, long start, long end, int isTweet, User user) {

		Custom custom = this.getCutsomByUser(user);

		MCustomStore store = this.getStore(custom);

		return store.queryPublishStatisticsByDay(uid, platform, start, end,
				isTweet);
	}

	/**
	 * 获得发布统计的明细
	 * 
	 * @param start
	 * @param end
	 * @param type
	 * @param user
	 * @return
	 */
	public Pagefile<ITweet> viewPublishDetail(long start, long end,
			PostType type, Pager pager, User user) {
		Custom custom = this.getCutsomByUser(user);

		MCustomStore store = this.getStore(custom);

		return store.queryPublishDetail(start, end, type, pager);
	}

	/**
	 * [提到我的]统计
	 * 
	 * @param start
	 * @param end
	 * @param unit
	 */
	public Map<Long, Integer> viewMention(String uid, Platform platform,
			long start, long end, PostType type, User user) {
		Custom custom = this.getCutsomByUser(user);

		MentionQueryParam param = new MentionQueryParam();
		param.setUser(user);
		param.setUid(uid);
		param.setPlatform(platform);
		param.setStart(start);
		param.setEnd(end);
		param.setAccFilter(FilterAccountType.All);
		param.setPostFilter(FilterPostType.All);

		return this.getStore(custom).queryMentionStatistics(param);

	}

	/**
	 * 转发微博趋势统计
	 * 
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @param _type
	 *            统计类型
	 */
	public Map<Long, Integer> viewCommentTrend(String uid, Platform platform,
			long start, long end, User user) {
		// 转发和评论趋势
		Custom custom = this.getCutsomByUser(user);

		return this.getStore(custom).queryCommentTrend(uid, platform, start,
				end);
	}

	/**
	 * 回复微博趋势统计
	 * 
	 * @param uid
	 * @param platform
	 * @param start
	 * @param end
	 * @param user
	 * @return
	 */
	public Map<Long, Integer> viewRetweetTrend(String uid, Platform platform,
			long start, long end, User user) {
		// 转发和评论趋势
		Custom custom = this.getCutsomByUser(user);

		return this.getStore(custom).queryRetweetTrend(uid, platform, start,
				end);
	}

	/**
	 * 帐号的粉丝趋势
	 * 
	 * @param uid
	 * @param platform
	 * @param start
	 * @param end
	 * @param user
	 */
	public List<AccountTrendResult> viewTrendFans(String uid,
			Platform platform, long start, long end, User user) {

		// 建立一个Trend表记录这个用户粉丝的变化情况
		// Trend表由Job更新，每天更新一次
		Custom custom = this.getCutsomByUser(user);

		return this.getStore(custom).queryTrendAccount(
				this._context.getPoolingHome(), uid, platform, start, end);

	}

	/**
	 * 新浪提供的API只返回粉丝数据的30%，这里只能做30%的采样数据分析...
	 * 
	 * @return
	 */
	public FansResult viewFans(String ucode, Platform platform) {

		AccountManager manager = this._context.getAccountManager();
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

	public VipResult viewFansVips() {
		// 认证比例
		return null;
	}

	public Map<String, Integer> viewFansCnts(String ucode, Platform platform) {
		// 用户粉丝数分组

		AccountManager manager = this._context.getAccountManager();
		int pageSize = 100;
		Pager pager = Pager.createPager(1, pageSize);

		Pagefile<IAccount> accounts = manager.getFollowers(ucode, platform,
				pager);

		int cnt = accounts.getRealcount();

		int pageCount = cnt / pageSize + 1;

		for (int i = 2; i < pageCount; i++) {
			pager.setPageIndex(2);
			List<IAccount> as = manager.getFollowers(ucode, platform, pager)
					.getRows();
			if (as != null)
				accounts.getRows().addAll(as);
		}

		return null;
	}

	public GenderResult viewFansGrenders() {
		// 性别比例
		return null;
	}

	public Map<String, Integer> viewFansTags() {
		// 用户标签统计
		return null;
	}

	public Map<String, Integer> viewFansLocations() {
		// 用户地域分布
		return null;
	}

	public Map<IAccount, Integer> viewFansInteracts() {
		// 粉丝交互排行
		return null;
	}

	public Map<IAccount, Integer> viewFansSupermans() {
		// 粉丝里粉丝数排行
		return null;
	}

	/**
	 * 实时获得一条微博的详细信息
	 * 
	 * @param tid
	 * @param platform
	 * @return
	 */
	public Result<ITweet> tweetGetByTid(String tid, Platform platform, User user) {
		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				platform);

		Result<ITweet> result = new Result<ITweet>();
		if (auth != null) {
			ITweet post;
			try {
				post = auth.tweetGet(tid);
				if (post != null) {
					this.getAccountManager().updateTweetAccount(post);
				}
				result.setData(post);
				return result;
			} catch (ApiException e) {
				result.setError(e.getErrorCode(), e.getError());
				// LOG.error("获得微博信息出错", e);
			}

		}
		return result;
	}

	/**
	 * 通过url实时获得一条微博的详细信息
	 * 
	 * @param url
	 * @return
	 */
	public Result<ITweet> tweetGetByUrl(String url, User user) {

		TweetUrl t = TweetUrlParserFactory.createTweetUrl(url);

		return this.tweetGetByTid(t.getTid(), t.getPlatform(), user);
	}

	/**
	 * 关注好友
	 * 
	 * @param user
	 * @param uid
	 * @param platform
	 * @param friendId
	 * @return
	 */
	public boolean createFriend(User user, String uid, Platform platform,
			String friendId) {
		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			return false;
		}

		IAccount account;
		try {
			account = auth.friendshipCreate(friendId);
			return account != null;
		} catch (ApiException e) {
			LOG.error("关注失败", e);
		}

		return false;
	}

	/**
	 * 移除好友关系
	 * 
	 * @param user
	 * @param uid
	 * @param platform
	 * @param friendId
	 * @return
	 */
	public boolean removeFriend(User user, String uid, Platform platform,
			String friendId) {
		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			return false;
		}

		IAccount account;
		try {
			account = auth.friendshipDestory(friendId);
			return account != null;
		} catch (ApiException e) {
			LOG.error("移除关注失败", e);
		}

		return false;
	}

	// ======================= 帐号基本信息 ==========================

	/**
	 * 通过ucode获得帐号的基本信息
	 * 
	 * @param uid
	 * @param platform
	 * @return
	 */
	public Result<IAccount> accountGetByUcode(String ucode, Platform platform,
			User user) {
		if (platform == Platform.Sina) {
			if (Utility.isNumber(ucode)) {
				return this.accountGetByUid(ucode, platform, user);
			} else {
				return this.accountGetByDomain(ucode, user);
			}
		}

		else if (platform == Platform.Tencent) {
			// 腾讯的ucode里面存的是name
			return this.accountGetByName(ucode, platform, user);
		}

		return null;

	}

	/**
	 * 通过uid获得帐号的基本信息
	 * 
	 * @param uid
	 * @param platform
	 * @param user
	 * @return
	 */
	public Result<IAccount> accountGetByUid(String uid, Platform platform,
			User user) {
		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				platform);
		Result<IAccount> result = new Result<IAccount>();
		if (auth != null) {
			LOG.info("用户{}获得平台{}的帐号{} by uid.", user.getId(), platform, uid);
			try {
				IAccount acc = auth.accountGetByUid(uid);
				result.setData(acc);
			} catch (ApiException e) {
				result.setError(e.getErrorCode(), e.getError());
				LOG.error("获得账号信息失败 by uid ", e);
			}
		}
		return result;
	}

	/**
	 * 通过名字获得帐号的基本信息
	 * 
	 * @param name
	 * @param platform
	 * @return
	 */
	public Result<IAccount> accountGetByName(String name, Platform platform,
			User user) {
		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				platform);

		Result<IAccount> result = new Result<IAccount>();
		if (auth != null) {
			LOG.info("用户{}获得平台{}的帐号{} by name.", user.getId(), platform, name);
			try {
				IAccount acc = auth.accountGetByName(name);
				result.setData(acc);
			} catch (ApiException e) {
				result.setError(e.getErrorCode(), e.getError());
				LOG.error("获得账号信息失败 by name", e);
			}
		}
		return null;

	}

	/**
	 * 通过个性化域名获得帐号信息，该方法只适用于SINA，所以授权也是用的SINA授权，无法调用腾讯的
	 * 
	 * @param domain
	 * @return
	 */
	public Result<IAccount> accountGetByDomain(String domain, User user) {
		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				Platform.Sina);

		Result<IAccount> result = new Result<IAccount>();

		if (auth != null) {
			LOG.info("用户{}获得平台Sina的帐号{}基本信息 by domain.", user.getId(), domain);
			try {
				IAccount acc = auth.accountGetByDomain(domain);
				result.setData(acc);
			} catch (ApiException e) {
				result.setError(e.getErrorCode(), e.getError());
				LOG.error("获得账号信息失败 by domain", e);
			}
		}
		return null;
	}

	/**
	 * 通过url获得该微博的评论列表
	 * 
	 * @param url
	 * @param pager
	 * @return
	 */
	public Result<Pagefile<ITweet>> commentsGetByUrl(String url, Pager pager,
			User user) {
		TweetUrl t = TweetUrlParserFactory.createTweetUrl(url);

		return this.commentsGetByTid(t.getTid(), t.getPlatform(), pager, user);
	}

	/**
	 * 通过tid获得该微博的评论列表
	 * 
	 * @param tid
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Result<Pagefile<ITweet>> commentsGetByTid(String tid,
			Platform platform, Pager pager, User user) {

		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				platform);
		Result<Pagefile<ITweet>> result = new Result<Pagefile<ITweet>>();
		if (auth != null) {
			try {
				Pagefile<ITweet> tweets = auth.commentsGet(tid, pager);
				result.setData(tweets);
			} catch (ApiException e) {
				result.setError(e.getErrorCode(), e.getError());
				LOG.error("获得微博的评论列表失败", e);
			}
		}
		return result;
	}

	/**
	 * 通过url获得该微博的转发列表
	 * 
	 * @param url
	 * @param pager
	 * @return
	 */
	public Result<Pagefile<ITweet>> retweetGetbyUrl(String url, Pager pager,
			User user) {
		TweetUrl t = TweetUrlParserFactory.createTweetUrl(url);

		return this.retweetGetByTid(t.getTid(), t.getPlatform(), pager, user);
	}

	/**
	 * 通过tid获得该微博的转发列表
	 * 
	 * @param tid
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Result<Pagefile<ITweet>> retweetGetByTid(String tid,
			Platform platform, Pager pager, User user) {

		AppAuth auth = this.getCustomManager().getAuthByDefaultStrategy(user,
				platform);
		Result<Pagefile<ITweet>> result = new Result<Pagefile<ITweet>>();
		if (auth != null) {
			Pagefile<ITweet> wrapper;
			try {
				wrapper = auth.retweetGet(tid, pager);
				result.setData(wrapper);
			} catch (ApiException e) {
				result.setError(e.getErrorCode(), e.getError());
				LOG.error("", e);
			}
		}
		return result;
	}

	public List<Result<ITweet>> commentReply(Sending sending, User user,
			int delaySeconds) {
		SendingController controller = new SendingController(_context);
		return controller.sendTweet(sending, user, delaySeconds);
	}

	public static void main(String[] args) {

		AppContext context = AppContext.getInstance();
		context.init();

		SendingController controller = new SendingController(context);
		User user = new UserDaoImpl().queryByUid(6);
		for (int i = 0; i < 60; i++) {
			Sending send = new Sending();
			send.setText("发送微博测试" + i + Utility.genClientID());
			List<Sender> sendList = new ArrayList<Sender>();
			Sender sender = new Sender();
			sender.setPlatform(1);
			sender.setSid(99999);
			send.setType(1);
			sender.setUid("5129884639");
			sendList.add(sender);
			send.setSendList(sendList);
			List<Result<ITweet>> result = controller.sendTweet(send, user, 2);
			System.out.println("每隔2秒发送,第[" + (i + 1) + "]次");
			for (Result<ITweet> tweet : result) {
				System.out.println(tweet.getMessage());
			}
		}
	}

	public List<Result<ITweet>> tweetCreate(Sending sending, User user,
			int delaySeconds) {
		SendingController controller = new SendingController(_context);
		return controller.sendTweet(sending, user, delaySeconds);
	}

	public List<Result<ITweet>> retweetCreate(Sending sending, User user,
			int delaySeconds) {
		SendingController controller = new SendingController(_context);
		return controller.sendTweet(sending, user, delaySeconds);
	}

	/**
	 * 删除评论
	 * 
	 * @param uid
	 * @param platform
	 * @param tid
	 * @param user
	 * @return
	 */
	public Result<ITweet> commentDestroy(String uid, Platform platform,
			String tid, User user) {

		Result<ITweet> result = new Result<ITweet>();
		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			LOG.info("未找到授权信息，无法删除评论！");
			result.setError(XKErrorCST.AUTH_NOTEXISTS_CODE, "未找到授权信息，无法删除评论！");
			return result;
		}

		ITweet tweet;
		try {
			tweet = auth.commentDestory(tid);
			if (tweet != null) {
				MCustomStore store = (MCustomStore) this.getCustomManager()
						.getCustom(user.getCustomID()).getMonitor().getStore(
								_context);
				store.deletePostByTidFromDB(tid, platform);
			}

			result.setData(tweet);
		} catch (ApiException e) {
			result.setData(null);
			result.setErrCode(e.getErrorCode());
			result.setMessage(e.getError());
		}

		return result;

	}

	public List<Result<ITweet>> commentCreate(Sending sending, User user,
			int delaySeconds) {
		SendingController controller = new SendingController(_context);
		return controller.sendTweet(sending, user, delaySeconds);
	}

	/**
	 * 删除微博
	 * 
	 * @param uid
	 * @param platform
	 * @param tid
	 * @param user
	 * @return
	 */
	public Result<ITweet> tweetDestroy(String uid, Platform platform,
			String tid, User user) {

		Result<ITweet> result = new Result<ITweet>();
		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			LOG.info("未找到授权信息，无法删除微博！");
			result.setData(null);
			result.setError(XKErrorCST.AUTH_NOTEXISTS_CODE, "未找到授权信息，无法删除微博！");
			return result;
		}

		ITweet tweet;
		try {
			tweet = auth.tweetDestory(tid);
			if (tweet != null) {
				MCustomStore store = (MCustomStore) this.getCustomManager()
						.getCustom(user.getCustomID()).getMonitor().getStore(
								_context);
				store.deletePostByTidFromDB(tid, platform);
			}
			result.setData(tweet);
		} catch (ApiException e) {
			result.setData(null);
			result.setError(e.getErrorCode(), e.getError());
		}
		return result;

	}

	/**
	 * 通过code获得指定app的授权票据token
	 * 
	 * @param code
	 * @param app
	 * @return
	 */
	public ApiToken tokenGetByCode(String code, App app) {

		APIAdpaterFactory factory = new APIAdpaterFactory();

		try {
			return factory.getAPI(app.getPlatform()).getAccessToken(code, app);
		} catch (ApiException ex) {
			LOG.error("通过CODE获得TOKEN失败", ex);
		}
		return null;
	}

	/**
	 * 获得授权的基本信息
	 * 
	 * @param token
	 * @param platform
	 * @return
	 */
	public ApiTokenInfo tokenInfoGet(String token, Platform platform) {

		APIAdpaterFactory factory = new APIAdpaterFactory();
		try {
			return factory.getAPI(platform).getTokenInfo(token);
		} catch (ApiException ex) {
			LOG.error("通过TOKEN获得TOKEN信息失败", ex);
		}
		return null;
	}

	public BeTrendResult getAccountBeTrend(String uid, Platform platform,
			User user) {
		Date date = new Date(DateHelper.formatBeforeYesterday());
		BeTrendResult r = this.queryBeTrend(date, uid, platform, user);
		if (r == null) {
			AppAuth auth = this.getCustomManager().getAuthByUcode(user, uid,
					platform);

			if (auth == null) {
				return null;
			}

			try {
				r = auth.behaveTrend(date);
				if (r != null) {
					DataAnalysisDao dao = new DataAnalysisDaoImpl();
					dao.insertBeTrend(date, uid, platform, r);
					return r;
				}
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				LOG.error("调用异常" + e.getMessage());

			}
			return null;
		} else {
			return r;
		}

	}

	public BeTrendResult queryBeTrend(Date date, String uid, Platform platform,
			User user) {
		DataAnalysisDao dao = new DataAnalysisDaoImpl();

		BeTrendResult result = dao.queryBeTrend(date, uid, platform);

		return result;

	}

	// ====================================================================

	/**
	 * 获得提到该帐号的微博信息
	 * 
	 * @param uid
	 * @param platform
	 * @param text
	 * @param accFilter
	 * @param postFilter
	 * @param pager
	 * @return
	 */
	private Pagefile<ITweet> tweetsGetByMention(MentionQueryParam p) {

		MCustomStore db = this.getStore(this.getCutsomByUser(p.getUser()));

		return db.tweetsMention(p);
	}

	private Custom getCutsomByUser(User user) {
		return this._context.getCustomManager().getCustom(user.getCustomID());
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(TweetOfficalManager.class);

	/**
	 * 默认的刷新策略
	 */
	IRefreshStrategy<AppAuth> _strategy;

	public MCustomStore getStore(Custom custom) {
		return this.getStore(custom.getMonitor());
	}

	private TweetOfficalManager(AppContext context) {
		super(context);
		this._strategy = new DefaultAppAccountStrategy<AppAuth>();
	}

	private MCustomStore getStore(IMonitor monitor) {
		MCustomStore store = (MCustomStore) this.getDB(monitor);
		return store;
	}
}
