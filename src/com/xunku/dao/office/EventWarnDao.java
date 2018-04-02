package com.xunku.dao.office;

import java.util.List;

import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.EventWarn;
import com.xunku.utils.Pagefile;

public interface EventWarnDao {

	void changeRunning(int eventid, int customid, boolean running);

	/**
	 * 功能描述<添加事件预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 20141:43:20 PM
	 */
	int insert(EventWarn ew);

	/**
	 * 功能描述<删除事件预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 20141:43:48 PM
	 */
	boolean deleteById(int ewid);

	/**
	 * 功能描述<更新事件预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 20141:44:02 PM
	 */
	boolean update(EventWarn ew);

	/**
	 * 功能描述<获取客户下事件预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<EventWarn>
	 * @version twitter 1.0
	 * @date Aug 29, 20141:44:19 PM
	 */
	List<EventWarn> queryEventWarnList(int customid);

	/**
	 * 获得指定客户下的事件预警分页列表
	 * 
	 * @param customid
	 * @param pager
	 * @return
	 */
	Pagefile<EventWarn> queryEventWarnList(int customid, Pager pager);

	/**
	 * 功能描述<通过事件id获取事件预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return EventWarn
	 * @version twitter 1.0
	 * @date Sep 12, 201411:37:11 AM
	 */
	EventWarn queryEventByEventid(int eventid);
}
