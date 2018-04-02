package com.xunku.app.result;

import java.util.List;
import java.util.Map;

import com.xunku.app.interfaces.IAccount;

/**
 * 粉丝统计结果
 * 
 * @author wujian
 * @created on Jul 21, 2014 6:36:19 PM
 */
public class FansResult {

	VipResult Vip;
	Map<String, Integer> Cnt;
	GenderResult Grender;
	int[] FollowersNums;
	Map<String, Integer> Locations;
	List<IAccount> Supermans;

	public VipResult getVip() {
		return Vip;
	}

	public void setVip(VipResult vip) {
		Vip = vip;
	}

	public Map<String, Integer> getCnt() {
		return Cnt;
	}

	public void setCnt(Map<String, Integer> cnt) {
		Cnt = cnt;
	}

	public GenderResult getGrender() {
		return Grender;
	}

	public void setGrender(GenderResult grender) {
		Grender = grender;
	}


	public Map<String, Integer> getLocations() {
		return Locations;
	}

	public void setLocations(Map<String, Integer> locations) {
		Locations = locations;
	}

	public List<IAccount> getSupermans() {
		return Supermans;
	}

	public void setSupermans(List<IAccount> supermans) {
		Supermans = supermans;
	}

	public int[] getFollowersNums() {
		return FollowersNums;
	}

	public void setFollowersNums(int[] followersNums) {
		FollowersNums = followersNums;
	}

}
