package com.xunku.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xunku.app.AppContext;
import com.xunku.app.adapters.APIAdpaterFactory;
import com.xunku.app.controller.OrganizationController;
import com.xunku.app.controller.SendingController;
import com.xunku.app.enums.AnaylisisStatus;
import com.xunku.app.enums.FilterAccountType;
import com.xunku.app.enums.FilterCommentOrient;
import com.xunku.app.enums.FilterPostType;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.SortKeyMan;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.ApiToken;
import com.xunku.app.model.App;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.TweetStatus;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.app.monitor.CustomMonitor;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.monitor.WeiboMonitor;
import com.xunku.app.result.BeTrendResult;
import com.xunku.app.result.FansResult;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.MAccountTrend;
import com.xunku.app.result.NavieCountResult;
import com.xunku.app.result.PublishCountResult;
import com.xunku.app.result.RepostOrganizationResult;
import com.xunku.app.result.Result;
import com.xunku.app.result.RetweetResult;
import com.xunku.app.result.SpreadResult;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.WeiboTextResult;
import com.xunku.app.result.event.MEventCountResult;
import com.xunku.app.result.event.MEventUserRegResult;
import com.xunku.app.result.spread.RetweetLevelResult;
import com.xunku.app.result.spread.RetweetStatisResult;
import com.xunku.app.stores.MCustomStore;
import com.xunku.constant.FiltrateEnum;
import com.xunku.constant.TimeSortEnum;
import com.xunku.constant.WeiboType;
import com.xunku.dto.task.TaskCntDTO;
import com.xunku.dto.task.TaskSearchDTO;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.pojo.home.FansTrend;
import com.xunku.pojo.home.Hot;
import com.xunku.pojo.my.Mention;
import com.xunku.pojo.my.Sending;
import com.xunku.pojo.office.WeiboWarn;

/**
 * AppServer的代理类
 * <p>
 * 让门户调用AppServer透明化
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:56:34 PM
 */
public class AppServerProxy {

	private static AppContext _context = AppContext.getInstance();

	// =========== 帐号信息 ===============
	public static Result<IAccount> getAccount(String uid, Platform platform,
			User user) {
		// return getAccountOnline(uid, platform, user);
		return _context.getAccountManager().accountGetByUcode(uid, platform);

	}

	public static void updateAccount(IAccount acc) {
		_context.getAccountManager().updateAccount(acc);
	}

