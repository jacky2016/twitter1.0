package com.xunku.dto.pubSentiment;
/*
 * TaskCollectionGroupDTO 任务分组集合
 * @author: hjian
 */
public class TaskCollectionGroupDTO {
	private int id;
	private String groupName;
	private int customID;
	private int operation;
	
	public int getOperation() {
		return operation;
	}
	public void setOperation(int operation) {
		this.operation = operation;
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
	public int getCustomID() {
		return customID;
	}
	public void setCustomID(int customID) {
		this.customID = customID;
	}
	
}
