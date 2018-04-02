package com.xunku.dto.app;

import com.xunku.app.enums.Platform;
import com.xunku.app.model.App;

/**
 * AppDTO描述app的客户端对象
 * 
 * @author wujian
 * @created on Jul 8, 2014 9:29:22 AM
 */
public class AppDTO {

	public String AppKey;
	public String CallbackUrl;
	public String Name;
	public Platform platform;

	public static AppDTO get(App app) {

		if (app == null)
			return null;

		AppDTO dto = new AppDTO();

		dto.AppKey = app.getKey();
		dto.CallbackUrl = app.getCallbackUrl();
		dto.Name = app.getName();
		dto.platform = app.getPlatform();

		return dto;
	}
}
