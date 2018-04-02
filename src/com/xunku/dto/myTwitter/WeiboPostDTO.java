package com.xunku.dto.myTwitter;

public class WeiboPostDTO {
    private int id;
    private String tid;            //微博tid
    private String text;       //微博内容
    private String createTime; //微博发布日期
    private int repostCount;   //转发数
    private int commentCount;  //评论数
    private int orgCount;      //转发机构数
    private String ucode;
    private int platform;
    private String uid;
    public int getPlatform() {
        return platform;
    }
    public void setPlatform(int platform) {
        this.platform = platform;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public int getRepostCount() {
        return repostCount;
    }
    public void setRepostCount(int repostCount) {
        this.repostCount = repostCount;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public int getOrgCount() {
        return orgCount;
    }
    public void setOrgCount(int orgCount) {
        this.orgCount = orgCount;
    }
    public String getUcode() {
        return ucode;
    }
    public void setUcode(String ucode) {
        this.ucode = ucode;
    }
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public int getId() {
	    return id;
	}
	public void setId(int id) {
	    this.id = id;
	}

}
