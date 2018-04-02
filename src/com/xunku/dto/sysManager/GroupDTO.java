package com.xunku.dto.sysManager;

import java.util.List;

/*
 * SysManager_BasePermissionDTO 系统管理 基本权限
 * @author: hjian
 */
public class GroupDTO {
	private int id;
	private String name;
	private String code;
	private List<UIElementCode> uicodelist;
	
	
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<UIElementCode> getUicodelist() {
		return uicodelist;
	}
	public void setUicodelist(List<UIElementCode> uicodelist) {
		this.uicodelist = uicodelist;
	}
	
	
}
