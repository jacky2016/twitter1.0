package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
/*
 * SysManager_Move 系统管理一级菜单移动
 * @author: hjian
 */
public class SysManager_Move extends ActionBase {

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		int option = Integer.parseInt(this.get("option"));
		int gid = Integer.parseInt(this.get("gid"));
		TaskDao dao = new TaskDaoImpl();
		if(option == 1){
			//left
			dao.parentUp(gid);
		}else{
			//right
			dao.parentDown(gid);
		}
		return "hjian";
	}

}
