package com.xunku.pojo.task;

public class CollectionGroup {
    private int id;          //分类编号
    private String groupName;//分类名称
    private int customId;    //客户编号
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public int getCustomId() {
        return customId;
    }
    public void setCustomId(int customId) {
        this.customId = customId;
    }
}
