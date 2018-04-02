package com.xunku.dto.pushservices;

public class EventDTO {
    private int id;     //事件编号
    private String name;//事件名称
    private int platform;// 平台类型 1 新浪  2 腾讯  5 人民
    
    public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
