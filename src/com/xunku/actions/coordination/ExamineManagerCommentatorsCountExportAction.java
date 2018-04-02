package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;


/**
 * 协同办公--考核管理--网评员统计中的导出按钮action类
 * @author shangwei
 *
 */
public class ExamineManagerCommentatorsCountExportAction    extends ActionBase{

	@Override
	public Object doAction() {
		
		   String  queryStr=this.get("tempList");
		   /**
		    * 网评员统计中的导出按钮事件
		    * 数据库后台  examineManager.weibodcommentatorscountexport
		    */
		   
		if(queryStr.equals("")){
			//++++++++++++++++++++++++++
			
				return null;
		}
		
		return null;
	}

}
