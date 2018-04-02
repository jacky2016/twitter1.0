package com.xunku.dto.accountMonitor;

import com.xunku.dto.AccountDTO;

/**
 * 帐号监控--博文分析-30天热门微博列表类
 * @author shaoqun
 *
 */
public class HotweiboListDTO {

	public int id;
	public String imageHead;		//头像
	public String wbname;			//热门微博
	public String createdate;		//创建时间
	public String url;				//内容地址
	public String text;				//微博内容
	public int zfnum;				//转发数
	public int plnum;				//评论数
	public AccountDTO account = new AccountDTO();
}
