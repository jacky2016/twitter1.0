package com.xunku.app.model;

import com.xunku.app.Utility;
import com.xunku.app.enums.LoginStatus;

/**
 * 
 * 登录结果描述
 * 
 * @author wujian
 * @created on Jul 7, 2014 10:26:52 AM
 */
public class LoginResult {

	String message;// 登录消息
	LoginStatus status;// 登录状态
	UserInfo userInfo;// 登录用户
	int times;// 尝试登录的次数

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LoginStatus getStatus() {
		return status;
	}

	public void setStatus(LoginStatus status) {
		this.status = status;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String toJSON() {
		return Utility.toJSON(this);
	}

	/**
	 * 有异常。。。
	 * 
	 * @param ex
	 * @return
	 */
	public static LoginResult getErrException(Exception ex) {

		LoginResult result = new LoginResult();
		result.message = ex.getMessage();
		result.status = LoginStatus.ERROE_INNER;
		result.userInfo = null;
		return result;

	}

	/**
	 * 用户名密码错误
	 * 
	 * @return
	 */
	public static LoginResult getErrUserName() {
		LoginResult result = new LoginResult();
		result.message = "用户名不存在或者密码错误";
		result.status = LoginStatus.USER_NOT_EXISTS;
		result.userInfo = null;
		return result;
	}

	public static LoginResult get(UserInfo info) {
		LoginResult result = new LoginResult();
		result.message = "登录成功";
		result.status = LoginStatus.SUCESS;
		result.userInfo = info;
		return result;
	}

	/**
	 * 验证码错误
	 * 
	 * @return
	 */
	public static LoginResult getErrVode() {
		LoginResult result = new LoginResult();
		result.message = "验证码错误";
		result.status = LoginStatus.ERROR_VCODE;
		result.userInfo = null;
		return result;
	}

	/**
	 * 帐号被锁定
	 * 
	 * @return
	 */
	public static LoginResult getUserLocked() {
		LoginResult result = new LoginResult();
		result.message = "用户已经被锁定";
		result.status = LoginStatus.ERROR_USER_LOCKED;
		result.userInfo = null;
		return result;
	}

	/**
	 * 客户无效
	 * 
	 * @return
	 */
	public static LoginResult getCustomInvaild() {
		LoginResult result = new LoginResult();
		result.message = "客户无效";
		result.status = LoginStatus.ERROR_CUSTOM_INVALID;
		result.userInfo = null;
		return result;
	}

	/**
	 * 客户已经过期
	 * 
	 * @return
	 */
	public static LoginResult getCustomExpired() {
		LoginResult result = new LoginResult();
		result.message = "客户已经过期";
		result.status = LoginStatus.ERROR_CUSTOM_EXPIRED;
		result.userInfo = null;
		return result;
	}

	public static LoginResult getTooManyOnline() {
		LoginResult result = new LoginResult();
		result.message = "该用户在线太多，请退出后再试，或者联系管理员。";
		result.status = LoginStatus.ERROR_CUSTOM_EXPIRED;
		result.userInfo = null;
		return result;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
