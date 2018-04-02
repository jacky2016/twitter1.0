package com.xunku.app.monitor;

import com.xunku.app.AppContext;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweetStore;

/**
 * 定义一个监测对象描述
 * 
 * @author wujian
 * @created on Jun 26, 2014 4:05:11 PM
 */
public interface IMonitor {

	ITweetStore getStore(AppContext context);

	int getMonitorType();
	
	/**
	 * 监测对象的编号
	 * 
	 * @return
	 */
	int getMonitorId();

	/**
	 * 监测对象的连接池描述
	 * 
	 * @return
	 */
	String getPooling();

	/**
	 * 监测对象存储对应的表名
	 * 
	 * @return
	 */
	String getTableName();

	/**
	 * 获得监测对象的token
	 * 
	 * @return
	 */
	String getToken();

	/**
	 * 获得监测对象最新的时间戳
	 * 
	 * @return
	 */
	long getTimestamp();

	Platform getPlatform();
}
