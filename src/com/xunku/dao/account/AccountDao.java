package com.xunku.dao.account;

import com.xunku.app.enums.Platform;
import com.xunku.app.model.Pooling;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.utils.Pagefile;

public interface AccountDao {

	/**
	 * 更新当日增量粉丝
	 * 
	 * @param pool
	 * @param id
	 * @param timezone
	 * @param fans
	 */
	void updateRealtime(Pooling pool, int id, long timezone, int fans);

	void updateRealtime(Pooling pool, int id, long timezone, int weibos,
			int retweets);

	/**
	 * 更新Monitor的微博数，粉丝数，关注数
	 * 
	 * @param id
	 * @param weibos
	 * @param fans
	 * @param friends
	 */
	void updateMonitorCnt(int id, int weibos, int fans, int friends);

	/**
	 * 分页获得监测帐号列表
	 * 
	 * @param pager
	 * @return
	 */
	Pagefile<AccountMonitor> queryAccountList(Pager pager);

	/***************************************************************************
	 * 功能描述<获取帐号监控列表【帐号监控】>
	 * 
	 * @author shaoqun
	 * @param dto
	 * @param userid
	 * @param platform
	 * @return Pagefile<AccountMonitor>
	 */
	public Pagefile<AccountMonitor> queryAccountList(Pager dto, int userid,
			Platform platform);

	/**
	 * 根据帐号id获得监测帐号对象
	 * 
	 * @param accid
	 * @return
	 */
	AccountMonitor queryAccountMonitorById(int accid);

	/**
	 * 将time之前的任务都清除了
	 * 
	 * @param time
	 */
	void clear(long time);

	/***************************************************************************
	 * 功能描述<添加帐号【帐号监控】>
	 * 
	 * @author shaoqun
	 * @param dto
	 * @param userid
	 * @param platform
	 * @return Pagefile<AccountMonitor>
	 */
	public boolean insert(AccountMonitor acc);

	/**
	 * 功能描述<判断帐号的名称是否已存在【帐号监控】>
	 * 
	 * @author shaoqun
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 */
	public boolean checkIsExsit(int customid, String name);

	/**
	 * 功能描述<删除帐号【帐号监控】>
	 * 
	 * @author shaoqun
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 */
	public void deleteByEId(int id);

	/**
	 * 功能描述<帐号监控：判断帐号有效期内的个数>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 */
	public int getPeriodAccountCount(int CustomID, Platform platform);

	/**
	 * 功能描述<帐号监控：根据帐号id获取预警信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 */
	public AccountWarn queryAccountById(int accid);
	/**
	 * 功能描述<判断帐是否存在【帐号监控】>
	 * 
	 * @author shaoqun
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 */
	public boolean checkIsAccount(int customid,int userid, String name);
}
