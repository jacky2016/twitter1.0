package com.xunku.dto.sysManager;
/*
 * SysManager_ApproveDTO 系统管理 审核信息
 * @authro: hjian
 */
public class SysManager_ApproveDTO {

    private int appid;    	//审核id
    private String userNick;//用户名称
    private String userName;//用户登录账号
    private String uid;   	//需要审核的微博账号
    private String check; 	//审核人
    private String isCheck;//是否需要审核
	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	public String getUserNick() {
		return userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	
	
	
	
	
}
