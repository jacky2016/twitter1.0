package com.xunku.app.model.people;

/**
 * 人民微博对象
 * 
 * @author wujian
 * @created on Oct 21, 2014 4:19:31 PM
 */
public class Pweet {
	long id;
	String body;
	long postTime;
	String source;// 来源
	PUser user;// 微博的作者

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getPostTime() {
		return postTime;
	}

	public void setPostTime(long postTime) {
		this.postTime = postTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public PUser getUser() {
		return user;
	}

	public void setUser(PUser user) {
		this.user = user;
	}
}
