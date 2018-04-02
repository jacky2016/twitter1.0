package com.xunku.dto;

import java.util.ArrayList;
import java.util.List;

/*
 * 微博通用DTO
 * @author sunao
 */
public class MyPostDTO {

	public int id = 0; // 数据库postID
	public String tid = ""; // 微博ID
	// 博主基本信息
	public AccountDTO account = new AccountDTO();
	public String url = "";
	public String text = ""; // 文本值
	public String createtime = ""; // 定时发布时间 2014-03-14 12:33:22
	public String analysisTime = "";// 传播分析模块分析时间

	// 从什么设备上发布的
	public String source = "";
	public long sourceID = 0; // 从什么设备上发布的来源ID
	public int repostCount = 0;// 转发数
	public int commentCount = 0;// 评论数
	public boolean isCreative = true; // 是否为原帖,true: 是，false: 不是
	public boolean isAnalysised = false; // 默认正在分析中，不能点击
	// f,排序好的
	public List<String> postImage = new ArrayList<String>();
	// 采样列表
	public List<RepostDTO> postRepost = new ArrayList<RepostDTO>();// 微博转发
	public List<CommentDTO> postComment = new ArrayList<CommentDTO>();// 微博评论
	public MyPostDTO item;

}
