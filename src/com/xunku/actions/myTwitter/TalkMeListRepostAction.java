package com.xunku.actions.myTwitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.interfaces.ITweet;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.AppServerProxy;
/**
 * 我的微博--@提到我的中的转发action类
 * 
 * @author shangwei
 */
public class TalkMeListRepostAction extends ActionBase {

	SendingDao sendingDao = new SendingDaoImpl();

	@Override
	public Object doAction() {
		User user = this.getUser().getBaseUser();
		int userid = user.getId();
		String queryStr = this.get("queryConditions");

		/**
		 * 我的微博--@提到我的的转发事件action
		 * 
		 * @author shangwei
		 */
		if (queryStr.equals("talkMeRepost")) {
			// 是否同时转发微博
			String isChecked = this.get("isChecked");
			String content = this.get("content");
			String twitterID = this.get("twitterID");
			// uids
			String accountuids = this.get("accountuids");
			// 平台
			String platform = this.get("platforms");
			// 当前就是最上面选择帐号下拉框中选中的那个帐号
			// 的UID
			// String UID=this.get("currentuserUID");
			// 计数 判断是否全部插入成功
			int countNum = 0;
			// 算序列号数
			Sending send = new Sending();
			String[] accountids = accountuids.split(",");
			String[] platfroms = platform.split(",");
			List<Sender> sendList = new ArrayList<Sender>();
			for (int i = 0; i < accountids.length; i++) {
				// modify by wanghui
				Sender sender = new Sender();
				sender.setUid(accountids[i]);
				sender.setPlatform(Integer.parseInt(platfroms[i]));
				// boolean appc = sendingDao.getApprovedCheck(accountids[i],
				// userid);
				boolean appc = sendingDao.getApprovedCheck(userid);
				if (appc) {
					send.setApproved(1);
				} else {
					send.setApproved(0);
				}
				sendList.add(sender);
			}
			send.setType(2);// 2 转发 3 评论
			send.setText(content);
			send.setSourceid(twitterID);
			send.setFlag(Boolean.parseBoolean(isChecked));
			send.setUserid(userid);
			// 获取当前时间
			String dateTime = "";
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateTime = sdf.format(dt);
			send.setSubmit(dateTime);
			send.setSendList(sendList);
			long sid = sendingDao.insert(send);
			if (sid != 0) {
				send.setId(sid);
				//List<ITweet> tweets = AppServerProxy.uploadPost(send, user);
				/*
				 * for (Sender s : sendList) { XKResult<ITweet> pt =
				 * AppServerProxy.uploadPost(s.getUid(),
				 * Utility.getPlatform(s.getPlatform()), content, null,
				 * this.getUser().getBaseUser()); if(pt.getData() != null){
				 * sendingDao.updateSendStatus(2, sid, pt.getData().getTid());
				 * }else{ sendingDao.updateSendStatus(2, sid, null); } }
				 */

				//countNum = tweets.size();
			}
			//if (countNum == accountids.length) {
				return "转发成功";
			//}
		}

		return null;
	}

}
