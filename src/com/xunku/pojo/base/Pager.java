package com.xunku.pojo.base;

import java.util.HashMap;
import java.util.Map;

import com.xunku.app.Utility;

/**
 * 分页信息描述，并且具有API分页标记的功能
 * 
 * @author wujian
 * @created on Jun 9, 2014 10:50:19 AM
 */
public class Pager {

	int pageIndex;
	int pageSize;
	long realCnt;

	// sina
	String _sinceId = "1";// default is 1

	// tencent
	long pagetime;
	long twitterid;

	Map<String, String> properties = new HashMap<String, String>();

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获得新浪的翻页标记
	 * 
	 * @return
	 */
	public String getSinceId() {
		return this._sinceId;
	}
	
	public void setSinceId(String sinceId){
		this._sinceId = sinceId;
	}

	/**
	 * 获得腾讯的翻页标记pagetime
	 * 
	 * @return
	 */
	public long getPagetime() {
		return this.pagetime;
	}

	/**
	 * 获得腾讯翻页标记twitterid
	 * 
	 * @return
	 */
	public long getTwitterid() {
		return this.twitterid;
	}

	/**
	 * 返回指定的pi页，每页指定ps条记录
	 * 
	 * @param pi
	 * @param ps
	 * @return
	 */
	public static Pager createPager(int pi, int ps) {
		Pager p = new Pager();
		p.setPageIndex(pi);
		p.setPageSize(ps);
		return p;
	}

	/**
	 * 创建新浪的分页对象
	 * 
	 * @param pageindex
	 * @param pagesize
	 * @param sinceId
	 * @return
	 */
	public static Pager BuildSinaPager(int pageindex, int pagesize,
			String sinceId) {

		if (Utility.isNullOrEmpty(sinceId)) {
			sinceId = "1";
		}

		Pager p = createPager(pageindex, pagesize);
		p._sinceId = sinceId;
		return p;
	}

	/**
	 * 创建腾讯的分页对象
	 * 
	 * @param pageindex
	 * @param pagesize
	 * @param pagetime
	 * @param tweeterid
	 * @return
	 */
	public static Pager BuildTencentPager(int pageindex, int pagesize,
			String pagetime, String tweeterid) {

		if (Utility.isNullOrEmpty(pagetime)) {
			pagetime = "0";
		}

		if (Utility.isNullOrEmpty(tweeterid)) {
			tweeterid = "0";
		}

		Pager p = createPager(pageindex, pagesize);
		p.pagetime = Long.valueOf(pagetime);
		p.twitterid = Long.valueOf(tweeterid);
		return p;
	}

	/**
	 * 返回第一页，每页20条记录
	 * 
	 * @return
	 */
	public static Pager getPager() {
		Pager p = new Pager();
		p.setPageIndex(1);
		p.setPageSize(20);
		return p;
	}

	public long getRealCnt() {
		return realCnt;
	}

	public void setRealCnt(long realCnt) {
		this.realCnt = realCnt;
	}
}