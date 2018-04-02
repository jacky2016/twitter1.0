package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.pushservices.PushUserDTO;
import com.xunku.dto.sysManager.UserApproveDTO;
import com.xunku.pojo.base.User;

/***
 * 获取用户列表集合
 *
 * @author shaoqun
 * @created Aug 4, 2014
 */
public class SysManager_UserApproveAction extends ActionBase {

	@Override
	public Object doAction() {
		
		User _user = this.getUser().getBaseUser();
		System.out.println(_user.getCustomID()+"---------------");
		List<User>  plist = new UserDaoImpl().queryUserByCid(_user.getCustomID());
		List<UserApproveDTO> list = new ArrayList<UserApproveDTO>();
		for(User push : plist){
			
			UserApproveDTO userApp = new UserApproveDTO();
			userApp.setId(push.getId());
			userApp.setUseName(push.getNickName());
			userApp.setEmail(push.getEmail());
			list.add(userApp);
		}
		return list;
	}

}
