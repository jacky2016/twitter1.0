package com.xunku.dto.sysManager;
/*
 * SysManager_RubbishDTO 系统管理 垃圾词信息
 * @author: hjian
 */
public class SysManager_RubbishDTO {
	private int id;
	private int customid;		//客户编号
	private String groupname;	//分类名称
	private String rubbishwords;//垃圾词
	private String createtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCustomid() {
		return customid;
	}
	public void setCustomid(int customid) {
		this.customid = customid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getRubbishwords() {
		return rubbishwords;
	}
	public void setRubbishwords(String rubbishwords) {
		this.rubbishwords = rubbishwords;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
