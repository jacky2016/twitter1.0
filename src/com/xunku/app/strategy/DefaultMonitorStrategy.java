package com.xunku.app.strategy;

import com.xunku.app.interfaces.IRefreshStrategy;
import com.xunku.app.monitor.IMonitor;

/**
 * 监测对象的默认刷新策略
 * 
 * @author wujian
 * @created on Jul 10, 2014 10:46:16 AM
 */
public class DefaultMonitorStrategy<T extends IMonitor> implements
		IRefreshStrategy<T> {

	@Override
	public boolean shouldImmediately(IMonitor monitor, String key) {

		return false;
	}

}
