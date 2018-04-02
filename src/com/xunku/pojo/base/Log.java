package com.xunku.pojo.base;

public class Log {
    private int id;            //日志编号
    private int type;          //日志类型
    private int moduleID;      //模块ID
    private String className;  //产生日志的类名
    private String action;     //日志的行为
    private String method;     //产生日志的方法
    private String description;//日志描述信息
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getModuleID() {
        return moduleID;
    }
    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}
