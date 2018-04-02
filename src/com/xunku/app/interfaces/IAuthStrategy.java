package com.xunku.app.interfaces;

import java.util.List;

import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;

/**
 * 策略接口定义
 * <p>给定一组帐号，从这些帐号里找到一个授权
 * @author wujian
 * @created on Jun 5, 2014 3:47:35 PM
 */
public interface IAuthStrategy {

	/**
	 * 根据策略实现来找到List里面最合适的授权信息
	 * @param list
	 * @return
	 */
	AppAuth getAuth(List<AppAccount> list);

}
