package com.xunku.pojo.task;

public class Rubbish {
    private int id;
    private int customID;      //客户编号
    private String groupName;     //垃圾词组
    private String rubbishWords;//垃圾词，用逗号分隔
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCustomID() {
        return customID;
    }
    public void setCustomID(int customID) {
        this.customID = customID;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getRubbishWords() {
        return rubbishWords;
    }
    public void setRubbishWords(String rubbishWords) {
        this.rubbishWords = rubbishWords;
    }
}
