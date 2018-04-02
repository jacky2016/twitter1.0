package com.xunku.pojo.event;
//按照博主的粉丝数取TOP-N【关键用户分析】by wanghui 
//对应数据库表Event_KeyUser_An
public class KeyUser {
    private int id;
    private int eventId; //事件id
    private String ucode;//ucode
    private int followers;//粉丝数
    private int weibos;   //微博数
    private int friends; //关注数
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public String getUcode() {
        return ucode;
    }
    public void setUcode(String ucode) {
        this.ucode = ucode;
    }
    public int getFollowers() {
        return followers;
    }
    public void setFollowers(int followers) {
        this.followers = followers;
    }
    public int getWeibos() {
        return weibos;
    }
    public void setWeibos(int weibos) {
        this.weibos = weibos;
    }
    public int getFriends() {
        return friends;
    }
    public void setFriends(int friends) {
        this.friends = friends;
    }
}
