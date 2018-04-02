package com.xunku.pojo.weibo;
//粉丝人气排行by wanghui
//对应表Weibo_Repost_Fans_Pop_An
public class RepostFansPop {
    private int id;
    private int weiboid;//微博id
    private int type;   //转发评论:1转发2评论
    private int v1;     //0-100粉丝数量
    private int v2;     //100-1000
    private int v3;     //1000-10000
    private int v4;     //10000-100000
    private int v5;     //100000-1000000
    private int v6;     //>100万
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
    public int getV1() {
        return v1;
    }
    public void setV1(int v1) {
        this.v1 = v1;
    }
    public int getV2() {
        return v2;
    }
    public void setV2(int v2) {
        this.v2 = v2;
    }
    public int getV3() {
        return v3;
    }
    public void setV3(int v3) {
        this.v3 = v3;
    }
    public int getV4() {
        return v4;
    }
    public void setV4(int v4) {
        this.v4 = v4;
    }
    public int getV5() {
        return v5;
    }
    public void setV5(int v5) {
        this.v5 = v5;
    }
    public int getV6() {
        return v6;
    }
    public void setV6(int v6) {
        this.v6 = v6;
    }
}
