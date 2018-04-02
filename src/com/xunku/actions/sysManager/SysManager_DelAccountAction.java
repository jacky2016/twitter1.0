package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.AccountDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.dto.sysManager.SysManager_AccountDTO;
import com.xunku.utils.Pagefile;

/***
 * 系统管理-帐号绑定-根据id删除信息
 * @author shaoqun
 *
 */
public class SysManager_DelAccountAction extends ActionBase {

	@Override
	public Object doAction() {
		AccountDao dao = new AccountDaoImpl();
		int id = Integer.parseInt(this.get("id"));
		String wbname = this.get("wbname");
		dao.deleteById(id,wbname); 
		
		return "true";
	}

}
