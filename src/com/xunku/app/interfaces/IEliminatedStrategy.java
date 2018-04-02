package com.xunku.app.interfaces;

import java.util.List;

import com.xunku.app.model.UserSession;

/**
 * 淘汰策略
 * 
 * @author wujian
 * @created on Jul 10, 2014 2:26:12 PM
 * @param <T>
 */
public interface IEliminatedStrategy {
	/**
	 * 从一组Session里面找到应该被淘汰的Session
	 * @param sessions
	 * @return
	 */
	UserSession getEliminated(List<UserSession> sessions);
}
