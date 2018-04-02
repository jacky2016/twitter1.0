package com.xunku.actions.accountMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.EventController;
import com.xunku.app.enums.Platform;
import com.xunku.app.result.MAccountTrend;
import com.xunku.dto.accountMonitor.AnalyListDTO;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;
/**
 * 帐号监控--获取实时信息
 * @author shaoqun
 *
 */
public class RealAnalysAction extends ActionBase{

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub

		int method = Integer.parseInt(this.get("method"));
		int id = Integer.parseInt(this.get("id"));
		
		EventController eventContor = new EventController();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date todayStartDate = null, todayEndDate = null,sevenStartDate = null,
		 	 sevenEndDate = null,thrStartDate = null, thrEndDate = null;
		String todaySTime = "",todayETime = "",sevenSTime = "",
			   sevenETime = "",thrSTime="",thrETime = "";
		try {
			//今天
			todaySTime = DateUtils.formatDate(calendar.getTime()) + " 00:00:00";
			todayETime = DateUtils.formatDate(calendar.getTime()) + " 23:59:59";
			todayStartDate = sdf.parse(todaySTime);
			todayEndDate = sdf.parse(todayETime);
			//7天
			sevenSTime = DateUtils.addDay(DateUtils.formatDate(calendar.getTime()) + " 00:00:00",-6);
			sevenETime = DateUtils.formatDate(calendar.getTime()) + " 23:59:59";
			sevenStartDate = sdf.parse(sevenSTime);
			sevenEndDate = sdf.parse(sevenETime);
			//30天
			thrSTime = DateUtils.addDay(DateUtils.formatDate(calendar.getTime()) + " 00:00:00",-29);
			thrETime = DateUtils.formatDate(calendar.getTime()) + " 23:59:59";
			thrStartDate = sdf.parse(thrSTime);
			thrEndDate = sdf.parse(thrETime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(method == 1){
		
			int[] todayCount = AppServerProxy.MAccountViewRealTime(id,todayStartDate,todayEndDate);
			int[] sevenDayCount = AppServerProxy.MAccountViewRealTime(id,sevenStartDate,sevenEndDate);
			int[] threeCount = AppServerProxy.MAccountViewRealTime(id,thrStartDate,thrEndDate);
			
			Pagefile<AnalyListDTO> list = new Pagefile<AnalyListDTO>();
			AnalyListDTO at1 = new AnalyListDTO();
	
			at1.monitortime = "今天";
			at1.addWbnum = todayCount[0];
			at1.addFansnum = todayCount[1];
			at1.addForwardnum = todayCount[2];
			
			AnalyListDTO at2 = new AnalyListDTO();
			
			at2.monitortime = "7天";
			at2.addWbnum = sevenDayCount[0];
			at2.addFansnum = sevenDayCount[1];
			at2.addForwardnum = sevenDayCount[2];
			
			AnalyListDTO at3 = new AnalyListDTO();
			
			at3.monitortime = "30天";
			at3.addWbnum = threeCount[0];
			at3.addFansnum = threeCount[1];
			at3.addForwardnum = threeCount[2];
			
			list.getRows().add(at1);
			list.getRows().add(at2);
			list.getRows().add(at3);
			return list;
		}else if(method == 2){
			int charType =Integer.parseInt(this.get("lintype"));
			int radiovalue =Integer.parseInt(this.get("radiovalue"));

			MAccountTrend mtrend = new MAccountTrend();
			Map<Long, Integer> weibomap = new HashMap<Long, Integer>();
			Map<Long, Integer> retweetmap = new HashMap<Long, Integer>();
			if(charType == 1){
				mtrend = AppServerProxy.MAccountViewTrendToday(id, todayStartDate, todayEndDate,radiovalue);
				if(mtrend != null){
					weibomap = mtrend.weibo;
				}
				return eventContor.getColumnDateByTimeToday(weibomap,todaySTime,todayETime,1);

			}else if(charType == 2){
				mtrend = AppServerProxy.MAccountViewTrend(id, sevenStartDate, sevenEndDate);
				weibomap = mtrend.weibo;
				retweetmap = mtrend.retweet;
				if(radiovalue == 1){
					return eventContor.getColumnDateByTime(weibomap,sevenSTime,sevenETime,2);
				}else if(radiovalue == 2){
					return eventContor.getColumnDateByTime(retweetmap,sevenSTime,sevenETime,2);
				}
			}else if(charType == 3){
				mtrend = AppServerProxy.MAccountViewTrend(id, thrStartDate, thrEndDate);
				weibomap = mtrend.weibo;
				retweetmap = mtrend.retweet;
				if(radiovalue == 1){
					return eventContor.getColumnDateByTime(weibomap,thrSTime,thrETime,2);
				}else if(radiovalue == 2){
					return eventContor.getColumnDateByTime(retweetmap,thrSTime,thrETime,2);
				}
			}
			
		}
		
		return "true";
	}

}
