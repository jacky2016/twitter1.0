package com.xunku.app.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.adapters.APIAdpaterFactory;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.BeTrendResult;
import com.xunku.constant.ApiCST;
import com.xunku.dao.base.AppDao;
import com.xunku.daoImpl.base.AppDaoImpl;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 授权信息、每个用户对应一个平台、每个平台有一个授权信息
 * <p>
 * 为了分流应用和授权，调用时将按照策略来选择授权调用API
 * <p>
 * 授权负责调用指定的API
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:44:33 PM
 */
public class AppAuth {

	/**
	 * 发微博的
	 */
	static final String Send_Tweet_Api = "statuses.update";
	static final String Send_Comment_Api = "statuses.comment";
	static final String Add_Following_Api = "friendship.create";

	App app;
	AppAccount account;
	Platform platform;
	String token;
	long expireIn;// 过期时间，单位秒
	long authzTime;
	Map<Long, Integer> times;// 使用次数
	/**
	 * API工厂，静态的工厂，实例之间共享
	 */
	private static final APIAdpaterFactory _factory = new APIAdpaterFactory();

	public AppAuth(AppAccount acc, App app) {
		this.account = acc;
		this.app = app;
		times = new HashMap<Long, Integer>();
	}

	public int getAppId() {
		return this.app.getId();
	}

	public App getApp() {
		return this.app;
	}

	/**
	 * 获得本小时调用的次数
	 * 
	 * @return
	 */
	public int getTimes() {

		long currTime = DateHelper.getTimezoneHour();

		if (!times.containsKey(currTime)) {

			times.put(currTime, 0);
		}
		return times.get(currTime);

	}

	/**
	 * 获得当前授权发布的最新微博列表
	 * 
	 * @return
	 */
	public Pagefile<ITweet> tweetsUserTimeline(Pager pager) throws ApiException {
		try {
			String lastId = "1";
			this.checkAuthExpired();
			Pagefile<ITweet> posts = _factory.getAPI(this).tweetsUserTimeline(
					lastId, pager);
			this.increase();
			return posts;
		} catch (ApiException ex) {
			// this.error("获取自己的微博列表失败", ex);
			throw ex;
		}
	}

	/**
	 * 获得提及当前帐号的最新微博列表
	 * 
	 * @return
	 */
	public Pagefile<ITweet> tweetsGetByMention(Pager pager, PostType type)
			throws ApiException {
		try {
			this.checkAuthExpired();
			Pagefile<ITweet> result = null;
			if (type == PostType.Comment) {
				result = _factory.getAPI(this).commentsMention(pager);
			}

			if (type == PostType.Creative) {
				result = _factory.getAPI(this).tweetsMention(pager);
			}
			this.increase();
			return result;
		} catch (ApiException ex) {
			// this.error("获取提到我的列表失败", ex);
			throw ex;
		}
	}

	public BeTrendResult behaveTrend(Date date) throws ApiException {
		try {
			this.checkAuthExpired();

			List<BeTrendResult> posts = _factory.getAPI(this).behaviorTrend(
					date, date);
			this.increase();
			if (posts.size() > 0) {
				return posts.get(0);
			}
			return null;
		} catch (ApiException ex) {
			throw ex;
		}
	}

	/**
	 * 获得当前帐号的评论信息，发给我的评论/我发出的评论
	 * 
	 * @return
	 */
	public Pagefile<ITweet> commentsTimeline(Pager pager) throws ApiException {
		try {
			this.checkAuthExpired();

			Pagefile<ITweet> posts = _factory.getAPI(this).commentsTimeline(
					pager);
			this.increase();
			return posts;
		} catch (ApiException ex) {
			// this.error("获取自己的评论（我评论的和评论我的）列表失败", ex);
			throw ex;
		}
	}

	public Pagefile<ITweet> commentToMe(Pager pager) throws ApiException {
		try {
			this.checkAuthExpired();
			Pagefile<ITweet> posts = _factory.getAPI(this).commentsToMe(pager);
			this.increase();
			return posts;
		} catch (ApiException ex) {
			// this.error("获取自己的评论（我评论的和评论我的）列表失败", ex);
			throw ex;
		}
	}

	public Pagefile<ITweet> commentByMe(Pager pager) throws ApiException {
		try {
			this.checkAuthExpired();
			Pagefile<ITweet> posts = _factory.getAPI(this).commentsByMe(pager);
			this.increase();
			return posts;
		} catch (ApiException ex) {
			// this.error("获取自己的评论（我评论的和评论我的）列表失败", ex);
			throw ex;
		}
	}

