package com.xunku.pojo.my;

import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

//官微提交微博信息 by wanghui
//对应数据库表My_Sending
public class Sending {

	/**
	 * 尝试最大次数
	 */
	public static final int MAX_TRIES = 4;

	private long id;
	private int userid; // 谁提交的
	private String text; // 提交的内容
	private String submit;// 提交的时间
	private long sent; // 发送时间 如果是0则立即发送，否则是按照指定时间的long格式发送
	private String images;// 图片列表
	private int type; // 类型 1 原创，2转发，3评论
	private String sourceid = "";// 转发或者评论微博Id
	private String orginalid = "";// 原始微博ID add by wujian
	private boolean flag; // 标志是否有相同的操作;是否评论或者转发为false，是否同时评论或者转发为true
	/**
	 * 审核状态：0不需要审核，1待审核，2审核通过，3审核失败
	 */
	private int approved; // 审核状态 0、不需要审核，可直接发送的 1、需要审核，待审核的，不能发送
	// 2、需要审核，审核通过的，可以发送的 3、需要审核，审核失败的，不能发送的
	private int auditor; // 审核人
	private List<Sender> sendList;

	long firstSent;
	
	/**
	 * 功能描述<获取原微博>
	 * 
	 * @author wanghui
	 * @param void
	 * @return ITweet
	 * @version twitter 1.0
	 * @date Aug 25, 20144:21:22 PM
	 */
	public ITweet getPost(User user, String sourceid, Platform platform) {
		if (sourceid == "" || sourceid.length() == 0) {
			return null;
		}

		ITweet tweet = AppServerProxy
				.queryITweetByTid(user, sourceid, platform);

		//if (tweet != null) {
		//	return tweet.getOriginalTweet();
		//}
		return tweet;
	}

	public List<Sender> getSendList() {
		return sendList;
	}

	public void setSendList(List<Sender> sendList) {
		this.sendList = sendList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public long getSent() {
		return sent;
	}

	public void setSent(long sent) {
		this.sent = sent;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public int getAuditor() {
		return auditor;
	}

	public void setAuditor(int auditor) {
		this.auditor = auditor;
	}

	public String getOrgId() {
		return this.orginalid;
	}

	public void setOrgId(String orgid) {
		this.orginalid = orgid;
	}

	public long getFirstSent() {
		return firstSent;
	}

	public void setFirstSent(long firstSent) {
		this.firstSent = firstSent;
	}
}
