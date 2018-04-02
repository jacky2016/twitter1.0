package com.xunku.app.result;

import java.util.Date;

public class AccountTrendResult {
	Date created;
	int followers;
	int weibos;
	int friends;
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getFollowers() {
		return followers;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	public int getWeibos() {
		return weibos;
	}
	public void setWeibos(int weibos) {
		this.weibos = weibos;
	}
	public int getFriends() {
		return friends;
	}
	public void setFriends(int friends) {
		this.friends = friends;
	}
}
