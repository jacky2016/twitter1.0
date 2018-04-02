package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.result.Result;
import com.xunku.dao.office.NavieDao;
import com.xunku.daoImpl.office.NavieDaoImpl;
import com.xunku.pojo.office.Navie;
import com.xunku.pojo.office.NavieAccount;
import com.xunku.utils.AppServerProxy;


/**
 * 协同办公--考核管理--网评员统计中的添加按钮action类
 * @author shangwei
 *
 */
public class ExamineManagerCommentatorsCountAddAction    extends ActionBase {

	NavieDao  navieDao=new NavieDaoImpl();
	@Override
	public Object doAction() {
		   String  queryStr=this.get("tempList");
		   int customId=this.getUser().getBaseUser().getCustomID();
		   /**
		    * 添加网评员统计模块的添加按钮所弹出的层的添加事件判断是否重复
		    */
		   if(queryStr.equals("checkCommentators")){
			   //uid
			   	 String uid=this.get("uid");
			   	 uid="3181276997";
			   	 //平台类型
			   	 int platform=Integer.parseInt(this.get("platform"));
			     //网评员人员名称
			   	 String  name= this.get("name");
			   	 //网评员弹出层中的微博帐号名称
			   	 String  accName=this.get("accName");
			     boolean flag=	navieDao.checkIsExsit( customId,platform, accName);
			     if(!flag){
			    		Platform pfm=Platform.Sina;
			 			if(platform==2){
			 				pfm=Platform.Tencent;
			 			}
			 			else if(platform==5){
			 				pfm=Platform.Renmin;
			 			}
			 			Result<IAccount> acct=	AppServerProxy.getAccountByNameOnline(accName,pfm,this.getUser().getBaseUser());
			 			
			 		   	if(acct.getErrCode() == 0){
			 		   		IAccount acc = acct.getData();
			 		 	if(acc!=null){
			 		  		 NavieAccount  na=new NavieAccount();
					    	 Navie  n=new Navie();
					    	 n.setCustomId(customId);
					    	 n.setName(name);
					    	 na.setNavie(n);
					    	 na.setDisplayName(accName);
					    	 if(platform==1){
					    		 na.setUid(acc.getUcode()); 
					    	 }else if(platform==2){
					    		 na.setUid(acc.getName());
					    	 }else if(platform==5){
					    		 na.setUid(acc.getUcode());
					    	 }else{
					    		 na.setUid(acc.getUcode()); 		
					    	 }
					    	 na.setPlatform(platform);
					    	 //插入成功返回这条数据的ID号 
					    	 int reNum=navieDao.insert(na);
					    	 return  reNum;
			 		   	}else{
			 		   		 return "-2";
			 		   	}
			 		   	}else{
			 		   		 return "-2";
			 		   	}
			     }else{
			    	 return "-1";
			    	 // return  "添加网评员已经存在，请重新添加"; -1存在 -2 是压根不存在
			     }
			   	
			     /*
			   	 if(!flag){
			    	 NavieAccount  na=new NavieAccount();
			    	 Navie  n=new Navie();
			    	 n.setCustomId(customId);
			    	 n.setName(name);
			    	 na.setNavie(n);
			    	 na.setDisplayName(accName);
			    	 na.setUid(uid);
			    	 na.setPlatform(platform);
			    	 //插入成功返回这条数据的ID号 
			    	 int reNum=navieDao.insert(na);
			    	 return  reNum;
			     }else {
			    	 return "";
			    	 // return  "添加网评员已经存在，请重新添加";
			     }
			   	 */
		   }
		   
		   
		   /**
		    * 修改网评员统计模块的     点击其中某一个   网评员帐号个数       所弹出的层的添加事件判断是否重复
		    * --------------------
		    * 添加网评员统计模块的添加按钮所弹出的层的当前输入的
		    * 网评员下的用管理的微博帐号的列表  
		    * 判断是否在微博平台存在这个帐号
		    */
		   if(queryStr.equals("modityCheckCommentators")){
			   //uid
			   	 //String uid=this.get("uid");
			   	 //uid="3181276997";
			   	 //平台类型
			   	 int platform=Integer.parseInt(this.get("platform"));
			     //网评员人员名称
			   	 String  name= this.get("name");
			   	 //网评员弹出层中的微博帐号名称
			   	 String  accName=this.get("accName");
			     boolean flag=	navieDao.checkIsExsit( customId,platform, accName);
			     if(!flag){
			    		Platform pfm=Platform.Sina;
			 			if(platform==2){
			 				pfm=Platform.Tencent;
			 			}
			 			else if(platform==5){
			 				pfm=Platform.Renmin;
			 			}
			 			//IAccount acct=	AppServerProxy.getAccountByName(accName,pfm,this.getUser().getBaseUser());
			 			Result<IAccount>  result=	AppServerProxy.getAccountByNameOnline(accName,pfm,this.getUser().getBaseUser());
			 			if(result.getErrCode()==0){
			 				IAccount  acct=result.getData();
			 			 	if(acct!=null){
				 		  		 NavieAccount  na=new NavieAccount();
						    	 Navie  n=new Navie();
						    	 n.setCustomId(customId);
						    	 n.setName(name);
						    	 na.setNavie(n);
						    	 na.setDisplayName(accName);
						    	 if(platform==1){
						    		 na.setUid(acct.getUcode()); 
						    	 }else if(platform==2){
						    		 na.setUid(acct.getName());
						    	 }else if(platform==5){
						    		 na.setUid(acct.getUcode());
						    	 }else{
						    		 na.setUid(acct.getUcode()); 		
						    	 }
						    	 na.setPlatform(platform);
						    	 //插入成功返回这条数据的ID号 
						    	 int reNum=navieDao.insert(na);
						    	 return  reNum;
				 		   	}else{
				 		   		 return "-2";
				 		   	}
			 			}else{
			 				 return "-2";
			 			}
			 		   	//-----------------
			 		   	
			     }else{
			    	 return "-1";
			    	 // return  "添加网评员已经存在，请重新添加"; -1存在 -2 是压根不存在
			     }
			     /*
			   	 if(!flag){
			   		 NavieAccount  na=new NavieAccount();
			    	 Navie  n=new Navie();
			    	 n.setCustomId(customId);
			    	 n.setName(name);
			    	 na.setNavie(n);
			    	 na.setDisplayName(accName);
			    	 na.setUid(uid);
			    	 na.setPlatform(platform);
			    	 //插入成功返回这条数据的ID号 
			    	 int reNum=navieDao.insert(na);
			    	 return  reNum;
			   	 }else {
			    	 return "1";
			    	 // return  "添加网评员已经存在，请重新添加"; 1存在 2 是压根不存在
			     }
			   	 */
		   }
		
		
		return null;
	}

}
