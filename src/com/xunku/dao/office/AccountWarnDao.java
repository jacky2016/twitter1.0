package com.xunku.dao.office;

import java.util.List;

import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.utils.Pagefile;

public interface AccountWarnDao {

	void changeRunning(int accountid, int customid, boolean running);

	/**
	 * 获得客户下的帐号监测分页列表
	 * 
	 * @param customid
	 * @param pager
	 * @return
	 */
	Pagefile<AccountWarn> queryAccountWarnList(int customid, Pager pager);

	/**
	 * 功能描述<添加账号预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 201410:09:25 AM
	 */
	public int insert(AccountWarn aw);

	/**
	 * 功能描述<删除账号预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 201410:10:06 AM
	 */
	public boolean deleteById(int awid);

	/**
	 * 功能描述<更新账号预警>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 29, 201410:10:22 AM
	 */
	public boolean update(AccountWarn aw);

	void updateSinceId(int warnid, int sinceId);

	/**
	 * 功能描述<获取客户下的账号预警列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<AccountWarn>
	 * @version twitter 1.0
	 * @date Aug 29, 201410:10:37 AM
	 */
	public List<AccountWarn> queryAccountWarnList(int customid);

	/**
	 * 功能描述<根据id获取客户下的预警账号>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<AccountWarn>
	 * @version twitter 1.0
	 * @date Aug 29, 201410:10:37 AM
	 */
	public AccountWarn queryAccountWarnById(int customid);
}
