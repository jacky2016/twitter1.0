package com.xunku.actions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.model.UserInfo;
/**
 * 业务逻辑基类
 * <p>
 * 提供对用户对象和参数的访问
 * 
 * @author wujian
 * @created on Jun 5, 2014 4:00:29 PM
 */
public abstract class ActionBase implements IAction {

	protected static final Logger LOG = LoggerFactory
			.getLogger(ActionBase.class);

	private ActionContext _context;

	public abstract Object doAction();

	public ActionContext getContext() {
		return _context;
	}

	public void init(ActionContext context) {
		this._context = context;
	}

	public String getName() {

		if (this._context == null) {
			return EMPTY_ACTION;
		}

		return this._context.getActionName();
	}

	public String get(String name) {

		Object value = this._context.getParameters().get(name);

		return this.getValue(value);
	}

	/**
	 * @deprecated 这个方法效率很低，建议使用get方法直接获得参数值
	 */
	public Map<String, String> getParameters() {

		Map<String, String[]> map = this._context.getParameters();

		Map<String, String> result = new HashMap<String, String>();

		for (String key : map.keySet()) {
			result.put(key, this.getValue(map.get(key)));
		}
		return result;
	}

	private String getValue(Object obj) {
		String[] values = (String[]) obj;

		String value;

		if ((values == null) || (values.length < 1)) {
			return null;
		}

		value = values[0];
		return value;
	}

	public UserInfo getUser() {
		Object objUser = this._context.getSession().getUser();
		return (UserInfo) objUser;
	}
}
