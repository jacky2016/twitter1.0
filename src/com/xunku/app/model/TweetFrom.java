package com.xunku.app.model;

/**
 * 微博来源对象
 * 
 * @author thriller
 * 
 */
public class TweetFrom {

	String name;
	String url;
	int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	static TweetFrom _empty;

	public static TweetFrom Empty() {
		if (_empty == null) {
			_empty = new TweetFrom();
			_empty.setId(0);
			_empty.setName("未知来源");
			_empty.setUrl("http://www.xunku.org/help?souorce=0");
		}
		return _empty;
	}

}
