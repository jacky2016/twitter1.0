package com.xunku.app.model;

import com.xunku.app.Utility;
import com.xunku.pojo.base.Pager;

/**
 * 定义一个查询对象用来完成查询
 * 
 * @author wujian
 * @created on Jun 11, 2014 3:02:47 PM
 */
public class Query {

	String tableName;
	String fields;
	int pageSize;
	int pageIndex;
	String where;
	String sortField;
	boolean asc;

	/**
	 * 获得表名或者视图名
	 * 
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表名或者视图名
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	/**
	 * 是否升序
	 * 
	 * @return
	 */
	public boolean isAsc() {
		return asc;
	}

	public int getSort() {
		return asc ? 1 : 0;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public String toSQL() {
		return this.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT ");
		sb.append(this.fields);
		sb.append(" FROM ");
		sb.append(this.tableName);
		sb.append(" ");
		sb.append(this.where);
		if (!Utility.isNullOrEmpty(this.sortField)) {
			sb.append(" ORDER BY ");
			sb.append(this.sortField);
			if (asc)
				sb.append(" ASC");
			else
				sb.append(" DESC");
		}

		return sb.toString();
	}

	public static Query getQuery(String fields, String where, Pager pager) {
		Query query = new Query();
		query.setFields(fields);
		query.setWhere(where);
		return query;
	}

	public static Query getQuery(String fields, String where) {
		return getQuery(fields, where, Pager.getPager());
	}
}
