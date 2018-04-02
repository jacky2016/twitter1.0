package com.xunku.dto.sysManager;
/*
 * SysManager_TaskGroupDTO 系统管理 任务组信息
 * @author: hjian
 */
public class SysManager_TaskGroupDTO {
	private int id;
	private String groupname;
	private int parentid;
	private int customid;
	private int sort;
	private int option;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	public int getCustomid() {
		return customid;
	}
	public void setCustomid(int customid) {
		this.customid = customid;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getOption() {
		return option;
	}
	public void setOption(int option) {
		this.option = option;
	}
	
}
