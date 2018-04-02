package com.xunku.actions.portal;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.pojo.base.User;

/*
 * portal模块，修改用户信息方法
 * @author sunao
 */
public class UserSettingAction extends ActionBase{
	
	UserDao userDao = new UserDaoImpl();
	
	@Override
	public Object doAction() {
		String phone = this.get("phone");
		String email = this.get("email");
		String userPwd = this.get("userPwd");
		int userid = this.getUser().getBaseUser().getId();
		
		User info =new User();
		info.setId(userid);
		info.setTel(phone);
		info.setEmail(email);
		info.setToken(userPwd);
		userDao.updateByUid(info);
		return true;
	}
	
}
