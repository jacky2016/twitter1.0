package com.xunku.dto.accountMonitor;

/**
 * 功能描述：账号预警
 * 
 * @author shaoqun
 * @see 对应表Office_Account_Warning
 */
public class AccountWarn {

	private int id;
	private int accid; // 帐号监控id
	private String groupName;// 关键词组
	private String keyword;// 关键词列表
	private String receiver; // 接收人
	private String type; // 类型
	private int customid; // 客户编号
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccid() {
		return accid;
	}
	public void setAccid(int accid) {
		this.accid = accid;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
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

}
