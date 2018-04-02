package com.xunku.pojo.weibo;

public class Monitor {
    private int id;
    private int userID;   //谁创建的这一条监测数据
    private int customID; //客户编号，这是个冗余设计
    private String url;   //从这里可以分析出:1、这是哪个平台的微博,2、这是检测微博的还是博主的
    private int status;   //检测的状态枚举
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getCustomID() {
        return customID;
    }
    public void setCustomID(int customID) {
        this.customID = customID;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
