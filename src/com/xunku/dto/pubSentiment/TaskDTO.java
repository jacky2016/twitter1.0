package com.xunku.dto.pubSentiment;
/*
 * TaskDTO 微博舆情 任务
 * @author: hjian
 */
public class TaskDTO {
    private int id;
    private String name;        //任务名称
    private String keywords;    //关键字组合
    private int searchTime;        //任务搜索时间
    private int customID;       //属于哪个企业？这是一个冗余设计，理论上通过Creator可以得到CustomID
    private String createTime;  //创建时间

    private int groupid;
    
    public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
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
    public int getSearchTime() {
        return searchTime;
    }
    public void setSearchTime(int searchTime) {
        this.searchTime = searchTime;
    }

}
