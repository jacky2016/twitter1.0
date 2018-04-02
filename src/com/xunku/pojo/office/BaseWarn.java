package com.xunku.pojo.office;

import java.util.Date;

public class BaseWarn {

	/*
	 * private String receiver;//接收人,多个接收人用逗号分隔 private String
	 * type;//预警的发送方式：系统消息，邮件，短信 private int customid;//客户编号 private Date
	 * created;//创建时间
	 */
	private String receiver; // 接收人
	private String type; // 预警的发送方式：系统消息，邮件，短信
	private int customid; // 客户编号
	private Date created; // 创建时间
	boolean isRunning;// 是否预警

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * 预警发送的方式，系统消息，邮件，短信
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCustomid() {
		return customid;
	}

	public void setCustomid(int customid) {
		this.customid = customid;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
