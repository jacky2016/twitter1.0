package com.xunku.pojo.weibo;
//转发态度分析by wanghui
//对应表Weibo_Repost_Opinion_An
public class RepostOpinion {
    private int id;
    private int weiboid;//微博id
    private int opinion;//表态数量
    private int unopinion;//未表态数量
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
    public int getOpinion() {
        return opinion;
    }
    public void setOpinion(int opinion) {
        this.opinion = opinion;
    }
    public int getUnopinion() {
        return unopinion;
    }
    public void setUnopinion(int unopinion) {
        this.unopinion = unopinion;
    }
}
