package com.xunku.actions.myTwitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.dto.myTwitter.MyReviewDTO;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/*******************************************************************************
 * <功能描述>我的评论-回复评论
 * 
 * @author shaoqun
 * 
 */
public class MyCommentsAction extends ActionBase {

	@Override
	public Object doAction() {

		SendingDao seDAO = new SendingDaoImpl();
		User user = this.getUser().getBaseUser();
		Sending send = new Sending();
		List<Sender> sendList = new ArrayList<Sender>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String nowdate = sdf.format(new Date());

		String textarea = this.get("textarea");
		String tid = this.get("tid");
		String orginalid = this.get("orginalid");
		String uid = this.get("uid"); // 博主名称
		String state = this.get("state");
		String ucodename = this.get("ucodename");
		send.setUserid(user.getId()); // 谁提交的

		Sender sender = new Sender();
		sender.setPlatform(1);// 新浪平台
		sender.setUid(uid);
		sender.setName(ucodename);
		sendList.add(sender);
		send.setSendList(sendList);
		
		boolean appc = seDAO.getApprovedCheck(user.getId());
		if (appc) {
			send.setApproved(1);
		} else {
			send.setApproved(0);
		}
		
		boolean checked = Boolean.parseBoolean(state);
		send.setText(textarea); // 提交的内容
		send.setSubmit(nowdate);// 提交的时间
		send.setSent(0); // 默认0
		send.setImages(""); // 默认空
		send.setSourceid(tid);// 转发或者评论微博Id
		send.setOrgId(orginalid);
		send.setType(3); // 回复类型3
		send.setFlag(checked);
		long csid = seDAO.insert(send);
		send.setId(csid);

		String warn = "-1";
		// add by wujian 
		if(csid == 0){
			warn = "抱歉评论失败!";
		}
		return warn;
		
		/*
		
		if(csid != 0){
			List<ITweet> list = AppServerProxy.commentReplyPost(send, user);
			if(list.size()>0){
				warn = "-1";     
			}else{
				warn = "抱歉，此微博已被作者删除!评论失败!";
			}
		}
		
		
		return warn;
		*/
	}

}
