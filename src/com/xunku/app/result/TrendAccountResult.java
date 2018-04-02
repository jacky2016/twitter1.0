package com.xunku.app.result;

import java.util.ArrayList;
import java.util.List;

import com.xunku.pojo.base.Account;

/**
 * 帐号趋势，给定时间点有多少账号搞事儿
 * 
 * @author wujian
 * @created on Jul 16, 2014 3:45:55 PM
 */
public class TrendAccountResult extends Result {

	/**
	 * 时间区域
	 */
	long _timeZone;
	/**
	 * 在这个时间区域内有多少帐号发布了内容
	 */
	List<Account> _accounts = new ArrayList<Account>();

	public TrendAccountResult(long timezone) {
		this._timeZone = timezone;
	}

	public void AddAccount(Account acc) {
		this._accounts.add(acc);
	}

	/**
	 * 该时间区域发布数量
	 * 
	 * @return
	 */
	public int Count() {
		return this._accounts.size();
	}
}
