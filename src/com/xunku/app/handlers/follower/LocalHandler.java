package com.xunku.app.handlers.follower;

import java.util.HashMap;
import java.util.Map;

import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IFollowerHandler;

/**
 * 地域处理器
 * 
 * @author wujian
 * @created on Sep 17, 2014 5:29:00 PM
 */
public class LocalHandler implements IFollowerHandler {

	Map<String, Integer> locals;

	public LocalHandler() {
		locals = new HashMap<String, Integer>();
	}

	@Override
	public void process(IAccount follower) {
		String local = follower.getLocation();

		String[] locals = local.split(" ");
		String l = "未知地域";
		if (locals.length >= 1) {
			l = locals[0];
		}

		int cnt = 1;
		if (this.locals.containsKey(l)) {
			cnt = this.locals.get(l);
			cnt++;
		}

		this.locals.put(l, cnt);
	}

	public Map<String, Integer> getLocals() {
		return this.locals;
	}

}
