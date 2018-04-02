package com.xunku.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警服务--微博推送当中  监测内容
 * 分两种情况 ：
 * 1： 日常监测  2 ：事件监测 
 * 两种情况的DTO
 * @author shangwei
 *
 */
public class TaskNameDTO {
     public  int id;
     public  String name;//组名
     public  String  platformNames;// 微博平台  (新浪 or  腾讯  or  人民)
     public  List<TaskNameDTO>   catlogs=new ArrayList<TaskNameDTO>();//其中每个组下的几个小组
}
