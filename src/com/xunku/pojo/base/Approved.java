package com.xunku.pojo.base;

import java.util.List;
//审核设置 add by wanghui
public class Approved {
    private int id;  
    private int userid;    //用户
    private int checkid;   //审核人
    private boolean isCheck;//审核的操作
    private List<String> uids;//账号列表
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public int getCheckid() {
        return checkid;
    }
    public void setCheckid(int checkid) {
        this.checkid = checkid;
    }
    public List<String> getUids() {
        return uids;
    }
    public void setUids(List<String> uids) {
        this.uids = uids;
    }
    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    
}
