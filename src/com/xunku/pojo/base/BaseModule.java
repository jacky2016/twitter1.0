package com.xunku.pojo.base;

import java.util.ArrayList;
import java.util.List;

public class BaseModule {
    private int id;          //模块的编号
    private String code;     //模块名称
    private String caption;  //模块的标题
    private String description;//模块描述
    private String template; //模块的模板位置
    private int parent;
    private int indes;       //模块的位置信息，Index和系统名冲突，顾叫Indes
    List<BaseModule> module = new ArrayList<BaseModule>();
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTemplate() {
        return template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public int getParent() {
        return parent;
    }
    public void setParent(int parent) {
        this.parent = parent;
    }
    public int getIndes() {
        return indes;
    }
    public void setIndes(int indes) {
        this.indes = indes;
    }
    public List<BaseModule> getModule() {
        return module;
    }
    public void setModule(List<BaseModule> module) {
        this.module = module;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getCaption() {
        return caption;
    }
    
}
