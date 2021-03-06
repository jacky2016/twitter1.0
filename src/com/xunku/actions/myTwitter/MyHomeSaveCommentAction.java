package com.xunku.actions.myTwitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.AppServerProxy;

/*
 * 我的首页模块，转发一条微博
 * @author sunao
 */
public class MyHomeSaveCommentAction extends ActionBase {

	SendingDao sendingDao = new SendingDaoImpl();

	
	@Override
	public Object doAction() {

		// 内容
		String content = this.get("content");
		// 是否同时转发
		String isChecked = this.get("isChecked");
		// uids
		String accountuids = this.get("accountuids");
		String sendNames = this.get("sendNames"); // 发布账号，拼接字符串
		// 平台
		String platform = this.get("platforms");
		// 微博ID
		String twitterid = this.get("twitterID");
		// 用户ID
		int userid = this.getUser().getBaseUser().getId();

		User user = this.getUser().getBaseUser();

		boolean checked = Boolean.parseBoolean(isChecked);
		// 获取当前时间
		String dateTime = "";
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTime = sdf.format(dt);
		int type = 3;// 评论

		Sending sendingEntity = new Sending();

		String[] accountids = accountuids.split(",");
		String[] platforms = platform.split(",");
		String[] sendNameArray = sendNames.split(",");

		int index = 0;
		List<Sender> sendList = new ArrayList<Sender>();
		for (String uid : accountids) {
			Sender sender = new Sender();
			sender.setUid(uid);
			sender.setPlatform(Integer.parseInt(platforms[index]));
			sender.setName(sendNameArray[index]);
			sendList.add(sender);
			index++;
		}
		boolean b = sendingDao.getApprovedCheck(userid);
		if (b) {
			sendingEntity.setApproved(1);
		} else {
			sendingEntity.setApproved(0);
		}
		sendingEntity.setSendList(sendList);
		sendingEntity.setSubmit(dateTime); // 提交时间
		sendingEntity.setUserid(userid); // 用户ID
		sendingEntity.setSourceid(twitterid); // 微博ID
		sendingEntity.setText(content);// 内容
		sendingEntity.setFlag(checked); // 是否同时转发
		sendingEntity.setType(type);// 类型（3评论）
		
		Result<Sending> result = AppServerProxy.submitSending(sendingEntity, user);
		String str = "发布成功";
		if (result.getErrCode() != 0) {
			str = result.getMessage();
		}
		return str;
	}

}