package com.xunku.dto.office;

public class NavieDTO {
    private int id;          //水军ID
    private String name;     //水军名称
    private int aCound;      //网评员账号的个数
    private int pCound;      //总转评到我的个数
    private int pageSize;    
    private int pageIndex;   
    private String startTime;//搜索开始时间
    private String endTime;  //结束时间
    private int accoutID;    //微博账号
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
    public int getAccoutID() {
        return accoutID;
    }
    public void setAccoutID(int accoutID) {
        this.accoutID = accoutID;
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
    public int getACound() {
        return aCound;
    }
    public void setACound(int cound) {
        aCound = cound;
    }
    public int getPCound() {
        return pCound;
    }
    public void setPCound(int cound) {
        pCound = cound;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
