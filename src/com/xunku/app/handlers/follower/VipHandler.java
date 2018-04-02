package com.xunku.app.handlers.follower;

import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IFollowerHandler;

public class VipHandler implements IFollowerHandler {

	int vip;
	int novip;

	@Override
	public void process(IAccount follower) {
		if (follower.isVerified()) {
			vip++;
		} else {
			novip++;
		}
	}

	public int getVipCnt() {
		return vip;
	}

	public int getNoVipCnt() {
		return this.novip;
	}

}
