package com.xunku.pojo.weibo;
//热门关键词by wanghui
//对应表Weibo_Repost_Hot_An
public class RepostHot {
    private int id;
    private int weiboid;//微博id
    private int type;   //转发评论:1转发2评论
    private String keyword;//关键词
    private int times;     //出现的次数
    private String updated;//最后一次更新时间
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getWeiboid() {
        return weiboid;
    }
    public void setWeiboid(int weiboid) {
        this.weiboid = weiboid;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public int getTimes() {
        return times;
    }
    public void setTimes(int times) {
        this.times = times;
    }
    public String getUpdated() {
        return updated;
    }
    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
