package com.xunku.app.model;

import java.util.Date;

/**
 * SubscriberTask
 * @author wujian
 * @created on Oct 17, 2014 10:25:04 AM
 */
public class SubscriberTask {

	int subid;
	int taskid;
	Date last;
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public Date getLast() {
		return last;
	}
	public void setLast(Date last) {
		this.last = last;
	} 
	
}
