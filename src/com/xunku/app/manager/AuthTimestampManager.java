package com.xunku.app.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权时间戳管理器
 * 
 * @author wujian
 * @created on Sep 17, 2014 2:27:00 PM
 */
public class AuthTimestampManager {

	static AuthTimestampManager _manager;
	/**
	 * 10分钟
	 */
	public static final int minutes10 = 10 * 60 * 1000;// 20分钟

	public synchronized static AuthTimestampManager getInstance() {
		if (_manager == null) {
			// 这里应该从配置文件里面读取策略和监听器
			_manager = new AuthTimestampManager();
		}
		return _manager;
	}

	AuthTimestampManager() {
		this.timestamps = new HashMap<String, Map<String, Long>>();
	}

	/**
	 * 时间戳
	 */
	Map<String, Map<String, Long>> timestamps;

	/**
	 * 获得授权对应apikey的时间戳
	 * 
	 * @param authToken
	 * @param apiKey
	 * @return
	 */
	public long getTimestamp(String authToken, String apiKey) {
		Map<String, Long> authTs = this.timestamps.get(authToken);
		if (authTs != null) {
			if (authTs.containsKey(apiKey)) {
				return authTs.get(apiKey);
			}
			return 0;
		}
		return 0;
	}

	/**
	 * 更新授权对应apikey的时间戳
	 * 
	 * @param authToken
	 * @param apiKey
	 * @param timestamp
	 */
	public void putTimestamp(String authToken, String apiKey, Long timestamp) {
		if (this.timestamps.containsKey(authToken)) {
			this.timestamps.get(authToken).put(apiKey, timestamp);
		} else {
			Map<String, Long> timestamps = new HashMap<String, Long>();
			timestamps.put(apiKey, timestamp);
			this.timestamps.put(authToken, timestamps);
		}
	}

}
