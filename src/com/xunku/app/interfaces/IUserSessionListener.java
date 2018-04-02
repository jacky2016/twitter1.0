package com.xunku.app.interfaces;

import java.util.EventListener;

import com.xunku.app.model.UserInfo;
import com.xunku.app.model.UserSessionEvent;
/**
 * 用户Session监听器
 * 
 * @author wujian
 * @created on Jul 11, 2014 3:38:30 PM
 */
public interface IUserSessionListener extends EventListener {

	/**
	 * 用户创建时触发
	 * 
	 * @param se
	 */
	public void userCreated(UserInfo use);

	/**
	 * 用户的会话销毁时触发
	 * 
	 * @param se
	 */
	public void sessionDestroyed(UserSessionEvent use);

	/**
	 * 用户销毁时触发
	 * 
	 * @param use
	 */
	public void userRemoved(UserInfo use);
}
