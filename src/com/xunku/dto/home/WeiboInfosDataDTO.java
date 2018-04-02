package com.xunku.dto.home;

import com.xunku.dto.IDTO;

public class WeiboInfosDataDTO implements IDTO{
	private int statusCount; //微博数量
	private int transpond; //转发
	private int comment; //评论
	private int like; //粉丝
	private int attention; //关注

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
}
