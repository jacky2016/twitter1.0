package com.xunku.app.model.accounts;

import java.util.List;
import java.util.Map;

import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.location.City;
/**
 * 不存在的用户
 * @author wujian
 * @created on Sep 18, 2014 2:46:17 PM
 */
public class AccountEmpty implements IAccount {

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
		return System.currentTimeMillis();
	}

	@Override
	public String getDescription() {
		return "该用户已经被删除，或者暂时不可用，请联系管理员.";
	}

	@Override
	public String getDisplayName() {
		return "该用户不存在";
	}

	@Override
	public int getFollowers() {
		return 0;
	}

	@Override
	public int getFriends() {
		return 0;
	}

	@Override
	public GenderEnum getGender() {
		return GenderEnum.Unknow;
	}

	@Override
	public String getHead() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHomeUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLargeHead() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLocation() {
		return "未知";
	}

	@Override
	public String getName() {
		return "该用户不存在";
	}

	@Override
	public Platform getPlatform() {
		return Platform.Sina;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
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
