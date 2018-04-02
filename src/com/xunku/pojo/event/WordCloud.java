package com.xunku.pojo.event;
//内容关键词图by wanghui
public class WordCloud {
    private int id;         
    private int eventId;      //事件ID
    private String keyword;   //关键词
    private int count;        //关键词出现的次数
    private String lastupdated;//最后一次更新时间
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
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getLastupdated() {
        return lastupdated;
    }
    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }
}
