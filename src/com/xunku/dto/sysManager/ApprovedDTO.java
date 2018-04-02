package com.xunku.dto.sysManager;
/*
 * ApprovedDTO 系统管理 审核信息
 * @author: hjian
 */
public class ApprovedDTO {
    private int appid;    //审核id
    private String userNick;  //操作员
    private String userName;
    private String uid;   //需要审核的微博账号
    private String check; //审核人
    private boolean isCheck;//是否需要审核
    public int getAppid() {
        return appid;
    }
    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getUserNick() {
        return userNick;
    }
    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getCheck() {
        return check;
    }
    public void setCheck(String check) {
        this.check = check;
    }
    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

}
