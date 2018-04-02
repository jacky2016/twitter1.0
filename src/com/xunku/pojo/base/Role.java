package com.xunku.pojo.base;

public class Role {
    private int id;            //角色编号
    private String name;       //角色名称
    private String description;//角色描述
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
