package com.xunku.dao.base;

import java.util.List;

import com.xunku.dto.PagerDTO;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

public interface OrganizationsDao {
	/**
	 * 功能描述<添加机构>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jun 27, 20141:58:23 PM
	 */
	public boolean insert(Organization o);

	/**
	 * 功能描述<删除机构>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jun 27, 20141:58:42 PM
	 */
	public boolean deleteById(int oid);

	/**
	 * 功能描述<修改机构>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jun 27, 20141:58:52 PM
	 */
	public boolean update(Organization o);

	/**
	 * 功能描述<获取机构列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<Organization>
	 * @version twitter 1.0
	 * @date Jun 27, 20141:59:04 PM
	 */
	public Pagefile<Organization> queryPagefile(PagerDTO dto, int platform,
			int userId);

	/**
	 * 功能描述<获取微博数【机构管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Jul 4, 201410:14:05 AM
	 */
	public int queryWeibos(String uid, int platform, String startTime,
			String endTime);

	/**
	 * 功能描述<获取@我的数量【机构管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Jul 4, 201410:27:03 AM
	 */
	public int queryMyMentions(String uid, int platform, String startTime,
			String endTime);

	/**
	 * 功能描述<判断机构管理的名称是否已存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 6, 20145:14:05 PM
	 */
	public boolean checkIsExsit(int customid, String name);

	/**
	 * 功能描述<当前客户下的转发机构列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<Organization>
	 * @version twitter 1.0
	 * @date Sep 11, 20146:30:27 PM
	 */
	public List<Organization> queryOrganizationList(int customid);

	Pagefile<Organization> queryOrganizationList(int customid, Pager pager);
}
