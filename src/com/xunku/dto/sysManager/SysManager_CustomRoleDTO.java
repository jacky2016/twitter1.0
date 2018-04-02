package com.xunku.dto.sysManager;
/*
 * SysManager_CustomRoleDTO 系统管理 客户角色信息
 * @author: hjian
 */
public class SysManager_CustomRoleDTO {
	private int id;
	private String name;
	private String description;
	private String createtime;
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
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
