package com.xunku.app.result;

import java.util.Date;

//微博内容统计result
public class WeiboTextResult {
    private String tid; //微博id
    private String text;//微博内容 
    private Date created;//日期
    private int comments;//评论
    private int reposts; //转发
    private int orgrans; //转发机构
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public int getComments() {
        return comments;
    }
    public void setComments(int comments) {
        this.comments = comments;
    }
    public int getReposts() {
        return reposts;
    }
    public void setReposts(int reposts) {
        this.reposts = reposts;
    }
    public int getOrgrans() {
        return orgrans;
    }
    /**
     * 设置转发机构转发数
     * @param orgrans
     */
    public void setOrgrans(int orgrans) {
        this.orgrans = orgrans;
    }

    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
}
