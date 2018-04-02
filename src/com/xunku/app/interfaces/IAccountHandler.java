package com.xunku.app.interfaces;

/**
 * 帐号处理器,当帐号发涩会那个变更时执行该处理器
 * @author wujian
 * @created on Jun 11, 2014 10:44:45 AM
 */
public interface IAccountHandler extends IHandler {

	void ProcessAccount(IAccount acc);
	
	
}
