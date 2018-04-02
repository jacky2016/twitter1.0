package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.dto.pubSentiment.TaskDTO;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Task;

import java.util.ArrayList;
import java.util.List;
/*
 * SysManager_GetTaskList 系统管理获取任务列表
 * @author: hjian
 */
public class SysManager_GetTaskList extends ActionBase {

	@Override
	public Object doAction() {
	
		int method = Integer.parseInt(this.get("method"));	
		TaskDao dao = new TaskDaoImpl();
		
		if(method == 1){
			int cid = Integer.parseInt(this.get("pid"));
			List<Task> list = dao.queryTaskByChildren(cid, this.getUser().getBaseUser().getCustomID());
			List<TaskDTO> lst = new ArrayList<TaskDTO>();
			
			for(Task item : list){
				TaskDTO dto = new TaskDTO();
				dto.setId(item.getId());
				dto.setCreateTime(item.getCreateTime());
				//dto.setCreator(item.getCreator());
				dto.setSearchTime(item.getSearchTime());
				dto.setCustomID(item.getCustomID());
				dto.setKeywords(item.getKeywords());
				dto.setName(item.getName());
				dto.setGroupid(item.getGroupID());
				lst.add(dto);
			}
			return lst;
		}else if(method == 2){
			int id = Integer.parseInt(this.get("id"));
			Task task = dao.queryTaskById(id);
			return task;
		}else if(method == 3){
			User _user = this.getUser().getBaseUser();
			String _name = this.get("_name");
			boolean flag = dao.tnameIsExsit(_name, _user.getCustomID());
			
			return flag;
		}
		return "true";
	}

}
