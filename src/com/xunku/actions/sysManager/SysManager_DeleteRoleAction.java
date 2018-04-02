package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.pojo.base.User;

/*
 * SysManager_DeleteRoleAction 系统管理删除角色
 * @author: hjian
 */
public class SysManager_DeleteRoleAction extends ActionBase {

	@Override
	public Object doAction() {
		
		int method = Integer.parseInt(this.get("method"));	
		User _user = this.getUser().getBaseUser();
		CustomRoleDao dao = new CustomRoleDaoImpl();
		int id = Integer.parseInt(this.get("gid"));
		if(method == 1){
			dao.deleteByRid(id);
			return "true";
		}else if(method == 2){
			boolean flag = dao.checkRoleIsUser(_user.getCustomID(),id);
			return flag;
		}
		return "true";
	}

}
