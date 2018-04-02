package com.xunku.app.model;

/**
 * 调用API返回的Token结构描述
 * @author wujian
 * @created on Jun 5, 2014 3:41:32 PM
 */
public class ApiToken {
/*
 * {
       "access_token": "ACCESS_TOKEN",
       "expires_in": 1234,
       "remind_in":"798114",
       "uid":"12341234"
 }
 * */
	
	private String token;
	private int expiresIn;
	private String uid;
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
}
