package com.xunku.app.model;

/**
 * ApiToken基本信息，包括创建时间，过期时间，和授权应用
 * @author wujian
 * @created on Jun 5, 2014 3:42:08 PM
 */
public class ApiTokenInfo {
/*
 * {
       "uid": 1073880650,
       "appkey": 1352222456,
       "scope": null,
       "create_at": 1352267591,
       "expire_in": 157679471
 }
 * */
	
	private String uid;
	private String appkey;
	private String scope;
	private String created;
	private String expireIn;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(String expireIn) {
		this.expireIn = expireIn;
	}
	
}
