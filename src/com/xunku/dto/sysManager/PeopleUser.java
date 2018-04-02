package com.xunku.dto.sysManager;

import com.xunku.app.enums.GenderEnum;

/***
 * 人民微博作者信息
 *
 * @author shaoqun
 * @created Oct 22, 2014
 */
public class PeopleUser {
	long puserId;
	String nickName;
	String location;
	GenderEnum gender;
	String headUrl;
	String personUrl;
	public long getPuserId() {
		return puserId;
	}
	public void setPuserId(long puserId) {
		this.puserId = puserId;
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
}
