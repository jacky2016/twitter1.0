package com.xunku.pojo.my;

import java.util.Date;

//统计我的微博帐号发布情况的统计信息by wanghui
//对应表My_Publish_An
public class Publish {
    private int id;
    private Date sampling;//采样时间
    private String uid; //uid
    private int platform;//平台
    private int weibos;  //微博数
    private int followers;//粉丝数
    private int friends;  //关注数
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getSampling() {
        return sampling;
    }
    public void setSampling(Date sampling) {
        this.sampling = sampling;
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
    public int getWeibos() {
        return weibos;
    }
    public void setWeibos(int weibos) {
        this.weibos = weibos;
    }
    public int getFollowers() {
        return followers;
    }
    public void setFollowers(int followers) {
        this.followers = followers;
    }
    public int getFriends() {
        return friends;
    }
    public void setFriends(int friends) {
        this.friends = friends;
    }
}
