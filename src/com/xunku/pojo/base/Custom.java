package com.xunku.pojo.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.model.AppAccount;
import com.xunku.app.monitor.CustomMonitor;

public class Custom {
	private int id; // 客户编号
	private String name; // 客户名称
	private String code; // 企业代码，冗余设计
	private String contact; // 联系人
	private String tel; // 联系电话
	private String address; // 客户地址
	private String bannerAddress;// banner
	private boolean isEnabled; // 是否启用
	private Date expirationDate;// 过期时间
	private int erpID;
	private Date createTime;
	private String dbserver;

	public String getDbserver() {
		return dbserver;
	}

	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBannerAddress() {
		return bannerAddress;
	}

	public void setBannerAddress(String bannerAddress) {
		this.bannerAddress = bannerAddress;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getErpID() {
		return erpID;
	}

	public void setErpID(int erpID) {
		this.erpID = erpID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void set(Custom that) {
		this.address = that.address;
		this.bannerAddress = that.bannerAddress;
		this.code = that.code;
		this.contact = that.contact;
		this.expirationDate = that.expirationDate;
		this.isEnabled = that.isEnabled;
		this.tel = that.tel;
		this.name = that.name;
	}

	private List<AppAccount> accounts;

	public Custom() {
		this.accounts = new ArrayList<AppAccount>();
	}

	public List<AppAccount> getAccounts() {
		return this.accounts;
	}

	private CustomMonitor _monitor;

	public CustomMonitor getMonitor() {
		if (_monitor == null) {
			_monitor = new CustomMonitor(this);
		}
		return this._monitor;
	}

	@Override
	public int hashCode(){
		return this.id;
	}

}
