package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.pojo.base.User;
/*
 * SysManager_DeleteTaskAction 系统管理删除任务
 * @author: hjian
 */
public class SysManager_DeleteTaskAction extends ActionBase {

	@Override
	public Object doAction() {
		
		User _user = this.getUser().getBaseUser();
		TaskDao dao = new TaskDaoImpl();
		int method = Integer.parseInt(this.get("method"));
		
		if(method == 1){
			//1级删除
			int pid = Integer.parseInt(this.get("pid"));
			dao.deleteByParent(pid, _user.getCustomID());
		}else if(method == 2){
			//2级删除
			int cid = Integer.parseInt(this.get("pid"));
			dao.deleteByChildren(cid, _user.getId());
		}else if(method == 3){
			int id = Integer.parseInt(this.get("gid"));
			dao.deleteByTask(id);
		}
		return "true";
	}

}
