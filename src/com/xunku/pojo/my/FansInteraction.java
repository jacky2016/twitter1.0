package com.xunku.pojo.my;
//每个博主的粉丝和其互动的信息表 by wanghui
//对应表My_Fans_Interaction
public class FansInteraction {
    private int id;
    private String ucode;//博主唯一标识
    private String fansucode;//粉丝的唯一标识ucode
    private int value; //互动次数
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUcode() {
        return ucode;
    }
    public void setUcode(String ucode) {
        this.ucode = ucode;
    }
    public String getFansucode() {
        return fansucode;
    }
    public void setFansucode(String fansucode) {
        this.fansucode = fansucode;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}
