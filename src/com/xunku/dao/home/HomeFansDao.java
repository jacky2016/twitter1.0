package com.xunku.dao.home;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xunku.app.enums.Platform;
import com.xunku.pojo.home.FansTrend;
import com.xunku.pojo.task.Group;
import com.xunku.pojo.task.TaskCount;

/**
 * 关键字是ucode,platform,created
 * 
 * @author wujian
 * @created on Aug 5, 2014 3:59:10 PM
 */
public interface HomeFansDao {

	FansTrend queryFansTrend(String ucode, Platform platform, Date created);

	List<FansTrend> queryFansTrends(String ucode, Platform platform,
			Date start, Date end);

	void insert(FansTrend trend);

	void update(FansTrend trend);

	void delete(String ucode, Platform platform, Date created);

	void updateCategories(int customid, int groupid, long now, int sina,
			int qq, int renmin);

	void updateTaskStatis(int customid, long now, int sinaCnt, int qqCnt,
			int rmCnt);

	void updateFanTrend(String ucode, int platform, long now, int weibos,
			int fans, int friends);

	List<TaskCount> queryCountsByCustomID(int customId, Date start, Date end);
	
	Map<String,Integer> queryCategoriesByCustomID(int customId, Date start, Date end);
}
