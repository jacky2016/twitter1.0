package com.xunku.dto.myTwitter;

import java.util.ArrayList;
import java.util.List;

import com.xunku.app.interfaces.ITweet;

//我的微博中发布管理模块
public class PublishManagerDTO {
	
	/**
	 * 我的微博--发布管理DTO
	 * @author shangwei
	 *
	 */
		public String ids ;//帖子的ID 集合
		//public  List<String> ucodes = new ArrayList<String>(); //   博客发帖子的集合
		public   String weiboAccountHTML;// 拼接html帐号字符串
		public  String text;//提交的内容
		public  String  submitTime;// 提交时间
		public  String  sentTime;//预定发送时间
		public  String  mofityers;//修改人 
		public  String  auditor;//审核人
		public   int   auditorInt;//审核人ID
		public  String   changeType;//是提交还是审核的行为
		//public  int   changeID;//如果审核人是审核人ID ，如果是提交人是提交人ID
		//转发的帖子 也就是阴影部分
	     public PublishManagerRepostDTO   pmr;// 阴影部分的 也就是转发的帖子的一个对象
		
}