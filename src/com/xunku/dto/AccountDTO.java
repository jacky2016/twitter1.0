package com.xunku.dto;

// author:huangjian
// 帐号基本信息异步DTO
// 
public class AccountDTO {
	public int id;
	public String uid; // 微博账号uid
	public String name;
	public String imgurl;
	public String imgurlBig;
	public String url; // 微博头像链接
	public int friends; // 关注
	public int weibos; // 发布微博数
	public int followers;// 粉丝数
	public int twitterType = 1;// 微博类型
	public String summany;
	public String area;// 地点
	public Boolean isAjax = true; // 前端是否需要异步加载头像（默认是加载）
	public String ucode; // 查询微博账号信息的ucode，如果account==null，鼠标经过的时候调用异步查出微博账号信息，不为null就不用在异步获取

}
