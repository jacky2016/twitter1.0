package com.xunku.pojo.push;

import java.util.Date;
import java.util.List;

/**
 * 推送pojo
 * 
 * @author wanghui
 */
public class Subscriber {
	private int id;
	private String name; // 推送名称
	private int creator;
	private int type; // 推送类型 0:日常监测,1:事件监测
	private boolean isSendMail; // 是否发送邮件
	private Date firstRunTime; // 首次执行时间
	private int topN; // 每栏目发送显示条数 5条-15条
	private int periodType; // 周期类型 0:分钟 1:小时 2:天 3:周 4 :月 5:年
	private int periodCount; // 周期数
	private boolean isStop; // 是否停止
	private List<String> contacts;// 推送包含email
	private List<Integer> tasks;// 推送包含任务id
	private List<Integer> events;// 事件监测
	int customid; // 推送任务所属的客户编号
	Date executedTime;//最后一次执行时间
	
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isSendMail() {
		return isSendMail;
	}

	public void setSendMail(boolean isSendMail) {
		this.isSendMail = isSendMail;
	}

	public Date getFirstRunTime() {
		return firstRunTime;
	}

	public void setFirstRunTime(Date firstRunTime) {
		this.firstRunTime = firstRunTime;
	}

	public int getTopN() {
		return topN;
	}

	public void setTopN(int topN) {
		this.topN = topN;
	}

	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	public int getPeriodCount() {
		return periodCount;
	}

	public void setPeriodCount(int periodCount) {
		this.periodCount = periodCount;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public List<Integer> getTasks() {
		return tasks;
	}

	public void setTasks(List<Integer> tasks) {
		this.tasks = tasks;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int userId) {
		this.creator = userId;
	}

	public List<String> getContacts() {
		return contacts;
	}

	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}

	public List<Integer> getEvents() {
		return events;
	}

	public void setEvents(List<Integer> events) {
		this.events = events;
	}

	public int getCustomid() {
		return customid;
	}

	public void setCustomid(int customid) {
		this.customid = customid;
	}

	public Date getExecutedTime() {
		return executedTime;
	}

	public void setExecutedTime(Date executedTime) {
		this.executedTime = executedTime;
	}
}
