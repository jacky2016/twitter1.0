package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.pojo.base.User;
/*
 * SysManager_InsertRoleAction 系统管理插入角色
 * @author: hjian
 */
public class SysManager_InsertRoleAction extends ActionBase {

	@Override
	public Object doAction() {
		
		User _user = this.getUser().getBaseUser();
		String name = this.get("rolename");
		int method = Integer.parseInt(this.get("mthod"));
		CustomRoleDao dao = new CustomRoleDaoImpl();
		
		if(method == 1){
			dao.insert(name, _user.getCustomID());
			return "true";
		}else if(method == 2){
			boolean flag = dao.checkIsExsit(_user.getCustomID(), name);
			return flag;
		}
		
		return "true";
	}

}
