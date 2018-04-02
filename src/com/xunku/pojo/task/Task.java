package com.xunku.pojo.task;

import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;

public class Task {
	private int id;
	private int groupID; // 属于哪个组
	private String name; // 任务名称
	private String keywords; // 关键字组合
	private int creator; // 谁创建的
	private int customID; // 属于哪个企业？这是一个冗余设计，理论上通过Creator可以得到CustomID
	private int searchTime; // 任务搜索时间
	private String createTime; // 创建时间
	private List<Rubbish> rubbishList; // 垃圾词

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public int getCustomID() {
		return customID;
	}

	public void setCustomID(int customID) {
		this.customID = customID;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(int searchTime) {
		this.searchTime = searchTime;
	}

	public List<Rubbish> getRubbishList() {
		return rubbishList;
	}

	public void setRubbishList(List<Rubbish> rubbishList) {
		this.rubbishList = rubbishList;
	}

	public String getRubbishString() {

		StringBuilder rubbish = new StringBuilder();
		List<String> rList = new ArrayList<String>();
		if (this.rubbishList != null) {
			for (Rubbish r : this.rubbishList) {
				String[] rws = r.getRubbishWords().split(" ");
				for (String s : rws) {
					rList.add(s);
				}
			}
		}

		if (rList.size() > 0) {
			rubbish.append(" AND(");
			rubbish.append(Utility.getMetaQuery(rList.get(0), true));
			for (int i = 1; i < rList.size(); i++) {
				rubbish.append(" AND ");
				rubbish.append(Utility.getMetaQuery(rList.get(i), true));
			}
			rubbish.append(")");
		}
		return rubbish.toString();
	}
}
