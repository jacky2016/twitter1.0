package com.xunku.actions.eventMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import com.xunku.actions.ActionBase;
import com.xunku.app.AppContext;
import com.xunku.app.controller.EventController;
import com.xunku.app.enums.EventMonitorEnum;
import com.xunku.app.enums.StatisticsUnit;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.monitor.TweetEventManager;
import com.xunku.app.result.TrendTweetResult;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.dto.myTwitter.DataAnalyzeDTO;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/**
 * 事件监控--趋势分析信息
 * 
 * @author shaoqun
 * 
 */
public class TrendAnalysAction extends ActionBase {

	EventDao eventDAO = new EventDaoImpl();

	@Override
	public Object doAction() {

		String charType = this.get("charType");
		int eid = Integer.parseInt(this.get("eid"));
		String startTime = this.get("startTime");
		String endTime = this.get("endTime");
		int dayCount = Integer.parseInt(this.get("day"));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null, endDate = null;

		EventMonitor event = eventDAO.queryEventByEId(eid);
		String startEventTime = DateUtils.format(event.getStartTime());
		String endEventTime = DateUtils.format(event.getEndTime());
		EventController eventContor = new EventController();
		
		Calendar calendar = Calendar.getInstance();
		endDate = calendar.getTime(); // 当前时间
		if (charType.equals(EventMonitorEnum.Hour_24.toString())) { // 24小时趋势分析统计

			int diff = 0, diff2 = 0;
			Map<Long, Integer> map = null;
			try {
				startTime = DateUtils.formatDate(calendar.getTime()) + " 00:00:00";
				endTime = DateUtils.formatDate(calendar.getTime()) + " 23:59:59";
				startDate = sdf.parse(startTime);
				endDate = sdf.parse(endTime);
				diff = startDate.compareTo(sdf.parse(startEventTime));
				diff2 = (sdf.parse(endEventTime)).compareTo(endDate);

				if(diff < 0){
					startDate = sdf.parse(startEventTime);
				}
				if(diff2 < 0){
					endDate = sdf.parse(endEventTime);
				}
				map = AppServerProxy.getEventTrendByHour(startDate, endDate, event);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// for test by wujian
			// for(Map.Entry<Long, Integer> e:map.entrySet()){
			// System.out.println(new Date(e.getKey())+":"+e.getValue());
			// }

			return eventContor.getColumnDateByTime(map,startTime,endTime,1);

		} else if (charType.equals(EventMonitorEnum.Day_7.toString())) { // 7天趋势分析统计

			int diff = 0, diff2 = 0;
			Map<Long, Integer> map = null;
			try {
				endTime = DateUtils.formatDate(calendar.getTime()) + " 23:59:59";
				startTime = DateUtils.addDay2(endTime, dayCount) + " 00:00:00";
				startDate = sdf.parse(startTime);
				endDate = sdf.parse(endTime);
				
				diff = startDate.compareTo(sdf.parse(startEventTime));
				diff2 = (sdf.parse(endEventTime)).compareTo(endDate);

				if(diff < 0){
					startDate = sdf.parse(startEventTime);
				}
				if(diff2 < 0){
					endDate = sdf.parse(endEventTime);
				}
				map = AppServerProxy.getEventTrendByDate(startDate,endDate, event);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return eventContor.getColumnDateByTime(map,startTime,endTime, 2);
			
		} else if (charType.equals(EventMonitorEnum.DayDate.toString())) { // 按日期查询


			int diff = 0, diff2 = 0, diff3 = 0,day = 0;
			Map<Long, Integer> map = null;
			try {
				startTime = startTime + " 00:00:00";
				endTime = endTime + " 23:59:59";
				endDate = sdf.parse(endTime);
				startDate = sdf.parse(startTime);
				diff = startDate.compareTo(sdf.parse(startEventTime));
				diff2 = (sdf.parse(endEventTime)).compareTo(endDate);
				diff3 = startDate.compareTo(sdf.parse(endEventTime));

				if(diff < 0 ){
					startDate = sdf.parse(startEventTime);
					startTime = startEventTime;
				}else{
					
				}
				if (diff2 < 0) {
					endDate = sdf.parse(endEventTime);
					endTime = endEventTime;
				}
				day = (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24)+1;
				if(day >= 5){
					map = AppServerProxy.getEventTrendByDate(startDate,endDate, event);
					return eventContor.getColumnDateByTime(map,startTime,endTime,2);
				}else{
					map = AppServerProxy.getEventTrendByHour(startDate, endDate, event);
//					 for(Map.Entry<Long, Integer> e:map.entrySet()){
//						 System.out.println(new Date(e.getKey())+"--------:"+e.getValue());
//						 }
					return eventContor.getColumnDateByTimeStep(map,startTime,endTime,1);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			return "true";
		}

		return "true";
	}
}
