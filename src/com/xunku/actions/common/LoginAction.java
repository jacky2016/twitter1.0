package com.xunku.actions.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.model.UserInfo;
import com.xunku.dto.IDTO;
import com.xunku.dto.login.LoginStatusDTO;
import com.xunku.pojo.base.User;
public class LoginAction extends ActionBase {

	private static final String USERNAME = "a";
	private static final String PASSWORD = "pwd";
	private static final String VCODE = "vcode";

	public LoginAction() {
	}

	public boolean Login(){
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LoginStatusDTO doAction() {
		Map<String, String> params = this.getParameters();

		String userName = params.get(USERNAME);
		
		if(this.Login()){
			
		}
		else{
			
		}

		LoginStatusDTO status = new LoginStatusDTO();

		UserInfo user1 = this.getUser();

		status.message = "登录成功!";
		status.username = user1.getBaseUser().getUserName();
		status.status = 1;
		status.Attributes = new Integer[10];
		for (int i = 0; i < 10; i++) {
			status.Attributes[i] = i;
		}

		status.Atts = new HashMap<String, Integer>();
		for (int i = 0; i < 10; i++) {
			status.Atts.put(String.valueOf(i), i);
		}
		return status;

		// return "{\"ID\":1, \"UserName\":\"SunAo\"}";
	}

}
