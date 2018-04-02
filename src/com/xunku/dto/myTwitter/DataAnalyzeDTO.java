package com.xunku.dto.myTwitter;
/**
 * 我的微博数据分析使用
 * @author wanghui
 *
 */
public class DataAnalyzeDTO {
    private String startTime;//开始日期
    private String endTime;  //结束日期
    private int publishCount;//发布的数量
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getPublishCount() {
        return publishCount;
    }
    public void setPublishCount(int publishCount) {
        this.publishCount = publishCount;
    }
}
