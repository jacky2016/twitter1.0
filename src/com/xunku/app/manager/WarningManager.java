package com.xunku.app.manager;

import java.util.List;

import com.xunku.app.enums.PushEnum;
import com.xunku.app.interfaces.ITweet;
import com.xunku.pojo.base.User;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.pojo.office.EventWarn;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.URLUtils;

/**
 * 预警管理器
 * 
 * @author wujian
 * @created on Sep 15, 2014 7:14:48 PM
 */
public class WarningManager {

	/**
	 * 推送一组微博
	 * 
	 * @param pushType
	 *            推送方式
	 * @param userIds
	 *            推给谁
	 * @param emailList
	 *            推给哪个邮件
	 * @param tweets
	 *            这些微博要被推送
	 */
	public void push(PushEnum pushType, List<User> users,
			List<String> emailList, List<ITweet> tweets) {

		// 根据推送类型调用不同的发送方式，推送消息
		//URLUtils.sendNet();
	}

	

}
