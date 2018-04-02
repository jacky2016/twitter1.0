package com.xunku.actions.pubSentiment;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.CollectionGroupDao;
import com.xunku.daoImpl.task.CollectionGroupDaoImpl;

/*
 * 舆情展示模块，删除收集组
 * @author sunao
 */
public class CollectGroupDeleteAction extends ActionBase {
	CollectionGroupDao collectionGroupDao = new CollectionGroupDaoImpl();

	@Override
	public Object doAction() {
		String groupid = this.get("groupId");
		return collectionGroupDao.deleteGroup(Integer.parseInt(groupid));
	}
}
