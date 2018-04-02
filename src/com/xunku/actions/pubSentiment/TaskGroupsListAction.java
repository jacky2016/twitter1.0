package com.xunku.actions.pubSentiment;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dto.pubSentiment.TaskDTO;
import com.xunku.dto.pubSentiment.TaskGroupsDTO;
import com.xunku.dao.task.*;
import com.xunku.daoImpl.task.TaskDaoImpl;
/*
 * TaskGroupsListAction 微博舆情获取任务组分集合
 * @author: hjian
 */
public class TaskGroupsListAction extends ActionBase {

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		TaskDao dao = new TaskDaoImpl();
		List<TaskGroupsDTO> lst =dao.queryByAll(this.getUser().getBaseUser().getId());
		return lst;
	}

}
