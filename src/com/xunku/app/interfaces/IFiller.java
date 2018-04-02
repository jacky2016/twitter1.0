package com.xunku.app.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 填充对象接口
 * 
 * @author wujian
 * @created on Sep 17, 2014 10:07:46 AM
 */
public interface IFiller<T> {

	/**
	 * 将rs填充到T对象里
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	T fill(ResultSet rs) throws SQLException;

}
