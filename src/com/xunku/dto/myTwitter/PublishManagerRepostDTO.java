package com.xunku.dto.myTwitter;

import java.util.ArrayList;
import java.util.List;


//发布管理中的转发的原帖DTO
public class PublishManagerRepostDTO {

	/**
	 * 我的微博--发布管理 如有是转发的  此为转发原帖的DTO
	 * @author shangwei
	 */

	public int id;
	public String  repostName;//转发原帖的作者
	public String  repostTime;//转发原帖的时间
	public  String  repostText;//转发原帖的内容
	public  int  repostNum;//转发原帖的转发数
	public  int  commentNum;//转发原帖的评论数
	public  	List<String> Images=new ArrayList<String>();//转发原帖的照片列表
	
}
