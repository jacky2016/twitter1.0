package com.xunku.actions.myTwitter;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.MessageController;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.utils.DateUtils;

/**
 * 我的微博--发布管理action类
 * @author shangwei
 *发布管理中模块中有修改按钮模块中的修改按钮action类
 */
public class MyPublicManageModifyAction   extends ActionBase {
	SendingDao  dao=new SendingDaoImpl();
	@Override
	public Object doAction() {
	
		 /**Author  ShangWei
		 * * Description  修改帖子功能操作
		 */
		   //"save"
		  String text=this.get("Text");
		  String  image=this.get("Images");
		  long sid=Long.parseLong(this.get("authorID"));
		  long tid=Long.parseLong(this.get("tid"));
		   String  time=this.get("SendTime");
		   long sent =0;
		   if(!time.equals("0")){
			   time+=" 00:00:00";
			   sent =DateUtils.dateStringToInteger(time);
		   }
		   
		   boolean success=true;
		   if(dao.getApprovedCheck(this.getUser().getBaseUser().getId())){
			   //立即发布的
			   if(this.get("SendTime").equals("0")){
				   boolean  flag=  dao.update(text, image, tid,1,0);
				    if(!flag){
				    	success=false;
				    	//return  "修改失败";
				    }
			   }
			   //定时发送的
			   else{
				   boolean  flag=  dao.update(text, image, tid,1,sent);
				    if(!flag){
				    	success=false;
				    //	return  "修改失败";
				    }
			   }
			    
			  // return    sent +"//////////////"+ text+"---"+image+"修改成功";
		   }else{
			   //立即发布
			   if(this.get("SendTime").equals("0")){
				   boolean  flag=  dao.update(text, image, tid,0,0);
				    if(!flag){
				    	success=false;
				    	//return  "修改失败";
				    }
			   }
			 //定时发送的
			   else{
				   boolean  flag=  dao.update(text, image, tid,0,sent);
				    if(!flag){
				    	success=false;
				    	//return  "修改失败";
				    }
			   }
			   
			  //return    sent +"//////////////"+ text+"---"+image+"修改成功";
		   }

		   //审核失败中点击发送按钮发消息
			UserDao udao = new UserDaoImpl();
			int userid=this.getUser().getBaseUser().getId();
			User curUser = udao.queryByUid(userid);
			User   uu2=null;
			boolean   or=false;
			//判断有没有审核人
		 	int checkID= curUser.getCheckid();
		 	if(checkID>0){
		 		User  checkUser=udao.queryByid(checkID);
		 		uu2=checkUser;
		 		or=true;
		 	}else{
		 		//判断当前是管理员
		 		boolean admin= curUser.isAdmin();
		 		if(!admin){
		 			User  adminUser=udao.queryUserByIsAdmin(userid);
		 			uu2=adminUser;
		 		}
		 	}  // 没有审核人结束
			
			if(uu2!=null){
				new MessageController().sendSendToAdminOrChecker(curUser,uu2, success,or);
				if(uu2.getNickName().equals(curUser.getNickName())){
					if(success){
						return "300_success";
					}else{
						return "300_fail";
					}
				}
			}
			
			if(success){
				return "success";
			}else{
				return "fail";
			}
		
	}
 
}
