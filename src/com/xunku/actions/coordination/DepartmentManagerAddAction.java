package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.result.Result;
import com.xunku.dao.base.OrganizationsDao;
import com.xunku.daoImpl.base.OrganizationsDaoImpl;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.Organization;
import com.xunku.utils.AppServerProxy;


/**
 * 协同办公--机构管理action类
 * @author shangwei
 * 协同办公--机构管理下添加功能的action类
 */
public class DepartmentManagerAddAction   extends ActionBase{

	OrganizationsDao dao=new OrganizationsDaoImpl();
	@Override
	public Object doAction() {
		
		   String queryStr=this.get("queryConditions");
		   int userid=this.getUser().getBaseUser().getId();
		   int customId=this.getUser().getBaseUser().getCustomID();
		    //机构名字
			String  name=this.get("name");
			
		/**Author  ShangWei
		 * * Description  协同办公下的机构管理添加按钮
		 */
		if(queryStr.equals("addOragnName")){
			//前台的 0是新浪  1是腾讯 2是人民
			//后台相对应的是新浪1  腾讯2  人民5
			int  plat= Integer.parseInt(this.get("platform"));
			Platform pf=Platform.Sina;
			//后台对应数据库当中的平台类型数字
			int platType=1;
			 if(plat==1){
				 pf=Platform.Tencent;
				 platType=2;
			 }else if(plat==2){
				 pf=Platform.Renmin;
				 platType=5;
			 }
		      IAccount  acc=	 AppServerProxy.getAccountByName(name,pf,this.getUser().getBaseUser());
		      if(acc!=null){
		    	  String uid=acc.getUcode();
				  int customid= this.getUser().getBaseUser().getCustomID();
				  Organization   organ=new Organization();
				    organ.setCustomID(customid);
				    organ.setName(name);
				    organ.setPlatform(platType);
				    organ.setUid(uid);
				    organ.setEnable(true);
				  boolean flag=   dao.insert(organ);
				   if(!flag){
					   return "添加失败";
				   }
				  return "添加成功";
		      }
		      return  "输入账号名字不存在，请重新输入";
		}
		
		/**Author  ShangWei
		 * * Description  协同办公下的机构管理添加按钮中检查
		 *     机构管理名称是否重复  
		 *     不重复则添加成功
		 */
		if(queryStr.equals("checkOragnName")){
			//前台的 0是新浪  1是腾讯 2是人民
			//后台相对应的是新浪1  腾讯2  人民5
			int  plat= Integer.parseInt(this.get("platform"));
			Platform pf=Platform.Sina;
			//后台对应数据库当中的平台类型数字
			int platType=1;
			 if(plat==1){
				 pf=Platform.Tencent;
				 platType=2;
			 }else if(plat==2){
				 pf=Platform.Renmin;
				 platType=5;
			 }
				OrganizationsDao  orgdao=new OrganizationsDaoImpl();
			
			 boolean flag=orgdao.checkIsExsit(customId, name);
			 if(!flag){
				 Result<IAccount>  result=	AppServerProxy.getAccountByNameOnline(name, pf, this.getUser().getBaseUser());
				 	//帐号在授权期，可以添加使用  getErrCode == 0
				 	if(result.getErrCode()!=500){
				 		 IAccount acc=  result.getData();
						 if(acc!=null){
						   	  String uid=acc.getUcode();
							  int customid= this.getUser().getBaseUser().getCustomID();
							    Organization   organ=new Organization();
							    organ.setCustomID(customid);
							    organ.setName(name);
							    organ.setPlatform(platType);
							    organ.setUid(uid);
							    organ.setEnable(true);
							  boolean flag2=   dao.insert(organ);
							   if(!flag2){
								  // return "添加失败"; // 说明存在
								       return "* 输入账号名字已存在，请重新输入"; 
							   }
							  return "";
							   //return "添加成功";
						 }else{
							  return  "* 输入账号名字不存在，请重新输入";
						 }
				 		
				 	}  //帐号在授权期，可以添加使用 结束 
				 	
				 	//帐号授权已经到期，请赶快授权  getErrCode  ==500 
				 	else if(result.getErrCode()==500){
				 		return    "* 帐号授权已经到期，请赶快授权";
				 	}   	 	//帐号授权已经到期，请赶快授权  结束
				  
		       //    return   "";
			 }
				  return  "* 机构管理名已经存在，请重新添加";
		}
		
			return null;
	}

}
