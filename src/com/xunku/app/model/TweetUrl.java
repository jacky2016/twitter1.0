package com.xunku.app.model;

import com.xunku.app.enums.Platform;

/**
 * 推特的Url描述信息
 * 
 * @author wujian
 * @created on Jun 25, 2014 6:02:56 PM
 */
public class TweetUrl {
	String url;
	String tid;
	Platform platform;
	String ucode;

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public TweetUrl(String url) {
		this.url = url;

	}
	
	public String getUrl(){
		return this.url;
	}

	public String getTid() {
		return this.tid;
	}

	public Platform getPlatform() {
		return this.platform;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	
}
