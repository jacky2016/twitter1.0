package com.xunku.app.result;

/**
 * 发布统计计数结果
 * 
 * @author wujian
 * @created on Aug 27, 2014 6:36:10 PM
 */
public class PublishCountResult {

	int retweets;
	int tweets;
	int comments;

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getRetweets() {
		return retweets;
	}

	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}

	public int getTweets() {
		return tweets;
	}

	public void setTweets(int tweets) {
		this.tweets = tweets;
	}

}
