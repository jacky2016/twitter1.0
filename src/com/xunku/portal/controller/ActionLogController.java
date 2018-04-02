package com.xunku.portal.controller;

import com.xunku.actions.IAction;


// Action日志控制器
public class ActionLogController {

	public boolean IsInLogger(IAction action){
		
		String actionName = action.getName();
		
		
		// 根据actionName去找注册信息，如果找到返回true,否则false
		
		if(actionName!=null){
			return true;
		}
		
		return false;
	}
	
	public void ActionLog(String message){
		
		// 记录一条自定义日志
		
	}
	
	public void ActionLogStart(){
		
		// 向日志数据库里面写一条对应Action的日志
		// 记录当前的时间和操作员
		
		
	}
	
	public void ActionLogEnd(){
		
	}
	
}
