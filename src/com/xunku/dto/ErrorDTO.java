package com.xunku.dto;

/*
 * 错误信息DTO
 * @author sunao
 */
public class ErrorDTO {
	public String err;
	
	/**
	 * 1= 用户信息无效，需要重新登录
	 */
	public int code=0;
}
