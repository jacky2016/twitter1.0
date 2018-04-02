package com.xunku.dto.pushservices;

import com.xunku.dto.MyPostDTO;


public class WarnServicesDTO {

	/**
	 * 预警服务--预警信息DTO
	 * @author shangwei
	 *
	 */
	public  int  id;
	public  int  autoId;//自动增长的id  为了好看
	public  String  platformStr;// 平台类型 新浪  腾讯 人民
	public  String  weiboTitleName;// 标题栏上的 紧跟着哪个平台后面的字段
	public  String  differentLanguages;// 账号博主在微博中提到了您设置的关键词（帐号预警） |(事件预警) 监测到新浪平台上“MM”事件发生预警，微博数量已经超过XX。 | (微博信息) 监测到新浪平台上微博“MM（列表上显示微博前15字）”发生预警，转发/评论已超过XX。
	public  String  text;// 微博内容
	public  String  warnServicesType;// {0} 为空字符串 即为微博信息or 微博预警 ;为其他的则为 帐号or事件预警 (就是出现后面小眼睛不)
	public  MyPostDTO   postDTO;// 微博信息 or 微博预警里的所有东西的对象
	public  int  readed;//已读 1 未读0
	public  int  isDelete;// 1 表示已经发生预警里的， 如果删除假删除 ; 0 表示微博信息里的  如果删除真删除
	
	
	//-----------------------
	public  int  overtakeNum;// 已超过的多少个 
	public  String  fileds;//高亮的关键词 是以空格分开的
	public  String  weiboAccountName;// 微博帐号名
	public  String  ymd;// 年月日
	public  String  hm;// 时分
	public  String  tid;//帖子的id 
    //public  String url;  
	public  int platform; //哪种类型平台 1 新浪 2 腾讯 5 人民 
	
	public   int   warnType;//   1 帐号预警  2 是事件预警  3  可能是微博预警  (status 1 ) or  微博信息 (status 0)
	public  int   warnStatus;//  0 未发生  1 已发生
}
