package com.xunku.dto.sysManager;

import java.util.ArrayList;
import java.util.List;

import com.xunku.dto.sysManager.UIElementCode;

public class ModuleCode {
    private String mcode; // 菜单code
    private String desc; // 描述
    private int pid;
    private List<ModuleCode> moduleList = new ArrayList<ModuleCode>();//二级菜单
    private List<UIElementCode> uicode = new ArrayList<UIElementCode>();// 菜单里的ui权限列表

    public ModuleCode() {
    }

    public List<UIElementCode> getUicode() {
	return uicode;
    }

    public void setUicode(List<UIElementCode> uicode) {
	this.uicode = uicode;
    }

    public String getMcode() {
	return mcode;
    }

    public void setMcode(String mcode) {
	this.mcode = mcode;
    }

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<ModuleCode> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<ModuleCode> moduleList) {
        this.moduleList = moduleList;
    }
}
