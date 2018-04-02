package com.xunku.app.model.accounts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.location.City;

public class AccountSina implements IAccount {

	Platform _platform;
	String _description;
	String _domain;
	String _screenName;
	String _name;
	String _ucode;
	String _openId;
	String _nick;
	String _location;
	String _headUrl;
	City _city;
	boolean _verified;
	String _url; // 博客地址
	GenderEnum _gender;
	long _created;
	int _followers;
	int _friends;
	int _statuses;
	List<String> _tags;
	String _largeHead;
	int _rLevel;
	boolean _isXunKu;
	long _timestamp;
	long _uid;
	int _province;
	int _favourites;
	String _profile_url;// string 用户的微博统一URL地址
	String _weihao;
	boolean _follow_me;

	public boolean getFollowMe() {
		return this._follow_me;
	}

	public void setFollowMe(boolean follow_me) {
		this._follow_me = follow_me;
	}

	public String getProfileUrl() {
		return this._profile_url;
	}

	public String getDomain() {
		return this._domain;
	}

	public void setUid(long uid) {
		this._uid = uid;
	}

	public AccountSina() {
		this._platform = Platform.Sina;
	}

	public void setDescription(String desc) {
		this._description = desc;
	}

	public void setDomain(String domain) {
		this._domain = domain;
	}

	public void setScreenName(String screenName) {
		this._screenName = screenName;
	}

	public void setName(String name) {
		this._name = name;
	}

	public void setUcode(String ucode) {
		this._ucode = ucode;
	}

	public void setLocation(String location) {
		this._location = location;
	}

	public void setHead(String head) {
		this._headUrl = head;
	}

	public void setCity(City city) {
		this._city = city;
	}

	public void setVerified(boolean verified) {
		this._verified = verified;
	}

	public void setUrl(String url) {
		this._url = url;
	}

	public void setGender(GenderEnum gender) {
		this._gender = gender;
	}

	public void setCreatedAt(long created) {
		this._created = created;
	}

	public void setFollowers(int followers) {
		this._followers = followers;
	}

	public void setFriends(int friends) {
		this._friends = friends;
	}

	public void setStatuses(int statuses) {
		this._statuses = statuses;
	}

	public void setTags(String tags) {
		this._tags = Utility.getTags(tags);
	}

	public void setLargeHead(String head) {
		this._largeHead = head;
	}

	public void setRLevel(int rlevel) {
		this._rLevel = rlevel;
	}

	public void setXunku(boolean isXunku) {
		this._isXunKu = isXunku;
	}

	public int getProvince() {
		return this._province;
	}

	public void setProvince(int provi) {
		this._province = provi;
	}

	public long getTimestamp() {
		return this._timestamp;
	}

	public int getLevel() {
		return this._rLevel;
	}

	public void setFavourites(int favs) {
		this._favourites = favs;
	}

	public int getFavourites() {
		return _favourites;
	}

	public String getWeihao() {
		return _weihao;
	}

	@Override
	public String getDescription() {
		return this._description;
	}

	@Override
	public int getFollowers() {
		return this._followers;
	}

	@Override
	public String getHomeUrl() {
		return "http://weibo.com/u/" + this._ucode;
	}

	@Override
	public Platform getPlatform() {
		return Platform.Sina;
	}

	@Override
	public String ToJson() {
		return Utility.toJSON(this);
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
	public GenderEnum getGender() {
		return this._gender;
	}

	@Override
	public String getLocation() {
		return this._location;
	}

	@Override
	public String getName() {
		return this._name;
	}

	@Override
	public List<String> getTags() {
		return this._tags;
	}

	@Override
	public String getUcode() {
		return this._ucode;
	}

	@Override
	public boolean isVerified() {
		return this._verified;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this._timestamp = timestamp;

	}

	public String getUrl() {
		return this._url;
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
		rt.put("ucode", this.getUcode() == null ? "" : this.getUcode());
		rt.put("uid", this.getUid() == null ? "" : this.getUid());
		rt.put("url", this.getUrl() == null ? "" : this.getUrl());
		int cityCode = 0;
		if (this.getCity() != null) {
			cityCode = this.getCity().getCode();
		}
		rt.put("prov", String.valueOf(this.getProvince()));
		rt.put("city", String.valueOf(cityCode));
		rt.put("favs", String.valueOf(this.getFavourites()));
		rt.put("fols", String.valueOf(this.getFollowers()));
		rt.put("frds", String.valueOf(this.getFriends()));
		int sex = 3;
		if (this.getGender() == GenderEnum.Male) {
			sex = 1;
		} else if (this.getGender() == GenderEnum.Famale) {
			sex = 2;
		}
		rt.put("sex", String.valueOf(sex));
		rt.put("lel", String.valueOf(this._rLevel));
		rt.put("tst", String.valueOf(this._timestamp));
		rt.put("tweets", String.valueOf(this.getWeibos()));
		rt.put("plt", String.valueOf(Utility.getPlatform(this.getPlatform())));
		rt.put("reg", String.valueOf(this._created));
		rt.put("tags", Utility.getTags(this.getTags()));
		rt.put("ver", String.valueOf(this._verified));

		String screen_name = this.getDisplayName() == null ? "" : this
				.getDisplayName();
		rt.put("sn", screen_name);
		String domain = this.getDomain() == null ? "" : this.getDomain();
		rt.put("dm", domain);
		return rt;
	}

	@Override
	public boolean isXunku() {
		return false;
	}

	public String getUid() {
		return String.valueOf(this._uid);
	}

	@Override
	public String getDisplayName() {
		return this._screenName;
	}

	@Override
	public int getWeibos() {
		return this._statuses;
	}

	@Override
	public int getFriends() {
		return this._friends;
	}

	@Override
	public String getHead() {
		return this._headUrl;
	}

	@Override
	public String getLargeHead() {
		return this._largeHead;
	}

}
