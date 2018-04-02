package com.xunku.dto.office;

public class MessageVO {
    private int id;            //消息编号
    private String message;    //发送消息
    private String receiver;   //接收人
    private int  rank;       //优先级 0低1中2高
    private String receiveTime;//发送时间
    private int status=-1;      //后台传的状态 -1 全部，0 未读，1 已读
    private int userId;         //当前登录用户id
    private int pageSize;
    private int pageIndex;
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getReceiveTime() {
        return receiveTime;
    }
    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
