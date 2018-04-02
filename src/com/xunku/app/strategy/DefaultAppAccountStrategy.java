package com.xunku.app.strategy;

import java.util.Date;

import com.xunku.app.interfaces.IRefreshStrategy;
import com.xunku.app.manager.AuthTimestampManager;
import com.xunku.app.model.AppAuth;

public class DefaultAppAccountStrategy<T extends AppAuth> implements
		IRefreshStrategy<T> {

	@Override
	public boolean shouldImmediately(AppAuth auth, String apiRefreshKey) {

		// return true;

		AuthTimestampManager manager = AuthTimestampManager.getInstance();

		int minute = AuthTimestampManager.minutes10;

		if (auth == null) {
			return false;
		}

		long lastrefshTime = manager.getTimestamp(auth.getToken(),
				apiRefreshKey);
		// System.out.println(new Date(lastrefshTime));
		long refreshTime = lastrefshTime + (minute * 60 * 1000);

		// System.out.println(new Date(lastrefshTime));
		// System.out.println(new Date(refreshTime));
		// System.out.println(new Date(System.currentTimeMillis()));
		// System.out.println(System.currentTimeMillis());
		// System.out.println(refreshTime);

		return System.currentTimeMillis() > refreshTime;
	}
}
