package com.xunku.pojo.task;

import java.util.Date;
//对应表Task_Count_An by wanghui
public class TaskCount {
    private int id;
    private int customId;//客户编号
    private int sina;    //新浪
    private int tencent; //腾讯
    private int renmin;  //人民
    private Date created;//创建时间
    private Date updated;//更新时间
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCustomId() {
        return customId;
    }
    public void setCustomId(int customId) {
        this.customId = customId;
    }
    public int getSina() {
        return sina;
    }
    public void setSina(int sina) {
        this.sina = sina;
    }
    public int getTencent() {
        return tencent;
    }
    public void setTencent(int tencent) {
        this.tencent = tencent;
    }
    public int getRenmin() {
        return renmin;
    }
    public void setRenmin(int renmin) {
        this.renmin = renmin;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
