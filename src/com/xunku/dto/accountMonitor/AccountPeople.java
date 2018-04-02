package com.xunku.dto.accountMonitor;

public class AccountPeople {

	private int id;
	private int customID; //当前账户customID
	private String username;//用户账户名
	private String password;//账户密码
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCustomID() {
		return customID;
	}
	public void setCustomID(int customID) {
		this.customID = customID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
}
