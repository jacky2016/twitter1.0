package com.xunku.dto.office;

public class MessageDTO {
    private int rank;             //优先级 0低1中2高
    private int[] receiver;       //接收人
    private String message;       //发送内容
    private int userid;           //发送人
    private int type=0;           //0-普通消息/1-预警消息/2-微博消息/等其他消息类型（预警消息可能需要处理）
    private String source="Portal"; //消息来源，可描述的来源均可以，如果没指定来源默认是Portal
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public int[] getReceiver() {
        return receiver;
    }
    public void setReceiver(int[] receiver) {
        this.receiver = receiver;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
}
