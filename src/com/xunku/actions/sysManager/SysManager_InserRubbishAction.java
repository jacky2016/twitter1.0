package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskDao;
import com.xunku.dao.task.TaskRubbishDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.daoImpl.task.TaskRubbishDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Rubbish;
/*
 * SysManager_InserRubbishAction系统管理插入垃圾词
 * @author: hjian
 */
public class SysManager_InserRubbishAction extends ActionBase {

	@Override
	public Object doAction() {

		User _user = this.getUser().getBaseUser();
		TaskRubbishDao dao = new TaskRubbishDaoImpl();
		TaskDao tdao = new TaskDaoImpl();
		int method = Integer.parseInt(this.get("method"));
		
		if(method == 1){
			String name = this.get("name");
			String words = this.get("words").trim();
			Rubbish r = new Rubbish();
			r.setCustomID(_user.getCustomID());
			r.setGroupName(name);
			r.setRubbishWords(words);
			dao.insert(r);
			
			return "true";
		}else if(method == 2){
			int taskcount = dao.getRubbishGroupCount(_user.getCustomID());
			if(taskcount >= 20){
				return -1;
			}
		}else if(method == 3){//验证一级菜单重名
			String _name = this.get("name");
			boolean flag = tdao.pnameIsExsit(_name, _user.getCustomID());
			return flag;
		}else if(method == 4){
			String name = this.get("_name");
			int id = Integer.parseInt(this.get("id"));
			boolean flag = tdao.cnameIsExsit(name, id);
			return flag;
		}
		return "true";
	}

}
