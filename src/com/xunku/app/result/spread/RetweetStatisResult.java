package com.xunku.app.result.spread;

import com.xunku.app.result.Result;

/**
 * 转发统计结果
 * 
 * @author wujian
 * @created on Jul 16, 2014 3:46:25 PM
 */
public class RetweetStatisResult extends Result {

	String name;// 昵称
	String location;// 区域
	int followes;// 有多少分析
	boolean vip;// 是否是vip
	long retweetTime;// 转发时间
	int retweets;// 转发次数
	int level;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getFollowes() {
		return followes;
	}
	public void setFollowes(int followes) {
		this.followes = followes;
	}
	public boolean isVip() {
		return vip;
	}
	public void setVip(boolean vip) {
		this.vip = vip;
	}
	public long getRetweetTime() {
		return retweetTime;
	}
	public void setRetweetTime(long retweetTime) {
		this.retweetTime = retweetTime;
	}
	public int getRetweets() {
		return retweets;
	}
	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}
}
