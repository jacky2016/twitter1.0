package com.xunku.pojo.base;

/**
 * 抓取任务的POJO对象
 * 
 * @author wujian
 * @created on Sep 2, 2014 11:57:46 AM
 */
public class CrawlTask {

	int id;
	long created;
	String homeUrl;// 微博账号主页地址
	int domainid;// 微博ID：1=新浪微博，2=腾讯微博；5=人民微博
	int type;// 类型：1=关注；2=粉丝；3=微博；
	long executed;
	long acquired;
	int customId;
	int monitorid;
	int monitorType;// AccountMonitor=1,CustomMonitor=2,EventMonitor=3,WeiboMonitor=4
	long timezone;
	String token;
	String result;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	public int getDomainid() {
		return domainid;
	}

	public void setDomainid(int domainid) {
		this.domainid = domainid;
	}

	/**
	 * 1=关注；2=粉丝；3=微博；
	 * @return
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getExecuted() {
		return executed;
	}

	public void setExecuted(long executed) {
		this.executed = executed;
	}

	public long getAcquired() {
		return acquired;
	}

	public void setAcquired(long acquired) {
		this.acquired = acquired;
	}

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

	public int getMonitorid() {
		return monitorid;
	}

	public void setMonitorid(int monitorid) {
		this.monitorid = monitorid;
	}

	/**
	 * AccountMonitor=1,CustomMonitor=2,EventMonitor=3,WeiboMonitor=4
	 * @return
	 */
	public int getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(int monitorType) {
		this.monitorType = monitorType;
	}

	public long getTimezone() {
		return timezone;
	}

	public void setTimezone(long timezone) {
		this.timezone = timezone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}