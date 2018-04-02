package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Group;
import com.xunku.pojo.task.Rubbish;
import com.xunku.pojo.task.Task;
/*
 * SysManager_UpdateTaskAction 系统管理更新任务
 * @author: hjian
 */
public class SysManager_UpdateTaskAction extends ActionBase {

	@Override
	public Object doAction() {

		User _user = this.getUser().getBaseUser();
		TaskDao dao = new TaskDaoImpl();
		int method = Integer.parseInt(this.get("method"));
		
		//1，编辑一级菜单  2，编辑二级菜单，5编辑任务
		
		if(method == 1){
			//1级编辑
			String name = this.get("firstName");
			int id = Integer.parseInt(this.get("gid"));
			dao.updateParent(id, name);
		}else if(method == 2){
			int id = Integer.parseInt(this.get("id"));
			Group group = dao.queryGroupById(id);
			return group;
		}else if(method == 3){
			//2级编辑
			String name = this.get("firstName");
			int id = Integer.parseInt(this.get("gid"));
			int pid = Integer.parseInt(this.get("pid"));
			dao.updateChildren(id, name, pid);
		}else if(method == 5){
			
			int id = Integer.parseInt(this.get("gid"));
			int gId = Integer.parseInt(this.get("groupid"));
			String rubbishId = this.get("rubbishid");
			String name = this.get("taskname");
			String keywords = this.get("keywords");
			int searchtime = Integer.parseInt(this.get("searchtime"));
	
			Task task = new Task();
			task.setCreator(_user.getId());
			task.setSearchTime(searchtime);
			task.setCustomID(_user.getCustomID());
			task.setGroupID(gId);
			task.setKeywords(keywords);
			task.setName(name);
			task.setId(id);
			List<Rubbish> list = new ArrayList<Rubbish>();
			if (rubbishId != "" || rubbishId.length() != 0) {
				String[] str = rubbishId.split(",");
				
				for(int i=0;i<str.length;i++){
					Rubbish rh = new Rubbish();
					rh.setId(Integer.parseInt(str[i]));
					list.add(rh);
				}
			}
			task.setRubbishList(list);
			dao.updateTask(task);
		}
		return "true";
	}

}
