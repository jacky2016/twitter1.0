package com.xunku.pojo.weibo;
//转发用户统计
//对应表Weibo_Repost_Count_An
public class RepostCount {
    private int id;
    private int weiboid;//
    private int type;   //转发，评论类型
    private int vip;    //转发用户里vip的数量
    private int novip;  //转发不是vip的数量
    private int female; //女的有多少
    private int male;   //男的有多少
    private int unsex;  //未知男女的有多少
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
    public int getVip() {
        return vip;
    }
    public void setVip(int vip) {
        this.vip = vip;
    }
    public int getNovip() {
        return novip;
    }
    public void setNovip(int novip) {
        this.novip = novip;
    }
    public int getFemale() {
        return female;
    }
    public void setFemale(int female) {
        this.female = female;
    }
    public int getMale() {
        return male;
    }
    public void setMale(int male) {
        this.male = male;
    }
    public int getUnsex() {
        return unsex;
    }
    public void setUnsex(int unsex) {
        this.unsex = unsex;
    }
}
