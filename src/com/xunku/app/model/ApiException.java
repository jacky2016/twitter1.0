package com.xunku.app.model;

import com.xunku.constant.MessageSina;

import weibo4j.model.WeiboException;

/**
 * API异常封装
 * 
 * @author wujian
 * @created on Jul 18, 2014 2:29:50 PM
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = 1237296705608172768L;

	private int statusCode = -1;
	private int errorCode = -1;
	private String request;
	private String error;

	public ApiException(WeiboException ex) {

		this.statusCode = ex.getStatusCode();

		String message = MessageSina.getMessage(ex);

		this.errorCode = ex.getErrorCode();
		this.error = message;// ex.getError();
		this.request = ex.getRequest();
	}

	public ApiException(String error, int errorCode) {

		this.error = MessageSina.getMessage(errorCode, error);
		;
		this.errorCode = errorCode;

	}

	public int getStatusCode() {
		return statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getRequest() {
		return request;
	}

	public String getError() {
		return error;
	}

}
