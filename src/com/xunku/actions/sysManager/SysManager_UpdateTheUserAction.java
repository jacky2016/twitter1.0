package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.dto.myTwitter.SendingDTO;
import com.xunku.pojo.base.User;

/***
 * 审核编辑设置
 *
 * @author shaoqun
 * @created Aug 11, 2014
 */
public class SysManager_UpdateTheUserAction extends ActionBase {

	@Override
	public Object doAction() {
		int method = Integer.parseInt(this.get("method"));
		int id = Integer.parseInt(this.get("id"));
		UserDao _udao = new UserDaoImpl();
		SendingDao sendDao = new SendingDaoImpl();
		//User _user = this.getUser().getBaseUser();
		User _user =_udao.queryByUserid(id );
		
		if(method == 1){
			int _shr = Integer.parseInt(this.get("_shr"));
			//boolean flag = _udao.updateApproved(id, _shr);
//			if(flag == true){
//				//boolean _flag = sendDao.updateApprovedFail(_shr, _user.getCustomID());
//				boolean _flag = sendDao.updateApprovedFail(_user.getCheckid(), _user.getId(),_shr);
//			}
			boolean _flag = sendDao.updateApprovedFail(_user.getCheckid(), _user.getId(),_shr);
			return _flag;
			
		}else if(method == 2){
			User user = _udao.queryByUid(id);
			
			return user;
		}
		
		return "true";
	}

}
