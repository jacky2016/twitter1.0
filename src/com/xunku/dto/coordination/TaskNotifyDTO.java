package com.xunku.dto.coordination;

//author   Shangwei
public class TaskNotifyDTO {
  
	/**
	 * 协同办公--任务通知DTO
	 * @author shangwei
	 *
	 */
	public  int  id;
	public   String   totifyContent;//通知名称
	public   String    sendName;//发送人
	public   int  rank;//优先级  0低  1 中  2 高
	public  int  states;//状态   已读1  未读0
	public  String   publicDate;//发布时间
	
}
