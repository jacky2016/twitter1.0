package com.xunku.app.model;

import com.xunku.app.enums.AppType;
import com.xunku.app.enums.Platform;

/**
 * 应用程序描述
 * <p>
 * 描述微博应用程序的基本信息
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:42:42 PM
 */
public class App {

	String name;
	String key;
	String secret;
	Platform platform;
	AppType type;
	String callbackUrl;
	int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public AppType getType() {
		return type;
	}

	public void setType(AppType type) {
		this.type = type;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("ID: " + this.id + "\n");
		buf.append("Name: " + this.name + "\n");
		buf.append("Platform: " + this.platform + "\n");
		buf.append("Type: " + this.type + "\n");
		buf.append("App Key: " + this.key + "\n");
		buf.append("App Secret: " + this.secret + "\n");
		buf.append("Callback: " + this.callbackUrl + "\n");
		return buf.toString();
	}
}
