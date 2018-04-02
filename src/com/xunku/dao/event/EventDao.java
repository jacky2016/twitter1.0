package com.xunku.dao.event;

import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.app.enums.WordCloudEnum;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.dto.pushservices.EventDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

public interface EventDao {

	void addWordCloudTask(int monitorId, int poolid, String sourceTable,
			WordCloudEnum type);

	/**
	 * 功能描述<事件监控：判断事件是否在有效期，过期的不算>
	 * 
	 * @author shaoqun
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Aug 19, 20146:09:55 PM
	 */
	public int getPeriodEventCountById(int customid, int id);

	/**
	 * 功能描述<事件监控：判断事件有效期内的个数，过期的不算>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Aug 19, 20146:09:55 PM
	 */
	public int getPeriodEventCount(int customid);

	/**
	 * 功能描述<获取事件列表【推送服务】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return EventDTO
	 * @version twitter 1.0
	 * @date May 23, 20143:30:36 PM
	 */
	public List<EventDTO> queryByPush(int userid);

	/**
	 * 功能描述<添加事件【事件监控】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 21, 20146:08:49 PM
	 */
	public void insert(EventMonitor e);

	/**
	 * 功能描述<更新事件【事件监控】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 6, 201411:34:13 AM
	 */
	public void update(EventMonitor e);

	/**
	 * 功能描述<删除事件【事件监控】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 6, 201411:42:55 AM
	 */
	public void deleteByEId(int eid);

	/**
	 * 功能描述<获取事件【事件监控】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Event
	 * @version twitter 1.0
	 * @date Jun 4, 20142:44:30 PM
	 */
	public EventMonitor queryEventByEId(int eid);

	/**
	 * 功能描述<获取事件监控列表【事件监控】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<EventListDTO>
	 * @version twitter 1.0
	 * @date Jun 4, 20149:56:45 AM
	 */
	public Pagefile<EventMonitor> queryEventList(Pager dto, int userid,
			Platform platform);

	/**
	 * 功能描述<获取所有事件列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<MEvent>
	 * @version twitter 1.0
	 * @date Jun 12, 20145:28:40 PM
	 */
	public List<EventMonitor> queryEventList();

	/**
	 * 获得当前库里有效的Event，就是没过期的EventList
	 * 
	 * @return
	 */
	List<EventMonitor> queryAvailableEventList();

	/**
	 * 功能描述<更新fetch时间>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Jul 1, 20144:54:08 PM
	 */
	public boolean updateFetchTime(int eid, String fetchTime);

	/**
	 * 功能描述<获取当前客户下事件监控的数量>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Jul 9, 20149:42:23 AM
	 */
	public int queryEventListTotal(int customId);
}
