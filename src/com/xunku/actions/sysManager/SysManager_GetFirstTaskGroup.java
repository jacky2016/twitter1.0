package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.dto.pubSentiment.TaskGroupsDTO;
import com.xunku.pojo.task.Group;
/*
 * SysManager_GetFirstTaskGroup 系统管理获取一级任务列表
 * @author: hjian
 */
public class SysManager_GetFirstTaskGroup extends ActionBase {

	@Override
	public Object doAction() {

		TaskDao dao = new TaskDaoImpl();
		int pid = Integer.parseInt(this.get("pid"));
		List<TaskGroupsDTO> lst = new ArrayList<TaskGroupsDTO>();
		List<Group> list = dao.queryByParent(pid, this.getUser().getBaseUser().getCustomID());
		for(Group item : list){
			TaskGroupsDTO dto = new TaskGroupsDTO();
			dto.setId(item.getId());
			dto.setGroupname(item.getGroupName());
			dto.setParentid(item.getParentID());
			lst.add(dto);	
		}
		return lst;
	}

}
