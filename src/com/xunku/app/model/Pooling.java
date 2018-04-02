package com.xunku.app.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jolbox.bonecp.BoneCP;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IHandler;

/**
 * 连接池包装器
 * 
 * @author wujian
 * @created on Jul 15, 2014 3:28:53 PM
 */
public class Pooling {

	String _name;
	
	public Pooling(String name, BoneCP datasource) {
		this.datasource = datasource;
		this._name = name;
	}

	public String getName() {
		return this._name;
	}

	@Override
	public int hashCode() {
		return this._name.hashCode();
	}

	/**
	 * 在当前的连接池上初始化Handler
	 * 
	 * @param handlName
	 * @param handlResource
	 */
	public void initHandler(String handlName, String handlResource) {
		try {
			if (!this._initializedHandlers.containsKey(handlName)) {
				DbHelper.execute(handlResource, this);
				this._initializedHandlers.put(handlName, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	BoneCP datasource;
	/**
	 * 已经初始化过的Handler
	 */
	private Map<String, Boolean> _initializedHandlers = new HashMap<String, Boolean>();

	/**
	 * 查看当前处理器的运行环境是否被初始化过
	 * 
	 * @param handler
	 * @return
	 */
	public boolean isInitialized(IHandler handler) {
		if (this._initializedHandlers.containsKey(handler.getName())) {
			return this._initializedHandlers.get(handler.getName());
		} else {
			return false;
		}

	}

	/**
	 * 获得当前连接池上的链接对象
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return this.datasource.getConnection();
	}

	/**
	 * 获得当前连接池对象的数据源
	 * 
	 * @return
	 */
	public BoneCP getDatasource() {
		return this.datasource;
	}
}
