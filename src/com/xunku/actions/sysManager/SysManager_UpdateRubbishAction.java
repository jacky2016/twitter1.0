package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskRubbishDao;
import com.xunku.daoImpl.task.TaskRubbishDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Rubbish;
/*
 * SysManager_UpdateRubbishAction 系统管理更新垃圾词信息
 * @author: hjian
 */
public class SysManager_UpdateRubbishAction extends ActionBase {

	@Override
	public Object doAction() {

		User _user = this.getUser().getBaseUser();
		TaskRubbishDao dao = new TaskRubbishDaoImpl();
		int method = Integer.parseInt(this.get("method"));
		
		if(method == 1){
			String name = this.get("name");
			String words = this.get("words").trim();
			int gid = Integer.parseInt(this.get("gid"));
			Rubbish r = new Rubbish();
			r.setId(gid);
			r.setCustomID(_user.getCustomID());
			r.setGroupName(name);
			r.setRubbishWords(words);
			dao.update(r);
			
			return "true";
		
		}else if(method == 2){
			String name = this.get("name");
			boolean flag = dao.checkIsExsit(_user.getCustomID(),name);
			
			return flag;
		}
		return "true";
	}

}
