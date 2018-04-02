package com.xunku.dto.base;

// author :wanghui
// 系统设置使用DTO
public class WeiboAccountDTO {
    private int id;            //博主的id
    private String typeName;   //微博类型名称（1:新浪/2:腾讯/5:人民）
    private String name;       //微博帐号名称 
    private String nickName;   //微博昵称
    private String status;     //微博状态
    private int day;           //剩余天数
    //private String dayStatus;  //如果没有天数限制（自动授权，永久有效）
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    
}
