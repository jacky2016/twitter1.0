package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;
import com.xunku.dao.office.NavieDao;
import com.xunku.daoImpl.office.NavieDaoImpl;
import com.xunku.utils.AppServerProxy;



/**
 * 协同办公--考核管理--网评员统计中的删除按钮action类
 * @author shangwei
 *
 */
public class ExamineManagerCommentatorsCountDelAction     extends ActionBase{

	NavieDao  navieDao=new NavieDaoImpl();
	@Override
	public Object doAction() {
		   String  queryStr=this.get("tempList");
		   /**
		    * 网评员统计中的（修改）状态下的删除按钮事件
		    *网评员统计模块的     网评员帐号个数点击   所弹出的层的table列表中的删除按钮事件
		    */
		   if(queryStr.equals("modityDeleteComtorsID")){
			    int  nid=Integer.parseInt(this.get("nid"));
			    String uid=this.get("comtorsUID");
			    //boolean flag=  navieDao.deleteById(nid);
			    boolean flag=AppServerProxy.deleteNavieRecord(this.getUser().getBaseUser(), nid, uid);
			    navieDao.deleteById(nid,uid);
			  //true 删除失败
			   if(flag){
				   return "删除失败";   
			   }
			   return  "删除成功";
		   }
		
		   /**
		    * 网评员统计中的（新建）状态下的删除按钮事件
		    * 添加网评员统计模块的添加按钮所弹出的层的table列表中的删除按钮事件
		    */
		   if(queryStr.equals("deleteComtorsID")){
			    int  nid=Integer.parseInt(this.get("nid"));
			    String uid=this.get("comtorsUID");
			    //boolean flag=  navieDao.deleteById(nid);
			    boolean flag=AppServerProxy.deleteNavieRecord(this.getUser().getBaseUser(), nid, uid);
			    navieDao.deleteById(nid,uid);
			    //true 删除失败
			    if(flag){
			 	   return "删除失败";   
				   }
				   return  "删除成功";
		   }
		
		return null;
	} 

}
