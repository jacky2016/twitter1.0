package com.xunku.actions.portal;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;

/*
 * portal模块，判断用户输入密码是否正确方法
 * @author sunao
 */
public class UserCheckPwdAction extends ActionBase {

	UserDao userDao = new UserDaoImpl();
	
	@Override
	public Boolean doAction() {
		String userPwd = this.get("userPwd");
		int userid = this.getUser().getBaseUser().getId();
		return userDao.checkPWD(userid, userPwd);
	}

}
