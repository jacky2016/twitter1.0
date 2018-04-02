package com.xunku.app.strategy;

import java.util.ArrayList;
import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAuthStrategy;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;

/**
 * 默认授权策略
 * <p>
 * 始终找授权列表里面本小时内调用次数最少的授权信息
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:47:16 PM
 */
public class AuthDefaultStrategy implements IAuthStrategy {

	private Platform _platform;

	public AuthDefaultStrategy(Platform platform) {
		this._platform = platform;
	}

	@Override
	public AppAuth getAuth(List<AppAccount> accounts) {

		AppAuth result = null;
		List<AppAccount> list = new ArrayList<AppAccount>();

		for (AppAccount acc : accounts) {
			if (acc.getPlatform() == this._platform) {
				if (acc.getAuths().size() > 0) {
					list.add(acc);
				}
			}
		}

		if (list.size() == 0) {
			// 未找到可用的帐号
			return null;
		}

		result = this.minTimes(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			if (result == null) {
				result = this.minTimes(list.get(i));
				i++;
			}
			AppAuth auth = this.minTimes(list.get(i));
			if (result.getTimes() > auth.getTimes()) {
				result = auth;
			}
		}

		return result;
	}

	/**
	 * 在授权列表里找到一个调用次数最少的授权
	 * 
	 * @param auths
	 * @return
	 */
	private AppAuth minTimes(AppAccount acc) {
		List<AppAuth> auths = acc.getAuths();
		// 当前帐号里面没有绑定的授权
		if (auths.size() == 0)
			return null;

		// 找到调用次数最少的那个授权
		AppAuth result = auths.get(0);

		for (int i = 1; i < auths.size(); i++) {
			if (result.getTimes() > auths.get(i).getTimes()) {
				result = auths.get(i);
			}
		}
		return result;

	}

}
