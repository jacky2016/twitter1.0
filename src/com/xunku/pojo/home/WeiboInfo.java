package com.xunku.pojo.home;

public class WeiboInfo {
    private int id;          //id
    private int accountID;   //微博帐号ID
    private int userID;      //用户ID，显示微博基本信息，此信息是基于人个性化的，所以需要用户ID，其他HOME则是基于CustomID个性化的
    private boolean closed;  //是否关闭 
    private int position;    //信息的位置Order
    private boolean expand;  //这个微博信息是否展开
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAccountID() {
        return accountID;
    }
    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public boolean isClosed() {
        return closed;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public boolean isExpand() {
        return expand;
    }
    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
