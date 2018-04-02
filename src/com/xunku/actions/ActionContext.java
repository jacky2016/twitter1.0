package com.xunku.actions;

import java.util.Map;

import com.xunku.app.model.UserSession;

/**
 * 业务对象上下文描述对象
 * 
 * @author wujian
 * @created on Jun 5, 2014 4:01:19 PM
 */
public class ActionContext {

	private IAction _action;
	private UserSession _session;
	private Map<String, String[]> _params;

	public ActionContext(UserSession session) {
		this._session = session;
	}

	public void setAction(IAction action) {
		_action = action;
	}

	public IAction getAction() {
		return this._action;
	}

	public void setParameters(Map<String, String[]> parameters) {
		this._params = parameters;
	}

	public Map<String, String[]> getParameters() {
		return this._params;
	}

	public String getActionName() {
		Map<String, String[]> map = this.getParameters();

		String[] actionNames = map.get(IAction.ACTION);

		String actionName;

		if ((actionNames == null) || (actionNames.length < 1)) {
			return null;
		}

		actionName = actionNames[0];

		return actionName;
	}

	public UserSession getSession() {
		return this._session;
	}

}
