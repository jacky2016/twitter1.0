package com.xunku.dto.office;
//网评员统计信息【考核管理】by wanghui
public class NavieCountDTO {
    private int id;    //网评员编号
    private String name;//网评员名称
    private int acount;//网评员账号的个数
    private int wcount;//总转评到我的个数
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
    public int getAcount() {
        return acount;
    }
    public void setAcount(int acount) {
        this.acount = acount;
    }
    public int getWcount() {
        return wcount;
    }
    public void setWcount(int wcount) {
        this.wcount = wcount;
    }
}
