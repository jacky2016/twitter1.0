package com.xunku.pojo.weibo;
//转发来源
//对应表Weibo_Repost_Source_An
public class RepostSource {
    private int id;
    private int weiboid;//微博id
    private int type;   //来源类型:1转发2评论
    private int source; //来源描述
    private int value; //来源数量
    private int total; //总数
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
    public int getSource() {
        return source;
    }
    public void setSource(int source) {
        this.source = source;
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
