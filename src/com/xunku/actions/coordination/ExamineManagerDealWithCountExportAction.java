package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;


/**
 * 协同办公--考核管理--微博处理统计中的导出按钮action类
 * @author shangwei
 *
 */
public class ExamineManagerDealWithCountExportAction   extends ActionBase {

	@Override
	public Object doAction() {
		
		   String  queryStr=this.get("tempList");
		   /**
		    * 微博处理统计中的导出按钮事件 
		    * 数据库后台 examineManager.weibodealwithcountexport
		    */
		if(queryStr.equals("")){
			//++++++++++++++++++++++++++
			
				return null;
		}
		
		
		return null;
	}

	
	
}
