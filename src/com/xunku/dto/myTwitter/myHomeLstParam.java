package com.xunku.dto.myTwitter;

public class myHomeLstParam {
	private int lstType; //0全部，1原创，2图片
	private int twitterType; //0新浪，1腾讯，2人民
	private int keyword; //搜索信息
	private int pageSize; //每页条数
	private int pageIndex; //页索引
	public int getLstType() {
		return lstType;
	}
	public void setLstType(int lstType) {
		this.lstType = lstType;
	}
	public int getTwitterType() {
		return twitterType;
	}
	public void setTwitterType(int twitterType) {
		this.twitterType = twitterType;
	}
	public int getKeyword() {
		return keyword;
	}
	public void setKeyword(int keyword) {
		this.keyword = keyword;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
}
