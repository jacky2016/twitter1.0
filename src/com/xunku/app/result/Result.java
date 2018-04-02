package com.xunku.app.result;

import com.xunku.app.enums.XKErrorCST;

/**
 * 结果基类
 * 
 * @author wujian
 * @created on Jul 16, 2014 4:54:00 PM
 */
public class Result<T> {

	String message = "sucess";// 返回结果的消息
	int errCode = 0; // 返回错误代码 0 为没有错误

	T _data;

	public Result() {
		this.errCode = 0;
	}

	public T getData() {
		return this._data;
	}

	public void setData(T data) {
		this._data = data;
	}

	/**
	 * 获得返回结果的消息
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置返回结果的消息
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 获得错误代码
	 * 
	 * @return
	 */
	public int getErrCode() {
		return errCode;
	}

	/**
	 * 设置错误代码
	 * 
	 * @param errCode
	 */
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public void setError(int errCode, String errMsg) {
		this.errCode = errCode;
		this.message = errMsg;
	}

	public void setError(String errMsg) {
		this.errCode = XKErrorCST.UNKNOW_ERROR_CODE;
		this.message = errMsg;
	}

}
