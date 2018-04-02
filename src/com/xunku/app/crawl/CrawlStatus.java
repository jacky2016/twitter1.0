package com.xunku.app.crawl;

/**
 * 爬虫状态
 * 
 * @author wujian
 * @created on Jul 29, 2014 11:59:04 AM
 */
public enum CrawlStatus {

	/**
	 * 待提交
	 */
	ToBeSubmitted,
	/**
	 * 已经提交
	 */
	Submitted,
	/**
	 * 数据已经取回
	 */
	Landed, Nothing

}
