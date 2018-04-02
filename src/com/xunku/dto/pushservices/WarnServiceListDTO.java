package com.xunku.dto.pushservices;

public class WarnServiceListDTO {
		
		 public int id;  
		 public String nickName;  // 昵称
		 public String  text;  //内容
		 public  int curRepost;  // 当前转发数
		 public  int  curComment; // 当前的评论数
		 public  int  warnRepost;// 预警的转发阀值
		 public  int  warnComment;// 预警的评论阀值
		 public  String  endTime;//   预警的结束时间 
		 public  String  recevier;// 接受人
		 public  String  revetype ;// 接受的方式   比如邮件 ，  手机短信 
		 public   int   isDelete; // 真假删除  1 代表是假删除
}
