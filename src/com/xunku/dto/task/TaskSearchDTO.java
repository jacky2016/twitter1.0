package com.xunku.dto.task;

import com.xunku.app.enums.Platform;
import com.xunku.constant.FiltrateEnum;
import com.xunku.constant.TimeSortEnum;
import com.xunku.dto.PagerDTO;
/**
 * 微博舆情按任务搜索
 * @author wanghui
 */
public class TaskSearchDTO {
    public int taskId;          //任务ID
    public String startTime;    //开始时间
    public String endTime;      //结束时间
    public FiltrateEnum type;   //筛选
    public TimeSortEnum timeSort;//排序
    public Platform platform;    //媒体
    public PagerDTO pagerDTO = new PagerDTO();         //分页
}
