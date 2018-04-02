package com.xunku.pojo.office;

public class Message {
    private int id;                 //
    private int type=0;             //0-普通消息/1-预警消息/2-微博消息/等其他消息类型（预警消息可能需要处理）
    private String message;         //消息内容
    private String source="Portal"; //消息来源，可描述的来源均可以，如果没指定来源默认是Portal
    private int rank;               //级别（0低1中2高3紧急）
    private String createdTime;     //创建时间（发送时间）
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
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
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

}
