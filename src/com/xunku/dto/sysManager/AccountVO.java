package com.xunku.dto.sysManager;

import java.util.Date;

//审核设置 add by wanghui
public class AccountVO {
    private int id;
    private String uid; //微博账号uid
    private String name;//微博名称
    private int type;   //平台类型 1 新浪，2 腾讯，5 人民
    private Date authTime;	//授权日期
	private int expiresin;  //授权有效期
	private boolean flag;	//是否有效
	
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
	public Date getAuthTime() {
		return authTime;
	}
	public void setAuthTime(Date authTime) {
		this.authTime = authTime;
	}
	public int getExpiresin() {
		return expiresin;
	}
	public void setExpiresin(int expiresin) {
		this.expiresin = expiresin;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
    
}
