package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.pojo.base.User;
/*
 * SysManager_CommonUidAction 系统管理公共获取UID
 * @author hjian
 */
public class SysManager_CommonUidAction extends ActionBase {

	@Override
	public Object doAction() {
		int uid = this.getUser().getBaseUser().getId();
		User _user = new UserDaoImpl().queryUserByIsAdmin(uid);
	
		return _user;
	}

}
