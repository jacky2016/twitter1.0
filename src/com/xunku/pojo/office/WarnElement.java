package com.xunku.pojo.office;

import java.util.Date;

//预警列表
public class WarnElement {
	private int id;
	private String text;// 消息内容
	private int warnType;// 预警类型：1 账号预警，2 事件预警，3 微博预警 4微博信息
	private int status;// 0 未发生的预警，1 已发生的预警
	private int customid;// 客户编号
	private Date created;// 创建时间
	int reciver;
	int warnId;
	int readed;// 未读 0  已读 1 
	
	
	public int getReaded() {
		return readed;
	}

	public void setReaded(int readed) {
		this.readed = readed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 1 账号预警，2 事件预警，3 微博预警 4微博信息
	 * 
	 * @return
	 */
	public int getWarnType() {
		return warnType;
	}

	/**
	 * 1 账号预警，2 事件预警，3 微博预警 4微博信息
	 * 
	 * @param warnType
	 */
	public void setWarnType(int warnType) {
		this.warnType = warnType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getCustomid() {
		return customid;
	}

	public void setCustomid(int customid) {
		this.customid = customid;
	}

	public int getReciver() {
		return reciver;
	}

	public void setReciver(int reciver) {
		this.reciver = reciver;
	}

	public int getWarnId() {
		return warnId;
	}

	public void setWarnId(int warnId) {
		this.warnId = warnId;
	}
}
