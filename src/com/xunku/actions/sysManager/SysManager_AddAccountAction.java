package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AccountDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;

/**
 * 添加帐号
 *
 * @author shaoqun
 * @created Aug 12, 2014
 */
public class SysManager_AddAccountAction extends ActionBase{

	@Override
	public Object doAction() {
		
		int method = Integer.parseInt(this.get("method"));
		
		User _user = this.getUser().getBaseUser();
		AccountDao accDao = new AccountDaoImpl();
		
		if(method == 1){
			PermissionController controll = new PermissionController(this.getUser());
			int maxCount = controll.GetCustomConfigValue(this.getUser(),
					PortalCST.weibo_account_binds);
			int taskcount = accDao.queryCountByCustomid(_user.getCustomID());    //取得绑定帐号数量
	
			if(taskcount >= maxCount){
				return -1;
			}
		}
		
		return "true";
	}

}
