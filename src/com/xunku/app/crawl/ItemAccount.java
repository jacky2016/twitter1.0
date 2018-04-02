package com.xunku.app.crawl;

import java.util.ArrayList;
import java.util.List;

import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

/**
 * 爬虫微博帐号结构定义
 * 
 * @author wujian
 * @created on Sep 3, 2014 4:03:28 PM
 */
public class ItemAccount {

	String uid;
	String ucode;
	String uname;
	int domainid;
	boolean verify;
	String image;
	String addr;
	String sex;
	String userType;
	int fansCount;// 粉丝量
	int followCount;// 关注量
	int weiboCount;// 微博量
	List<ItemAccount> followers = new ArrayList<ItemAccount>();
	List<ItemAccount> followings = new ArrayList<ItemAccount>();
	List<ItemTweet> tweets = new ArrayList<ItemTweet>();

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public int getDomainid() {
		return domainid;
	}

	public void setDomainid(int domainid) {
		this.domainid = domainid;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getFansCount() {
		return fansCount;
	}

	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}

	public int getFollowCount() {
		return followCount;
	}

	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}

	public int getWeiboCount() {
		return weiboCount;
	}

	public void setWeiboCount(int weiboCount) {
		this.weiboCount = weiboCount;
	}

	public String toString() {
		return this.getUname();
	}

	public static ItemAccount create(JSONObject obj) throws JSONException {
		ItemAccount acc = new ItemAccount();
		acc.setAddr(obj.getString("addr"));
		acc.setDomainid(obj.getInt("domainid"));
		acc.setFansCount(obj.getInt("fansCount"));
		acc.setFollowCount(obj.getInt("followCount"));
		acc.setImage(obj.getString("image"));
		acc.setSex(obj.getString("sex"));
		acc.setUcode(obj.getString("ucode"));
		acc.setUid(obj.getString("uid"));
		acc.setUname(obj.getString("uname"));
		acc.setUserType(obj.getString("userType"));
		acc.setVerify(obj.getBoolean("verify"));
		acc.setWeiboCount(obj.getInt("weibocount"));
		return acc;
	}

	public List<ItemAccount> getFollowers() {
		return followers;
	}

	public void setFollowers(List<ItemAccount> followers) {
		this.followers = followers;
	}

	public List<ItemAccount> getFollowings() {
		return followings;
	}

	public void setFollowings(List<ItemAccount> followings) {
		this.followings = followings;
	}

	public List<ItemTweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<ItemTweet> tweets) {
		this.tweets = tweets;
	}
}
