package com.xunku.dto.office;
//考核管理 微博处理统计by wanghui
public class PostDealDTO {
    private int id;
    private String useName;
    private String uid;
    private String ucode;
    private int reposts;
    private int comments;
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
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
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUseName() {
        return useName;
    }
    public void setUseName(String useName) {
        this.useName = useName;
    }
}
