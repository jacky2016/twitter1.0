package com.xunku.dao.monitor;

import java.util.List;
import java.util.Map;

import com.xunku.app.monitor.WeiboMonitor;
import com.xunku.dto.PagerDTO;
import com.xunku.utils.Pagefile;

//传播分析
public interface MWeiBoDao {
	/**
	 * 功能描述<添加一条微博分析【传播分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return >1 添加成功，0 添加失败，-1 已经存在
	 * @version twitter 1.0
	 * @date Jun 18, 20143:11:14 PM
	 */
	public int insert(WeiboMonitor mw);

	/**
	 * 功能描述<删除微博分析【传播分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jun 18, 20143:15:03 PM
	 */
	public boolean deleteById(int weiboid, int customid);

	/**
	 * 功能描述<获取微博分析列表【传播分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<MWeibo>
	 * @version twitter 1.0
	 * @date Jun 18, 20143:22:01 PM
	 */
	public Pagefile<WeiboMonitor> queryWeiboList(PagerDTO dto, int customId);

	/**
	 * 功能描述<获取微博分析列表【传播分析】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<MWeibo>
	 * @version twitter 1.0
	 * @date Jun 19, 20149:53:27 AM
	 */
	public List<WeiboMonitor> queryMWeiboList();

	/**
	 * 功能描述<根据tid和platform获取一条微博>
	 * 
	 * @author wanghui
	 * @param void
	 * @return MWeibo
	 * @version twitter 1.0
	 * @date Jun 25, 20149:45:24 AM
	 */
	public WeiboMonitor queryMWeiboByTid(String tid, int platform);

	/**
	 * 功能描述<检查URL是否存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jul 2, 201411:11:08 AM
	 */
	public boolean checkUrlIsExists(String url, int customId);

	/**
	 * 功能描述<检查微博分析是否存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,Integer>
	 * @version twitter 1.0
	 * @date Jul 4, 201411:57:26 AM
	 */
	public Map<String, Integer> checkWeiboList(List<String> tids, int customId);

	/**
	 * 功能描述<获取当前客户下的分析数量>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Jul 9, 20149:29:48 AM
	 */
	public int queryWeiboListTotal(int customId);

	/**
	 * 根据监测微博的id获得微博监测项
	 * 
	 * @param weiboid
	 * @return
	 */
	public WeiboMonitor queryMWeiboById(int weiboid);

	/**
	 * 功能描述<更新analysised>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 27, 20148:16:25 PM
	 */
	public boolean updateMWeiboById(int weiboid, boolean analysised);

	/**
	 * 获得所有等待分析的监视项，根据analysised返回结果
	 * 
	 * @return
	 */
	List<WeiboMonitor> queryWaitAnalysis();
}
