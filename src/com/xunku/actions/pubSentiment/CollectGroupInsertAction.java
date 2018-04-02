package com.xunku.actions.pubSentiment;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.CollectionGroupDao;
import com.xunku.daoImpl.task.CollectionGroupDaoImpl;
import com.xunku.pojo.task.CollectionGroup;

/*
 * 舆情展示模块，添加收集组
 * @author sunao
 */
public class CollectGroupInsertAction extends ActionBase {

	CollectionGroupDao collectionGroupDao = new CollectionGroupDaoImpl();

	@Override
	public Object doAction() {
		String groupName = this.get("groupName");
		int customid = this.getUser().getBaseUser().getCustomID();
		//如果名称存在，返回id=-1的对象，不存在调用数据库，插入成功返回id>0的对象，不成功返回id=0的对象
		Boolean isExist = collectionGroupDao.checkGroupIsExist(customid, groupName);
		if (isExist) {
			CollectionGroup group = new CollectionGroup();
			group.setId(-1);
			group.setGroupName("");
			return group;
		} else {
			CollectionGroup entity = new CollectionGroup();
			entity.setGroupName(groupName);
			entity.setCustomId(customid);
			return collectionGroupDao.insert(entity);
		}
	}

}
