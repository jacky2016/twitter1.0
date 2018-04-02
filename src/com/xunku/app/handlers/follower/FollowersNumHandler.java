package com.xunku.app.handlers.follower;

import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IFollowerHandler;

/**
 * 用户粉丝数统计
 * 
 * @author wujian
 * @created on Sep 17, 2014 5:21:39 PM
 */
public class FollowersNumHandler implements IFollowerHandler {

	int cnt100;// 100以内的
	int cnt1000;// 100-1000的
	int cnt1W;// 1000-10000的
	int cnt10W;// 1W-10W的
	int cnt100W;// >10W的

	@Override
	public void process(IAccount follower) {
		int f = follower.getFollowers();
		if (f >= 0 && f < 100) {
			cnt100++;
		} else if (f >= 100 && f < 1000) {
			cnt1000++;
		} else if (f >= 1000 && f < 10000) {
			cnt1W++;
		} else if (f >= 10000 & f < 100000) {
			cnt10W++;
		} else {
			cnt100W++;
		}

	}

	public int getCnt100() {
		return cnt100;
	}

	public int getCnt1000() {
		return cnt1000;
	}

	public int getCnt1W() {
		return cnt1W;
	}

	public int getCnt10W() {
		return cnt10W;
	}

	public int getCnt100W() {
		return cnt100W;
	}

}
