package com.xunku.dto.sysManager;

import java.util.List;
/*
 * SysManager_PermissionGroupDTO 系统管理权限组信息
 * @author: hjian
 */
public class ModuleGroupDTO {
	private int id;
	private String name;
	private String code;
	private List<GroupDTO> grouplist;
	
	
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
	public List<GroupDTO> getGrouplist() {
		return grouplist;
	}
	public void setGrouplist(List<GroupDTO> grouplist) {
		this.grouplist = grouplist;
	}

	
	
}
