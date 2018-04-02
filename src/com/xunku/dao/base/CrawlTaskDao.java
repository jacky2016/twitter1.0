package com.xunku.dao.base;

import java.util.List;

import com.xunku.pojo.base.CrawlTask;

public interface CrawlTaskDao {

	/**
	 * 获得未获取结果的Task，该方法用来尝试获得结果
	 * 
	 * @param customId
	 * @param crawlType
	 * @param monitorType
	 * @return
	 */
	List<CrawlTask> queryUnAcquiredTasks(int monitorId, int monitorType,
			long created, long expired);

	/**
	 * 获得未提交的Task,该方法用来尝试提交任务
	 * 
	 * @param customId
	 * @param crawlType
	 * @param monitorType
	 * @return
	 */
	List<CrawlTask> queryUnSubmitedTasks(int customId, int crawlType,
			int monitorType);

	/**
	 * 插入task对象，如果成功则返回CrawlTask
	 * 
	 * @param task
	 * @return
	 */
	boolean insert(CrawlTask task);

	boolean update(CrawlTask task);

	boolean delete(int crawlTaskId);

	CrawlTask queryById(int crawlTaskId);

}
