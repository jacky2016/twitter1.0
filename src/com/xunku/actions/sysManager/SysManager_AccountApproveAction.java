package com.xunku.actions.sysManager;

import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.AccountAuthsDao;
import com.xunku.daoImpl.base.AccountAuthsDaoImpl;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.sysManager.AccountVO;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;

/**
 * 获取授权帐号列表集合
 *
 * @author shaoqun
 * @created Aug 4, 2014
 */
public class SysManager_AccountApproveAction extends ActionBase{

	@Override
	public Object doAction() {

		User _user = this.getUser().getBaseUser();
		List<AccountInfo> list = new AccountDaoImpl().queryByCustomId(_user.getCustomID());
		
		return list;
	}

}
