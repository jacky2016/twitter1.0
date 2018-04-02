package com.xunku.dto.myTwitter;

import com.xunku.dto.AccountDTO;

/**
 * 我的评论--数据列表类
 * @author shaoqun
 *
 */
public class MyReviewDTO{

	public String id;// 转发或者评论微博Id
	public String orginalid;// 原始微博ID 
	public AccountDTO account = new AccountDTO(); 
	public String content; 	 	 		 	//回复的内容
	public String posttext; 	 			//微博内容
	public String createtime; 	 			//回复时间 
	public String url; 	 					//微博内容连接 
	public String source;	 	 			//来源
	public boolean isCreative;				//是否是当前微博true: 是，false: 不是
	public MyReviewDTO my;

}
