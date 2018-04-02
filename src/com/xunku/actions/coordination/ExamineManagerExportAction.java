package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;


/**
 * 协同办公--考核管理--所有的导出按钮action类
 * @author shangwei
 *
 */
public class ExamineManagerExportAction   extends ActionBase {

	   @Override
		public Object doAction() {
			String  queryStr=this.get("tempList");
			   /**
			    * 考核管理导出按钮事件 
			    */
			if(queryStr.equals("")){
				//++++++++++++++++++++++++++
				
					return null;
			}			
			
			
			return null;
		}
}
