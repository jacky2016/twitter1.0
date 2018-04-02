package com.xunku.app.strategy;

import java.util.List;

import com.xunku.app.interfaces.IEliminatedStrategy;
import com.xunku.app.model.UserSession;

/**
 * 默认的Session淘汰策略，总是淘汰最先登录的那个Session
 * 
 * @author wujian
 * @created on Jul 10, 2014 2:37:48 PM
 * @param <T>
 */
public class DefaultSessionStrategy implements IEliminatedStrategy {

	/**
	 * 保留的会话数量
	 */
	private final int remain = 3;

	@Override
	public UserSession getEliminated(List<UserSession> sessions) {

		UserSession result = null;
		if (sessions == null)
			return null;
		if (sessions.size() == 0)
			return null;

		// 会话数少于保留的会话数，不做淘汰
		if (sessions.size() <= remain) {
			return null;
		} else {
			result = sessions.get(0);
			for (int i = 1; i < sessions.size(); i++) {
				UserSession session = sessions.get(i);
				// 取时间早的（小的）
				if (session.getLoginTime() < result.getLoginTime()) {
					result = session;
				}
			}
		}
		return result;
	}

}
