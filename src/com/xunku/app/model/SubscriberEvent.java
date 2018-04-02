package com.xunku.app.model;

import java.util.Date;

/**
 * SubscriberEvent 描述
 * @author wujian
 * @created on Oct 17, 2014 10:23:05 AM
 */
public class SubscriberEvent {

	int subid;
	int eventid;
	Date last;
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	public Date getLast() {
		return last;
	}
	public void setLast(Date last) {
		this.last = last;
	}
	
}
