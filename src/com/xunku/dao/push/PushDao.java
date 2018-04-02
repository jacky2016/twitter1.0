package com.xunku.dao.push;

import java.util.Date;
import java.util.List;

import com.xunku.app.model.SubscriberEvent;
import com.xunku.app.model.SubscriberTask;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.pushservices.SubscriberDTO;
import com.xunku.pojo.push.Subscriber;
import com.xunku.utils.Pagefile;

public interface PushDao {

	List<SubscriberTask> queryTasks(int subId);

	/**
	 * 更新订阅任务的最后更新时间
	 * 
	 * @param subid
	 * @param taskid
	 * @param date
	 */
	void updateSubTaskLastCT(int subid, int taskid, Date date);

	void updateSubEventLastCT(int subid, int eventid, Date date);

	void insertPushTaskHis(int subid, int taskid, String tid, Date pushTime);

	List<String> queryPushTaskHis(int subid, int taskid);

	List<SubscriberEvent> queryEvents(int subId);

	List<String> queryEmails(int subId);

	/**
	 * 检查指定的订阅在当前类型下是否已经有数据了
	 * 
	 * @param subid
	 *            指定的订阅
	 * @param type
	 *            周期类型 0:分钟 1:小时 2:天 3:周 4 :月 5:年
	 * @param pcount
	 *            周期数
	 * @return
	 */
	int queryPushStatus(Subscriber sub);

	/**
	 * 获得客户下的所有订阅内容
	 * 
	 * @param customid
	 * @return
	 */
	List<Subscriber> querySubscriberByCustomId(int customid);

	/**
	 * 根据订阅id获得订阅内容
	 * 
	 * @param subscriberId
	 * @return
	 */
	Subscriber queryById(int subscriberId);

	/**
	 * 往pusher表里添加数据
	 * 
	 * @param sid
	 * @param tid
	 * @param platform
	 * @param text
	 */
	int addPusher(String customName, int sid, String text, String emailList);

	/**
	 * 检查指定的推送者是否存在
	 * 
	 * @param sid
	 * @param tid
	 * @param platform
	 * @return
	 */
	boolean existsPusher(int sid, String tid, int platform);

	/**
	 * 功能描述<推送服务[新建推送]>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 6, 20149:51:28 AM
	 */
	public void insert(Subscriber sub);

	/**
	 * 功能描述<推送服务[删除推送]>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 6, 201410:24:45 AM
	 */
	public boolean deleteByID(int subId);

	/**
	 * 功能描述<推送服务[获取推送列表]>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<Subscriber>
	 * @version twitter 1.0
	 * @date May 6, 201410:32:33 AM
	 */
	public Pagefile<Subscriber> queryByUserId(int customid, PagerDTO dto);

	/**
	 * 功能描述<推送服务[更改状态]>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 6, 201410:43:19 AM
	 */
	public void updateStatus(int subId, boolean isStop);

	/**
	 * 功能描述<更新推送信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 16, 20142:46:54 PM
	 */
	public void updateSubscriber(Subscriber sub);

	/**
	 * 功能描述<TODO>
	 * 
	 * @author wanghui
	 * @param void
	 * @return SubscriberDTO
	 * @version twitter 1.0
	 * @date May 16, 20143:08:47 PM
	 */
	public SubscriberDTO queryBySubID(int subId);

	/**
	 * 检查当前的推送服务是否停止，停止的条件是
	 * <p>
	 * 1、没有指定的任务，或者事件
	 * <p>
	 * 2、没有指定的发送人
	 * <p>
	 * 3、本身就是停止状态的
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean 返回true则表示未停止，返回false为已经停止
	 * @version twitter 1.0
	 * @date Sep 12, 20142:58:55 PM
	 */
	public boolean checkSubscriberStatus(int subId);
}
