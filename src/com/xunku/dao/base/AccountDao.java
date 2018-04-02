package com.xunku.dao.base;

import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.base.WeiboAccountDTO;
import com.xunku.dto.sysManager.AccountVO;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.utils.Pagefile;

public interface AccountDao {

	/**
	 * 检查帐号accid还有剩余多少秒过期
	 * 
	 * @param accid
	 * @return
	 */
	long remainAuth(int accid);

	void updateFansLoaded(int accid);

	/**
	 * 查看当前uid是否已经绑定了
	 * 
	 * @param uid
	 * @param platform
	 * @return
	 */
	boolean checkAccountBind(String uid, Platform platform);

	/**
	 * 通过accountid获得帐号信息
	 * 
	 * @param accountId
	 * @return
	 */
	public AccountInfo queryById(int accountId);

	/**
	 * 功能描述<根据账号查询微博账号的信息>
	 * 
	 * @author wanghui
	 * @param cId
	 *            账号的数组
	 * @return WeiboAccount
	 * @version twitter 1.0
	 * @date Apr 17, 20143:09:07 PM
	 */
	public List<AccountInfo> queryById(int[] cId);

	/**
	 * 功能描述<获取账号信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return AccountInfo
	 * @version twitter 1.0
	 * @date Jun 10, 20145:13:47 PM
	 */
	public AccountInfo queryByUid(String uid);

	/**
	 * 功能描述<根据用户查询博主信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<WeiboAccount>
	 * @version twitter 1.0
	 * @date Apr 17, 20144:50:41 PM
	 */
	public Pagefile<WeiboAccountDTO> queryByUserID(PagerDTO dto, int userId);

	/**
	 * 功能描述<账号设置[添加微博账号]>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 22, 20149:39:15 AM
	 */
	public void insert(AccountInfo wa, AccountAuths auth, int userId);

	/**
	 * 功能描述<账号设置[根据微博账号id删除微博账号]>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 22, 201410:55:49 AM
	 */
	public boolean deleteById(int aId,String wbname);

	/**
	 * 功能描述<获取客户下的微博账号【审核设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<AccountVO>
	 * @version twitter 1.0
	 * @date May 29, 20144:52:54 PM
	 */
	public List<AccountVO> queryAccountByUid(int userid);

	/**
	 * 功能描述<获取用户列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<AccountInfo>
	 * @version twitter 1.0
	 * @date Jul 8, 20145:54:38 PM
	 */
	public Pagefile<AccountInfo> queryPagefile(PagerDTO dto, int userId);

	/**
	 * 功能描述<检查该账号是否已存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jul 10, 20144:13:20 PM
	 */
	public boolean checkAccountExist(String uid, int userId);

	/**
	 * 功能描述<获取客户下的账号>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<AccountInfo>
	 * @version twitter 1.0
	 * @date Jul 14, 20143:30:44 PM
	 */
	public List<AccountInfo> queryByCustomId(int customId);

	/**
	 * 功能描述<获取用户下的所有账号>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<AccountInfo>
	 * @version twitter 1.0
	 * @date Aug 8, 20144:16:12 PM
	 */
	public List<AccountInfo> queryByUserId(int userid);

	/**
	 * 功能描述<获取帐号绑定de数量>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Aug 19, 20145:38:02 PM
	 */
	public int queryCountByCustomid(int customid);
}
