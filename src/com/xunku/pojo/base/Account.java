package com.xunku.pojo.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;

/**
 * 微博帐号信息定义
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:55:29 PM
 */
public class Account {

	String nick;
	int city;
	String description;
	String url;
	long regTime;
	int favourites; // 收藏数
	List<String> tags;
	String largeHead;
	String homeUrl;
	boolean fromXK;// 这个作者是否来自讯库，来自讯库的作者信息不全，不能用于更新

	public Account() {
		this.tags = new ArrayList<String>();
	}
/*
	public static Account createAccount(AccountSimple simple) {
		Account acc = new Account();
		acc._timestamp = simple._timestamp;
		acc.city = 0;
		acc.description = null;
		acc.favourites = 0;
		acc.followers = simple.followers;
		acc.friends = simple.friends;
		acc.gender = simple.gender;
		acc.head = simple.head;
		acc.homeUrl = null;
		acc.largeHead = null;
		acc.location = simple.location;
		acc.name = simple.name;
		acc.nick = "";
		acc.platform = simple.platform;
		acc.regTime = 0l;
		acc.rLevel = simple.rLevel;
		acc.tags = null;
		acc.ucode = simple.ucode;
		acc.uid = simple.uid;
		acc.url = null;
		acc.verified = simple.verified;
		acc.weibos = simple.weibos;
		return acc;
	}
*/

	public void copy(Account that) {
		this.city = that.city;
		this.description = that.description;
		this.favourites = that.favourites;
		this.followers = that.followers;
		this.friends = that.friends;
		this.gender = that.gender;
		this.head = that.head;
		this._timestamp = that._timestamp;
		this.location = that.location;
		this.name = that.name;
		this.nick = that.nick;
		this.platform = that.platform;
		// this.refreshLevel 这个级别不变
		this.tags = that.tags;
		this.weibos = that.weibos;
		this.largeHead = that.largeHead;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getDescription() {
		if (Utility.isNullOrEmpty(this.description))
			return "";
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		if (Utility.isNullOrEmpty(url))
			return "";
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getRegTime() {
		return regTime;
	}

	public void setRegTime(long regTime) {
		this.regTime = regTime;
	}

	public int getFavourites() {
		return favourites;
	}

	public void setFavourites(int favourites) {
		this.favourites = favourites;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Platform: " + this.platform + "\n");
		buf.append("UCODE: " + this.ucode + "\n");
		buf.append("UID: " + this.uid + "\n");
		buf.append("Name: " + this.name + "\n");
		buf.append("Head: " + this.getHead() + "\n");
		buf.append("Decription: " + this.description + "\n");
		buf.append("Weibos: " + this.weibos + "\n");
		buf.append("Friends: " + this.friends + "\n");
		buf.append("Followers: " + this.followers + "\n");
		buf.append("City: " + this.city + "\n");
		buf.append("Location: " + this.location + "\n");
		buf.append("Gender: " + Utility.getGender(this.gender) + "\n");
		buf.append("Tags: " + Utility.getTags(this.tags) + "\n");
		return buf.toString();
	}

	public String ToJson() {
		return Utility.toJSON(this);
	}

	public String getLargeHead() {
		return largeHead;
	}

	public void setLargeHead(String largeHead) {
		this.largeHead = largeHead;
	}

	public String getHomeUrl() {
		if (Utility.isNullOrEmpty(homeUrl)) {
			if (this.platform == Platform.Sina) {
				homeUrl = "http://weibo.com/u/" + this.uid;
			}

			if (this.platform == Platform.Tencent) {
				homeUrl = "http://t.qq.com/" + this.name;
			}
		}
		return homeUrl;
	}

	public Map<String, String> toHashMap() {
		Map<String, String> rt = new HashMap<String, String>();
		rt.put("des", this.getDescription() == null ? "" : this
				.getDescription());
		rt.put("hed", this.getHead() == null ? "" : this.getHead());
		rt.put("lhed", this.getLargeHead() == null ? "" : this.getLargeHead());
		rt.put("loc", this.getLocation() == null ? "" : this.getLocation());
		rt.put("name", this.getName() == null ? "" : this.getName());
		rt.put("nick", this.getNick() == null ? "" : this.getNick());
		rt.put("ucode", this.getUcode() == null ? "" : this.getUcode());
		rt.put("uid", this.getUid() == null ? "" : this.getUid());
		rt.put("url", this.getUrl() == null ? "" : this.getUrl());
		rt.put("city", String.valueOf(this.getCity()));
		rt.put("favs", String.valueOf(this.getFavourites()));
		rt.put("fols", String.valueOf(this.getFollowers()));
		rt.put("frds", String.valueOf(this.getFriends()));
		rt.put("sex", String.valueOf(this.getGender()));
		rt.put("lel", String.valueOf(this.getLevel()));
		rt.put("tst", String.valueOf(this.getTimestamp()));
		rt.put("tweets", String.valueOf(this.getWeibos()));
		rt.put("plt", String.valueOf(Utility.getPlatform(this.getPlatform())));
		rt.put("reg", String.valueOf(this.getRegTime()));
		rt.put("tags", Utility.getTags(this.getTags()));
		return rt;
	}

	// ==================== 基本属性 =========================

	String uid;
	String ucode;
	Platform platform;
	String name;
	boolean verified; // 是否认证
	String head;
	String location;
	/**
	 * 1男，2女，3未知
	 */
	int gender;// 性别1=男,2=女,3=未知
	int followers; // 粉丝数
	int friends; // 关注数
	int weibos; // 微博数

	int rLevel;// 刷新级别
	long _timestamp;// 最后一次刷新时间

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUcode() {
		if (Utility.isNullOrEmpty(ucode)) {
			return uid;
		}
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platfrom) {
		this.platform = platfrom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		if (Utility.isNullOrEmpty(location))
			return "";
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 1=男、2女、3未知
	 * 
	 * @return
	 */
	public int getGender() {

		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFriends() {
		return friends;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

	public int getWeibos() {
		return weibos;
	}

	public void setWeibos(int weibos) {
		this.weibos = weibos;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getHead() {
		if (Utility.isNullOrEmpty(head)) {
			return "";
		}
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public long getTimestamp() {
		return _timestamp;
	}

	public void setTimestamp(long timestamp) {
		this._timestamp = timestamp;
	}

	public int getLevel() {
		return rLevel;
	}

	public void setLevel(int rLevel) {
		this.rLevel = rLevel;
	}


	public boolean isFromXK() {
		return fromXK;
	}

	public void setFromXK(boolean fromXK) {
		this.fromXK = fromXK;
	}
}
