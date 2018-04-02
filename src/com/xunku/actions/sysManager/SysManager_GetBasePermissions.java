package com.xunku.actions.sysManager;

import java.util.List;
import java.util.ArrayList;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.SysManagerController;
import com.xunku.app.model.UserInfo;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.dao.base.ModuleDao;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.daoImpl.base.ModuleDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.sysManager.CustomPermissionInRoleDTO;
import com.xunku.dto.sysManager.ModuleCode;
import com.xunku.dto.sysManager.GroupDTO;
import com.xunku.dto.sysManager.ModuleGroupDTO;
import com.xunku.dto.sysManager.UIElementCode;
import com.xunku.pojo.base.BaseModule;
import com.xunku.pojo.base.PermissionInRole;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;
/*
 * SysManager_GetBasePermissions 系统管理获取当前用户基本权限集合
 * @author: hjian
 */
public class SysManager_GetBasePermissions extends ActionBase {

	@SuppressWarnings("unchecked")
	@Override
	public Object doAction() { 
		UserInfo user = this.getUser();
		
		if(user.isAdmin()){		
			List<ModuleGroupDTO> lst = SysManagerController.GetAdminAuth(user);
			return lst;
		}else{
			int roleid = Integer.valueOf(this.get("roleid")); 
			//取到角色上admin分配给user的
			//通过权限接口 过滤 -- UI
			CustomRoleDao dao = new CustomRoleDaoImpl(); 
			List<PermissionInRole> rolelist = dao.queryByRoleID(roleid);
			
			User _user = new UserDaoImpl().queryAdmin(user.getBaseUser().getCustomID());
			UserInfo userNew = new UserInfo(_user);//初始一个新的adminuser
			PermissionController controller = new PermissionController(userNew);
			List<CustomPermissionInRoleDTO> list = new ArrayList<CustomPermissionInRoleDTO>();

			for(PermissionInRole per:rolelist){
				CustomPermissionInRoleDTO dto = new CustomPermissionInRoleDTO();
				boolean flag = controller.HasMenuAuthority(userNew, per.getMcode());
				if(flag){
					dto.setMcode(per.getMcode());
				}
				boolean flag2 = controller.HasMenuAuthority(userNew, per.getCode());
				if(flag2){
					dto.setCode(per.getCode());
				}
				boolean flag3 = controller.HasUIAuthority(userNew, per.getUicode());
				if(flag3){
					dto.setUicode(per.getUicode());
				}
				list.add(dto);
			}
			List<ModuleGroupDTO> lst = SysManagerController.Convert(list,user);
			return lst;
		}
		
	}

}
