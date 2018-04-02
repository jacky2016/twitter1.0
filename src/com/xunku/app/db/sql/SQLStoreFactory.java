package com.xunku.app.db.sql;

import com.xunku.app.enums.Platform;
import com.xunku.app.model.Pooling;

public class SQLStoreFactory {

	public AccountSQLStore createStore(Pooling pool, Platform platform) {

		if (platform == Platform.Sina) {
			return new SinaAccountSQLStore(pool, platform);
		}

		if (platform == Platform.Tencent)
			return new TencentAccountSQLStore(pool, platform);

		if (platform == Platform.Renmin)
			return new RenminAccountSQLStore(pool, platform);

		return null;
	}

}
