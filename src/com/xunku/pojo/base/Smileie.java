package com.xunku.pojo.base;

public class Smileie {
    private int id;
    private String name;    //表情的名称
    private String fileName;//表情路径Path不包含根，表情的根定义在配置文件中例如：$/smile/haha.png，其中$是根目录的占位符
    private String sina;
    private String qq;
    private String renmin;
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
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getSina() {
        return sina;
    }
    public void setSina(String sina) {
        this.sina = sina;
    }
    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getRenmin() {
        return renmin;
    }
    public void setRenmin(String renmin) {
        this.renmin = renmin;
    }
}
