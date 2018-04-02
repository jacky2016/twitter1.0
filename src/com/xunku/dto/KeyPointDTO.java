package com.xunku.dto;

import com.xunku.app.enums.Platform;
//关键观点 by wanghui
public class KeyPointDTO {
    private String tid;
    private Platform platform;
    private int reposts;
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public Platform getPlatform() {
        return platform;
    }
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
    public int getReposts() {
        return reposts;
    }
    public void setReposts(int reposts) {
        this.reposts = reposts;
    }
}
