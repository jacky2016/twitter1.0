package com.xunku.app.model;

import weibo4j.org.json.JSONException;

/**
 * 应用程序异常信息
 * @author wujian
 * @created on Jun 5, 2014 3:46:00 PM
 */
public class AppException extends Exception {
	
	private static final long serialVersionUID = 862870379088833349L;
	
	public AppException(String msg) {
		super(msg);
	}

	public AppException(Exception cause) {
		super(cause);
	}

	public AppException(String msg, JSONException je) {

	}
}