	public static IAccount getAccountByToken(String token, String uid,
			Platform platform) {

		APIAdpaterFactory factory = new APIAdpaterFactory();

		try {
			return factory.getAPI(platform).accountGet(token, uid);
		} catch (ApiException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Result<IAccount> getAccountOnline(String uid,
			Platform platform, User user) {

		AppAuth auth = _context.getCustomManager().getAuthByDefaultStrategy(
				user, platform);

		return _context.getAccountManager().accountGetByUcodeOnline(uid,
				platform, user, auth);
	}

	public static BeTrendResult getAccountBeTrend(String uid,
			Platform platform, User user) {
		return _context.getMOfficialManager().getAccountBeTrend(uid, platform,
				user);

	}

	public static IAccount getAccountByName(String name, Platform platform,
			User user) {

		return _context.getAccountManager().accountGetByName(name, platform);
	}

	public static Result<IAccount> getAccountByUcode(String ucode,
			Platform platform, User user) {

		return _context.getAccountManager().accountGetByUcode(ucode, platform);
	}

	public static Result<IAccount> getAccountByUcodeOnline(String ucode,
			Platform platform, User user) {

		AppAuth auth = _context.getCustomManager().getAuthByDefaultStrategy(
				user, platform);

		return _context.getAccountManager().accountGetByUcodeOnline(ucode,
				platform, user, auth);
	}

	public static Result<IAccount> getAccountByUcodeOnline4Tip(String ucode,
			Platform platform, User user) {

		AppAuth auth = _context.getCustomManager().getAuthByDefaultStrategy(
				user, platform);

		return _context.getAccountManager().accountGetByUcodeOnline4Tip(ucode,
				platform, user, auth);
	}

	public static Result<IAccount> getAccountByNameOnline(String name,
			Platform platform, User user) {

		AppAuth auth = _context.getCustomManager().getAuthByDefaultStrategy(
				user, platform);

		return _context.getAccountManager().accountGetByNameOnline(name,
				platform, user, auth);

	}

	// ==================== 微博信息 ====================
	/**
	 * 通过tid和平台获得Post
	 * 
	 * @param tid
	 * @param platform
	 * @return
	 */
	public static Result<ITweet> getPostByTid(String tid, Platform platform,
			User user) {
		return _context.getMOfficialManager()
				.tweetGetByTid(tid, platform, user);
	}

	/**
	 * 通过url获得Post
	 * 
	 * @param url
	 * @return
	 */
	public static Result<ITweet> getPostByUrl(String url, User user) {
		return _context.getMOfficialManager().tweetGetByUrl(url, user);
	}

	/**
	 * 获得评论列表
	 * 
	 * @param url
	 * @param pager
	 * @return
	 */
	public static Result<Pagefile<ITweet>> getCommentsByUrl(String url,
			Pager pager, User user) {
		return _context.getMOfficialManager()
				.commentsGetByUrl(url, pager, user);
	}

	/**
	 * 获得评论列表
	 * 
	 * @param tid
	 * @param platform
	 * @param pager
	 * @return
	 */
	public static Result<Pagefile<ITweet>> getCommentsByTid(String tid,
			Platform platform, Pager pager, User user) {
		return _context.getMOfficialManager().commentsGetByTid(tid, platform,
				pager, user);
	}

	/**
	 * 获得转发列表
	 * 
	 * @param url
	 * @return
	 */
	public static Result<Pagefile<ITweet>> getRetweetsByUrl(String url,
			Pager pager, User user) {
		return _context.getMOfficialManager().retweetGetbyUrl(url, pager, user);
	}

	/**
	 * 获得转发列表
	 * 
	 * @param tid
	 * @param platform
	 * @return
	 */
	public static Result<Pagefile<ITweet>> getRetweetsByTid(String tid,
			Platform platform, Pager pager, User user) {
		return _context.getMOfficialManager().retweetGetByTid(tid, platform,
				pager, user);
	}

	// ================ 获得官微的基本信息 =====================
	public static Pagefile<ITweet> getMyPost(User user, String uid,
			String query, FilterPostType type, int pageIndex, int pageSize,
			Platform platform) {

		return _context.getMOfficialManager().TweetsHomeTimelineOnline(user,
				uid, query, Pager.createPager(pageIndex, pageSize), type,
				platform, true);

	}

	public static Pagefile<ITweet> getMentions(User user, String uid,
			Platform platform, long start, long end, String text,
			FilterAccountType accFilter, FilterPostType postFilter, Pager pager) {

		return _context.getMOfficialManager().TweetsGetByMentionOnline(user,
				uid, platform, start, end, text, accFilter, postFilter, pager,
				true);
	}

	public static Pagefile<ITweet> getComments(User user, String uid,
			Platform platform, FilterCommentOrient filter, Pager pager) {
		return _context.getMOfficialManager().commentsGetByAccountOnline(user,
				uid, platform, filter, pager, true);
	}

	// 趋势分析-评论
	public static Map<Long, Integer> getViewRetweetTrend(String uid,
			Platform platform, long start, long end, User user) {

		return _context.getMOfficialManager().viewRetweetTrend(uid, platform,
				start, end, user);
	}

	// 趋势分析-转发
	public static Map<Long, Integer> getViewCommentTrend(String uid,
			Platform platform, long start, long end, User user) {

		return _context.getMOfficialManager().viewCommentTrend(uid, platform,
				start, end, user);
	}

	// 发布统计按时间
	public static Map<Long, PublishCountResult> officialPublishByDate(
			String uid, Platform platform, long start, long end, PostType type,
			User user) {
		return _context.getMOfficialManager().viewPublishByDate(uid, platform,
				start, end, 4, user);
	}

	// 发布统计按时间段
	public static Map<Integer, PublishCountResult> officialPublishByDay(
			String uid, Platform platform, long start, long end, PostType type,
			User user) {
		return _context.getMOfficialManager().viewPublishByDay(uid, platform,
				start, end, 4, user);
	}

	// @我的统计
	public static Map<Long, Integer> officialMention(String uid,
			Platform platform, long start, long end, PostType type, User user) {
		return _context.getMOfficialManager().viewMention(uid, platform, start,
				end, type, user);
	}

	// 我的评论_删除评论
	public static Result<ITweet> deletePost(String uid, Platform platform,
			String tid, User user) {
		return _context.getMOfficialManager().commentDestroy(uid, platform,
				tid, user);
	}

	public static Result<Sending> submitSending(Sending sending, User user) {
		SendingController controller = new SendingController(_context);
		return controller.submitSending(sending, user);
	}

	public static void commentReplyPost(Sending sending, User user) {
		// return _context.commentReply(sending, user);
	}

	// 评论信息
	public static void commentPost(Sending sending, User user) {
		// return _context.commentCreate(sending, user);
	}

	// 转发信息
	public static void retweetCreate(Sending sending, User user) {
		// return _context.retweetCreate(sending, user);
	}

	// 发布一个新微博
	public static void uploadPost(Sending sending, User user) {
		// return _context.tweetCreate(sending, user, 10);
	}

	// ======================= 帐号监测 ================================
	public static Pagefile<ITweet> getMAccountPost(AccountMonitor account,
			Date startDate, Date endDate, Pager pager, User user) {
		return _context.getMAccountManager().TweetsGetByMAccount4RT(account,
				startDate, endDate, pager, user);
	}

	// ======================= 授权信息 ================================
	public static ApiToken getTokenByCode(String code, App app) {
		return _context.getMOfficialManager().tokenGetByCode(code, app);
	}

	/**
	 * 更新帐号的授权信息，如果帐号存在则更新，否则添加新的帐号和授权
	 * 
	 * @param acc
	 * @param auth
	 */
	public static void updateToken(User user, AccountInfo acc, AccountAuths auth) {
		_context.getCustomManager().addAccount(user, acc, auth);
	}

	public static boolean createFriend(User user, String uid, String friendId,
			Platform platform) {

		return _context.getMOfficialManager().createFriend(user, uid, platform,
				friendId);

	}

	public static boolean removeFriend(User user, String uid, String friendId,
			Platform platform) {

		// 暂时未实现
		return true;
		// return _context.removeFriend(user, uid, platform, friendId);

	}

	// ========================= 传播分析 ==============================
	/**
	 * 根据url分析Post
	 * 
	 * @param url
	 * @return
	 */
	/**
	 * 获得指定检测微博的分析结果
	 * 
	 * @param weiboid
	 * @return
	 */
	public static SpreadResult getSpreadResult(User user, int monitorId) {
		return _context.getMWeiboManager().getResult(user, monitorId);
	}

	public static void anaylsisSpreadNow(User user, int monitorId, ITweet post) {

		_context.getMWeiboManager().landing(monitorId, user, post);
	}

	public static boolean anaylsisSpreadNow(User user, int monitorId) {
		_context.getMWeiboManager().anaylsisSpreadNow(monitorId);
		return true;
	}

	public static AnaylisisStatus getAnaylisisStatus(User user, int monitorId) {
		return AnaylisisStatus.Finished;

	}

	public static ITweet getPostBySpreadId(int monitorid) {
		return _context.getMWeiboManager().tweetGetByMonitorID(monitorid);
	}

	// 返回监测对象的趋势分析结果
	public static Map<Long, Integer> spreadViewTrend(int monitorid,
			PostType type, Date startTime, Date endTime) {
		return _context.getMWeiboManager().viewTimeTrend(monitorid, type);
	}

	// 返回该对象的转发层级
	public static List<RetweetLevelResult> spreadViewRetweetLevel(int monitorId) {
		return _context.getMWeiboManager().viewRetweetLevel(monitorId);
	}

	// 获得已传播的关键用户
	public static List<RetweetStatisResult> spreadViewRetweetSpread(
			int monitorId, SortKeyMan sort) {
		return _context.getMWeiboManager().viewKeyMrDissGet(monitorId, sort);
	}

	// 获得未传播的关键用户
	public static List<RetweetStatisResult> spreadViewRetweetUnspread(
			int monitorId, SortKeyMan sort) {
		return _context.getMWeiboManager().viewKeyMrUnDissGet(monitorId, sort);
	}

	// 传播分析 用户分析 认证比例(%保留1位小数)
	public static VipResult spreadViewVipNum(int monitorId, PostType type) {
		return _context.getMWeiboManager().viewVipNum(monitorId, type);
	}

	// 传播分析 用户分析 性别比例(%保留1位小数)
	public static GenderResult spreadViewGender(int monitorId, PostType type) {
		return _context.getMWeiboManager().viewGender(monitorId, type);
	}

	// 传播分析 用户分析 来源比例(%保留1位小数)
	public static Map<String, Integer> spreadViewFromsNum(int monitorId,
			PostType type) {
		return _context.getMWeiboManager().viewFromsNum(monitorId, type);
	}

	// 传播分析 用户分析 用户粉丝数(%保留1位小数)
	public static int[] spreadViewFansHistogram(int monitorId, PostType type) {
		return _context.getMWeiboManager().viewFansHistogram(monitorId, type);
	}

	// 传播分析 用户分析 地域分析(%保留1位小数)
	public static Map<String, Integer> viewLocation(int monitorId, PostType type) {
		return _context.getMWeiboManager().viewLocation(monitorId, type);
	}

	// 传播分析 用户分析 转发态度分析(%保留1位小数)
	public static double[] viewRetweetAttitude(int monitorId) {
		return _context.getMWeiboManager().viewRetweetAttitude(monitorId);
	}

	// ========================= 事件监控 ==============================
	/**
	 * 趋势分析
	 * 
	 * @param start
	 * @param end
	 * @param event
	 * @return
	 */
	public static Map<Long, Integer> getEventTrendByHour(Date start, Date end,
			EventMonitor event) {
		return _context.getMEventManager().viewTrendStatByHour(start, end,
				event);
	}

	public static Map<Long, Integer> getEventTrendByDate(Date start, Date end,
			EventMonitor event) {
		return _context.getMEventManager().viewTrendStatByDate(start, end,
				event);
	}

	/**
	 * 获得事件统计信息
	 * 
	 * @param events
	 * @return
	 */
	public static Map<Integer, MEventCountResult> getEventCount(
			List<EventMonitor> events) {
		return _context.getMEventManager().viewCounts(events);
	}

	public static Pagefile<ITweet> getEventPosts(EventMonitor event,
			String text, Date startDate, Date endDate, FiltrateEnum filter,
			TimeSortEnum sort, Pager pager) {
		return _context.getMEventManager().tweetsGetByMEvent(event, text,
				startDate, endDate, filter, sort, pager);
	}

	// 返回转发和评论的比例，这里返回的是转发\原发\评论的的数量
	public static RetweetResult viewRetweetCnt(EventMonitor event) {
		return _context.getMEventManager().viewRetweetCnt(event);
	}

	// 返回热门关键词
	public static Map<String, Float> viewHotwords(EventMonitor event) {
		return _context.getMEventManager().viewHotwords(event);
	}

	// 返回热门关键词
	public static Map<String, Float> viewHotwords(int monitorid) {
		return _context.getMWeiboManager().viewHotwords(monitorid);
	}

	// 返回关键观点
	public static Map<ITweet, Integer> viewSuperTweet(EventMonitor event) {
		return _context.getMEventManager().viewSuperTweet(event);
	}

	// 性别比例
	public static GenderResult viewGrender(EventMonitor event) {
		return _context.getMEventManager().viewGrender(event);
	}

	// 认证比例
	public static VipResult viewVip(EventMonitor event) {
		return _context.getMEventManager().viewVip(event);
	}

	// 来源分析
	public static Map<String, Integer> viewForms(EventMonitor event) {
		return _context.getMEventManager().viewForms(event);
	}

	// 用户注册时间分析
	public static MEventUserRegResult viewReg(EventMonitor event) {
		return _context.getMEventManager().viewReg(event);
	}

	// 地域分布
	public static Map<String, Integer> viewLocation(EventMonitor event) {
		return _context.getMEventManager().viewLocation(event);
	}

	// 关键用户分析
	public static Map<IAccount, Integer> viewSuperman(EventMonitor event) {
		return _context.getMEventManager().viewSuperman(event);
	}

	// ===================== 获得收集状态 ====================
	public static Map<String, TweetStatus> getCollectionStatus(
			List<String> urls, int customid) {

		return _context.getFavManager().getTweetStatus(urls, customid);
	}

	public static Map<String, String[]> getWarningStatus(List<String> urls,
			int customid) {
		return _context.getFavManager().getTweetStatus4Warn(urls, customid);
	}

	public static App getMainApp(Platform platform) {
		App app = _context.getCustomManager().getMainApp(platform);

		return app;
	}

	public static Custom getCustom(int cid) {
		return _context.getCustomManager().getCustom(cid);
	}

	// ===================== 我的首页 ====================
	// 回复列表
	public static Result<Pagefile<ITweet>> myHomePageViewComments(String uid,
			String tid, Platform platform, Pager pager, User user) {
		return _context.getMOfficialManager().viewCommentsOnline(tid, platform,
				pager, user);
	}

	// 转发列表
	public static Result<Pagefile<ITweet>> myHomePageViewRetweets(String uid,
			String tid, Platform platform, Pager pager, User user) {
		return _context.getMOfficialManager().viewRetweetsOnline(tid, platform,
				pager, user);
	}

	// 获得离线的评论列表
	public static Result<Pagefile<ITweet>> myHomePageViewCommentsOffline(
			String uid, String tid, Platform platform, Pager pager, User user) {

		return _context.getMOfficialManager().viewComments(tid, platform,
				pager, user);
	}

	// 获得离线的转发列表
	public static Result<Pagefile<ITweet>> myHomePageViewRetweetsOffline(
			String uid, String tid, Platform platform, Pager pager, User user) {
		return _context.getMOfficialManager().viewRetweets(tid, platform,
				pager, user);
	}

	// ====================== 首页展示 ====================

	// 一周舆情监测统计
	public static Map<Long, TaskCntDTO> viewTaskCount(int customId, Date start,
			Date end) {
		return _context.getTaskManager().viewTaskCount(customId, start, end,
				null);
	}

	// 今日舆情类别分析
	public static Map<String, Integer> viewTaskGroupByPlatform(int customId) {
		return _context.getTaskManager()
				.viewTaskGroupByPlatform(customId, null);
	}

	// 最近七天粉丝趋势
	public static Map<String, List<FansTrend>> viewHomeFansTrend(int customId,
			Date start, Date end) {
		return _context.getTaskManager()
				.viewHomeFansTrend(customId, start, end);
	}

	// 返回首页的热词列表
	public static List<Hot> viewHomeHots(Platform platform, Date date) {
		return _context.getTaskManager().viewHomeHots(platform, date);
	}

	public static Pagefile<ITweet> searchTask(TaskSearchDTO taskSearchDTO) {
		return _context.getTaskManager().searchTask(taskSearchDTO);
	}

	// ======================== 考核管理 ===========================
	// 微博内容统计【考核管理】
	public static Pagefile<WeiboTextResult> queryTweetCountDetail(User user,
			Pager pager, long start, long end, String uid) {
		return _context.getMOfficialManager().queryTweetCountDetail(user,
				pager, start, end, uid);
	}

	// 转发机构反查列表
	public static Pagefile<RepostOrganizationResult> queryRODetail(User user,
			Pager pager, String tid) {
		return _context.getMOfficialManager().queryRODetail(user, pager, tid);
	}

	// 网评员统计【考核管理】
	public static Pagefile<NavieCountResult> queryNavieCount(User user,
			Pager pager, long start, long end) {
		return _context.getMOfficialManager().queryNavieCount(user, pager,
				start, end);
	}

	// 网评员账号的个数的反查列表
	/*
	 * public static Pagefile<NavieResult> queryNavieResult(User user, Pager
	 * pager, int navieid) { return
	 * _context.getMOfficialManager().queryNavieResult(user, pager, navieid); }
	 */

	// 网评员总转评到我的个数反查列表
	public static Pagefile<ITweet> queryNavieRCResult(User user, Pager pager,
			int navieid, long start, long end, WeiboType type) {
		return _context.getMOfficialManager().queryNavieRCResult(user, pager,
				navieid, start, end, type);
	}

	// 删除网评员的账号
	public static boolean deleteNavieRecord(User user, int navieid, String uid) {
		return _context.getMOfficialManager().deleteNavieRecord(user, navieid,
				uid);
	}

	// 获取转发机构的微博数量
	public static int queryOrganWeibos(User user, String uid, long start,
			long end) {
		return _context.getMOfficialManager().queryOrganWeibos(user, uid,
				start, end);
	}

	// 获取转发机构的@我的数量
	public static int queryMyMentions(User user, String uid, long start,
			long end) {
		return _context.getMOfficialManager().queryMyMentions(user, uid, start,
				end);
	}

	// 获取转发机构的微博数量反查列表
	public static Pagefile<ITweet> queryOrganITweets(Pager pager, User user,
			String uid, long start, long end) {
		return _context.getMOfficialManager().queryOrganITweets(pager, user,
				uid, start, end);
	}

	public static Pagefile<Mention> queryMentionITweet(Pager pager, User user,
			String uid, long start, long end) {
		return _context.getMOfficialManager().queryMentionITweet(pager, user,
				uid, start, end);
	}

	// 获取一条微博
	public static ITweet queryITweetByTid(User user, String tid,
			Platform platform) {
		return _context.getMOfficialManager().queryITweetByTid(user, tid,
				platform);
	}

	// ======================== 舆情展示 ===========================
	// 获取舆情展示评论列表
	public static Result<Pagefile<ITweet>> viewComments(String uid, String url,
			Pager pager, User user) {
		return _context.getMOfficialManager().viewCommentsOnline(url, pager,
				user);
	}

	// 获取舆情展示转发列表
	public static Result<Pagefile<ITweet>> viewReposts(String uid, String url,
			Pager pager, User user) {
		return _context.getMOfficialManager().viewRetweetsOnline(url, pager,
				user);
	}

	// ======================== 数据分析 ===========================
	// 获取粉丝页签所有图表数据
	public static FansResult viewFans(String ucode, Platform platform) {
		return _context.getMOfficialManager().viewFans(ucode, platform);
	}

	/**
	 * 通过预警对象获得该预警对象的微博信息
	 * 
	 * @param warn
	 * @return
	 */
	public static ITweet getWeiboByWarn(WeiboWarn warn) {
		String tid = warn.getTid();
		Custom custom = _context.getCustomManager().getCustom(
				warn.getCustomid());
		CustomMonitor monitor = custom.getMonitor();
		MCustomStore store = (MCustomStore) monitor.getStore(_context);
		return store.queryITweetByTid(tid, warn.getPlatform());
	}

	// 帐号监测分析数据
	public static MAccountTrend MAccountViewTrend(int id, Date start, Date end) {
		return _context.getMAccountManager().viewTrend(id, start, end);
	}

	public static MAccountTrend MAccountViewTrendToday(int id, Date start,
			Date end, int radiovalue) {
		return _context.getMAccountManager().viewTrendToday(id, start, end,
				radiovalue);
	}

	public static int[] MAccountViewRealTime(int id, Date start, Date end) {
		return _context.getMAccountManager().viewRealtime(id, start, end);
	}

	public static List<ITweet> MAccountViewTop10(int id) {
		return _context.getMAccountManager().viewHotTweets(id);
	}

	public static FansResult MAccountViewFans(int id) {
		return _context.getMAccountManager().viewFans(id);
	}

	public static Pagefile<Organization> orgList(int customid, Pager pager,
			Date start, Date end, String uid) {
		OrganizationController controller = new OrganizationController();

		return controller.queryOrgList(_context, customid, pager, start, end,
				uid);
	}

	public static Pagefile<ITweet> orgDetail(int customid, Pager pager,
			Date start, Date end, String orgUid, PostType type) {
		OrganizationController controller = new OrganizationController();
		return controller.queryOrgDetail(_context, customid, pager, start, end,
				orgUid, type);
	}

	public static Pagefile<ITweet> tweetsRealtimeDetail(String text,
			int accoundid, Date start, Date end, Pager pager) {

		return _context.getMAccountManager().tweetsRealtimeDetail(text,
				accoundid, start, end, pager);
	}

}
