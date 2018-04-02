package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
/*
 * SysManager_DeleteBaseUserAction 系统管理删除用户
 * @author: hjian
 */
public class SysManager_DeleteBaseUserAction extends ActionBase {

	@Override
	public Object doAction() {

		UserDao dao = new UserDaoImpl();
		int id = Integer.parseInt(this.get("id"));
		dao.deleteByID(id);
		
		return "true";
	}

}
