package com.xunku.app.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.xunku.ObjectModel.AuthorityType;
import com.xunku.ObjectModel.PermissionStatus;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.CustomRole;
import com.xunku.pojo.base.User;

/**
 * 用户信息描述，包括权限和用户配置
 * 
 * @author wujian
 * @created on Jul 11, 2014 2:05:11 PM
 */
public class UserInfo {

	private User user;

	private Custom custom;// 用户所在的客户

	private List<CustomRole> roles;
	
	private Hashtable<AuthorityType, Hashtable<String, Boolean>> _permissions;// 用户权限

	public UserInfo(User u) {
		user = u;
		roles = new ArrayList<CustomRole>();
	}

	/**
	 * 获得基于当前用户所在客户的角色列表
	 * @return
	 */
	public List<CustomRole> getRoles() {
		
		return this.roles;
	}

	public User getBaseUser() {
		return this.user;
	}

	public Hashtable<AuthorityType, Hashtable<String, Boolean>> getPermissions() {
		return _permissions;
	}

	public void setPermissions(
			Hashtable<AuthorityType, Hashtable<String, Boolean>> pmi) {
		_permissions = pmi;
	}

	/*
	 * 验证当前的用户是否具有某个权限
	 */
	public PermissionStatus checkPerm(String perm, AuthorityType type) {
		if (this._permissions == null)
			return PermissionStatus.Deined;

		if (this._permissions.get(type) == null)
			return PermissionStatus.Deined;

		if (this._permissions.get(type).get(perm) == null) {
			return PermissionStatus.Deined;
		}

		if (this._permissions.get(type).get(perm)) {
			return PermissionStatus.Allow;
		}

		return PermissionStatus.Deined;
	}

	public Custom getCustom() {
		return custom;
	}

	public void setCustom(Custom custom) {
		this.custom = custom;
	}
	public Boolean isAdmin(){
		return user.isAdmin();
	}
}