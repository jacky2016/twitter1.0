package com.xunku.pojo.base;

import java.util.Date;

import com.xunku.app.enums.Platform;

public class AccountInfo {

	private int customId;
	private int id;
	String ucode;
	String name;
	Platform platform;
	String uid;
	int creator;
	private Date authTime;
	private int expiresin;
	boolean verify;
	boolean isLoadedFullFans;
	
	
	public int getCustomId() {
		return customId;
	}
	public void setCustomId(int customId) {
		this.customId = customId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUcode() {
		return ucode;
	}
	public void setUcode(String ucode) {
		this.ucode = ucode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public Date getAuthTime() {
	    return authTime;
	}
	public void setAuthTime(Date authTime) {
	    this.authTime = authTime;
	}
	public int getExpiresin() {
	    return expiresin;
	}
	public void setExpiresin(int expiresin) {
	    this.expiresin = expiresin;
	}
	public boolean isVerify() {
		return verify;
	}
	public void setVerfiy(boolean verify) {
		this.verify = verify;
	}
	public boolean isLoadedFullFans() {
		return isLoadedFullFans;
	}
	public void setLoadedFullFans(boolean isLoadedFullFans) {
		this.isLoadedFullFans = isLoadedFullFans;
	}
}
