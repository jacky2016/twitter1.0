package com.xunku.actions.sysManager;

import java.util.List;
import java.util.ArrayList;

import com.xunku.actions.ActionBase;
import com.xunku.dao.task.TaskRubbishDao;
import com.xunku.daoImpl.task.TaskRubbishDaoImpl;
import com.xunku.dto.sysManager.SysManager_RubbishDTO;
import com.xunku.dto.task.RubbishDTO;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Rubbish;
/*
 * SysManager_GetRubbish系统管理获取垃圾词列表
 * @author: hjian
 */
public class SysManager_GetRubbish extends ActionBase {

	@Override
	public Object doAction() {
		
		User _user = this.getUser().getBaseUser();
		
		TaskRubbishDao dao = new TaskRubbishDaoImpl();
		List<RubbishDTO> dlist = dao.queryByCustom(_user.getCustomID());
		List<SysManager_RubbishDTO> lst = new ArrayList<SysManager_RubbishDTO>();
		for(RubbishDTO rd : dlist){
			SysManager_RubbishDTO r = new SysManager_RubbishDTO();
			r.setCustomid(rd.getCustomId());
			r.setGroupname(rd.getGroupName());
			r.setId(rd.getId());
			r.setRubbishwords(rd.getRubbishWords());
			lst.add(r);
		}
	
		return lst;
	}

}
