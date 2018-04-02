package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.dto.sysManager.SysManager_TaskGroupDTO;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Group;
/*
 * SysManager_TaskGroup_InsertUpdateDelete 系统管理任务分组操作
 * @author: hjian
 */
public class SysManager_TaskGroup_InsertUpdateDelete extends ActionBase {

	@Override
	public Object doAction() {

		User _user = this.getUser().getBaseUser();
		int option = Integer.parseInt(this.get("option"));
		//option 十位1:1级，2：2级 个位0：删除 1：新增 2：更新
		TaskDao dao = new TaskDaoImpl();
		if(option == 11){
//			String name = this.get("firstName");
//			//1级新增
//			dao.insertParent(name, _user.getCustomID());	
		}else if(option == 10){
//			//1级删除
//			int pid = Integer.parseInt(this.get("pid"));
//			dao.deleteByParent(pid, _user.getCustomID());
		}else if(option == 20){
//			//2级删除
//			int cid = Integer.parseInt(this.get("pid"));
//			dao.deleteByChildren(cid, _user.getId());
		}else if(option == 21){
//			//2级新增
//			String name = this.get("firstName");
//			int parentID = Integer.parseInt(this.get("pid"));
//			Group group = new Group();
//			group.setCustomID(_user.getCustomID());
//			group.setGroupName(name);
//			group.setParentID(parentID);
//			dao.insertChildren(group);
		}else if(option == 12){
//			//1级编辑
//			String name = this.get("firstName");
//			int id = Integer.parseInt(this.get("gid"));
//			dao.updateParent(id, name);
			
		}else if(option == 22){
			//2级编辑
//			String name = this.get("firstName");
//			int id = Integer.parseInt(this.get("gid"));
//			int pid = Integer.parseInt(this.get("pid"));
//			dao.updateChildren(id, name, pid);
		}else if(option == 1002){
//			int id = Integer.parseInt(this.get("id"));
//			Group group = dao.queryGroupById(id);
//			return group;
		}else if(option == 1000){
//			String name = this.get("_name");
//			boolean flag = dao.pnameIsExsit(name, _user.getCustomID());
//			return flag;
		}else if(option == 1001){
//			String name = this.get("_name");
//			int id = Integer.parseInt(this.get("id"));
//			boolean flag = dao.cnameIsExsit(name, id);
//			return flag;
		}else if(option == 1003){
			
//			int taskcount = dao.getTaskByParent(_user.getCustomID());
//			if(taskcount >= 8){
//				return -1;
//			}
		}
		return "true";
	}

}
