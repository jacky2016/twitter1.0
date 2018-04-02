package com.xunku.actions.myTwitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.UploadImage;
import com.xunku.app.result.Result;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.my.PostDao;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.my.PostDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.ImageUtil;

/*
 * 我的首页模块，发布一条微博信息
 * @author sunao
 */
public class MyHomeReleaseInfoAction extends ActionBase {

	PostDao postDao = new PostDaoImpl();
	SendingDao sendingDao = new SendingDaoImpl();
	AccountDao accountDao = new AccountDaoImpl();

	@Override
	public String doAction() {
		String content = this.get("content");// 内容
		String images = this.get("images"); // 图片id集合
		String accountIDs = this.get("accountIDs");// 账号ids集合
		String sendType = this.get("sendType"); // 发送方式类型，0：立即方式，1：定时发送
		String sendTime = this.get("sendTime");// 定时发送时间
		String sendNames = this.get("sendNames"); // 发布账号，拼接字符串
		User user = this.getUser().getBaseUser();
		int userid = user.getId();

		String[] accountids = accountIDs.split(",");
		String[] imagesArray = images.split(",");
		String[] sendNameArray = sendNames.split(",");

		// 如果只上传图片，没有内容，就默认加上 "分享图片"
		if (!images.equals("") && content.equals("")) {
			content = "分享图片";
		}

		List<Sender> sendList = new ArrayList<Sender>();
		int index = 0;
		for (String uid : accountids) {
			AccountInfo info = accountDao.queryByUid(uid);
			Sender sender = new Sender();
			if (info != null) {
				sender.setUid(uid);
				sender.setPlatform(Utility.getPlatform(info.getPlatform()));
				sender.setName(sendNameArray[index]);
			}
			sendList.add(sender);
			index++;
		}

		// 时间
		long time = 0;
		if (sendType.equals("1"))
			time = DateUtils.dateStringToInteger(sendTime);

		Sending entity = new Sending();
		boolean b = sendingDao.getApprovedCheck(userid);
		if (b) {
			entity.setApproved(1);
		} else {
			entity.setApproved(0);
		}
		entity.setSendList(sendList);
		entity.setUserid(userid);

		entity.setImages(images); // 逗号分隔多张图片
		entity.setSent(time);
		entity.setText(content);
		entity.setType(1);

		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		temp_str = sdf.format(dt);
		entity.setSubmit(temp_str);

		Result<Sending> result = AppServerProxy.submitSending(entity, user);
		String str = "发布成功";
		if (result.getErrCode() != 0) {
			str = result.getMessage();
		}
		return str;
	}

}
