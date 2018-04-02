package com.xunku.app.result;

import com.xunku.app.enums.Platform;
//转发机构
public class RepostOrganizationResult {
    private String name;  //转发机构名称
    private String uid;   //uid
    private Platform platform;//平台
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
}
