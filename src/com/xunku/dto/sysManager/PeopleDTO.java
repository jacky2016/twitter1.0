package com.xunku.dto.sysManager;

import com.xunku.app.model.people.PUser;

/***
 * 人民微博账户信息
 *
 * @author shaoqun
 * @created Oct 21, 2014
 */
public class PeopleDTO {

	private int id;
	private int userid;
	private int customId;
	private String username;
	private String password;
	PeopleUser user;// 微博的作者
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCustomId() {
		return customId;
	}
	public void setCustomId(int customId) {
		this.customId = customId;
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
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public PeopleUser getUser() {
		return user;
	}
	public void setUser(PeopleUser user) {
		this.user = user;
	}
	
	
}
