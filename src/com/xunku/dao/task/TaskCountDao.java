package com.xunku.dao.task;

import java.util.Date;
import java.util.List;

import com.xunku.pojo.task.TaskCount;

/**
 * 任务统计DAO，对应数据表->Task_Count_An
 * <p>
 * created为插入时的时间，只有日期部分没有时间部分
 * <p>
 * updated为更新的时间，精确到时间
 * 
 * @author wujian
 * @created on Aug 5, 2014 3:06:36 PM
 */
public interface TaskCountDao {
	/**
	 * 返回指定之间区间的客户任务统计信息，按创建时间排序
	 * @param customId
	 * @param start
	 * @param end
	 * @return
	 */
	List<TaskCount> queryCountsByCustomID(int customId, Date start, Date end);

	/**
	 * 将taskcnt插入到数据库里，如果插入成功则将taskCnt的id设置为插入后的ID，否则不设置
	 * <p>
	 * 
	 * @param taskCnt
	 */
	void insert(TaskCount taskCnt);

	/**
	 * 更新taskCnt，用ID更新
	 * 
	 * @param taskCnt
	 */
	void update(TaskCount taskCnt);

	/**
	 * 删除一个统计
	 * 
	 * @param taskCntId
	 */
	void delete(int taskCntId);

	/**
	 * 根据客户id和创建时间获得一个统计信息
	 * <p>
	 * customId和created组合唯一
	 * 
	 * @param customId
	 * @param created
	 * @return
	 */
	TaskCount queryCountByCustomID(int customId, Date created);

	/**
	 * 根据ID查询一条统计信息
	 * 
	 * @param cntId
	 * @return
	 */
	TaskCount queryCountByID(int cntId);
}
