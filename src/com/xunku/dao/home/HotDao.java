package com.xunku.dao.home;

import java.util.Date;
import java.util.List;

import com.xunku.pojo.home.Hot;

/**
 * 关键字：text+platform+date Date字段为日期字段，没有时间部分
 * 
 * @author wujian
 * @created on Aug 5, 2014 4:19:36 PM
 */
public interface HotDao {
	/**
	 * 功能描述<获取首页的热点文字>，按Num排序
	 * 
	 * @author wanghui
	 * @param Platform
	 *            0=新浪，1=腾讯，2=人民
	 * @return List<Hot>
	 * @version twitter 1.0
	 * @date Apr 18, 20141:55:03 PM
	 */
	public List<Hot> queryByPlatform(int platform, Date date);

	void insert(Hot hot);

	void update(Hot hot);

}
