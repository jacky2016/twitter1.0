package com.xunku.portal;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xunku.utils.WebCache;
import com.xunku.app.AppContext;
import com.xunku.portal.controller.*;

public class ListenerUrl implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		AppContext context = AppContext.getInstance();
		context.init();

		System.out.println("初始化Base链接->" + context.getPoolingHome().getName()
				+ "成功.");
		// 初始化系统菜单
		WebCache.initSystemModule();
		// 权限列表放入缓存
		WebCache.initRolePermission();
		// 初始化地域信息
		WebCache.initLocation();
		// add by tengsx ，这个机制不规范，后续王辉要改
		PermissionController.initMeta();
	}

}
