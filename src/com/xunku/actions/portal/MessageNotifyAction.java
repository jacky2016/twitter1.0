package com.xunku.actions.portal;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.WarningController;
import com.xunku.dao.office.MessageDao;
import com.xunku.daoImpl.office.MessageDaoImpl;

/*
 * portal模块，获取通知消息数量信息
 * @author sunao
 */
public class MessageNotifyAction extends ActionBase {
	MessageDao messageDao = new MessageDaoImpl();
	WarningController warningController = new WarningController();

	@Override
	public String doAction() {
		// TODO Auto-generated method stub
		int userid = this.getUser().getBaseUser().getId();
		int meaageCount = messageDao.queryByCount(userid, 0);
		int alertCount = warningController.getUnreadWarnListCount(userid);
		return meaageCount + "," + alertCount;
	}
}
