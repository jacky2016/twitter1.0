package com.xunku.app.model;

/**
 * 提到我的封装器
 * @author wujian
 * @created on Jul 16, 2014 6:54:38 PM
 */
public class MentionWrapper {
	PostsWrapper _posts;
	PostsWrapper _comments;

	public MentionWrapper(PostsWrapper posts, PostsWrapper comments) {
		this._posts = posts;
		this._comments = comments;
	}

	/**
	 * 获得发布转发里面提到我的
	 * 
	 * @return
	 */
	public PostsWrapper getPosts() {
		return this._posts;
	}

	/**
	 * 获得评论里提到我的
	 * 
	 * @return
	 */
	public PostsWrapper getComments() {
		return this._comments;
	}
}
