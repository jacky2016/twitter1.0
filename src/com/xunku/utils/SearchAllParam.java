package com.xunku.utils;

//微博舆情搜索的参数by wanghui
public class SearchAllParam {
	private String queryString;
	private int start;//pageIndex
	private int rows;//pageSize
	private boolean isDedup; // 是否去重
	private int sortType; // 排序类型
	private boolean needsummary; // 是否显示摘要
	private String startTime; // 开始时间
	private int dataRange; // 搜索时间范围
	private int searchType = 2; // 搜索类型 0新闻、1论坛、2微博
	private int sortOrder = 1; // 搜索排序,0升序;1降序
	private int textType = 3;//

	// private int SourceID; //平台类型 1新浪2腾讯5人民
	// private String transferurl;//如果为null 是原创，否则是转发

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * 是否去重
	 * 
	 * @return
	 */
	public boolean isDedup() {
		return isDedup;
	}

	/**
	 * 是否去重
	 * 
	 * @param isDedup
	 */
	public void setDedup(boolean isDedup) {
		this.isDedup = isDedup;
	}

	/**
	 * 排序类型
	 * 
	 * @return
	 */
	public int getSortType() {
		return sortType;
	}

	/**
	 * 排序类型
	 * 
	 * @param sortType
	 */
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}

	public boolean isNeedsummary() {
		return needsummary;
	}

	public void setNeedsummary(boolean needsummary) {
		this.needsummary = needsummary;
	}

	/**
	 * 开始时间
	 * 
	 * @return
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * 开始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * 搜索时间范围
	 * 
	 * @return
	 */
	public int getDataRange() {
		return dataRange;
	}

	/**
	 * 搜索时间范围
	 * 
	 * @param dataRange
	 */
	public void setDataRange(int dataRange) {
		this.dataRange = dataRange;
	}

	/**
	 * 搜索类型 0新闻、1论坛、2微博
	 * 
	 * @return
	 */
	public int getSearchType() {
		return searchType;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getTextType() {
		return textType;
	}

	public void setTextType(int textType) {
		this.textType = textType;
	}

}
