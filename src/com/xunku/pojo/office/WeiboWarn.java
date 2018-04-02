package com.xunku.pojo.office;

import com.xunku.app.enums.Platform;

/**
 * 功能描述：微博预警
 * <p>
 * 
 * 如果设置转发和评论，则当转发“或”评论超过阀值时发生预警，预警任务只有当两个值都超过阀值停止任务。如果没有发生预警，则任务在一周后停止。
 * 
 * @author wanghui
 * @see 对应表Office_Weibo_Warning
 */
public class WeiboWarn extends BaseWarn {
	private int id;
	private String tid;// 微博id
	private Platform platform;// 平台
	private int comment;// 评论达到XX条预警
	private int repost; // 转发达到XX条预警

	private boolean isDelete; // 1(true) 表示 已经发生预警的已经假删除了
	boolean commentHappen; // 评论预警是否发生了
	boolean repostHappen; // 转发预警是否发生了

	private long time;// 结束时间
	String author; // 微博作者昵称
	String text;// 微博内容
 
	String endTime;// 预警的结束时间
	private   int   rHappen; // 转发发生  0 或 null 为启动  1 为关闭
	private   int   cHappen;  //评论发生  0 或 null 为启动  1 为关闭
	
	
	private  int currepost;//当前已经转发的数量
	private  int curcomment;// 当前已经评论的数量

	public int getCurrepost() {
		return currepost;
	}

	public void setCurrepost(int currepost) {
		this.currepost = currepost;
	}

	public int getCurcomment() {
		return curcomment;
	}

	public void setCurcomment(int curcomment) {
		this.curcomment = curcomment;
	}

	public WeiboWarn() {

	}

	/**
	 * 获得结束时间
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}

	/**
	 * 设置结束时间
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public int getRepost() {
		return repost;
	}

	public void setRepost(int repost) {
		this.repost = repost;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isCommentHappen() {
		return commentHappen;
	}

	public void setCommentHappen(boolean commentHappen) {
		this.commentHappen = commentHappen;
	}

	public boolean isRepostHappen() {
		return repostHappen;
	}

	public void setRepostHappen(boolean repostHappen) {
		this.repostHappen = repostHappen;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getRHappen() {
		return rHappen;
	}

	public void setRHappen(int happen) {
		rHappen = happen;
	}

	public int getCHappen() {
		return cHappen;
	}

	public void setCHappen(int happen) {
		cHappen = happen;
	}

}
