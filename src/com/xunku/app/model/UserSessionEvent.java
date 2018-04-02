package com.xunku.app.model;

import java.util.EventObject;

/**
 * 定义UserSession事件
 * @author wujian
 * @created on Jul 11, 2014 3:41:52 PM
 */
public class UserSessionEvent extends EventObject {

	public UserSessionEvent(UserSession session) {
		super(session);
	}

	private static final long serialVersionUID = -7451181902354473162L;

	public UserSession getSession() {
		return (UserSession) this.source;
	}
}
