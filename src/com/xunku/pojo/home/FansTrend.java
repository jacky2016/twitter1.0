package com.xunku.pojo.home;

import java.util.Date;

import com.xunku.app.enums.Platform;

public class FansTrend {

	String ucode;
	Platform platform;
	Date created;
	int fans;
	int weibos;
	int friends;
	Date updated;
	public String getUcode() {
		return ucode;
	}
	public void setUcode(String ucode) {
		this.ucode = ucode;
	}
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
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
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
