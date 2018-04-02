package com.xunku.actions.pubSentiment;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.CollectionDao;
import com.xunku.daoImpl.task.CollectionDaoImpl;

/*
 * 舆情处理模块，忽略按钮功能
 * @author sunao
 */
public class CollectProcessedAction  extends ActionBase {
	CollectionDao collectionDao = new CollectionDaoImpl();
	@Override
	public Boolean doAction() {
		String collectid = this.get("collectid");

		Boolean b = collectionDao.updateStatus(Integer
				.parseInt(collectid), 1);
		return b;
	}

}
