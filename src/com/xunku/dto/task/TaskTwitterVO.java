package com.xunku.dto.task;

import java.util.ArrayList;
import java.util.List;

public class TaskTwitterVO {
	private String author; // 作者
	private String authorcode;// 作者code
	private int comtcount; // 评论数
	private String content; // 内容
	private String contentid; // 相似数id
	private int numofsameContent; // 相似数
	private String publishdate; // 发布时间
	private String refcontent; // 引用原微博的内容
	private int replycount; // 转发次数
	private int sentiment; // 正负面,搜索引擎返回的数字是大于0的是负面新闻，其余都是正面
	private int sourceID; // 发布源（媒体平台）
	private int appsource; // 发布来源,通过缓存中提取数据
	private String url; // 原始链接
	private String transferurl = "";// 如果不为空字符串，是转发；为空字符串 原创
	private String gender;  //性别（m 男，f 女，空 未知）
	private String address; //地址
	private int verify;   //是否认证（0未认证，1认证）
	private List<String> imgurl = new ArrayList<String>();

	public String getGender() {
	    return gender;
	}

	public void setGender(String gender) {
	    this.gender = gender;
	}

	public String getAddress() {
	    return address;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	public int getVerify() {
	    return verify;
	}

	public void setVerify(int verify) {
	    this.verify = verify;
	}

	public String getHead() {
		if(imgurl.size()!=0){
			return imgurl.get(0);
		}
		return null;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getComtcount() {
		return comtcount;
	}

	public void setComtcount(int comtcount) {
		this.comtcount = comtcount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public int getNumofsameContent() {
		return numofsameContent;
	}

	public void setNumofsameContent(int numofsameContent) {
		this.numofsameContent = numofsameContent;
	}

	public String getPublishdate() {
		return publishdate;
	}

	public void setPublishdate(String publishdate) {
		this.publishdate = publishdate;
	}

	public String getRefcontent() {
		return refcontent;
	}

	public void setRefcontent(String refcontent) {
		this.refcontent = refcontent;
	}

	public int getReplycount() {
		return replycount;
	}

	public void setReplycount(int replycount) {
		this.replycount = replycount;
	}

	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public int getSourceID() {
		return sourceID;
	}

	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getImgurl() {
		return imgurl;
	}

	public void setImgurl(List<String> imgurl) {
		this.imgurl = imgurl;
	}

	public String getTransferurl() {
		return transferurl;
	}

	public void setTransferurl(String transferurl) {
		this.transferurl = transferurl;
	}

	public String getAuthorcode() {
		return authorcode;
	}

	public void setAuthorcode(String authorcode) {
		this.authorcode = authorcode;
	}

	public int getAppsource() {
		return appsource;
	}

	public void setAppsource(int appsource) {
		this.appsource = appsource;
	}
}
