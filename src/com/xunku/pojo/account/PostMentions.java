package com.xunku.pojo.account;
//博文提及by wanghui
//对应表Account_Post_Mentions
public class PostMentions {
    private int id;
    private int accountId; //发文帐号
    private String tid;    //发布博文id
    private String mentionName;//提及的名字
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getMentionName() {
        return mentionName;
    }
    public void setMentionName(String mentionName) {
        this.mentionName = mentionName;
    }
}
