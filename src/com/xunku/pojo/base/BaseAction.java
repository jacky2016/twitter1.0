package com.xunku.pojo.base;

public class BaseAction {
    private int id;
    private String moduleCode;      //模块编号
    private String code;
    private String description;//描述
    private String author;     //作者
    private String clazz;      //类名
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getModuleCode() {
        return moduleCode;
    }
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getClazz() {
        return clazz;
    }
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
    
}
