package com.xunku.actions.coordination;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.office.MessageDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.office.MessageDaoImpl;
import com.xunku.dto.coordination.TaskNotifyDTO;
import com.xunku.dto.office.MessageDTO;
import com.xunku.dto.office.MessageVO;
import com.xunku.dto.pushservices.PushUserDTO;
import com.xunku.utils.Pagefile;

/**
 *协同办公--任务通知action类
 * @author shangwei
 *
 */
public class TaskNotifyAction extends  ActionBase {
    
	MessageDao  messageDao=new  MessageDaoImpl();
	UserDao  userDao =new UserDaoImpl();
	@Override
	public Object doAction() {
		
	   String  queryStr=this.get("queryConditions");
	    int userid=this.getUser().getBaseUser().getId();
	    
		
		/**Author  ShangWei
		 * * Description  协同办公下的任务通知列表
		 */
	     if( queryStr.equals("queryList")){
	 		Pagefile<TaskNotifyDTO>   pagefile=new Pagefile<TaskNotifyDTO>();
	    	 	int  status =Integer.parseInt(this.get("statu"));
	    	 	// 调后台的 queryReceiveMsg方法
	    	 	MessageVO mv=new MessageVO();
	    	 	   mv.setPageIndex(Integer.parseInt(this.get("pageIndex")));
	    	 	   mv.setPageSize(Integer.parseInt(this.get("pageSize")));
	    	 	   mv.setUserId(userid);
	    	 	   mv.setStatus(status);
	    	 	  Pagefile<MessageVO> pf= messageDao.queryReceiveMsg(mv);
	    	 	  if(pf!=null){
	    	 	   	    pagefile.setRealcount(pf.getRealcount());
	    	 	   	    List<MessageVO>  mvos= pf.getRows();
	    	 	   	    for(MessageVO vo : mvos){
	    	 	   		TaskNotifyDTO   t=new TaskNotifyDTO();
	    	 	   			t.id=vo.getId();
	    	 	   			t.publicDate=vo.getReceiveTime();
	    	 	   			t.rank=vo.getRank();
	    	 	   			t.sendName=vo.getReceiver();
	    	 	   			t.states=vo.getStatus();
	    	 	   			//判断下content内容字符串长度
	    	 	   			if(vo.getMessage().length()>20){
	    	 	   			t.totifyContent=vo.getMessage().substring(0,20)+"..."; 
	    	 	   			}else{
	    	 	   			t.totifyContent=vo.getMessage();
	    	 	   			}
	    	 	   		  pagefile.getRows().add(t);
	    	 	   	    }
	     }
	    		return pagefile;
	     }
	     
		return null;
	}
	
	
	
}
