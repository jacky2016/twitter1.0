package com.xunku.app.adapters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weibo4j.Business;
import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.http.AccessTokenInfo;
import weibo4j.http.ImageItem;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.xunku.app.Utility;
import com.xunku.app.interfaces.IAPIAdapter;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.ApiToken;
import com.xunku.app.model.ApiTokenInfo;
import com.xunku.app.model.App;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.UploadImage;
import com.xunku.app.model.accounts.AccountFactory;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.app.result.BeTrendResult;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 新浪API适配器实现
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:53:16 PM
 */
public class SinaAPIAdapter implements IAPIAdapter {

	@Override
	public ApiToken getAccessToken(String code, App app) throws ApiException {
		Oauth auth = new Oauth();
		try {
			String clientId = app.getKey();
			String clientSe = app.getSecret();
			String callback = app.getCallbackUrl();

			AccessToken token = auth.getAccessTokenByCode(clientId, clientSe,
					code, callback);

			ApiToken result = new ApiToken();

			result.setExpiresIn(Integer.parseInt(token.getExpireIn()));
			result.setToken(token.getAccessToken());
			result.setUid(token.getUid());

			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ApiTokenInfo getTokenInfo(String tokenString) throws ApiException {
		// TODO Auto-generated method stub
		Oauth auth = new Oauth();
		ApiTokenInfo info = new ApiTokenInfo();
		try {
			AccessTokenInfo token = auth.getAccessTokenInfo(tokenString);

			info.setAppkey(token.getAppkey());
			info.setCreated(token.getCreated());
			info.setExpireIn(token.getExpireIn());
			info.setScope(token.getScope());
			info.setUid(token.getUid());
			return info;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	private AppAuth _auth;

	public SinaAPIAdapter() {
	}

	public void setAuth(AppAuth auth) {
		_auth = auth;
	}

	@Override
	public Pagefile<ITweet> tweetsMention(Pager pager) throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {
			StatusWapper status = tm
					.getMentions(this.getPaging(pager), 0, 0, 0);
			List<Status> sList = status.getStatuses();

			for (Status s : sList) {
				ITweet p = TweetFactory.createTweet(s);
				result.getRows().add(p);
			}
			result.setRealcount((int) status.getTotalNumber());
			return result;

		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<ITweet> commentsMention(Pager pager) throws ApiException {
		Comments ct = new Comments();
		ct.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {
			CommentWapper status = ct.getCommentMentions(this.getPaging(pager),
					0, 0);
			List<Comment> comments = status.getComments();
			for (Comment c : comments) {
				ITweet p = TweetFactory.createCommentTweet(c);
				result.getRows().add(p);
			}
			result.setRealcount((int) status.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<ITweet> commentsToMe(Pager pager) throws ApiException {
		Comments cm = new Comments();
		cm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {
			CommentWapper comment = cm.getCommentToMe(this.getPaging(pager), 0,
					0);
			List<Comment> comments = comment.getComments();
			for (Comment c : comments) {
				ITweet p = TweetFactory.createCommentTweet(c);
				result.getRows().add(p);
			}
			result.setRealcount((int) comment.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<ITweet> commentsGet(String tid, Pager pager)
			throws ApiException {
		Comments cm = new Comments();
		cm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {
			CommentWapper comment = cm.getCommentById(tid, this
					.getPaging(pager), 0);
			List<Comment> comments = comment.getComments();
			for (Comment c : comments) {
				ITweet p = TweetFactory.createCommentTweet(c);
				result.getRows().add(p);
			}
			result.setRealcount((int) comment.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<ITweet> commentsTimeline(Pager pager) throws ApiException {
		Comments cm = new Comments();
		cm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		CommentWapper comment;
		try {
			comment = cm.getCommentTimeline(this.getPaging(pager));
			List<Comment> comments = comment.getComments();
			for (Comment c : comments) {
				ITweet p = TweetFactory.createCommentTweet(c);
				result.getRows().add(p);
			}
			result.setRealcount((int) comment.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<IAccount> friendshipFollowersGet(String uid, int cursor)
			throws ApiException {
		Friendships fm = new Friendships();
		fm.client.setToken(_auth.getSinaToken());
		Pagefile<IAccount> result = new Pagefile<IAccount>();
		try {
			UserWapper wapper = fm.getFollowersById(uid, 200, cursor);
			List<weibo4j.model.User> users = wapper.getUsers();
			for (weibo4j.model.User c : users) {
				IAccount p = AccountFactory.createAccount(c);
				result.getRows().add(p);
			}
			result.setRealcount((int) wapper.getTotalNumber());
			result.setNext_cursor((int) wapper.getNextCursor());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<IAccount> friendshipFriendsGet(String uid, Pager pager)
			throws ApiException {
		Friendships fm = new Friendships();
		fm.client.setToken(_auth.getSinaToken());
		Pagefile<IAccount> result = new Pagefile<IAccount>();
		try {
			UserWapper wapper = fm.getFriendsByID(uid);
			List<weibo4j.model.User> users = wapper.getUsers();
			for (weibo4j.model.User c : users) {
				IAccount p = AccountFactory.createAccount(c);
				result.getRows().add(p);
			}
			result.setRealcount((int) wapper.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<ITweet> commentsByMe(Pager pager) throws ApiException {
		Comments cm = new Comments();
		cm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {
			Paging paging = this.getPaging(pager);
			CommentWapper comment = cm.getCommentByMe(paging, 0);
			List<Comment> comments = comment.getComments();
			for (Comment c : comments) {
				ITweet p = TweetFactory.createCommentTweet(c);
				result.getRows().add(p);
			}
			result.setRealcount((int) comment.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<ITweet> tweetsHomeTimeline(Pager pager) throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {

			Paging paging = this.getPaging(pager);
			StatusWapper wapper = tm.getHomeTimeline(0, 0, paging);
			List<Status> sList = wapper.getStatuses();
			for (Status s : sList) {
				ITweet p = TweetFactory.createTweet4Home(s, this._auth
						.getAccount().getUid());
				result.getRows().add(p);
			}
			result.setRealcount((int) wapper.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public Pagefile<ITweet> tweetsUserTimeline(String lastid, Pager pager)
			throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {
			String uid = this._auth.getAccount().getUid();
			Paging paging = this.getPaging(pager);
			StatusWapper status = tm.getUserTimelineByUid(uid, paging, 0, 0);
			List<Status> sList = status.getStatuses();
			for (Status s : sList) {
				ITweet p = TweetFactory.createTweet(s);
				result.getRows().add(p);
			}
			result.setRealcount((int) status.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	private Paging getPaging(Pager pager) {

		Paging p = new Paging();
		p.setCount(pager.getPageSize());
		p.setPage(pager.getPageIndex());
		p.sinceId(Long.parseLong(pager.getSinceId()));
		return p;
	}

	@Override
	public Pagefile<ITweet> retweetsTimeline(String wbId, Pager pager)
			throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		Pagefile<ITweet> result = new Pagefile<ITweet>();
		try {
			Paging paging = this.getPaging(pager);
			StatusWapper wapper = tm.getRepostTimeline(wbId, paging);
			List<Status> sList = wapper.getStatuses();
			for (Status s : sList) {
				ITweet p = TweetFactory.createTweet(s);
				result.getRows().add(p);
			}
			result.setRealcount((int) wapper.getTotalNumber());
			return result;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ITweet tweetGet(String wbId) throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		try {
			Status status = tm.showStatus(wbId);

			return TweetFactory.createTweet(status);

		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public IAccount accountGetByUid(String uid) throws ApiException {
		Users um = new Users();
		um.client.setToken(_auth.getSinaToken());
		try {
			User u = um.showUserById(uid);
			return AccountFactory.createAccount(u);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public IAccount accountGet(String token, String uid) throws ApiException {
		Users um = new Users();
		um.client.setToken(token);
		try {
			User u = um.showUserById(uid);
			return AccountFactory.createAccount(u);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public IAccount accountGetByName(String name) throws ApiException {
		Users um = new Users();
		um.client.setToken(_auth.getSinaToken());
		try {
			User u = um.showUserByScreenName(name);
			return AccountFactory.createAccount(u);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public IAccount accountGetByDomain(String domain) throws ApiException {
		Users um = new Users();
		um.client.setToken(_auth.getSinaToken());
		try {
			User u = um.showUserByDomain(domain);
			return AccountFactory.createAccount(u);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ITweet tweetCreate(String text, UploadImage image)
			throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		try {
			Status s = null;
			if (image == null) {
				s = tm.UpdateStatus(text);
			} else {
				try {
					text = URLEncoder.encode(text, "utf-8");
					ImageItem item = new ImageItem("pic", image.getContent());
					s = tm.UploadStatus(text, item);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (s != null)
				return TweetFactory.createTweet(s);
			else
				return null;
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ITweet tweetCreate(String text, List<String> images)
			throws ApiException {
		// 如果有图片则按发送图片的方式发送
		if (images != null && images.size() > 0) {
			// 先调用http://open.weibo.com/wiki/2/statuses/upload_pic上传图片
			// 再调用http://open.weibo.com/wiki/2/statuses/upload_url_text发布带图片的微博

			// 等待高级接口审批
			return null;
		} else {
			// return this.tweetCreate(text, null);
		}

		return null;

	}

	@Override
	public ITweet commentCreated(String wbId, String text) throws ApiException {
		Comments cm = new Comments();
		cm.client.setToken(_auth.getSinaToken());
		try {
			Comment comment = cm.createComment(text, wbId);
			return TweetFactory.createCommentTweet(comment);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ITweet tweetRepost(String wbId, String text, int is_comment)
			throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		try {
			Status status = tm.Repost(wbId, text, is_comment);
			return TweetFactory.createTweet(status);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ITweet tweetDestroy(String wbId) throws ApiException {
		Timeline tm = new Timeline();
		tm.client.setToken(_auth.getSinaToken());
		try {
			Status status = tm.Destroy(wbId);
			return TweetFactory.createTweet(status);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ITweet commentDestory(String wbId) throws ApiException {
		Comments cm = new Comments();
		cm.client.setToken(_auth.getSinaToken());
		try {
			Comment comment = cm.destroyComment(wbId);
			return TweetFactory.createCommentTweet(comment);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public ITweet commentReply(String text, String wbId, String cid)
			throws ApiException {
		Comments cm = new Comments();
		cm.client.setToken(_auth.getSinaToken());
		try {
			Comment comment = cm.replyComment(cid, wbId, text);
			return TweetFactory.createCommentTweet(comment);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public IAccount friendshipCreate(String uid) throws ApiException {
		Friendships cm = new Friendships();
		cm.client.setToken(_auth.getSinaToken());
		try {
			User user = cm.createFriendshipsById(uid);
			return AccountFactory.createAccount(user);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public IAccount friendshipDestory(String uid) throws ApiException {
		Friendships cm = new Friendships();
		cm.client.setToken(_auth.getSinaToken());
		try {
			User user = cm.destroyFriendshipsDestroyById(uid);
			return AccountFactory.createAccount(user);
		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

	@Override
	public List<BeTrendResult> behaviorTrend(Date start, Date end)
			throws ApiException {
		// TODO Auto-generated method stub
		Business b = new Business();
		b.setToken(this._auth.getSinaToken());
		try {
			String bt = b.behaviorTrend(start, end);
			List<BeTrendResult> result = new ArrayList<BeTrendResult>();
			if (!Utility.isNullOrEmpty(bt)) {
				try {
					JSONObject jsonObj = new JSONObject(bt);
					JSONArray array = jsonObj.getJSONArray("result");
					if (array != null) {
						for (int i = 0; i < array.length(); i++) {
							BeTrendResult btr = new BeTrendResult();
							JSONObject t = array.getJSONObject(i);
							btr.setUcode(jsonObj.getString("uid"));
							btr.setBi_followers_count(t
									.getInt("bi_followers_count"));
							btr.setComments_count(t.getInt("comments_count"));
							btr.setDaren_followers_count(t
									.getInt("daren_followers_count"));
							btr.setFollowers_count(t.getInt("followers_count"));
							btr.setFriends_count(t.getInt("friends_count"));
							btr.setReceive_comments_count(t
									.getInt("receive_comments_count"));
							btr.setRepost_count(t.getInt("repost_count"));
							btr.setReposted_count(t.getInt("reposted_count"));
							btr.setStatuses_count(t.getInt("statuses_count"));
							btr.setV_followers_count(t
									.getInt("v_followers_count"));
							btr.setV_friends_count(t.getInt("v_friends_count"));
							result.add(btr);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;

		} catch (WeiboException e) {
			throw new ApiException(e);
		}
	}

}
