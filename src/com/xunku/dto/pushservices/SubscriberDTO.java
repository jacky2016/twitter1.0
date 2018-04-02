package com.xunku.dto.pushservices;

import java.util.List;

import com.xunku.pojo.push.ContactInfo;
import com.xunku.pojo.task.Task;
//推送前端展示 by wanghui
public class SubscriberDTO {
    private int id;
    private String name;         // 推送名称
    private int userId;
    private int type;            // 推送类型 0:日常监测,1:事件监测
    private boolean isSendMail;  // 是否发送邮件
    private String firstRunTime; // 首次执行时间
    private String lastExecuteTime;//最后执行的时间
    private int topN;            // 每栏目发送显示条数 5条-15条
    private int periodType;      // 周期类型 0:分钟 1:小时 2:天 3:周 4 :月 5:年
    private int periodCount;     // 周期数
    //private boolean isStop;      // 是否停止
    private List<ContactInfo> contacts;// 推送包含收件人email
    private List<Task> taskList;// 推送包含任务
    private List<EventDTO> eventList;//推送包含事件
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
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public boolean isSendMail() {
        return isSendMail;
    }
    public void setSendMail(boolean isSendMail) {
        this.isSendMail = isSendMail;
    }
    public String getFirstRunTime() {
        return firstRunTime;
    }
    public void setFirstRunTime(String firstRunTime) {
        this.firstRunTime = firstRunTime;
    }
    public int getTopN() {
        return topN;
    }
    public void setTopN(int topN) {
        this.topN = topN;
    }
    public int getPeriodType() {
        return periodType;
    }
    public void setPeriodType(int periodType) {
        this.periodType = periodType;
    }
    public int getPeriodCount() {
        return periodCount;
    }
    public void setPeriodCount(int periodCount) {
        this.periodCount = periodCount;
    }
    public List<ContactInfo> getContacts() {
        return contacts;
    }
    public void setContacts(List<ContactInfo> contacts) {
        this.contacts = contacts;
    }
    public List<Task> getTaskList() {
        return taskList;
    }
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
    public String getLastExecuteTime() {
        return lastExecuteTime;
    }
    public void setLastExecuteTime(String lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }
    public List<EventDTO> getEventList() {
        return eventList;
    }
    public void setEventList(List<EventDTO> eventList) {
        this.eventList = eventList;
    }
}
