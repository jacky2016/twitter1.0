package com.xunku.actions.home;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.dao.home.WeiboInfoDao;
import com.xunku.daoImpl.home.WeiboInfoDaoImpl;

/*
 * 更新首页模块关闭块状态
 * @author sunao
 */
public class UpdateCloseAction extends ActionBase {

	WeiboInfoDao weiboInfoDao =new WeiboInfoDaoImpl();
	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		Map<String, String> params = this.getParameters();
		String id = params.get("id");
		String close = params.get("close");
		int blockID = Integer.parseInt(id);
		weiboInfoDao.updateClose(blockID, Boolean.parseBoolean(close));
		return true;
	}

}
