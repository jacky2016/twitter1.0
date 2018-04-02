package com.xunku.pojo.base;

import java.util.List;
import java.util.Map;

public class CustomRole {
    private int id;
    private String name;//角色名称
    private int customId;//客户编号
    private Map<String, List<String>> Permission;//权限id

    public Map<String, List<String>> getPermission() {
        return Permission;
    }
    public void setPermission(Map<String, List<String>> permission) {
        Permission = permission;
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
    public int getCustomId() {
        return customId;
    }
    public void setCustomId(int customId) {
        this.customId = customId;
    }
}
