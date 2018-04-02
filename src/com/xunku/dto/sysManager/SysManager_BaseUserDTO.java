package com.xunku.dto.sysManager;
/*
 * SysManager_BaseUserDTO 系统信息 基本用户信息
 * @author: hjian
 */
public class SysManager_BaseUserDTO {
	
	private int id;
	private int customid;
	private String username="";
	private String nickname="";
	private String token="";
	private String mail="";
	private String tele="";
	private int isadmin;
	private String isadminstr;
	private String createtime;
	private String rolename;
	private int roleid;
	
	private String check;//审核人
	private String ischeck;//是否需要审核
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCustomid() {
		return customid;
	}
	public void setCustomid(int customid) {
		this.customid = customid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public int getIsadmin() {
		return isadmin;
	}
	public void setIsadmin(int isadmin) {
		this.isadmin = isadmin;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getIsadminstr() {
		return isadminstr;
	}
	public void setIsadminstr(String isadminstr) {
		this.isadminstr = isadminstr;
	}
	
	

}
