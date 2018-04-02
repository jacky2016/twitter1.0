package com.xunku.app.model.accounts;

import java.util.List;
import java.util.Map;

import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.location.City;

/**
 * 人民的帐号
 * 
 * @author wujian
 * @created on Sep 15, 2014 7:43:27 PM
 */
public class AccountRenmin implements IAccount {

	boolean _isXunku;
	String _ucode;
	String _head;
	String _name;

	public void setName(String value) {
		this._name = value;
	}

	public void setHead(String value) {
		this._head = value;
	}

	public void setXunku(boolean value) {
		this._isXunku = value;
	}

	public void setUcode(String value) {
		this._ucode = value;
	}

	@Override
	public String ToJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public City getCity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCreated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFollowers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFriends() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GenderEnum getGender() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHead() {
		return this._head;
	}

	@Override
	public String getHomeUrl() {
		return "http://t.people.com.cn/"+this._ucode;
	}

	@Override
	public String getLargeHead() {
		return this._head;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this._name;
	}

	@Override
	public Platform getPlatform() {
		return Platform.Renmin;
	}

	@Override
	public List<String> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getUcode() {
		return this._ucode;
	}

	@Override
	public int getWeibos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isVerified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isXunku() {
		return this._isXunku;
	}

	@Override
	public void setTimestamp(long timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> toHashMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
