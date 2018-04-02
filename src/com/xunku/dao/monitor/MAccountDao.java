package com.xunku.dao.monitor;

import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.dto.PagerDTO;
import com.xunku.utils.Pagefile;

//@Deprecated
public interface MAccountDao {

	/**
	 * 获得系统下所有正在监控的帐号，已经无人监控的帐号则不需要再监控了
	 * <p>
	 * status用来判断当前帐号是否还需要监控相当于IsEnabled字段
	 * 
	 * @return
	 */
	List<AccountMonitor> queryAll(int status);

	/**
	 * 功能描述<记录所有待监测的帐号信息【账号监测】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Jun 16, 20142:36:50 PM
	 */
	public void insert(AccountMonitor ma);

	/**
	 * 功能描述<删除监测账号【账号监测】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jun 16, 20142:40:41 PM
	 */
	public boolean deleteByMid(int mid);

	/**
	 * 功能描述<获取监测账号信息的列表【账号监测】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<MAccount>
	 * @version twitter 1.0
	 * @date Jun 16, 20142:42:22 PM
	 */
	public Pagefile<AccountMonitor> queryPagefile(PagerDTO dto,
			Platform platform);

	/**
	 * 功能描述<获取账号监测列表【账号监测】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<MAccount>
	 * @version twitter 1.0
	 * @date Jun 19, 20149:43:44 AM
	 */
	public List<AccountMonitor> queryMAccountList();

	/**
	 * 通过ID获得监测项
	 * 
	 * @param monitorId
	 * @return
	 */
	public AccountMonitor queryMAccountByID(int monitorId);

}
