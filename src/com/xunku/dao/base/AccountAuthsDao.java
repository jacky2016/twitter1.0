package com.xunku.dao.base;

import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.app.model.people.PUser;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.sysManager.PeopleDTO;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

public interface AccountAuthsDao {
	/**
	 * 功能描述<添加授权账号【账号设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Jun 5, 20149:45:07 AM
	 */
	public boolean insert(AccountAuths auth);

	/**
	 * 功能描述<延长授权信息【账号设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Jun 5, 20149:48:20 AM
	 */
	public boolean update(AccountAuths auth);

	/**
	 * 功能描述<删除授权>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jun 27, 20142:49:48 PM
	 */
	public boolean deleteById(int authId);

	/**
	 * 功能描述<获取授权列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<AccountAuths>
	 * @version twitter 1.0
	 * @date Jun 27, 20142:58:11 PM
	 */
	public Pagefile<AccountAuths> queryPagefile(Pager dto, int userId);

	public List<AccountAuths> queryAuthsByAccountIds(int[] accountids, int appid);

	public List<AccountAuths> queryAuthByAccountId(int accountid, int appid);

	public PUser queryPeopleUser(long peopleId);

	/**
	 * 功能描述<添加人民授权账号【账号设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Jun 5, 20149:45:07 AM
	 */
	public boolean insertPeople(PeopleDTO people);

	/**
	 * 功能描述<验证添加的人民微博帐号是否存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Jun 5, 20149:45:07 AM
	 */
	public boolean checkAccountBind(String name);
}
