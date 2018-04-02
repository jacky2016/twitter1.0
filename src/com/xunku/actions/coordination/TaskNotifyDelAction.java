package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;
import com.xunku.dao.office.MessageDao;
import com.xunku.daoImpl.office.MessageDaoImpl;


/**
 *协同办公--任务通知action类
 * @author shangwei
 *任务通知的删除按钮action类
 */
public class TaskNotifyDelAction  extends  ActionBase{
	
	MessageDao  messageDao=new  MessageDaoImpl();
	@Override
	public Object doAction() {

		/**Author  ShangWei
		 * * Description  协同办公下的任务通知列表当中点击删除按钮
		 */
     				int id =Integer.parseInt(this.get("deleteID"));
     				messageDao.deleteByID(id);
     				return "删除成功";
	}
 
}
