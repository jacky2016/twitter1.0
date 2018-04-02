package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.pushservices.PushUserDTO;

/*
 * SysManager_GetCustomListByApproveAction 系统管理根据当前审核人获取用户列表
 * @author: hjian
 */
public class SysManager_GetCustomListByApproveAction extends ActionBase {

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		int uid = this.getUser().getBaseUser().getId();
		UserDao dao = new UserDaoImpl();
		List<PushUserDTO> lst =dao.queryUserByUID(uid);
		return lst;
	}

}
