package com.xunku.dao.office;

import java.util.List;
import java.util.Map;

import com.xunku.dto.pushservices.WarnServiceListDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.Pagefile;

public interface WeiboWarnDao {

	/**
	 * 设置微博的运行状态
	 * 
	 * @param tid
	 * @param customid
	 * @param running
	 */
	void changeRunning(String tid, int customid, boolean running);

	/**
	 * 设置微博监测项的评论状态
	 * 
	 * @param id
	 * @param flag
	 */
	void setWeiboCommentFlag(int id, boolean flag);

	/**
	 * 设置微博监测项的转发状态
	 * 
	 * @param id
	 * @param flag
	 */
	void setWeiboRepostFlag(int id, boolean flag);

	WeiboWarn query(int customid, String tid);

	Map<String, String[]> checkWarn(int customId, List<String> tids);

	/**
	 * 功能描述<添加微博预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 20142:21:34 PM
	 */
	int insert(WeiboWarn warn);

	/**
	 * 功能描述<删除微博预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 20142:22:00 PM
	 */
	boolean deleteById(int id);

	/**
	 * 功能描述<更新微博预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 20142:22:15 PM
	 */
	boolean update(WeiboWarn warn);

	/**
	 * 功能描述<获取客户下的微博预警列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<WeiboWarn>
	 * @version twitter 1.0
	 * @date Aug 29, 20142:22:32 PM
	 */
	List<WeiboWarn> queryWeiboWarnList(int customid);

	/**
	 * 获得微博监测的分页数据
	 * 
	 * @param customid
	 * @param pager
	 * @return
	 */
	Pagefile<WeiboWarn> queryWeiboWarnList(int customid, Pager pager);

	/**
	 * 查询设置微博预警的个数
	 * 
	 * @param customid
	 *            客户id
	 */
	int getCurrentWeiboWarnCount(int customid);

	/**
	 * 预警服务--预警信息的取消预警
	 * 
	 * @param tid
	 *            微博的tid customid 客户id return 0 是取消预警失败 1 是取消预警成功
	 */
	int getCanenlWeiboWarnServices(String tid, int customid);
	
	
	/**
	 * 预警服务--预警通知中的  删除某一条消息的方法
	 * @param   id   Office_WarnList表的id
	 */
	 int   deleteWarnNofity(int id);
	

	/**********************以下是预警列表模块的*************************/
	
	/**
	 * 预警服务--预警列表中的列表展示方法
	 * @param  customid  客户id
	 * @return 
	 */
	Pagefile<WeiboWarn>    queryWarnWeiboShowList(int customid,Pager  pager);
 
	/**
	 * 预警服务--预警列表中的修改某一条预警设置得回来的数据的方法
	 *  @param   id  
	 */
	  WeiboWarn  modifyWarningList(int id);
	  
	  
		/**
		 * 预警服务--预警列表中的修改某一条预警设置的方法
		 *  @param   WeiboWarn warn
		 *  @return    1 为修改成功 0 则为修改失败
		 */
	  int  updateWarnList(WeiboWarn warn);
	   
		/**
		 *  预警列表的删除事件
		 * */
	  int  deleteWeiboWarnList(int id,int customid);
}
