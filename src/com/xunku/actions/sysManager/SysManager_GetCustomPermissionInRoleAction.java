package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xunku.actions.ActionBase;
import com.xunku.app.model.UserInfo;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.sysManager.CustomPermissionInRoleDTO;
import com.xunku.pojo.base.PermissionInRole;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;
/*
 * SysManager_GetCustomPermissionInRoleAction 系统管理当前角色的权限
 * @author: hjian
 */
public class SysManager_GetCustomPermissionInRoleAction extends ActionBase {

	@Override
	public Object doAction() {
		
		int roleid = Integer.valueOf(this.get("roleid"));
		int customID = this.getUser().getBaseUser().getCustomID();
		CustomRoleDao dao = new CustomRoleDaoImpl();

		List<PermissionInRole> rolelist = dao.queryByRoleID(roleid);
		List<CustomPermissionInRoleDTO> list = new ArrayList<CustomPermissionInRoleDTO>();
		//UserInfo user = this.getUser();
		User _user = new UserDaoImpl().queryAdmin(customID);
		UserInfo user = new UserInfo(_user);//初始一个新的adminuser
		PermissionController controller = new PermissionController(user);
		
		for(PermissionInRole per : rolelist){
			CustomPermissionInRoleDTO dto = new CustomPermissionInRoleDTO();
			boolean flag = controller.HasMenuAuthority(user, per.getMcode());
			if(flag){
				dto.setMcode(per.getMcode());
			}
			boolean flag2 = controller.HasMenuAuthority(user, per.getCode());
			if(flag2){
				dto.setCode(per.getCode());
			}
			boolean flag3 = controller.HasUIAuthority(user, per.getUicode());
			if(flag3){
				dto.setUicode(per.getUicode());
			}
			list.add(dto);
		}
		
		return list;
	}

}
