package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.dto.sysManager.SysManager_CustomRoleDTO;
import com.xunku.pojo.base.CustomRole;
import com.xunku.pojo.base.User;
/*
 * SysManager_GetCustomRoles 系统管理获取客户角色
 * @author: hjian
 */
public class SysManager_GetCustomRoles extends ActionBase {

	@Override
	public Object doAction() {
		User _user = this.getUser().getBaseUser();
		CustomRoleDao dao = new CustomRoleDaoImpl();
		List<CustomRole> clist = dao.queryByUid(_user.getCustomID());
		List<SysManager_CustomRoleDTO> lst = new ArrayList<SysManager_CustomRoleDTO>();
		for(CustomRole role : clist){
			SysManager_CustomRoleDTO dto = new SysManager_CustomRoleDTO();
			dto.setId(role.getId());
			dto.setName(role.getName());
			dto.setDescription(role.getName());
			lst.add(dto);
		}

		return lst;
	}

}
