package com.xunku.app.model.accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.location.City;

public class AccountTencent implements IAccount {

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	String _largHead;
	String _head;
	int _friends;
	int _weibos;
	boolean _isXunku;
	int _followers;
	String _desc;
	long _timestamp;
	boolean _verified;
	City _city;
	long _created;
	String _location;
	String _name;
	int _favourites;
	String _nick;
	String _homePage;
	String _openId;
	Platform _platform;
	GenderEnum _gender;
	List<String> _tags;
	String _homeUrl;

	public void setXunku(boolean isXunku) {
		this._isXunku = isXunku;
	}

	public void setName(String name) {
		this._name = name;

	}

	public void setUcode(String ucode) {
		this._name = ucode;
	}

	public void setOpenId(String openid) {
		this._openId = openid;
	}

	public String getOpenId() {
		return this._openId;
	}

	public void setHomePage(String homepage) {
		this._homePage = homepage;
	}

	public String getHomePage() {
		return this._homePage;
	}

	public void setNick(String nick) {
		this._nick = nick;
	}

	public void setLocation(String location) {
		this._location = location;
	}

	public void setHead(String head) {
		this._head = head;
	}

	public void setCity(City city) {
		this._city = city;
	}

	public void setVerified(boolean verified) {
		this._verified = verified;
	}

	public void setIntroduction(String introduction) {
		this._desc = introduction;
	}

	public void setFavourites(int favourites) {
		this._favourites = favourites;
	}

	public void setFollowers(int followers) {
		this._followers = followers;
	}

	public void setFollowings(int followings) {
		this._friends = followings;
	}

	public void setTweets(int tweets) {
		this._weibos = tweets;
	}

	public void setGender(GenderEnum gender) {
		this._gender = gender;
	}

	public void setRegTime(long regTime) {
		this._created = regTime;
	}

	public void addTag(String tag) {
		if (!this._tags.contains(tag)) {
			this._tags.add(tag);
		}
	}

	public AccountTencent() {
		_platform = Platform.Tencent;
		_tags = new ArrayList<String>();
	}

	@Override
	public String getUcode() {
		return this._name;
	}

	@Override
	public String getName() {
		return this._name;
	}

	@Override
	public Platform getPlatform() {
		return Platform.Tencent;
	}

	@Override
	public String getHomeUrl() {
		return "http://t.qq.com/" + this._name;
	}

	@Override
	public int getFollowers() {
		return this._followers;
	}

	@Override
	public String getDescription() {
		return this._desc;
	}

	@Override
	public Map<String, String> toHashMap() {
		Map<String, String> rt = new HashMap<String, String>();
		rt.put("des", this.getDescription() == null ? "" : this
				.getDescription());
		rt.put("hed", this.getHead() == null ? "" : this.getHead());
		rt.put("lhed", this.getLargeHead() == null ? "" : this.getLargeHead());
		rt.put("loc", this.getLocation() == null ? "" : this.getLocation());
		rt.put("name", this.getName() == null ? "" : this.getName());
		rt.put("nick", this.getDisplayName() == null ? "" : this
				.getDisplayName());
		rt.put("ucode", this.getUcode() == null ? "" : this.getUcode());
		rt.put("uid", this.getOpenId() == null ? "" : this.getOpenId());
		rt.put("url", this.getHomePage() == null ? "" : this.getHomePage());
		int cityCode = 0;
		if (this.getCity() != null) {
			cityCode = this.getCity().getCode();
		}
		// rt.put("prov", String.valueOf(this.getProvince()));
		rt.put("city", String.valueOf(cityCode));
		// rt.put("favs", String.valueOf(this.getFavourites()));
		rt.put("fols", String.valueOf(this.getFollowers()));
		rt.put("frds", String.valueOf(this.getFriends()));
		int sex = 3;
		if (this.getGender() == GenderEnum.Male) {
			sex = 1;
		} else if (this.getGender() == GenderEnum.Famale) {
			sex = 2;
		}
		rt.put("sex", String.valueOf(sex));
		rt.put("lel", String.valueOf(0));
		rt.put("tst", String.valueOf(this._timestamp));
		rt.put("tweets", String.valueOf(this.getWeibos()));
		rt.put("plt", String.valueOf(Utility.getPlatform(this.getPlatform())));
		rt.put("reg", String.valueOf(this._created));
		rt.put("tags", Utility.getTags(this.getTags()));
		rt.put("ver", String.valueOf(this._verified));
		return rt;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this._timestamp = timestamp;
	}

	@Override
	public String ToJson() {
		return Utility.toJSON(this);
	}

	@Override
	public boolean isVerified() {
		return this._verified;
	}

	@Override
	public GenderEnum getGender() {
		return this._gender;
	}

	@Override
	public City getCity() {
		return this._city;
	}

	@Override
	public long getCreated() {
		return this._created;
	}

	@Override
	public String getLocation() {
		return this._location;
	}

	@Override
	public List<String> getTags() {
		return this._tags;
	}

	@Override
	public boolean isXunku() {
		return this._isXunku;
	}

	@Override
	public String getDisplayName() {
		if (Utility.isNullOrEmpty(this._nick)) {
			return this._name;
		}
		return this._nick;
	}

	@Override
	public int getWeibos() {
		return this._weibos;
	}

	@Override
	public int getFriends() {
		return this._friends;
	}

	@Override
	public String getHead() {
		return this._head;
	}

	@Override
	public String getLargeHead() {
		return this._largHead;
	}

	@Override
	public long getTimestamp() {
		return this._timestamp;
	}
}
