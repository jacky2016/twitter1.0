package com.xunku.dto.myTwitter;

import java.util.List;

/*
 * sunao
 * 保存我的微博模块数据的参数对象
 */
public class MyHomeReleaseInfoParam {
	public String content; //内容
	public List<Integer> imageIDs; //图片id集合
	public List<Integer> accountIDs; //账号id集合
	public int sendType; //发送方式类型，0：立即方式，1：定时发送
	public String sendTime; //定时发送时间
}
