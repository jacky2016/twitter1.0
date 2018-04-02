package com.xunku.app.result;

import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
//网评员信息
public class NavieResult {
    private int customid; //客户编号
    private String uid;   //uid
    private Platform platform;//平台
    private int navieid;   //网评员id
    private String tid;  //tid
    private PostType type;//转发或评论
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public PostType getType() {
        return type;
    }
    public void setType(PostType type) {
        this.type = type;
    }
    public int getCustomid() {
        return customid;
    }
    public void setCustomid(int customid) {
        this.customid = customid;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public Platform getPlatform() {
        return platform;
    }
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
    public int getNavieid() {
        return navieid;
    }
    public void setNavieid(int navieid) {
        this.navieid = navieid;
    }
}
