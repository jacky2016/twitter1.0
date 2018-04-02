package com.xunku.actions.myTwitter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.MessageController;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.UploadImage;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.ImageUtil;
import com.xunku.utils.PropertiesUtils;

/**
 * 我的微博--发布管理action类
 * 
 * @author shangwei 发布管理中模块中有审核按钮模块的审核按钮action类
 */
public class MyPublicManageCheckAction extends ActionBase {
	SendingDao dao = new SendingDaoImpl();

	@Override
	public Object doAction() {
		/**
		 * Author ShangWei * Description 审核帖子功能操作
		 */
		String tip = "";
		int auId = Integer.parseInt(this.get("auID"));
		int apst = Integer.parseInt(this.get("approvedStatue"));
		// uid 带,
		String uid = this.get("uid");
		// platform--- 带, 的
		String platform = this.get("platform");
		// 发送的时间
		 String sendTime=this.get("sendTime")+":00";
		//String sendTime = this.get("sendTime");
		//String[] strs2 = sendTime.split("_");
		//sendTime = strs2[0] + ":00";
		 // strs2 =0 为立即发布   strs2=1 为定时发布
		 int  strs2=0;
   
	    	 Date   nowDate=new Date();
		   String  curSystemDate=  DateUtils.format(nowDate);
		   
			  try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				   java.util.Date now = df.parse(curSystemDate);
				   java.util.Date date=df.parse(sendTime);
				 if(date.after(now)){
					 //定时
					  strs2=1;
				 }
				 else{
					 //立即
				 }
			  } catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
			      }
		
		long sendDate = 0;
		sendDate = DateUtils.dateStringToInteger(sendTime);

		SendingDao sendDao = new SendingDaoImpl();
		String strs = this.get("arrayID");
		long ids = Long.parseLong(strs);
		// dao.checkSending(ids, apst, auId);
		dao.checkSending(ids, apst, this.getUser().getBaseUser().getId());

		User user = this.getUser().getBaseUser();
		String[] uids = uid.split(",");
		String[] platforms = platform.split(",");
		boolean sucess = false;
		Sending send = sendDao.querySendById(ids);
		// 吴工提供API
		if (apst == 2) {
			// try {
			// String url = PropertiesUtils.getString("config",
			// "userWeiboImagePath");

			// List<ITweet> tweets = AppServerProxy.uploadPost(send, user);

			/*
			 * String text = send.getText(); UploadImage image = null; if
			 * (!send.getImages().equals("") && send.getImages() != null) {
			 * String[] imgs = send.getImages().split(","); for (int i = 0; i <
			 * imgs.length; i++) { imgs[i] = imgs[i].replace("$", url); } //
			 * 只传一张图片 String img = imgs[0]; // String
			 * imgName=img.substring(img.lastIndexOf("/")+1); image =
			 * ImageUtil.getImages(img); }
			 * 
			 * for (int i = 0; i < platforms.length; i++) { int plat =
			 * Integer.parseInt(platforms[i]); String ud = uids[i]; Platform pf;
			 * if (plat == 1) { pf = Platform.Sina; } else if (plat == 2) { pf =
			 * Platform.Tencent; } else if (plat == 5) { pf = Platform.Renmin; }
			 * else { pf = Platform.Sina; } XKResult<ITweet> pt =
			 * AppServerProxy.uploadPost(ud, pf, text, image, user); //
			 * API调用发送成功 if (pt.getData() != null) { // if(pt!=null){
			 * sendDao.updateSendStatus(2, ids, pt.getData().getTid()); //
			 * tip="审核通过"; } // 调用失败 else { sendDao.updateSendStatus(3, ids,
			 * null); // tip="审核失败"; } } // for 结束
			 */
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			if (send != null) {
				List<Sender> senders = send.getSendList();
				if (senders != null) {
					for (Sender sender : senders) {
						if (strs2==1) {
							sendDao.updateSendStatus(send.getId(), 1, sender
									.getId(), null, 0, sendDate);
						} else {
							sendDao.updateSendStatus(send.getId(), 1, sender
									.getId(), null, 0, 0);
						}
						
					} // for
				} // senders

			} // sending
			sucess = true;

			// sendDao.updateSendStatus(2,ids,0,sendDate,0);
			// sendDao.updateSendStatus(send.getId(),2, ids, null, 0, 0);
		}
		// 审核失败的
		else if (apst == 3) {
			// Sending send = sendDao.querySendById(ids);
			if (send != null) {
				List<Sender> senders = send.getSendList();
				if (senders != null) {
					for (Sender sender : senders) {
						if (strs2==1) {
							sendDao.updateSendStatus(send.getId(), 3, sender
									.getId(), null, 0, sendDate);
						} else {
							sendDao.updateSendStatus(send.getId(), 3, sender
									.getId(), null, 0, 0);
						}
					} // for
				} // senders
			} // sending

			// sendDao.updateSendStatus(send.getId(), 3, ids, null, 0, 0);
			tip = "审核失败";
			sucess = false;
		}
		// 发送审核是否通过的消息
		//modify by tengsx 2014-09-29
		UserDao udao = new UserDaoImpl();
		User uu1 = udao.queryByUid(send.getUserid()); 
		new MessageController().sendApprovedMessage(uu1, sucess, send);   
	    User  uu2=udao.queryByUid(send.getAuditor());
	    if(uu2.getNickName().equals(uu1.getNickName())){
	    	//这个表示 自己是提交人 也是审核人
	    	return   1;
	    }else{
	    	return    0;
	    }
		//return "";
	}

}
