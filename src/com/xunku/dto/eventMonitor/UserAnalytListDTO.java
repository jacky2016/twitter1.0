package com.xunku.dto.eventMonitor;

import com.xunku.dto.AccountDTO;

/**
 * 事件监控--关键用户列表类
 * @author shaoqun
 *
 */
public class UserAnalytListDTO {

		public int id;
		public String imageHead;				//头像
		public String nickname;			//昵称
		public String area;				//地域
		public int fansnum;				//粉丝数
		public String authentication;	//用户类型

		// 博主基本信息
		public AccountDTO account = new AccountDTO();
}