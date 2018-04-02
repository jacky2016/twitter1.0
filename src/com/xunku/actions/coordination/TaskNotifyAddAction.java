package com.xunku.actions.coordination;

import com.xunku.actions.ActionBase;
import com.xunku.dao.office.MessageDao;
import com.xunku.daoImpl.office.MessageDaoImpl;
import com.xunku.dto.office.MessageDTO;

/**
 * 协同办公--任务通知action类
 * 
 * @author shangwei 新添加一条任务通知到数据库中去action类
 */
public class TaskNotifyAddAction extends ActionBase {

    MessageDao messageDao = new MessageDaoImpl();

    @Override
    public Object doAction() {

	/**
	 * Author ShangWei * Description 协同办公下的任务通知新建通知确定事件
	 */
	int userid = this.getUser().getBaseUser().getId();
	MessageDTO messageDTO = new MessageDTO();
	messageDTO.setMessage(this.get("taskNotifysContents"));
	messageDTO.setRank(Integer.parseInt(this.get("pri")));
	String ary = this.get("array");
	String[] arys = ary.split(",");
	int[] ranks = new int[arys.length];
	// 判断是否自己给自己发邮件 1 为自己给自己发的 0则不是
	int flag = 0;
	for (int i = 0; i < arys.length; i++) {
	    ranks[i] = Integer.parseInt(arys[i]);
	    // 判断是否自己给自己发邮件
	    if (ranks[i] == this.getUser().getBaseUser().getId()) {
		flag = 1;
	    }
	}
	messageDTO.setReceiver(ranks);
	messageDTO.setUserid(userid);
	messageDao.insert(messageDTO);

	return flag;
	// return "添加成功";
    }

}
