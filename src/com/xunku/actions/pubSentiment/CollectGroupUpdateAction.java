package com.xunku.actions.pubSentiment;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.CollectionGroupDao;
import com.xunku.daoImpl.task.CollectionGroupDaoImpl;
import com.xunku.pojo.task.CollectionGroup;

/*
 * 舆情展示模块，更新收集组
 * @author sunao
 */
public class CollectGroupUpdateAction extends ActionBase {

	CollectionGroupDao collectionGroupDao = new CollectionGroupDaoImpl();

	@Override
	public Object doAction() {
		String groupid = this.get("groupId");
		String groupName = this.get("groupName");
		int customid = this.getUser().getBaseUser().getCustomID();

		// 如果名称存在，返回id=-1，不存在调用数据库，插入成功返回id>0，不成功返回id=0
		Boolean isExist = collectionGroupDao.checkGroupIsExist(customid,
				groupName);
		if (isExist) {
			return -1;
		} else {
			CollectionGroup entity = new CollectionGroup();
			entity.setId(Integer.parseInt(groupid));
			entity.setGroupName(groupName);
			entity.setCustomId(customid);
			Boolean flg = collectionGroupDao.updateByID(entity);
			if (flg) {
				return Integer.parseInt(groupid);
			} else {
				return 0;
			}
		}
	}
}
