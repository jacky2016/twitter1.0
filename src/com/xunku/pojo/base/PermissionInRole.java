package com.xunku.pojo.base;
//分配权限 by wanghui
public class PermissionInRole {
    private String mcode;//一级菜单
    private String code; //二级菜单
    private String uicode;//权限
    public String getMcode() {
        return mcode;
    }
    public void setMcode(String mcode) {
        this.mcode = mcode;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getUicode() {
        return uicode;
    }
    public void setUicode(String uicode) {
        this.uicode = uicode;
    }
}
