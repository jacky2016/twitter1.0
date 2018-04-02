package com.xunku.app.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.xunku.app.helpers.DateHelper;
import com.xunku.app.manager.UserSessionManager;

/**
 * 用户的会话描述信息
 * <p>
 * 该会话绑定了一个HttpSession
 * <p>
 * 每个Client对应一个Session，每个Session对应一个User
 * 
 * @author wujian
 * @created on Jul 10, 2014 12:06:52 PM
 */
public class UserSession {

	int timeout = 20 * 60 * 1000;// 20分钟过期

	String ip;// 登录ip
	String userAgent;// 登录的浏览器
	long loginTime;// 登录时间 --登录成功的时间
	long created;// 会话创建时间
	UserInfo user;
	boolean isLogined;// 当前会话是否是已经登录的会话
	String vcode;// 登录验证码

	int loginTimes;// 登录次数

	String clientID;// 客户端标识 sessionID的实现

	Map<String, Object> _attributes;

	UserSessionManager _manager;

	protected long creationTime = 0L;
	protected volatile long thisAccessedTime = creationTime;
	protected volatile long lastAccessedTime = creationTime;
	protected transient AtomicInteger accessCount = null;// 这个Session访问的次数
	protected volatile boolean isValid = false; // session是否有效

	/**
	 * 移除当前会话
	 */
	public void invalidate() {
		this.isLogined = false;
		this.loginTimes = 0;
		this._attributes.clear();
		this.ip = null;
		this.userAgent = null;
		this.vcode = null;
		this.user = null;
		this.loginTime = 0L;
		this.created = 0L;
		this.isValid = false;
		this._manager.remove(this);
		// 这个必须在后面移除
		this.clientID = null;
	}

	public UserSession(UserSessionManager manager) {
		this._attributes = new HashMap<String, Object>();
		this._manager = manager;
		this.isValid = true;
		this.accessCount = new AtomicInteger();
		this.timeout = manager.getExpried();
	}

	public Object getAttribute(String name) {
		return this._attributes.get(name);
	}

	public void setAttribute(String name, Object value) {
		if (value != null)
			this._attributes.put(name, value);
	}

	public String getIp() {
		return ip;
	}

	/**
	 * 判断当前的会话是否超时了
	 * 
	 * @return
	 */
	public boolean isExpire() {
		return (this.lastAccessedTime + timeout) < System.currentTimeMillis();
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public int getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public void access() {
		this.lastAccessedTime = System.currentTimeMillis();
		accessCount.incrementAndGet();
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{id: " + this.clientID);
		buf
				.append(",created: "
						+ DateHelper.formatDBTime(new Date(this.created)));
		buf.append(",timeout: " + this.timeout);
		buf.append(",lastAccess: "
				+ DateHelper.formatDBTime(new Date(this.lastAccessedTime)) + "}");
		return buf.toString();
	}
}
