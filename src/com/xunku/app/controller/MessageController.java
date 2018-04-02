package com.xunku.app.controller;

import com.xunku.dao.base.UserDao;
import com.xunku.dao.office.MessageDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.office.MessageDaoImpl;
import com.xunku.dto.office.MessageDTO;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sending;

public class MessageController {

	public void sendMessage(MessageDTO message) {
		MessageDao messageDao = new MessageDaoImpl();

		messageDao.insert(message);
	}

	/**
	 * 发送需要审核消息，发给审核人的
	 * 
	 * @param sendingId
	 * @param sucess
	 * @param reciver
	 */
	public void sendApproveMessage(User user) {

		String msg = "[" + user.getNickName() + "]提交了一条微博需要审核，请去发布管理中处理。";
		MessageDTO message = new MessageDTO();
		message.setMessage(msg);
		message.setRank(0);
		message.setType(0);
		message.setUserid(user.getId());
		message.setReceiver(new int[] { user.getCheckid() });

		this.sendMessage(message);

	}

	/**
	 * 发送一个消息通知
	 * 
	 * @param msg
	 * @param sender
	 * @param receiver
	 */
	public void sendAttentionMessage(String msg, int sender, int receiver) {
		MessageDTO message = new MessageDTO();
		message.setMessage(msg);
		message.setRank(0);
		message.setType(0);
		message.setUserid(sender);
		message.setReceiver(new int[] { receiver });
		this.sendMessage(message);
	}

	/**
	 * 审核失败中，点击发送的告知 如果没有审核人就告诉管理员 如果有审核人告诉审核人 boolean or 是 判断这条是属于审核 还是 不审核的帖子
	 */
	public void sendSendToAdminOrChecker(User curUser, User user,
			boolean success, boolean or) {
		String msg = "[" + curUser.getNickName() + "]";
		// 是审核的帖子
		if (or) {
			msg += "提交了一条微博需要审核，请去发布管理中处理。";
		} else {
			msg += "发布了一条微博,请您查阅。";
		}

		MessageDTO message = new MessageDTO();
		message.setMessage(msg);
		message.setRank(0);
		message.setType(0);
		message.setUserid(curUser.getId());
		message.setReceiver(new int[] { user.getId() });
		this.sendMessage(message);
	}

	/**
	 * 发送已经审核消息，审核人发给待审核人的
	 */
	public void sendApprovedMessage(User user, boolean sucess, Sending sending) {
		String msg;
		if (sucess) {
			msg = "您提交的微博[" + sending.getText() + "]审核成功，已经提交发送。";
		} else {
			msg = "您提交的微博[" + sending.getText() + "]审核失败。";
		}

		MessageDTO message = new MessageDTO();
		message.setMessage(msg);
		message.setRank(0);
		message.setType(0);
		message.setUserid(user.getCheckid());
		message.setReceiver(new int[] { user.getId() });

		this.sendMessage(message);
	}

	/**
	 * 发送帐号过期提醒
	 */
	public void sendAccountExpired(int customid, String name, long hour,
			boolean isMail) {
		String msg;
		if (hour <= 0) {
			msg = "您绑定的帐号[" + name + "]已经过期，为了不影响使用，请尽快授权。";
		} else {
			msg = "您绑定的帐号[" + name + "]还有[" + hour + "]小时过期，为了不影响使用，请尽快授权。";
		}

		MessageDTO message = new MessageDTO();
		message.setMessage(msg);
		message.setRank(0);
		message.setType(0);
		message.setUserid(0);

		// 发送给管理员
		UserDao dao = new UserDaoImpl();
		User user = dao.queryAdmin(customid);

		message.setReceiver(new int[] { user.getId() });
		this.sendMessage(message);

	}

}
