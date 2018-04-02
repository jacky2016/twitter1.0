package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskRubbishDao;
import com.xunku.daoImpl.task.TaskRubbishDaoImpl;
/*
 * SysManager_DeleteRubbishAction 系统管理删除垃圾词
 * @author: hjian
 */
public class SysManager_DeleteRubbishAction extends ActionBase {

	@Override
	public Object doAction() {
		
		TaskRubbishDao dao = new TaskRubbishDaoImpl();
		int id = Integer.parseInt(this.get("gid"));
		dao.deleteByGID(id);
		
		return "true";
	}

}
