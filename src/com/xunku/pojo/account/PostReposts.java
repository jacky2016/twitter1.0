package com.xunku.pojo.account;
//博文转发by wanghui
//对应表Account_Post_Reposts
public class PostReposts {
    private int id;
    private int accountId;   //发文帐号
    private String tid;      //发布博文id
    private String sourceId; //源微博id
    private String sourceName;//原微博作者名称
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
    public String getSourceId() {
        return sourceId;
    }
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
