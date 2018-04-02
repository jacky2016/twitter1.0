package com.xunku.dto.eventMonitor;

import java.util.Date;

public class EventDTO {

	private int id;
	private String name; // 事件名称
	private String keywords; // 关键词
	private String notkeywords;// 不包含关键词
	private int location; // 地域
	private String startTime; // 开始时间
	private String endTime; // 结束时间
	private int creator; // 创建者
	private int customID; // 客户
	private int platform; // 平台类型：1新浪2腾讯5人民
	private String dbserver; // server连接串
	private Date fetchTime;// 抓取时间modity by wanghui
	// private String table; //表名
	private String prov; // 用于地点修改省直辖市
	private String city; // 用于地点修改的市区
	
	private String err;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getNotkeywords() {
		return notkeywords;
	}
	public void setNotkeywords(String notkeywords) {
		this.notkeywords = notkeywords;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public int getCustomID() {
		return customID;
	}
	public void setCustomID(int customID) {
		this.customID = customID;
	}
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public String getDbserver() {
		return dbserver;
	}
	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}
	public Date getFetchTime() {
		return fetchTime;
	}
	public void setFetchTime(Date fetchTime) {
		this.fetchTime = fetchTime;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	
	
	
}
