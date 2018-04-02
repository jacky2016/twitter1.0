package com.xunku.dto.pubSentiment;

import java.util.ArrayList;
import java.util.List;
/*
 * TaskGroupsDTO 微博舆情 任务分组
 * @author: hjian
 */
public class TaskGroupsDTO {
	private int id;
	private String groupname;//组名
	private int parentid;
	private List<TaskGroupsDTO> groups = new ArrayList<TaskGroupsDTO>();
	private List<TaskDTO> tasks = new ArrayList<TaskDTO>();
	private int orderid;
	
	
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public List<TaskGroupsDTO> getGroups() {
	    return groups;
	}
	public void setGroups(List<TaskGroupsDTO> groups) {
	    this.groups = groups;
	}
	public List<TaskDTO> getTasks() {
	    return tasks;
	}
	public void setTasks(List<TaskDTO> tasks) {
	    this.tasks = tasks;
	}
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
}
