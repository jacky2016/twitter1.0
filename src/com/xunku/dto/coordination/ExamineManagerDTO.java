package com.xunku.dto.coordination;

import java.util.ArrayList;
import java.util.List;

import com.xunku.dto.AccountDTO;
import com.xunku.dto.myTwitter.MyReviewDTO;

public class ExamineManagerDTO {
			
	/**
	 * 协同办公--考核管理DTO
	 * @author shangwei
	 *
	 */
	public  int id;
	public String  content;//内容
	
	/**
	 * 微博内容统计的几个字段(列表)
	 */
	public  String  date;//日期
	public  String  tid;//帖子的id
	public  int   forwardNum;//转发数
	public  int  reviewNum;//评论数
	public   int   organizationNum;//转发机构数量
	public   int platform;//平台类型 1 新浪 2 腾讯   5 人民
	public   List<String>  imageUrls=new ArrayList<String>();//图片的URL地址集合 
	
	
	/**
	 * 微博发布统计的几个字段(列表)
	 */
	public  String uid;//博主ID
	public  String   submitName;//提交人员名字
	public  int submitNum;//提交数
	public int adoptNum;//采纳数
	
	/**
	 * 微博处理统计的几个字段(列表)
	 */
	public  String  dealName;//处理人员名字
	public  int comments;//处理的评论数
	public  int reposts;//处理的转发数
	
	/**
	 * 微博内容(处理)统计的评论(列表)
	 */
	public String   dealwithCommentsID;//   评论此帖子的 tid号
	public AccountDTO account = new AccountDTO(); 
	public String dealwithcontent; 	 	 		 	//回复的内容
	public String posttext; 	 			//微博内容
	public String createtime; 	 			//回复时间 
	public String source;	 	 			//来源
	public String sourceID;//原微博ID
	public String  comtorsURL;//评论的 原始URL链接
	public ExamineManagerDTO   dealwithDto;
	
	
	
	
	/**
	 * 网评员统计的几个字段(列表)
	 */
	public  String commentatorsName;//网评员名字
	public  int   commentatorsAccountNum;//网评员帐号个数
	public  int   totalRepostNum; //总转评到我的个数
	
	/**
	 * 网评员统计中添加按钮中输入当前网评员的列表的
	 */
	public  String  personName;//人员名字
	public  String  platType;//微博类型1 新浪 2 腾讯  5 人民
	public  String  accName;//微博名
	public  String  comtorsUID;//网评员中的删除中需要删除哪个微博的uid的值

	/**
	 * 网评员统计列表中的网评员账号个数列表（全部网评员展示）-----字段   
	 */
	public String  comtorsEveryOneName;//网评员的人名
	public String  comtorsAccName;//网评员账号个数的帐号名 (帐号昵称)
	public  String comtorsAccPlatform;//网评员账号个数的帐号的微博类型  (媒体类型)
    public  String comtorsAllUID;// 网评员下的此微博帐号的uid
	
}
