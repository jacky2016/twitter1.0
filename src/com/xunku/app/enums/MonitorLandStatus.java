package com.xunku.app.enums;

/**
 * 监视对象的数据落地状态
 * 
 * @author wujian
 * @created on Jul 28, 2014 1:59:37 PM
 */
public enum MonitorLandStatus {

	/**
	 * 监视对象落地完成
	 */
	finished,

	/**
	 * 监视对象正在落地
	 */
	landing,
	/**
	 * 监视对象已经过期
	 */
	expired,
	/**
	 * 监测对象无效，不存在
	 */
	invalid

}
