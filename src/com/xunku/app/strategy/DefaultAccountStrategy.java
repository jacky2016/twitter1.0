package com.xunku.app.strategy;

import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IRefreshStrategy;

/**
 * 帐号默认的刷新策略，该策略决定当前帐号的基本信息是否要刷新
 * 
 * @author wujian
 * @created on Jul 8, 2014 3:40:42 PM
 * @param <T>
 */
public class DefaultAccountStrategy<T extends IAccount> implements
		IRefreshStrategy<T> {

	/*
	 * (non-Javadoc)判断这个刷新对象是否需要刷新
	 * 
	 * @see com.xunku.app.interfaces.IRefreshStrategy#isRefresh(com.xunku.app.model.RefreshObj)
	 */
	@Override
	public boolean shouldImmediately(IAccount acc, String key) {
		// 测试阶段实时刷新
		//return true;
		
		
		int minute = 10 - acc.getLevel();
		long lastrefshTime = acc.getTimestamp();
		long refreshTime = lastrefshTime + (minute * 60 * 1000);

		return System.currentTimeMillis() > refreshTime;
		
	}
}
