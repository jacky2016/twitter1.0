package com.xunku.pojo.event;
//注册时间分布by wanghui
public class EventReg {
    private int eventId;//事件ID
    private int v1;    //半年以内的 
    private int v2;    //半年到1年的
    private int v3;    //一年到两年的
    private int v4;    //两年以上的
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public int getV1() {
        return v1;
    }
    public void setV1(int v1) {
        this.v1 = v1;
    }
    public int getV2() {
        return v2;
    }
    public void setV2(int v2) {
        this.v2 = v2;
    }
    public int getV3() {
        return v3;
    }
    public void setV3(int v3) {
        this.v3 = v3;
    }
    public int getV4() {
        return v4;
    }
    public void setV4(int v4) {
        this.v4 = v4;
    }
}
