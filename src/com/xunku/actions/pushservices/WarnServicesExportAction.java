package com.xunku.actions.pushservices;

import com.xunku.actions.ActionBase;

/**
 * 预警服务--预警信息--导出事件  action类
 * @author shangwei
 */
public class WarnServicesExportAction  extends ActionBase{

	@Override
	public Object doAction() {
		String queryStr=this.get("queryConditions");
			
		/**
		 * 预警信息的导出按钮事件
		 * 数据库后台:   warnservices.warnservicesImport
		 */
		 if(queryStr.equals("")){
			 
			 		return null;
		 }
		
		return null;
	}
}
