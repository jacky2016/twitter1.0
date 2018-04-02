package com.xunku.dto.eventMonitor;

//事件监控数据列表
/***
 * 事件监测-数据列表类
 * @author shaoqun
 *
 */
public class EventListDTO {

	private int id;
	private String topicname;  			//话题名称
	private String monitorStarttime;	//监控开始时间
	private String monitorEndtime;		//监控结束时间
	private int todaynum;				//今天话题数量
	private int yesterdaynum;			//昨天话题数量
	private int allnum;					//全部话题数量 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTopicname() {
		return topicname;
	}
	public void setTopicname(String topicname) {
		this.topicname = topicname;
	}

	public String getMonitorStarttime() {
		return monitorStarttime;
	}
	public void setMonitorStarttime(String monitorStarttime) {
		this.monitorStarttime = monitorStarttime;
	}
	public String getMonitorEndtime() {
		return monitorEndtime;
	}
	public void setMonitorEndtime(String monitorEndtime) {
		this.monitorEndtime = monitorEndtime;
	}
	public int getTodaynum() {
		return todaynum;
	}
	public void setTodaynum(int todaynum) {
		this.todaynum = todaynum;
	}
	public int getYesterdaynum() {
		return yesterdaynum;
	}
	public void setYesterdaynum(int yesterdaynum) {
		this.yesterdaynum = yesterdaynum;
	}
	public int getAllnum() {
		return allnum;
	}
	public void setAllnum(int allnum) {
		this.allnum = allnum;
	}
	
	
	
	
}
