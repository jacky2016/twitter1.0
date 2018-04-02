package com.xunku.app.result.spread;

import java.util.ArrayList;
import java.util.List;

import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.KV;
import com.xunku.app.result.Result;

/**
 * 转发层级分析结果
 * 
 * @author wujian
 * @created on Jul 16, 2014 3:45:44 PM
 */
public class RetweetLevelResult extends Result {

	int level;// 当前的层级
	int _tweets;// 当前这个层级多少微博
	int followers;// 当前这个层级覆盖了多少人

	public RetweetLevelResult() {
		this.superMans = new ArrayList<KV<IAccount, Integer>>();
	}

	List<KV<IAccount, Integer>> superMans;

	public List<KV<IAccount, Integer>> getSuperMans() {
		return this.superMans;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTweets() {
		return _tweets;
	}

	public void setTweets(int tweets) {
		this._tweets = tweets;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

}
