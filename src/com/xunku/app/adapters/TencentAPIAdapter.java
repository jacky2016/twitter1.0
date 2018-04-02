package com.xunku.app.adapters;

import java.util.Date;
import java.util.List;

import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.xunku.app.interfaces.IAPIAdapter;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.ApiToken;
import com.xunku.app.model.ApiTokenInfo;
import com.xunku.app.model.App;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.UploadImage;
import com.xunku.app.result.BeTrendResult;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 腾讯API适配器实现
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:53:35 PM
 */
public class TencentAPIAdapter implements IAPIAdapter {

	private AppAuth _auth;

	String format = "json";
	String jing = "";
	String wei = "";
	String syncflag = "";
	String clientip = "127.0.0.1";

	public TencentAPIAdapter() {
	}

	private OAuthV2 getAuthV2() {
		OAuthV2 oAuth = new OAuthV2();
		oAuth.setClientId(String.valueOf(_auth.getAppId()));
		oAuth.setClientSecret(_auth.getApp().getSecret());
		oAuth.setRedirectUri(_auth.getApp().getCallbackUrl());
		oAuth.setAccessToken(_auth.getToken());
		oAuth.setOpenid(_auth.getAccount().getUid());
		oAuth.setOpenkey(_auth.getAccount().getKey());
		oAuth.setExpiresIn(String.valueOf(_auth.getExpire()));

		return oAuth;
	}
	
	@Override
	public ApiToken getAccessToken(String code, App app) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiTokenInfo getTokenInfo(String tokenString) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount accountGetByDomain(String domain) throws ApiException {
		// TODO Auto-generated method stub
		this.getAuthV2();
		return null;
	}

	@Override
	public IAccount accountGetByName(String name) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount accountGetByUid(String uid) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITweet commentCreated(String tid, String text) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITweet commentDestory(String tid) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITweet commentReply(String text, String tid, String cid)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> commentsByMe(Pager pager) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> commentsGet(String tid, Pager pager)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> commentsMention(Pager pager) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> commentsTimeline(Pager pager) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> commentsToMe(Pager pager) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount friendshipCreate(String uid) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount friendshipDestory(String uid) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<IAccount> friendshipFollowersGet(String uid, int cursor)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<IAccount> friendshipFriendsGet(String uid, Pager pager)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Pagefile<ITweet> retweetsTimeline(String tid, Pager pager)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuth(AppAuth auth) {
		// TODO Auto-generated method stub

	}

	@Override
	public ITweet tweetCreate(String text, List<String> images)
			throws ApiException {

		TAPI tAPI = new TAPI(this.getAuthV2().getOauthVersion());// 根据oAuth配置对应的连接管理器

		// 取得返回结果
		try {
			String response = tAPI.add(this.getAuthV2(), format, text,
					clientip, jing, wei, syncflag);
			System.out.println(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITweet tweetCreate(String text, UploadImage iamge)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITweet tweetDestroy(String tid) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITweet tweetGet(String tid) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITweet tweetRepost(String tid, String text, int is_comment)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> tweetsHomeTimeline(Pager pager) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> tweetsMention(Pager pager) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pagefile<ITweet> tweetsUserTimeline(String lastid, Pager pager)
			throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount accountGet(String token, String uid) throws ApiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeTrendResult> behaviorTrend(Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
	}

}
