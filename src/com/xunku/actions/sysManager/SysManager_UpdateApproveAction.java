package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.base.ApprovedDao;
import com.xunku.dao.my.MyAccountDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.ApprovedDaoImpl;
import com.xunku.daoImpl.my.MyAccountDaoImpl;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.Approved;
import com.xunku.pojo.base.User;

/***
 * 帐号授权设置
 *
 * @author shaoqun
 * @created Aug 5, 2014
 */
public class SysManager_UpdateApproveAction extends ActionBase{

	@Override
	public Object doAction() {
		
		int method = Integer.parseInt(this.get("method"));
		int id = Integer.parseInt(this.get("id"));

		if(method == 1){
			User _user = this.getUser().getBaseUser();
			List<String> ulst = new ArrayList<String>();
			ulst.addAll(Arrays.asList(this.get("_ui").split(",")));
			MyAccountDao mydao = new MyAccountDaoImpl();
			mydao.insert(_user.getCustomID(),id,ulst);
			
			return "true";
		}else if(method == 2){

			List<AccountInfo> list = new ArrayList<AccountInfo>();
			AccountDao adao = new AccountDaoImpl();
			list = adao.queryByUserId(id);
			return list;
		}
		
		return "true";
	}

}
