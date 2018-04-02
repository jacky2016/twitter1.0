package com.xunku.dto.myTwitter;

import com.xunku.dto.IDTO;

public class myHomePageListDTO implements IDTO{
	private int iD;
	private String userImage; //头像
	private String content; //内容
	private String [] images; //图片集合
	private String name; //昵称
	private String date; //发布时间 
	private int transpond; //转发数
	private int comment; //评论数
	private String sourceName;//来源
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public int getID() {
		return iD;
	}
	public void setID(int id) {
		iD = id;
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
}
