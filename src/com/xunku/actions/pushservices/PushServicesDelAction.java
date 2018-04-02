package com.xunku.actions.pushservices;

import com.xunku.actions.ActionBase;
import com.xunku.dao.push.PushDao;
import com.xunku.daoImpl.push.PushDaoImpl;


/**
 * 预警服务--微博推送action类
 * @author shangwei
 *微博推送的删除按钮功能action类
 */
public class PushServicesDelAction  extends   ActionBase {
		
	PushDao  pushdao= new PushDaoImpl();
	@Override
	public Object doAction() {
		/**Author  ShangWei
		 * * Description 推送服务首页列表中删除某一行方法
		 */
		     int id=Integer.parseInt(  this.get("deleteID"));
		     pushdao.deleteByID(id);
		     return  "删除成功";
	}
   
}
