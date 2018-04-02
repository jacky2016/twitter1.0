package com.xunku.pojo.log;

public class LogsEntity {
	private String moduleCode; // 模块code
	private String moduleClass; // action对应的完整的类名
	private int userID; // 登陆用户id
	private String userName; // 登陆用户名称
	private int customID; // 客户id
	private String customName; // 客户名称
	private int state; // 记录状态, 0:访问action失败, 1: 访问action成功
	private String logsContent; // 日志内容
	private String ip; // 登陆用户IP
	private String actionName; // actiony异步请求Name

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleClass() {
		return moduleClass;
	}

	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getCustomID() {
		return customID;
	}

	public void setCustomID(int customID) {
		this.customID = customID;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getLogsContent() {
		return logsContent;
	}

	public void setLogsContent(String logsContent) {
		this.logsContent = logsContent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}
