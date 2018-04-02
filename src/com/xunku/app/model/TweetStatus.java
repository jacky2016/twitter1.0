package com.xunku.app.model;

/**
 * 微博的状态信息
 * @author wujian
 * @created on Jul 4, 2014 11:44:00 AM
 */
public class TweetStatus {

	// 微博被搜集的id
	int collectID;
	
	// 微博被检测的id
	int moniterID;
	
	// 预警ID
	int warningID;
	
	

	public int getCollectID() {
		return collectID;
	}

	public void setCollectID(int collectID) {
		this.collectID = collectID;
	}

	public int getMoniterID() {
		return moniterID;
	}

	public void setMoniterID(int moniterID) {
		this.moniterID = moniterID;
	}

	public int getWarningID() {
		return warningID;
	}

	public void setWarningID(int warningID) {
		this.warningID = warningID;
	}
	
}
