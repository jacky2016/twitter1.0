package com.xunku.dto.home;

public class WeiboInfosListItemDTO {
	private int id; 
	private String uid; //账号对应uid
	private int accountid; //账号ID
	private boolean closed; //是否关闭
	private boolean expand; //是否扩展
	private int type; //微博类型0:新浪，1:腾讯，2：人民
	private String name; //微博名称
	private int statusCount; //微博数量
	private int transpond; //转发
	private int comment; //评论
	private int like; //粉丝
	private int attention; //关注
	private boolean isExpire =false;//授权是否false:没到期，true:到期
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public boolean isExpand() {
		return expand;
	}
	public void setExpand(boolean expand) {
		this.expand = expand;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatusCount() {
		return statusCount;
	}
	public void setStatusCount(int statusCount) {
		this.statusCount = statusCount;
	}
	public int getTranspond() {
		return transpond;
	}
	public void setTranspond(int transpond) {
		this.transpond = transpond;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public int getAttention() {
		return attention;
	}
	public void setAttention(int attention) {
		this.attention = attention;
	}
	public int getAccountid() {
		return accountid;
	}
	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public boolean isExpire() {
		return isExpire;
	}
	public void setExpire(boolean isExpire) {
		this.isExpire = isExpire;
	}
}
