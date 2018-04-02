package com.xunku.app.db;

import java.io.IOException;


import com.xunku.app.enums.CacheStatus;

public class RedisAccountFillThread extends Thread {

	AccountDB _accountDB;

	public RedisAccountFillThread(AccountDB db) throws IOException {
		_accountDB = db;
	}

	public synchronized void run() {

		_accountDB.loadAccountCache();

		this._accountDB.setCacheStatus(CacheStatus.online);
	}

}
