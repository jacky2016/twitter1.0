package com.xunku.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果集
 * 
 * @author wanghui
 * @param <T>
 */
public class Pagefile<T> {
	private int realcount = 0;
	private String err = ""; // 前台页面提示错误信息
	private List<T> rows = new ArrayList<T>();
	int next_cursor;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getRealcount() {
		return realcount;
	}

	public void setRealcount(int realcount) {
		this.realcount = realcount;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

	public int getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(int next_cursor) {
		this.next_cursor = next_cursor;
	}
}
