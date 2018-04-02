package com.xunku.actions.pubSentiment;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.CollectionGroupDao;
import com.xunku.daoImpl.task.CollectionGroupDaoImpl;
import com.xunku.dto.pubSentiment.TaskCollectionGroupDTO;
import com.xunku.pojo.task.CollectionGroup;
/*
 * TaskCollectionGroupsListAction 微博舆情任务分类组
 * @author: hjian
 */
public class TaskCollectionGroupsListAction extends ActionBase {

	CollectionGroupDao collectionGroupDao = new CollectionGroupDaoImpl();
	@Override
	public List<CollectionGroup> doAction() {
		// 用户ID
		int userid = this.getUser().getBaseUser().getId();
		List<CollectionGroup> lst = collectionGroupDao.queryByAll(userid);
		return lst;
	}

}
