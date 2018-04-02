package com.xunku.app.crawl;

public interface CrawlerType {
	/**
	 * 关注
	 */
	int following = 1;
	/**
	 * 粉丝
	 */
	int follower = 2;
	/**
	 * 微博
	 */
	int tweet = 3;
	// 1=关注；2=粉丝；3=微博；
}
