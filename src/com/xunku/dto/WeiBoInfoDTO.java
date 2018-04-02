package com.xunku.dto;

public class WeiBoInfoDTO {
	private int id;
	private String userImage; //头像
	private String content; //内容
	private String [] images; //图片集合
	private String name; //昵称
	private String date; //发布时间 
	private int transpond; //转发数
	private int comment; //评论数
	private String sourceName;//来源
	private int accountid;
	private int rtranspond;
	private int rcomment;
	private String rsourceName;
	private String rdate;
	private String [] rimages;
	private String rcontent;
	
	
	public int getAccountid() {
		return accountid;
	}
	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}
	
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRtranspond() {
		return rtranspond;
	}
	public void setRtranspond(int rtranspond) {
		this.rtranspond = rtranspond;
	}
	public int getRcomment() {
		return rcomment;
	}
	public void setRcomment(int rcomment) {
		this.rcomment = rcomment;
	}
	public String getRsourceName() {
		return rsourceName;
	}
	public void setRsourceName(String rsourceName) {
		this.rsourceName = rsourceName;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public String[] getRimages() {
		return rimages;
	}
	public void setRimages(String[] rimages) {
		this.rimages = rimages;
	}
	public String getRcontent() {
		return rcontent;
	}
	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}
}
