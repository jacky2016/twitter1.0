package com.xunku.app.model;

import java.util.List;

import com.xunku.app.interfaces.ITweet;
/**
 * Post集合的封装器，返回一个总数
 * 
 * @author wujian
 * @created on Jul 16, 2014 6:35:06 PM
 */
public class PostsWrapper {
	
	List<ITweet> posts;
	long totalNumber;

	public PostsWrapper(List<ITweet> posts, long total) {
		this.posts = posts;
		this.totalNumber = total;
	}

	/**
	 * 获得当前包装器的微博集合
	 * @return
	 */
	public List<ITweet> getPosts() {
		return this.posts;
	}

	/**
	 * 获得当前结果集的总数，用来算页码的
	 * @return
	 */
	public long getTotal() {
		return this.totalNumber;
	}
}
