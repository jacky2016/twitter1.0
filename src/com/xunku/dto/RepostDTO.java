package com.xunku.dto;
/*
 * 微博转发DTO
 * @author sunao
 */
public class RepostDTO {
    public int id; // 删除评论时使用
    
    //private int postID;      //回复的哪个微博
    
    public String text;  // 转发的内容
    public AccountDTO account;   //谁回复的
    
    public String createTime;//什么时间回复的
}
