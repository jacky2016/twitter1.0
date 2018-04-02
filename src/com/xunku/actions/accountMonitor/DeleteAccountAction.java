package com.xunku.actions.accountMonitor;

import com.xunku.actions.ActionBase;
import com.xunku.dao.account.AccountDao;
import com.xunku.daoImpl.account.AccountDaoImpl;

/**
 * 帐号监控--删除帐号监控
 * @author shaoqun
 *
 */
public class DeleteAccountAction extends ActionBase {

	@Override
	public Object doAction() {
		
		int id = Integer.parseInt(this.get("id"));
		AccountDao accDAO = new AccountDaoImpl();
		accDAO.deleteByEId(id);

		return "true";
	}

}
