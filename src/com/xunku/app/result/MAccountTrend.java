package com.xunku.app.result;

import java.util.HashMap;
import java.util.Map;

public class MAccountTrend {

	public MAccountTrend() {
		this.weibo = new HashMap<Long, Integer>();
		this.retweet = new HashMap<Long, Integer>();
	}

	public Map<Long, Integer> weibo;
	public Map<Long, Integer> retweet;

}
