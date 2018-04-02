package com.xunku.utils;

import com.jolbox.bonecp.BoneCP;
import com.xunku.app.AppContext;

public class DatabaseUtils {

	public static class cpdsMap {
		public static BoneCP get(String name) {
			return AppContext.getInstance().getPoolManager().getPooling(name)
					.getDatasource();
		}
	}

}
