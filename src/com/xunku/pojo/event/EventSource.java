package com.xunku.pojo.event;
//来源统计
public class EventSource {
    private long sourceId;
    private int value;
    private int total;
    public long getSourceId() {
        return sourceId;
    }
    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
