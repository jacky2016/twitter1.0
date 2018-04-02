package com.xunku.dto.login;

import com.xunku.app.enums.LoginStatus;

public class LoginInfoDTO {
	public String message;// 登录消息
	public LoginStatus status;// 登录状态
	public int times;// 尝试登录的次数
}
