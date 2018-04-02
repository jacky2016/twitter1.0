package com.xunku.pojo.base;

import java.util.Date;

//账号授权by wanghui
public class AccountAuths {
    private int id;
    private int accountId;//账号id
    private String uid;   //
    private int platform; //微博类型
    private String token; //账号密码
    private int appId;    //appid
    private int expiresin;//过期的秒数
    private String ucode;
    private Date authTime;//授权时间
    private int customId;   //客户
    private String name;
    public Date getAuthTime() {
        return authTime;
    }
    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }
    public int getCustomId() {
        return customId;
    }
    public void setCustomId(int customId) {
        this.customId = customId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public int getPlatform() {
        return platform;
    }
    public void setPlatform(int platform) {
        this.platform = platform;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getAppId() {
        return appId;
    }
    public void setAppId(int appId) {
        this.appId = appId;
    }
    public int getExpiresin() {
        return expiresin;
    }
    public void setExpiresin(int expiresin) {
        this.expiresin = expiresin;
    }
    public String getUcode() {
        return ucode;
    }
    public void setUcode(String ucode) {
        this.ucode = ucode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
