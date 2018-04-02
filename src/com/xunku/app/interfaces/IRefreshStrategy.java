package com.xunku.app.interfaces;

/**
 * 刷新策略
 * 
 * @author wujian
 * @created on Jul 4, 2014 2:41:04 PM
 */
public interface IRefreshStrategy<T> {

	/**
	 * 判断是否要立即刷新当前对象
	 * 
	 * @param acc
	 * @return
	 */
	boolean shouldImmediately(T refreshableObject, String apiRefreshKey);

	// 该策略暂时不提供延时刷新的功能
	// boolean shouldWithDelay();
	// int getDelay();
}
