package com.xunku.pojo.weibo;
//转发地域分布by wanghui
//对应表Weibo_Repost_Local_An
public class RepostLocal {
    private int id;
    private int weiboid;  //微博id
    private int type;     //转发评论:1转发2评论
    private int city;     //城市编码
    private String location;//区域信息
    private int value;    //这个地域有多少转发的
    private int total;
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
    public int getCity() {
        return city;
    }
    public void setCity(int city) {
        this.city = city;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
