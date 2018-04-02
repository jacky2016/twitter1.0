package com.xunku.app.enums;

/**
 * 登录状态
 * 
 * @author wujian
 * @created on Jul 7, 2014 10:29:19 AM
 */
public enum LoginStatus {

	/**
	 * 成功
	 */
	SUCESS,
	/**
	 * 密码错误
	 */
	ERROR_PASSWORD,
	/**
	 * 验证码失败
	 */
	ERROR_VCODE,
	/**
	 * 客户无效
	 */
	ERROR_CUSTOM_INVALID,

	/**
	 * 客户已过期
	 */
	ERROR_CUSTOM_EXPIRED,
	/**
	 * 太多的用户在线
	 */
	ERROR_USER_TOOMANY_ONLINE,

	/**
	 * 用户被锁定
	 */
	ERROR_USER_LOCKED,
	/**
	 * 未知错误
	 */
	ERROE_UNKNOW,

	/**
	 * 内部错误
	 */
	ERROE_INNER,
	/**
	 * 用户不存在
	 */
	USER_NOT_EXISTS,
	/**
	 * 尝试次数太多
	 */
	TRY_TIMES_OVERLOAD

}
