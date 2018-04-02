package com.xunku.app.controller;

import com.xunku.app.helpers.DateHelper;
import com.xunku.app.model.ClientLoginTimes;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;

public class UserController {

	/**
	 * 获得登录次数
	 * 
	 * @param clientId
	 * @return
	 */
	public int getLoginTimes(String clientId) {
		UserDao dao = new UserDaoImpl();

		ClientLoginTimes times = dao.getLoginTimes(clientId);

		if (times == null) {
			long expired = System.currentTimeMillis() + DateHelper.ONE_HOUR;
			dao.addLoginTimes(clientId, expired);
			return 1;
		} else {
			// 如果已经超时，则重置登录次数
			if (times.getExpired() < System.currentTimeMillis()) {
				this.clearLoginTimes(clientId);
				return 0;
			}
		}

		return times.getTimes();
	}

	/**
	 * 累加登录次数
	 * 
	 * @param clientId
	 */
	public void addLoginTimes(String clientId) {
		UserDao dao = new UserDaoImpl();
		dao.incLoginTimes(clientId, System.currentTimeMillis()
				+ DateHelper.ONE_HOUR);
	}

	/**
	 * 清空登录次数
	 * 
	 * @param clientId
	 */
	public void clearLoginTimes(String clientId) {
		UserDao dao = new UserDaoImpl();

		dao.clearLoginTimes(clientId);
	}
}
