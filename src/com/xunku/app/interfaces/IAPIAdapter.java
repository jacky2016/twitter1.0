package com.xunku.app.interfaces;

import java.util.Date;
import java.util.List;

import com.xunku.app.model.ApiToken;
import com.xunku.app.model.ApiTokenInfo;
import com.xunku.app.model.App;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.UploadImage;
import com.xunku.app.result.BeTrendResult;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * API适配器
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:51:36 PM
 */
public interface IAPIAdapter {

	List<BeTrendResult> behaviorTrend(Date start, Date end) throws ApiException;

	IAccount accountGet(String token, String uid) throws ApiException;

	/**
	 * 获得用户基本信息
	 * 
	 * @param uid
	 * @return
	 */
	IAccount accountGetByUid(String uid) throws ApiException;

	/**
	 * 通过名字获得帐号信息
	 * 
	 * @param name
	 * @return
	 */
	IAccount accountGetByName(String name) throws ApiException;

	/**
	 * 通过个性化域名获得帐号信息
	 * 
	 * @param domain
	 * @return
	 */
	IAccount accountGetByDomain(String domain) throws ApiException;

	/**
	 * 创建当前授权和uid之间的关系
	 * 
	 * @param uid
	 */
	IAccount friendshipCreate(String uid) throws ApiException;

	/**
	 * 移除当前授权和uid之间的关系
	 * 
	 * @param uid
	 */
	IAccount friendshipDestory(String uid) throws ApiException;

	/**
	 * 获得用户关注列表
	 * 
	 * @param uid
	 * @return
	 */
	Pagefile<IAccount> friendshipFriendsGet(String uid, Pager pager)
			throws ApiException;

	/**
	 * 获得用户粉丝列表
	 * 
	 * @param uid
	 * @return
	 */
	Pagefile<IAccount> friendshipFollowersGet(String uid, int cursor)
			throws ApiException;

	/**
	 * 获得当前用户首页的最新微博
	 * 
	 * @return
	 */
	Pagefile<ITweet> tweetsHomeTimeline(Pager pager) throws ApiException;

	/**
	 * 获得当前用户发布的最新微博 获取自己的微博，参数uid与screen_name可以不填，则自动获取当前登录用户的微博；
	 * 指定获取他人的微博，参数uid与screen_name二者必选其一，且只能选其一；
	 * 接口升级后：uid与screen_name只能为当前授权用户，第三方微博类客户端不受影响；
	 * 
	 * @param lastid
	 * @return
	 */
	Pagefile<ITweet> tweetsUserTimeline(String lastid, Pager pager)
			throws ApiException;

	/**
	 * 获得微博的转发列表
	 * 
	 * @param wbId
	 * @return
	 */
	Pagefile<ITweet> retweetsTimeline(String tid, Pager pager)
			throws ApiException;

	Pagefile<ITweet> tweetsMention(Pager pager) throws ApiException;

	Pagefile<ITweet> commentsMention(Pager pager) throws ApiException;

	/**
	 * 根据微博ID获取单条微博内容
	 * 
	 * @param wbId
	 * @return
	 */
	ITweet tweetGet(String tid) throws ApiException;

	/**
	 * 转发微博
	 * 
	 * @param wbId
	 * @param text
	 * @return
	 */
	ITweet tweetRepost(String tid, String text, int is_comment)
			throws ApiException;

	/**
	 * 发布带图片的微博
	 * 
	 * @param text
	 * @param images
	 *            TODO
	 * @return
	 */
	ITweet tweetCreate(String text, List<String> images) throws ApiException;

	/**
	 * 发不带图片的微博
	 * 
	 * @param text
	 * @return
	 */
	ITweet tweetCreate(String text, UploadImage iamge) throws ApiException;

	/**
	 * 删除微博
	 * 
	 * @param wbId
	 * @return
	 */
	ITweet tweetDestroy(String tid) throws ApiException;

	/**
	 * 根据微博ID返回某条微博的评论列表
	 * 
	 * @param wbId
	 * @return
	 */
	Pagefile<ITweet> commentsGet(String tid, Pager pager) throws ApiException;

	/**
	 * 获得我发出的评论
	 * 
	 * @return
	 */
	Pagefile<ITweet> commentsByMe(Pager pager) throws ApiException;

	/**
	 * 获取当前登录用户的最新评论包括接收到的与发出的
	 * 
	 * @return
	 */
	Pagefile<ITweet> commentsTimeline(Pager pager) throws ApiException;

	/**
	 * 功能描述<获取@到我的评论>
	 */
	Pagefile<ITweet> commentsToMe(Pager pager) throws ApiException;

	/**
	 * 回复评论
	 * 
	 * @param text
	 * @param weiboId
	 *            需要评论微博的ID
	 * @param cid
	 *            需要评论的评论id
	 * @return
	 */
	ITweet commentReply(String text, String tid, String cid)
			throws ApiException;

	/**
	 * 删除评论
	 * 
	 * @param wbId
	 * @return
	 */
	ITweet commentDestory(String tid) throws ApiException;

	/**
	 * 评论微博
	 * 
	 * @param wbId
	 * @param text
	 * @return
	 */
	ITweet commentCreated(String tid, String text) throws ApiException;

	/**
	 * 为当前适配器设置授权信息
	 * 
	 * @param auth
	 */
	void setAuth(AppAuth auth);

	/**
	 * 查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限。
	 * 
	 * @param tokenString
	 * @return
	 */
	ApiTokenInfo getTokenInfo(String tokenString) throws ApiException;

	/**
	 * 通过code获得Token描述
	 * 
	 * @param code
	 * @param redirect_uri
	 * @return
	 */
	ApiToken getAccessToken(String code, App app) throws ApiException;

}
