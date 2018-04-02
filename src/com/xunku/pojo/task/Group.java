package com.xunku.pojo.task;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private int id;
	private String groupName;// 分组名称
	private int parentID; // 分组的父分组NULL为无父分组，程序控制三层，数据库结构不控制层次
	private int customID; // 当前客户编号

	int sina;// 该分组上新浪的微博有多少条
	int qq;
	int renmin;
	List<Integer> subGroupIds = new ArrayList<Integer>();

	public int getCustomID() {
		return customID;
	}

	public void setCustomID(int customID) {
		this.customID = customID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public int getSina() {
		return sina;
	}

	public void setSina(int sina) {
		this.sina = sina;
	}

	public int getQq() {
		return qq;
	}

	public void setQq(int qq) {
		this.qq = qq;
	}

	public int getRenmin() {
		return renmin;
	}

	public void setRenmin(int renmin) {
		this.renmin = renmin;
	}

	public List<Integer> getSubGroupIds() {
		return subGroupIds;
	}
}
