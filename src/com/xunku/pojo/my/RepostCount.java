package com.xunku.pojo.my;

import java.util.Date;
//数据分析中趋势分析 by wanghui
public class RepostCount {
    private int id;
    private String ucode;//ucode
    private int reposts; //转发数
    private int comments;//评论数
    private Date create;//采样时间
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUcode() {
        return ucode;
    }
    public void setUcode(String ucode) {
        this.ucode = ucode;
    }
    public int getReposts() {
        return reposts;
    }
    public void setReposts(int reposts) {
        this.reposts = reposts;
    }
    public int getComments() {
        return comments;
    }
    public void setComments(int comments) {
        this.comments = comments;
    }
    public Date getCreate() {
        return create;
    }
    public void setCreate(Date create) {
        this.create = create;
    }
}
