package com.xunku.pojo.event;
//关键观点TOP【内容分析】by wanghui
public class KeyPoint {
    private int id;
    private int eventId;//事件id
    private String tid; //微博id
    private int reposts;//转发数
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public int getReposts() {
        return reposts;
    }
    public void setReposts(int reposts) {
        this.reposts = reposts;
    }
}
