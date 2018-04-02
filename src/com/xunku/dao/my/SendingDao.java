package com.xunku.dao.my;

import java.util.List;

import com.xunku.app.enums.PostType;
import com.xunku.constant.SendCountEnum;
import com.xunku.constant.SendStatusEnum;
import com.xunku.constant.WeiboType;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.office.PostDealDTO;
import com.xunku.dto.office.SendInfoDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.Pagefile;

public interface SendingDao {
	/**
	 * 功能描述<判断当前账号是否需要审核>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 6, 201410:45:19 AM
	 */
	public boolean getApprovedCheck(int userid);

	/**
	 * 功能描述<提交的官微信息【我的首页】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 22, 20149:14:57 AM
	 */
	public long insert(Sending send);

	/**
	 * 功能描述<删除待审核的官微【发布管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 22, 20149:16:31 AM
	 */
	public boolean deleteBySId(Long sid);

	/**
	 * 功能描述<获取待发布的信息>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Sending
	 * @version twitter 1.0
	 * @date Jul 30, 20143:36:54 PM
	 */
	public Sending querySendById(long sid);

	/**
	 * 功能描述<修改待审核的官微【发布管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 22, 20149:18:48 AM
	 */
	public boolean update(String text, String images, long sid, int approved,long   sent); 

	/**
	 * 功能描述<审核待审核的官微【发布管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 22, 20149:20:45 AM
	 */
	public boolean checkSending(long sid, int approved, int auditor);

	/**
	 * 功能描述<待我审核、定时发布、审核失败【发布管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<Sending>
	 * @version twitter 1.0
	 * @date May 22, 20149:27:42 AM
	 */
	public Pagefile<Sending> queryListByStatus(PagerDTO dto, int userId,
			SendStatusEnum status);

	/**
	 * 功能描述<获取我发布的and所有已发布>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<SendingDTO>
	 * @version twitter 1.0
	 * @date Aug 5, 20143:21:57 PM
	 */
	public Pagefile<Sending> queryListByStatus(PagerDTO dto, int userid,
			int customid, PostType type, String startTime, String endTime,
			SendStatusEnum status);

	/**
	 * 功能描述<微博发布统计【考核管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<SendInfoDTO>
	 * @version twitter 1.0
	 * @date Jun 3, 20149:49:52 AM
	 */
	public Pagefile<SendInfoDTO> querySendInfo(PagerDTO dto, int customId,
			String startTime, String endTime);

	/**
	 * 功能描述<更改发送状态>
	 * 
	 * @author wanghui
	 * @param 发送状态：1=未发送、2=发送成功、3=发送失败
	 * @return boolean
	 * @version twitter 1.0
	 * @date Jul 14, 20143:41:06 PM
	 */
	public boolean updateSendStatus(long sendingid, int status, long senderid,
			String tid, int tries,long created);

	public void updateOrgID(long sid, String orgID);

	/**
	 * 功能描述<微博发布统计反查列表【微博发布统计】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<Sending>
	 * @version twitter 1.0
	 * @date Jul 25, 201410:24:24 AM
	 */
	public Pagefile<Sending> querySubmitInfo(Pager pager, String starttime,
			String endTime, int userid, SendCountEnum senum);

	/**
	 * 功能描述<微博处理统计【考核管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<Sending>
	 * @version twitter 1.0
	 * @date Aug 25, 20142:01:59 PM
	 */
	public Pagefile<PostDealDTO> querySendDeal(Pager pager, String startTime,
			String endTime, String uid, int customId);

	/**
	 * 功能描述<获取微博处理统计反查列表【考核管理】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<Sending>
	 * @version twitter 1.0
	 * @date Aug 25, 20143:42:41 PM
	 */
	public Pagefile<Sending> queryCommentList(Pager pager, String startTime,
			String endTime, String uid, int userid, WeiboType type);

	/**
	 * 功能描述<更改审核人把待我审核的改为审核失败>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 25, 20145:07:07 PM
	 */
	//public boolean updateApprovedFail(int checkid, int customid);
	public boolean updateApprovedFail(int checkid, int userid,int checkuserid);

	/**
	 * 返回Sending里面所有定时发送的微博和发送失败的微博
	 * 
	 * @return
	 */
	public List<Sending> queryAllUnSending();
}
