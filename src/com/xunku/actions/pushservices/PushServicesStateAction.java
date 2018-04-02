package com.xunku.actions.pushservices;

import com.xunku.actions.ActionBase;
import com.xunku.dao.push.PushDao;
import com.xunku.daoImpl.push.PushDaoImpl;


/**
 * 预警服务--微博推送action类
 * @author shangwei
 *微博推送列表的中更改状态按钮事件
 */
public class PushServicesStateAction  extends   ActionBase {

	  PushDao  pushdao= new PushDaoImpl();
	@Override
	public Object doAction() {

		/**Author  ShangWei
		 * * Description 推送服务首页列表中点击更改状态按钮方法
		 */
	   	 int value =Integer.parseInt(this.get("isStop"));
	      boolean isStop;
	     if(value==1){
	     isStop=true;
	     }else{
	     isStop=false;
	     }
		   pushdao.updateStatus(Integer.parseInt(this.get("subId")),isStop );
		     return   "更改成功";
	
	} 

}
