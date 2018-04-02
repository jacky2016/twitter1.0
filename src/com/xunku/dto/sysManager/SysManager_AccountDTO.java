package com.xunku.dto.sysManager;

import com.xunku.app.enums.Platform;

/*
 * SysManager_AccountDTO 系统管理 账号信息
 * @author: hjian
 */
public class SysManager_AccountDTO {
	private int id;
	private String wbType;	//微博类别
	private String loginName;	//微博登录帐号
	private String twitterState;//微博状态
	private String liveDay;		//剩余天数
	private int platFormType;
	private String twitterName;
	private String nickName;	//微博名称
	private String uid ;		
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTwitterName() {
		return twitterName;
	}
	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}
	public int getPlatFormType() {
		return platFormType;
	}
	public void setPlatFormType(int platFormType) {
		this.platFormType = platFormType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getTwitterState() {
		return twitterState;
	}
	public void setTwitterState(String twitterState) {
		this.twitterState = twitterState;
	}
	public String getLiveDay() {
		return liveDay;
	}
	public void setLiveDay(String liveDay) {
		this.liveDay = liveDay;
	}
	public String getWbType() {
		return wbType;
	}
	public void setWbType(String wbType) {
		this.wbType = wbType;
	}
	
	
	
}
