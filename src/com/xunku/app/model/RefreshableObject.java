package com.xunku.app.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xunku.constant.PortalCST;

/**
 * 可刷新的对象
 * 
 * @author wujian
 * @created on Jul 8, 2014 4:07:23 PM
 */
public class RefreshableObject {


	protected int _refreshLevel;
	// 最后一次刷新的时间
	protected Map<String, Long> timestamps = new HashMap<String, Long>();

	/**
	 * 返回刷新级别
	 * <p>
	 * 级别分0-9，0级为默认级别刷新时间为10分钟
	 * <p>
	 * 9级为1分钟中间的以此类推
	 * 
	 * @return
	 */
	public int getLevel() {
		return _refreshLevel;
	}

	/**
	 * 返回刷新级别
	 * <p>
	 * 级别分0-9，0级为默认级别刷新时间为10分钟
	 * <p>
	 * 9级为1分钟中间的以此类推
	 * 
	 * @return
	 */
	public void setLevel(int rLevel) {
		this._refreshLevel = rLevel;
	}

	public long getTimestamp(String stampKey) {
		if (timestamps.get(stampKey) == null) {
			this.initTimestamp(stampKey);
		}
		return timestamps.get(stampKey);
	}

	public void setTimestamp(String key, long value) {
		System.out.println("set timestamp:"+new Date(value));
		this.timestamps.put(key, value);
	}

	public void initTimestamp(String stampKey) {
		this.timestamps.put(stampKey, System.currentTimeMillis()
				- PortalCST.ONE_MONTH);
	}
}
