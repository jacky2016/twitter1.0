package com.xunku.pojo.office;

/**
 * 功能描述：事件预警<p>
 * 事件预警：满足数量时发生预警，产生一条消息。再次修改 ，产生新的预警任务，发生新消息。
 * @author wanghui
 * @see 对应表Office_Event_Warning
 */
public class EventWarn extends BaseWarn{
    private int id;
    private int eventid;//事件id
    private String  eventName;//事件名字
    private int weibo;//微博达到多少条时触发预警
    boolean happen;//是否已经发生过，如果发生则不再预警
    
    public EventWarn(){}
    
    public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getWeibo() {
        return weibo;
    }
    public void setWeibo(int weibo) {
        this.weibo = weibo;
    }

    public int getEventid() {
        return eventid;
    }
    public void setEventid(int eventid) {
        this.eventid = eventid;
    }
	public boolean isHappen() {
		return happen;
	}
	public void setHappen(boolean happen) {
		this.happen = happen;
	}
}
