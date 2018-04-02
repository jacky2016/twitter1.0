package com.xunku.dto.task;

public class RubbishDTO {
    private int id;
    private int customId;       //客户编号
    private String groupName;   //分类名称
    private String rubbishWords;//垃圾词
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
