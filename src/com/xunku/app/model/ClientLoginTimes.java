package com.xunku.app.model;

/**
 * 客户端登录次数对象
 * 
 * @author wujian
 * @created on Sep 24, 2014 1:46:31 PM
 */
public class ClientLoginTimes {

	String clientId;
	int times;
	long expired;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public long getExpired() {
		return expired;
	}

	public void setExpired(long expired) {
		this.expired = expired;
	}

}
