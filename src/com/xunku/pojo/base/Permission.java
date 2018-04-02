package com.xunku.pojo.base;

public class Permission {
    private int id;
    private String name;       //权限名称
    private String description;//权限描述
    private int resourceID;    //资源编号，这个值是系统里面定义的值，用来控制可访问的资源 
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
    public int getResourceID() {
        return resourceID;
    }
    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }
    
}
