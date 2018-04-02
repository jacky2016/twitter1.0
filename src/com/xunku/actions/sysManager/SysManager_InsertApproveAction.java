package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.ApprovedDao;
import com.xunku.daoImpl.base.ApprovedDaoImpl;
import com.xunku.pojo.base.Approved;
import com.xunku.pojo.base.User;

/***
 * 添加审核设置
 *
 * @author shaoqun
 * @created Aug 5, 2014
 */
public class SysManager_InsertApproveAction extends ActionBase {

	@Override
	public Object doAction() {

		ApprovedDao dao = new ApprovedDaoImpl();
		User _user = this.getUser().getBaseUser();
		int method = Integer.parseInt(this.get("mthod"));
		int _usersel = Integer.valueOf(this.get("_usersel"));
		
		if(method == 1){
			int flag = Integer.valueOf(this.get("flag"));
			int _shr = Integer.valueOf(this.get("_shr"));
			String operate = this.get("operate");
			List<String> ulst = new ArrayList<String>();
			ulst.addAll(Arrays.asList(this.get("_ui").split(",")));
			Approved app = new Approved();
			app.setUserid(_usersel);
			app.setCheckid(_shr);
			if(flag == 0){
				app.setCheck(true);
			}else{
				app.setCheck(false);
			}
			app.setUids(ulst);
			dao.insert(app);
			
			return "true";
		}else if(method == 2){
			
			//boolean flag = dao.checkIsExsit(_usersel, _user.getCustomID());
			
		}
		
		
		return "true";
	}

}
