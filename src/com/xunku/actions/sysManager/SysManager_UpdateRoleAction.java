package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
/*
 * SysManager_UpdateRoleAction 系统管理更新角色信息
 * @author: hjian
 */
public class SysManager_UpdateRoleAction extends ActionBase {

	@Override
	public Object doAction() {

		String name = this.get("rolename");
		int id = Integer.parseInt(this.get("gid"));
		CustomRoleDao dao = new CustomRoleDaoImpl();
		dao.update(id, name);
		
		return "true";
	}

}
