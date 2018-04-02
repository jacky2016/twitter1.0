package com.xunku.constant;

/**
 * 定义关于Session的常量
 * 
 * @author wujian
 * @created on Jul 7, 2014 11:28:20 AM
 */
public interface SessionCST {

	/**
	 * 记录登录用户
	 */
	public static final String USER_OBJECT = "User_object";

	/**
	 * 记录登录次数
	 */
	public static final String LOGIN_TIMES = "loginTimes";

	/**
	 * 登录需要的验证码
	 */
	public static final String LOGIN_VODE = "loginVcode";

	/**
	 * 登录的用户名
	 */
	public static final String LOGIN_USERNAME = "__username__";

	/**
	 * 登录用户的密码MD5
	 */
	public static final String LOGIN_TOKEN = "__token__";

	/**
	 * 两周
	 */
	public static final int TWO_WEEKS = 60 * 60 * 24 * 14;
	
	/**
	 * 客户端标识
	 */
	public final static String COOKIE_CLIENT_ID = "XUNKUID";
}
