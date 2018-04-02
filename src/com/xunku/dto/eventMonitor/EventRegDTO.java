package com.xunku.dto.eventMonitor;
//注册时间分布【用户分析】by wanghui
public class EventRegDTO {
    private float v1;    //半年以内的 
    private float v2;    //半年到1年的
    private float v3;    //一年到两年的
    private float v4;    //两年以上的
    public float getV1() {
        return v1;
    }
    public void setV1(float v1) {
        this.v1 = v1;
    }
    public float getV2() {
        return v2;
    }
    public void setV2(float v2) {
        this.v2 = v2;
    }
    public float getV3() {
        return v3;
    }
    public void setV3(float v3) {
        this.v3 = v3;
    }
    public float getV4() {
        return v4;
    }
    public void setV4(float v4) {
        this.v4 = v4;
    }
}
