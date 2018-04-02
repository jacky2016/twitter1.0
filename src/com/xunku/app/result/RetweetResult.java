package com.xunku.app.result;

/**
 * 转发/原创/评论数量
 * @author wujian
 * @created on Jul 21, 2014 7:04:32 PM
 */
public class RetweetResult {

	int retweetCnt;
	int tweetCnt;
	int commentCnt;
	
	public int getRetweetCnt() {
		return retweetCnt;
	}
	public void setRetweetCnt(int retweetCnt) {
		this.retweetCnt = retweetCnt;
	}
	public int getTweetCnt() {
		return tweetCnt;
	}
	public void setTweetCnt(int tweetCnt) {
		this.tweetCnt = tweetCnt;
	}
	public int getCommentCnt() {
		return commentCnt;
	}
	public void setCommentCnt(int commentCnt) {
		this.commentCnt = commentCnt;
	}
}
