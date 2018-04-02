package com.xunku.dao.event;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jolbox.bonecp.BoneCP;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.app.result.BeTrendResult;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.RetweetResult;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.event.MEventUserRegResult;
import com.xunku.dto.KeyPointDTO;

public interface DataAnalysisDao {
	/**
	 * 功能描述<按日期查询，七天或日期范围搜索【趋势分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<DataAnalyzeDTO>
	 * @version twitter 1.0
	 * @date Jun 10, 201410:16:51 AM
	 */
	public Map<String, Integer> queryTrendAnalyze(BoneCP dataSource,
			Date startTime, Date endTime, String table);

	/**
	 * 功能描述<按日期查询，24小时搜索【趋势分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,List<Integer>>
	 * @version twitter 1.0
	 * @date Jun 10, 201410:18:50 AM
	 */
	// public Map<String, Integer> queryDataByTimeQuantum(ComboPooledDataSource
	// dataSource,String table);
	/**
	 * 功能描述<获取原创数和转发数【内容分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<Integer,Integer>
	 * @version twitter 1.0
	 * @date Jun 10, 20146:05:29 PM
	 */
	public RetweetResult queryOriginalReposts(int eid, Pooling pool);

	/**
	 * 功能描述<性别比例【用户分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return float
	 * @version twitter 1.0
	 * @date Jun 11, 20143:21:16 PM
	 */
	public GenderResult querySexScale(int eid, Pooling pool);

	/**
	 * 功能描述<认证比例【用户分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return float
	 * @version twitter 1.0
	 * @date Jun 11, 20143:36:26 PM
	 */
	public VipResult queryVIPScale(int eid, Pooling pool);

	/**
	 * 功能描述<来源分布【用户分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<EventSourceDTO>
	 * @version twitter 1.0
	 * @date Jun 12, 20145:08:59 PM
	 */
	public Map<String, Integer> queryEventSource(int eid, Pooling pool);

	/**
	 * 功能描述<注册时间分布【用户分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return EventReg
	 * @version twitter 1.0
	 * @date Jun 11, 20144:55:31 PM
	 */
	public MEventUserRegResult queryEventReg(int eid, Pooling pool);

	/**
	 * 功能描述<地域分布【用户分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<EventLocal>
	 * @version twitter 1.0
	 * @date Jun 11, 20145:08:32 PM
	 */
	public Map<String, Integer> queryEventLocal(int eid, Pooling pool);

	/**
	 * 功能描述<关键用户分析：按照博主的粉丝数取TOP-N【用户分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<KeyUser>
	 * @version twitter 1.0
	 * @date Jun 13, 20142:24:47 PM
	 */
	public Map<String, Integer> queryEventKeyUser(int eid, Pooling pool);

	/**
	 * 功能描述<热门关键词【内容分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,Integer>
	 * @version twitter 1.0
	 * @date Jul 23, 20145:42:44 PM
	 */
	public Map<String, Integer> queryHotWord(int eventid, Pooling pool);

	/**
	 * 功能描述<查询关键观点【内容分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,Integer>
	 * @version twitter 1.0
	 * @date Jul 24, 201410:35:50 AM
	 */
	public List<KeyPointDTO> queryKeyPoint(int eventid, Pooling pool,
			long start, long end);

	/**
	 * 功能描述<查询当前事件的趋势分析结果，含详细信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<Long,List<String>>
	 * @version twitter 1.0
	 * @date Jul 24, 20144:01:30 PM
	 */
	public Map<Long, List<String>> queryTrendDetail(Date start, Date end,
			Query query, Pooling pool);

	/**
	 * 功能描述<查询当前事件的趋势分析结果，不含详细信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<Long,Integer>
	 * @version twitter 1.0
	 * @date Jul 24, 20144:06:43 PM
	 */
	public Map<Long, Integer> queryTrend(Date start, Date end, Query query,
			Pooling pool);

	BeTrendResult queryBeTrend(Date date, String ucode, Platform platform);

	void insertBeTrend(Date date, String ucode, Platform platform,
			BeTrendResult trend);
}
