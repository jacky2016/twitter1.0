package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.OrganizationsDao;
import com.xunku.daoImpl.base.OrganizationsDaoImpl;


/**
 * 协同办公--机构管理action类
 * @author shangwei
 * 协同办公--机构管理下删除功能的action类
 */
public class DepartmentManagerDelAction   extends ActionBase{
 
	OrganizationsDao  dao=new  OrganizationsDaoImpl();
	
	@Override
	public Object doAction() {
		/**Author  ShangWei
		 * * Description  协同办公下的机构管理删除按钮
		 */
		int  id=Integer.parseInt(this.get("deleteID"));
		boolean  flag=  dao.deleteById(id);
		if(!flag){
			return   "删除成功";
		}
		return   "删除失败";
	}

}
