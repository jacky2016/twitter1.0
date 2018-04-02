package com.xunku.pojo.office;

import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;

/**
 * 功能描述：账号预警
 * <p>
 * 帐号预警：只要满足条件就发生预警，产生一条消息。预警任务不会停止，一直随帐号进行。
 * 
 * @author wanghui
 * @see 对应表Office_Account_Warning
 */
public class AccountWarn extends BaseWarn {
	private int id;
	private int accid; // 帐号监控id
	private String groupName;// 关键词组
	private List<String> keyword;// 关键词列表
	int sinceid;
	boolean happen;// 是否发生过，如果发生过则不再预警

	public AccountWarn() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccid() {
		return accid;
	}

	public void setAccid(int accid) {
		this.accid = accid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getKeyword() {

		StringBuilder sb = new StringBuilder();
		if (this.keyword.size() > 0) {
			sb.append(this.keyword.get(0));
		}
		for (int i = 1; i < this.keyword.size(); i++) {
			sb.append(";" + this.keyword.get(i));
		}

		return sb.toString();
	}

	public void setKeyword(String keyword) {

		if (!Utility.isNullOrEmpty(keyword) && this.keyword == null) {
			this.keyword = new ArrayList<String>();
			String[] ks = keyword.split(";");
			for (int i = 0; i < ks.length; i++) {
				if (!Utility.isNullOrEmpty(ks[i])) {
					this.keyword.add(ks[i]);
				}
			}
		}
	}

	public int getSinceid() {
		return sinceid;
	}

	public void setSinceid(int sinceid) {
		this.sinceid = sinceid;
	}

	public boolean isHappen() {
		return happen;
	}

	public void setHappen(boolean happen) {
		this.happen = happen;
	}
}
