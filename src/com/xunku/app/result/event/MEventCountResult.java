package com.xunku.app.result.event;

import com.xunku.app.result.Result;

/**
 * 时间统计信息
 * 
 * @author wujian
 * @created on Jul 3, 2014 3:57:22 PM
 */
public class MEventCountResult extends Result {

	int today;// 今天数量
	int yesterday;// 昨天数量
	int all;// 全部数量

	public int getToday() {
		return today;
	}

	public void setToday(int today) {
		this.today = today;
	}

	public int getYesterday() {
		return yesterday;
	}

	public void setYesterday(int yesterday) {
		this.yesterday = yesterday;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

}
