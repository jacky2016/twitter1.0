package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;
import com.xunku.dao.office.MessageDao;
import com.xunku.daoImpl.office.MessageDaoImpl;
import com.xunku.dto.coordination.TaskNotifyDTO;

/**
 * 协同办公--任务通知action类
 * @author shangwei
 *  任务通知中点击任务内容的事件效果
 */
public class TaskNotifyUpdateAction extends ActionBase {

	MessageDao messageDao = new MessageDaoImpl();

	@Override
	public Object doAction() {
		String queryStr = this.get("queryConditions");
	    int userid=this.getUser().getBaseUser().getId();
		/**
		 * Author ShangWei * Description 点击内容列弹出内容弹出层
		 */
		if (queryStr.equals("lookContent")) {
			String id = this.get("id");
			// 后台调用 ID指定的内容
			String contents = messageDao.queryMessageByID(Integer.parseInt(id));
				TaskNotifyDTO ff = new TaskNotifyDTO();
				ff.totifyContent = contents;
				return ff;				
		}
		
		/**Author  ShangWei
		 * * Description   未读变成已读状态
		 */
	   if(queryStr.equals("updateStatus")){
		   		String  id=this.get("updateid");
		   		messageDao.updateStatus(Integer.parseInt(id), userid);
		   		return "已读成功";
	   }

		return null;
	}

}
