package com.xunku.pojo.event;
//性别分布【用户分析】by wanghui
//对应数据库表Event_Count_An
public class EventCount {
    private int id;
    private int eventId;//事件id
    private int posts;  //微博数
    private int reposts;//转发数
    private int females;//女性数量
    private int males;  //男性数量
    private int unsex;  //未知性别
    private int vip;    //认证数量
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
    public int getPosts() {
        return posts;
    }
    public void setPosts(int posts) {
        this.posts = posts;
    }
    public int getReposts() {
        return reposts;
    }
    public void setReposts(int reposts) {
        this.reposts = reposts;
    }
    public int getFemales() {
        return females;
    }
    public void setFemales(int females) {
        this.females = females;
    }
    public int getMales() {
        return males;
    }
    public void setMales(int males) {
        this.males = males;
    }
    public int getUnsex() {
        return unsex;
    }
    public void setUnsex(int unsex) {
        this.unsex = unsex;
    }
    public int getVip() {
        return vip;
    }
    public void setVip(int vip) {
        this.vip = vip;
    }
}
