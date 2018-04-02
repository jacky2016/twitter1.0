package com.xunku.app.model;

import java.util.ArrayList;
import java.util.List;

import com.xunku.app.enums.AccountType;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.people.PUser;
import com.xunku.pojo.base.AccountInfo;

/**
 * 帐号信息，这个对象和AccountInfo对象定义重复了....暂时先不改
 * <p>
 * 该帐号是用来调用API使用的，一个帐号可以对应多个应用的授权
 * <p>
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:43:30 PM
 */
public class AppAccount {

	int accountId;
	String uid;
	String ucode;
	Platform platform;
	String password;
	AccountType type;
	String loginName;
	List<AppAuth> auths;
	String name;
	String key;// 用户的key，这个字段新浪帐号没有，腾讯帐号有

	boolean isLoadedFullFans;// 是否装载过全部粉丝
	boolean verify;

	private AppAccount() {
		this.auths = new ArrayList<AppAuth>();
	}

	/**
	 * 给当前帐号设置一个应用授权，新的授权会覆盖旧的授权
	 * 
	 * @param appid
	 * @param auth
	 */
	public void AddAuth(AppAuth a) {
		AppAuth auth = this.getAuth(a.getAppId());
		if (auth == null) {
			this.auths.add(a);
		} else {
			// 新授权刷新旧授权
			auth = a;
		}
	}

	/**
	 * 获得指定应用的授权信息
	 * 
	 * @param appid
	 * @return
	 */
	public AppAuth getAuth(int appid) {
		for (AppAuth auth : this.auths) {
			if (auth.getAppId() == appid) {
				return auth;
			}
		}
		return null;
	}

	public List<AppAuth> getAuths() {
		return this.auths;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String name) {
		loginName = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("UID: " + this.uid + "\n");
		buf.append("Name: " + this.name + "\n");
		buf.append("Login Name: " + this.loginName + "\n");
		buf.append("Account Id: " + this.accountId + "\n");
		buf.append("Platform: " + this.platform + "\n");
		buf.append("Password: " + this.password + "\n");
		return buf.toString();
	}

	public static AppAccount create(AccountInfo acc) {
		AppAccount result = new AppAccount();
		result.setAccountId(acc.getId());
		result.setLoginName(null);// 官微不记录登录名
		result.setName(acc.getName());
		result.setPassword(null);// 官微不记录登录的密码
		result.setPlatform(acc.getPlatform());
		result.setType(AccountType.OfficalAccount);
		result.setUid(acc.getUid());
		result.setUcode(acc.getUcode());
		result.setLoadedFullFans(acc.isLoadedFullFans());
		result.setVerify(acc.isVerify());
		return result;
	}

	/**
	 * 创建一个人民的帐号
	 * 
	 * @param acc
	 * @param accountid
	 * @return
	 */
	public static AppAccount createPeople(PUser acc, int accountid) {
		AppAccount result = new AppAccount();
		result.setAccountId(accountid);
		result.setLoginName(acc.getLoginName());
		result.setName(acc.getNickName());
		result.setPassword(acc.getPassword());
		result.setPlatform(Platform.Renmin);
		result.setType(AccountType.OfficalAccount);
		result.setUid(String.valueOf(acc.getUserId()));
		result.setUcode(result.getUid());
		result.setLoadedFullFans(true);
		result.setVerify(false);
		return result;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isLoadedFullFans() {
		return isLoadedFullFans;
	}

	public void setLoadedFullFans(boolean isLoadedFullFans) {
		this.isLoadedFullFans = isLoadedFullFans;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}
}
