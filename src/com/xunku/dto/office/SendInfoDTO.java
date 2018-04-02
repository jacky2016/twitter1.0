package com.xunku.dto.office;
//考核管理-微博发布统计by wanghui	
public class SendInfoDTO {
    private int id;//用户id
    private String name;  //用户名称
    private int subcount; //提交的数量
    private int sendcount;//发布的数量 

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSubcount() {
        return subcount;
    }
    public void setSubcount(int subcount) {
        this.subcount = subcount;
    }
    public int getSendcount() {
        return sendcount;
    }
    public void setSendcount(int sendcount) {
        this.sendcount = sendcount;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
