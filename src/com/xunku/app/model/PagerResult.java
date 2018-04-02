package com.xunku.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回结果集
 * 
 * @author wujian
 * @created on Aug 8, 2014 2:39:59 PM
 * @param <T>
 */
public class PagerResult<T> {
	private long realcount;
	private String err; // 前台页面提示错误信息
	private List<T> rows = new ArrayList<T>();

	public List<T> getRows() {
		return rows;
	}

	public long getRealcount() {
		return realcount;
	}

	public void setRealcount(long realcount) {
		this.realcount = realcount;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}
}
