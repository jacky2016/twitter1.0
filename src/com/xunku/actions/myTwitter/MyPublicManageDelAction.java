package com.xunku.actions.myTwitter;

import com.xunku.actions.ActionBase;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.my.SendingDaoImpl;

/**
 * 我的微博--发布管理action类
 * @author shangwei
 *发布管理中模块中有删除按钮模块中的删除按钮action类
 */
public class MyPublicManageDelAction  extends ActionBase {

	SendingDao  dao=new SendingDaoImpl();
	@Override
	public Object doAction() {
		 /**Author  ShangWei
		 * * Description  删除帖子功能操作
		 */
			//"deleteID"
		   String  strs= this.get("arrayID");
		      long ids=    Long.parseLong(strs);
		      dao.deleteBySId(ids);
		  return "删除成功";
		
	}

}
