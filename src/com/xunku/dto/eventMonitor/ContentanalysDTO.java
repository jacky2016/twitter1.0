package com.xunku.dto.eventMonitor;

import java.util.Date;

import com.xunku.dto.AccountDTO;

/***
 * 事件监测-内容分析-关键观点列表类
 * @author Administrator
 *
 */
public class ContentanalysDTO {
	
	public long id;
	public String imageHead;		//头像
	public String nickname;			//昵称
	public String text;				//内容
	public String authentication;	//认证
	public int fansnum;				//粉丝数
	public int zfnum;				//转发数
	public String createtime;		//创建时间
	
	// 博主基本信息
	public AccountDTO account = new AccountDTO();


}
