package com.xunku.app.model.people;

import com.xunku.app.enums.GenderEnum;

/**
 * 人民网用户对象描述
 * 
 * @author wujian
 * @created on Oct 22, 2014 9:54:29 AM
 */
public class PUser {
	long userId;
	String nickName;
	String location;
	GenderEnum gender;
	String headUrl;
	String personUrl;
	String loginName;
	String password;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public void setGender(int gender) {
		if (gender == 1) {
			this.setGender(GenderEnum.Male);
		} else if (gender == 2) {
			this.setGender(GenderEnum.Famale);
		} else {
			this.setGender(GenderEnum.Unknow);
		}
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getPersonUrl() {
		return personUrl;
	}

	public void setPersonUrl(String personUrl) {
		this.personUrl = personUrl;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
