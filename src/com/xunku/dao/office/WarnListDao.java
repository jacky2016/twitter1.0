package com.xunku.dao.office;

import java.util.Date;

import com.xunku.app.enums.WarnStatusEnum;
import com.xunku.constant.WarnType;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.WarnElement;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.Pagefile;

public interface WarnListDao {
	/**
	 * 功能描述<添加预警消息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Sep 11, 20143:20:02 PM
	 */
	boolean insert(WarnElement wl);

	/**
	 * 功能描述<预警消息列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<WarnList>
	 * @version twitter 1.0
	 * @date Sep 11, 20143:20:50 PM
	 */
	Pagefile<WarnElement> queryWarnListPagefile(Pager pager, int userid,
			Date startDate, Date endDate, WarnType type, WarnStatusEnum status);

	Pagefile<WarnElement> queryWarnListPagefile(Pager pager, int userid);

	/**
	 * 读取预警列表
	 * 
	 * @param userid
	 *            当前用户
	 * @param ids
	 *            当前已经读了的预警信息id
	 */
	int readWarnList(int userid, int[] ids);

	/**
	 * 更新数据库的某一条的未读到已读状态
	 * @param  id    Office_WarnList表中的id
	 * */
	int updateReadWarnList(int userid, int id) ;
	
	
	int getUnreadWarnListCount(int userid);

	WeiboWarn queryWeiboWarn(int warnListId);

	WarnElement queryUnwarnWeiboElement(int weibowarnId, int reciver);

}
