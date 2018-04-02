package com.xunku.dto.eventMonitor;
//来源分布by wanghui
public class EventSourceDTO {
    private String source;//来源名称
    private float count;  //所占比例
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public float getCount() {
        return count;
    }
    public void setCount(float count) {
        this.count = count;
    }
}
