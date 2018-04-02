package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.sysManager.SysManager_BaseUserDTO;
import com.xunku.pojo.base.CustomRole;
import com.xunku.pojo.base.Role;
import com.xunku.pojo.base.User;
/*
 * SysManager_UpdateBaseUserAction 系统管理维护用户信息
 * @author: hjian
 */
public class SysManager_UpdateBaseUserAction extends ActionBase {

	@Override
	public Object doAction() {
		
		int method = Integer.parseInt(this.get("method"));
		UserDao dao = new UserDaoImpl();
		User user = new User();
		User _user = this.getUser().getBaseUser();
		if(method == 1){
			
			int id = Integer.parseInt(this.get("gid"));
			String email = this.get("email");
			String nickName = this.get("nickName");
			String tel = this.get("tel");
			String userName = this.get("userName");
			//String password = this.get("password");
			int rid = Integer.parseInt(this.get("rid"));
	
			user.setId(id);
			user.setAdmin(false);
			user.setCustomID(_user.getCustomID());
			user.setEmail(email);
			user.setNickName(nickName);
			user.setTel(tel);
			user.setUserName(userName);
			//user.setToken(password);
			CustomRole role = new CustomRole();
			role.setId(rid);
			user.setRole(role);
			dao.updateByID(user);
			
			return "true";
		}else if(method == 2){
			
			int id = Integer.parseInt(this.get("id"));
			user = dao.queryByUserid(id);
			SysManager_BaseUserDTO sbu = new SysManager_BaseUserDTO();
			sbu.setNickname(user.getNickName());
			sbu.setMail(user.getEmail());
			sbu.setTele(user.getTel());
			sbu.setUsername(user.getUserName());
			sbu.setToken(user.getToken());
			sbu.setId(user.getId());
			if(user.getRole() != null){
				sbu.setRoleid(user.getRole().getId());
				sbu.setCustomid(user.getRole().getCustomId());
			}

			return sbu;
		}else if(method == 3){
			String name = this.get("username");
			boolean flag = dao.checkIsExsit(name);
			
			return flag;
		}
		
		return "true";
	}

}