	/**
	 * 获得当前授权的帐号详细信息
	 * 
	 * @return
	 */
	public IAccount accountGet() throws ApiException {
		IAccount result = null;
		try {
			this.checkAuthExpired();
			result = _factory.getAPI(this).accountGetByUid(
					this.getAccount().getUid());
			this.increase();
			return result;
		} catch (ApiException ex) {
			// this.error("获取自己的用户信息失败", ex);
			throw ex;
		}
	}

	/**
	 * 使用该授权获得指定名称的帐号信息
	 * 
	 * @param name
	 * @return
	 */
	public IAccount accountGetByName(String name) throws ApiException {
		IAccount result = null;
		try {
			this.checkAuthExpired();
			result = _factory.getAPI(this).accountGetByName(name);
			this.increase();
			return result;
		} catch (ApiException ex) {
			// this.error("通过名称获取用户信息失败", ex);
			throw ex;
		}
	}

	/**
	 * 使用该授权获得指定帐号的帐号信息
	 * 
	 * @param uid
	 * @return
	 */
	public IAccount accountGetByUid(String uid) throws ApiException {
		IAccount result = null;
		try {
			this.checkAuthExpired();
			result = _factory.getAPI(this).accountGetByUid(uid);
			this.increase();
			return result;
		} catch (ApiException ex) {
			// this.error("通过uid获取用户信息失败", ex);
			throw ex;
		}
	}

	/**
	 * 使用该授权获得指定个性化域名的帐号信息，这个方法只对新浪授权有效
	 * 
	 * @param domain
	 * @return
	 */
	public IAccount accountGetByDomain(String domain) throws ApiException {
		IAccount result = null;
		try {
			this.checkAuthExpired();
			result = _factory.getAPI(this).accountGetByDomain(domain);
			this.increase();
			return result;
		} catch (ApiException ex) {
			// this.error("通过域名获取用户信息失败", ex);
			throw ex;
		}
	}

	/**
	 * 获得粉丝列表
	 * 
	 * @param pager
	 * @return
	 */
	public Pagefile<IAccount> followersGet(int cursor) throws ApiException {
		try {
			this.checkAuthExpired();
			String uid = this.account.uid;
			return _factory.getAPI(this).friendshipFollowersGet(uid, cursor);
		} catch (ApiException ex) {
			// this.error("获得粉丝列表", ex);
			throw ex;
		}
	}

	/**
	 * 获得关注列表
	 * 
	 * @param pager
	 * @return
	 */
	public Pagefile<IAccount> friendGet(Pager pager) throws ApiException {
		try {
			this.checkAuthExpired();
			String uid = this.account.uid;
			return _factory.getAPI(this).friendshipFriendsGet(uid, pager);
		} catch (ApiException ex) {
			// this.error("获得粉丝列表", ex);
			throw ex;
		}
	}

