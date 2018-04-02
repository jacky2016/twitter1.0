package com.xunku.app.adapters;

import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAPIAdapter;
import com.xunku.app.model.AppAuth;

/**
 * API的工厂方法，负责创建API的适配器
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:49:03 PM
 */
public class APIAdpaterFactory {

	/**
	 * 获得指定平台的API，该API不包含授权信息
	 * 
	 * @param platform
	 * @return
	 */
	public IAPIAdapter getAPI(Platform platform) {
		if (platform == Platform.Sina)
			return new SinaAPIAdapter();
		if (platform == Platform.Tencent)
			return new TencentAPIAdapter();
		return null;
	}

	/**
	 * 根据授权信息创建指定平台的API，该API使用该授权信息来访问网络资源
	 * 
	 * @param auth
	 *            授权信息
	 * @param conf
	 *            其他参数信息，如不需要则为null
	 * @return
	 */
	public IAPIAdapter getAPI(AppAuth auth) {
		IAPIAdapter result = null;
		if (auth != null) {
			result = getAPI(auth.getPlatform());
			if (result != null) {
				result.setAuth(auth);
			}
		}
		return result;
	}
}
