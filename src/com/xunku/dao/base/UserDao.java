package com.xunku.dao.base;

import java.util.List;

import com.xunku.app.model.ClientLoginTimes;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.pushservices.PushUserDTO;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;

public interface UserDao {

	ClientLoginTimes getLoginTimes(String clientId);

	void addLoginTimes(String clientId, long expired);

	void clearLoginTimes(String clientId);

	void incLoginTimes(String clientId, long expired);

	/**
	 * 获得指定客户的管理员
	 * 
	 * @param customid
	 * @return
	 */
	public User queryAdmin(int customid);

	/**
	 * 功能描述<用户维护：获取用户维护数量>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Aug 19, 20146:01:32 PM
	 */
	public int getUserByCustomid(int customid);

	/**
	 * 功能描述<添加用户>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 18, 20144:47:00 PM
	 */
	public void insert(User user);

	/**
	 * 功能描述<查询用户列表信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<User>
	 * @version twitter 1.0
	 * @date Apr 18, 20145:31:07 PM
	 */
	public Pagefile<User> queryByAll(PagerDTO dto, int uid);

	/**
	 * 功能描述<根据用户ID删除用户>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 18, 20145:54:48 PM
	 */
	public boolean deleteByID(int id);

	/**
	 * 功能描述<根据用户ID更新用户信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 18, 20145:59:29 PM
	 */
	public void updateByID(User user);

	/**
	 * 功能描述<获取系统用户信息【新建通知】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<UserDTO>
	 * @version twitter 1.0
	 * @date May 20, 20144:13:47 PM
	 */
	public List<PushUserDTO> queryUserByUID(int userid);

	/**
	 * 功能描述<获取当前客户下的用户列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<User>
	 * @version twitter 1.0
	 * @date Aug 7, 201410:21:50 AM
	 */
	public List<User> queryUserByCid(int customid);

	/**
	 * 功能描述<更新个人信息【个人设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 21, 20143:20:02 PM
	 */
	public void updateByUid(User user);

	/**
	 * 功能描述<通过uid获取当前用户【个人设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return User
	 * @version twitter 1.0
	 * @date May 21, 20143:36:28 PM
	 */
	public User queryByUid(int userid);

	/**
	 * 功能描述<验证密码是否正确【个人设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date May 23, 20141:36:20 PM
	 */
	public boolean checkPWD(int uid, String token);

	/**
	 * 功能描述<通过用户名称获取用户>
	 * 
	 * @author wanghui
	 * @param void
	 * @return User
	 * @version twitter 1.0
	 * @date Jul 31, 201410:04:42 AM
	 */
	public User getUserByName(String username);

	/**
	 * 功能描述<通过userid获取当前客户下的管理员>
	 * 
	 * @author wanghui
	 * @param void
	 * @return User
	 * @version twitter 1.0
	 * @date Jul 31, 201410:05:36 AM
	 */
	public User queryUserByIsAdmin(int userid);

	/**
	 * 功能描述<用户维护判断是否已存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 6, 20144:42:11 PM
	 */
	public boolean checkIsExsit(String name);

	/**
	 * 功能描述<重置密码>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 7, 201411:00:14 AM
	 */
	public boolean resetPWD(int userid, String password);

	/**
	 * 功能描述<指定审核人>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 11, 20144:15:26 PM
	 */
	public boolean updateApproved(int userid, int checkid);

	/**
	 * 功能描述<通过userid获取user>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Aug 22, 20145:29:02 PM
	 */
	public User queryByUserid(int userid);
	/**
	 * 功能描述<通过checkid获取当前用户【个人设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return User
	 * @version twitter 1.0
	 * @date May 21, 20143:36:28 PM
	 */
	public User queryByid(int id);
}
