package com.xunku.app.model;

import java.util.ArrayList;
import java.util.List;

import com.xunku.pojo.base.Custom;

public class CustomInfo {

	Custom pojoCustom;
	List<UserInfo> users;

	public Custom getBaseCustom() {
		return this.pojoCustom;
	}

	public CustomInfo(Custom custom) {
		this.pojoCustom = custom;
		this.accounts = new ArrayList<AppAccount>();
		this.users = new ArrayList<UserInfo>();
	}

	private List<AppAccount> accounts;

	public List<AppAccount> getAccounts() {
		return this.accounts;
	}

	public void addUser(UserInfo user) {
		this.users.add(user);
	}
}
