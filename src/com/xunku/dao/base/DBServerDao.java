package com.xunku.dao.base;

import java.util.Map;

public interface DBServerDao {
	Map<String,String> queryAll();
	
	String queryByName(String name);
}