	/**
	 * 关注uid
	 * 
	 * @param uid
	 * @return
	 */
	public IAccount friendshipCreate(String uid) throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).friendshipCreate(uid);
		} catch (ApiException ex) {
			// this.error("关注指定用户失败", ex);
			throw ex;
		}
	}

	/**
	 * 取消关注uid
	 * 
	 * @param uid
	 * @return
	 */
	public IAccount friendshipDestory(String uid) throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).friendshipDestory(uid);
		} catch (ApiException ex) {
			// this.error("取消关注指定用户失败", ex);
			throw ex;
		}
	}

	/**
	 * 获得当前授权的首页的微博列表
	 * 
	 * @return
	 */
	public Pagefile<ITweet> tweetsHomeTimeline(Pager pager) throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).tweetsHomeTimeline(pager);
		} catch (ApiException ex) {
			// this.error("获取首页微博列表失败", ex);
			throw ex;
		}
	}

	/**
	 * 删除微博
	 * 
	 * @param tid
	 * @return
	 * @throws ApiException
	 */
	public ITweet tweetDestory(String tid) throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).tweetDestroy(tid);
		} catch (ApiException ex) {
			// this.error("微博删除失败", ex);
			throw ex;
		}
	}

	/**
	 * 删除评论
	 * 
	 * @param tid
	 * @return
	 * @throws ApiException
	 */
	public ITweet commentDestory(String tid) throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).commentDestory(tid);
		} catch (ApiException ex) {
			// this.error("评论删除失败", ex);
			throw ex;
		}
	}

	/**
	 * 创建评论
	 * 
	 * @param tid
	 * @param text
	 * @return
	 * @throws ApiException
	 */
	public ITweet commentCreate(String tid, String text) throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).commentCreated(tid, text);
		} catch (ApiException ex) {
			// this.error("评论创建失败", ex);
			throw ex;
		}
	}

	/**
	 * 回复一条评论
	 * 
	 * @param tid
	 * @param cid
	 * @param text
	 * @return
	 * @throws ApiException
	 */
	public ITweet commentReply(String tid, String cid, String text)
			throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).commentReply(text, tid, cid);
		} catch (ApiException ex) {
			// this.error("评论创建失败", ex);
			throw ex;
		}
	}

	/**
	 * 用这个授权发布一条带图片的微博
	 * 
	 * @param text
	 * @param images
	 * @return
	 * @throws ApiException
	 */
	public ITweet tweetCreate(String text, List<String> images)
			throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).tweetCreate(text, images);
		} catch (ApiException ex) {
			// this.error("发送微博失败", ex);
			throw ex;
		}
	}

	/**
	 * 用这个授权发布一条微博，不带图片
	 * 
	 * @param text
	 * @return
	 * @throws ApiException
	 */
	public ITweet tweetCreate(String text, UploadImage image)
			throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).tweetCreate(text, image);
		} catch (ApiException ex) {
			// this.error("发送不带图片微博失败", ex);
			throw ex;
		}
	}

	public ITweet retweetCreate(String tid, String text, boolean isComment)
			throws ApiException {
		try {
			this.checkAuthExpired();
			int is_comment = 0;
			// 0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0
			if (isComment)
				is_comment = 1;
			return _factory.getAPI(this).tweetRepost(tid, text, is_comment);
		} catch (ApiException ex) {
			// this.error("发送不带图片微博失败", ex);
			throw ex;
		}
	}

	/**
	 * 用这个授权获得一个微博的具体信息
	 * 
	 * @param tid
	 * @return
	 */
	public ITweet tweetGet(String tid) throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).tweetGet(tid);
		} catch (ApiException ex) {
			// this.error("获取微博详情失败", ex);
			throw ex;
		}
	}

	/**
	 * 通过该授权获得指定微博的评论
	 * 
	 * @param tid
	 * @param sinceId
	 *            从这个id之后开始获取
	 * @return
	 */
	public Pagefile<ITweet> retweetGet(String tid, Pager pager)
			throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).retweetsTimeline(tid, pager);
		} catch (ApiException ex) {
			// this.error("获取微博" + tid + "转发列表失败", ex);
			throw ex;
		}
	}

	/**
	 * 通过该授权获得指定微博的评论
	 * 
	 * @param tid
	 * @param sinceId
	 *            从这个id之后开始获取
	 * @return
	 */
	public Pagefile<ITweet> commentsGet(String tid, Pager pager)
			throws ApiException {
		try {
			this.checkAuthExpired();
			return _factory.getAPI(this).commentsGet(tid, pager);
		} catch (ApiException ex) {
			// LOG.error("获取微博" + tid + "的评论列表失败", ex);
			throw ex;
		}
	}

	/**
	 * 判断当前授权是否过期
	 * 
	 * @return
	 */
	public boolean expired() {
		long now = System.currentTimeMillis();
		long expTime = this.authzTime + (this.expireIn * 1000);
		return now > expTime;
	}

	// ======================================================

	private void checkAuthExpired() throws ApiException {
		if (this.expired()) {
			throw new ApiException("授权已经过期!", ApiCST.ERROR_AUTH_EXPIRED);
		}
	}

	/**
	 * 每次调用完API后都需要调用该方法
	 */
	private void increase() {
		Long currTime = DateHelper.getTimezoneHour();
		int t = this.getTimes();
		this.times.put(currTime, t + 1);
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpire() {
		return expireIn;
	}

	public void setExpire(long expirein) {
		this.expireIn = expirein;
	}

	public long getAuthzTime() {
		return authzTime;
	}

	public void setAuthzTime(long authzTime) {
		this.authzTime = authzTime;
	}

	public String getSinaToken() {
		return this.getToken();
	}

	public AppAccount getAccount() {
		return account;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Account: " + this.account + "\n");
		buf.append("App: " + this.app + "\n");
		buf.append("Token: " + this.token + "\n");
		buf.append("Expired: " + this.expireIn + "\n");
		buf.append("AuthzTime: " + this.authzTime + "\n");
		buf.append("Times: " + this.times + "\n");
		return buf.toString();
	}

	public static AppAuth create(AppAccount acc, AccountAuths pojoAuth) {
		AppDao dao = new AppDaoImpl();
		App app = dao.queryById(pojoAuth.getAppId());
		AppAuth auth = new AppAuth(acc, app);
		auth.setAuthzTime(pojoAuth.getAuthTime().getTime());
		auth.setExpire(pojoAuth.getExpiresin());
		auth.setPlatform(Utility.getPlatform((pojoAuth.getPlatform())));
		auth.setToken(pojoAuth.getToken());
		return auth;

	}
}
