package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.sysManager.SysManager_BaseUserDTO;
import com.xunku.pojo.base.CustomRole;
import com.xunku.pojo.base.Role;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;
/*
 * SysManager_InsertBaseUserAction 系统管理新增基本用户
 * @author: hjian
 */
public class SysManager_InsertBaseUserAction extends ActionBase {

	@Override
	public Object doAction() {
		
		UserDao dao = new UserDaoImpl();
		User user = new User();
		User _user = this.getUser().getBaseUser();
		
		int method = Integer.parseInt(this.get("method"));
		
		if(method == 1){
			String email = this.get("email");
			String nickName = this.get("nickName");
			String tel = this.get("tel");
			String userName = this.get("userName");
			int rid = Integer.parseInt(this.get("rid"));
			String rname = this.get("rname");
			String password = this.get("password");
				
			user.setAdmin(false);
			user.setCustomID(_user.getCustomID());
			user.setEmail(email);
			user.setNickName(nickName);
			user.setTel(tel);
			user.setUserName(userName);
			user.setToken(password);
			CustomRole role = new CustomRole();
			role.setId(rid);
			role.setName(rname);
			user.setRole(role);
			dao.insert(user);
			return "true";
		}else if(method == 2){
			PermissionController controll = new PermissionController(this.getUser());
			int maxCount = controll.GetCustomConfigValue(this.getUser(),
					PortalCST.weibo_account_childs);
			int usercount = dao.getUserByCustomid(_user.getCustomID());   //取得绑定帐号数量
			if(usercount >= maxCount){
				return -1;
			}
		}
		
		return "true";
	}

}
