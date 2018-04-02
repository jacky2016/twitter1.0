package com.xunku.app.result.event;

/**
 * 事件分析的用户注册分析结果
 * 
 * @author wujian
 * @created on Jul 21, 2014 7:18:35 PM
 */
public class MEventUserRegResult {

	int halfYears;// 半年内的
	int oneYears;// 1-半年的
	int twoYears;// 1-2年的
	int moreYears;// 2年以上的

	public int getHalfYears() {
		return halfYears;
	}

	public void setHalfYears(int halfYears) {
		this.halfYears = halfYears;
	}

	public int getOneYears() {
		return oneYears;
	}

	public void setOneYears(int oneYears) {
		this.oneYears = oneYears;
	}

	public int getTwoYears() {
		return twoYears;
	}

	public void setTwoYears(int twoYears) {
		this.twoYears = twoYears;
	}

	public int getMoreYears() {
		return moreYears;
	}

	public void setMoreYears(int moreYears) {
		this.moreYears = moreYears;
	}

}
