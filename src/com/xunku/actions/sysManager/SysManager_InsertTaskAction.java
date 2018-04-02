package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.constant.PortalCST;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Group;
import com.xunku.pojo.task.Task;
import com.xunku.portal.controller.PermissionController;
/*
 * SysManager_InsertTaskAction 系统管理添加任务
 * @author: hjian
 */
public class SysManager_InsertTaskAction extends ActionBase {

	@Override
	public Object doAction() {

		User _user = this.getUser().getBaseUser();
		TaskDao dao = new TaskDaoImpl();
		int method = Integer.parseInt(this.get("method"));
		
		//method：2，新建一级菜单。3，新建二级菜单。5，新建二级子菜单
		
		if(method == 1){
			int taskcount = dao.getTaskByParent(_user.getCustomID());
			if(taskcount >= 8){
				return -1;
			}
		}else if(method == 2){
			String name = this.get("firstName");
			//1级新增
			dao.insertParent(name, _user.getCustomID());
		}else if(method == 3){
			//2级新增
			String name = this.get("firstName");
			int parentID = Integer.parseInt(this.get("pid"));
			Group group = new Group();
			group.setCustomID(_user.getCustomID());
			group.setGroupName(name);
			group.setParentID(parentID);
			dao.insertChildren(group);
		}else if(method == 4){
			PermissionController controll = new PermissionController(this.getUser());
			int maxCount = controll.GetCustomConfigValue(this.getUser(),
					PortalCST.weibo_task_count);
			int taskcount = dao.getTaskListCount(_user.getCustomID());

			if(taskcount >= maxCount){
				return -1;
			}
		}else if(method == 5){
			int gid = Integer.parseInt(this.get("groupid"));
			int searchtime = Integer.parseInt(this.get("searchtime"));
			String rubbishId = this.get("rubbishid");
			String name = this.get("taskname");
			String keywords = this.get("keywords");
			
			Task task = new Task();
			task.setCreator(_user.getId());
			task.setCustomID(_user.getCustomID());
			task.setGroupID(gid);
			task.setKeywords(keywords);
			task.setName(name);
			task.setSearchTime(searchtime);
			dao.insertTask(task, rubbishId);
			
			return "true";
		}
		return "true";
	}

}
