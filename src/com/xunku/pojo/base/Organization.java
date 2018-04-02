package com.xunku.pojo.base;

public class Organization {
	private int id; // 机构编号
	private int customID; // 客户编号，标记这个机构属于哪个客户
	private String name; // 机构名称
	private int platform; // 0=新浪，1=腾讯，2=人民
	private String uid;// 机构账号
	private boolean IsEnable;// 是否可用

	int retweets;// 这个组织机构的转发数
	int comments;// 组织机构的评论数

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomID() {
		return customID;
	}

	public void setCustomID(int customID) {
		this.customID = customID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public boolean isEnable() {
		return IsEnable;
	}

	public void setEnable(boolean isEnable) {
		IsEnable = isEnable;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getRetweets() {
		return retweets;
	}

	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}
}
