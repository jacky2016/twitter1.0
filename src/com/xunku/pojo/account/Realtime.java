package com.xunku.pojo.account;
//用户实时分析by wanghui
//对应表Account_Realtime
public class Realtime {
    private int id;
    private String sampling;//采样时间
    private int accountId;  //监测帐号编号
    private String ucode;   //ucode
    private int followers;  //粉丝数
    private int weibos;     //微博数
    private int friends;    //关注数
    private int reposts;    //转发数
    private int comments;   //评论数
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSampling() {
        return sampling;
    }
    public void setSampling(String sampling) {
        this.sampling = sampling;
    }
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
    public int getReposts() {
        return reposts;
    }
    public void setReposts(int reposts) {
        this.reposts = reposts;
    }
    public int getComments() {
        return comments;
    }
    public void setComments(int comments) {
        this.comments = comments;
    }
}
