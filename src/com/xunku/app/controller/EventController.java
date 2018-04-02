package com.xunku.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Vector;

import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;

public class EventController {

	// 组织成图表Line对象(线状图)(24小时、七天、日期搜索)
	public LineDTO getColumnDateByTime(Map<Long, Integer> maps,String startTime,String endTime,int id) {
		LineDTO column = new LineDTO();
		LineDataDTO dataDTO = new LineDataDTO();
		// 添加顶部标题 
		column.title = "时间分布统计查询";
		dataDTO.name = "发布";
		int k = 0;
		
		if (maps != null) {
			if(maps.size() != 0){
				//Iterator<Entry<Long, Integer>> iter = maps.entrySet().iterator();
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
				int value = 0,step = 0;;
				long key = 0;
				String str = "",day="";
				
				GregorianCalendar[] ga;
				try {
					ga = getBetweenDate(startTime, endTime,id);
					for(GregorianCalendar e:ga){
						int j = 0;
						if(id == 1){
							if(e.get(Calendar.HOUR_OF_DAY) < 10){
								day = "0"+e.get(Calendar.HOUR_OF_DAY)+ "时";
							}else{
								day = String.valueOf((e.get(Calendar.HOUR_OF_DAY)))+ "时";
							}
						}else if(id == 2){
							if(e.get(Calendar.DAY_OF_MONTH) < 10){
								day = "0"+e.get(Calendar.DAY_OF_MONTH)+ "日";
							}else{
								day = String.valueOf((e.get(Calendar.DAY_OF_MONTH)))+ "日";
							}
						}

						//判断相同的日期并赋值
						for(Map.Entry<Long, Integer> entry : maps.entrySet()){
							key = entry.getKey();
							Date date = new Date(key);
							
							if(Integer.parseInt(sdf2.format(date)) < 10){
								if(id == 1){
									str = sdf.format(date) + "时";
								}else if(id == 2){
									str = sdf2.format(date) + "日";
								}
							}else{
								if(id == 1){
									str = sdf.format(date) + "时";
								}else if(id == 2){
									str = sdf2.format(date) + "日";
								}//str = (e.get(Calendar.MONTH)+1)+"月"+e.get(Calendar.DAY_OF_MONTH)+ "日";
							}
							if(str.equals(day)){
								value = entry.getValue();
								column.categories.add(str); // 添加x坐标数据
								dataDTO.data.add(Double.valueOf(value));// 添加y坐标数据 
								if(value == 0){
									k++;
								}
								break; 
							}
							j++;
						}
						
						if(j == maps.entrySet().size()){//匹配不上的日期赋值0
							value = 0;
							column.categories.add(day); // 添加x坐标数据
							dataDTO.data.add(Double.valueOf(value));// 添加y坐标数据 
							k++;
						}
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}  
			}else{
				column.test = -2;
			}
		}else{
			column.test = -2;
		}
		if(k == column.categories.size()){
			column.test = -2;
		}
		column.series.add(dataDTO);
		return column;
	}

	//获取两日期之间所有日期
	public GregorianCalendar[] getBetweenDate(String d1,String d2,int id) throws ParseException {
		 Vector<GregorianCalendar> v=new Vector<GregorianCalendar>();  
	     SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	     GregorianCalendar gc1=new GregorianCalendar(),gc2=new GregorianCalendar();  
	     gc1.setTime(sdf.parse(d1));  
	     gc2.setTime(sdf.parse(d2));  
	     do{  
	         GregorianCalendar gc3=(GregorianCalendar)gc1.clone();  
	         v.add(gc3);  
	         if(id == 1){
	        	 gc1.add(Calendar.HOUR_OF_DAY, 1);  
	         }else if(id == 2){
	        	 gc1.add(Calendar.DAY_OF_MONTH, 1);  
	         }
	                      
	    }while(!gc1.after(gc2));  
		
		return v.toArray(new GregorianCalendar[v.size()]);  
	}
	
	// 组织成图表Line对象(线状图)
	public LineDTO getColumnDateByTimeStep(Map<Long, Integer> maps,String startTime,String endTime,int id) {
		LineDTO column = new LineDTO();
		LineDataDTO dataDTO = new LineDataDTO();
		// 添加顶部标题 
		column.title = "时间分布统计查询";
		dataDTO.name = "发布";

		if (maps != null) {
			if(maps.size() != 0){
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
				int value = 0,step = 0,k = 0,x = 0;
				GregorianCalendar[] ga;
				try {
					ga = getBetweenDate(startTime, endTime,id);
					step = (ga.length)/8;//取得步长

					for(GregorianCalendar e:ga){
						int j = 0;String str = "",time="",day="",today="";long key = 0;
						if(e.get(Calendar.DAY_OF_MONTH) < 10){
							today = "0"+String.valueOf((e.get(Calendar.DAY_OF_MONTH)))+ "日";
						}else{
							today = String.valueOf((e.get(Calendar.DAY_OF_MONTH)))+ "日";
						}
						if(e.get(Calendar.HOUR_OF_DAY) < 10){
							day = today+"0"+e.get(Calendar.HOUR_OF_DAY)+ "时";
							time = String.valueOf((e.get(Calendar.MONTH))+1)+"-"+String.valueOf((e.get(Calendar.DAY_OF_MONTH)))+" 0"+e.get(Calendar.HOUR_OF_DAY)+ ":00";
						}else{
							day = today+e.get(Calendar.HOUR_OF_DAY)+ "时";//e.get(Calendar.DAY_OF_MONTH)+
							time = String.valueOf((e.get(Calendar.MONTH))+1)+"-"+String.valueOf((e.get(Calendar.DAY_OF_MONTH)))+" "+e.get(Calendar.HOUR_OF_DAY)+ ":00";
						}
						
						//判断相同的日期并赋值
						for(Map.Entry<Long, Integer> entry : maps.entrySet()){
							key = entry.getKey();
							Date date = new Date(key);
							str = sdf2.format(date)+"日"+sdf.format(date) + "时";
							if(str.equals(day)){
								value += entry.getValue();
								k++;
								break; 
							}
							j++;
						}
						
						if(j == maps.entrySet().size()){//匹配不上加1继续判断
							k++;
						}
						
						if(k == step){
							x++;
							column.categories.add(time); // 添加x坐标数据
							dataDTO.data.add(Double.valueOf(value));// 添加y坐标数据 
							value = 0;
							k = 0;
						}
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}  
					
			}else{
				column.test = -2;
			}
		}else{
			column.test = -2;
		}
		column.series.add(dataDTO);
		return column;
	}
	
	
	// 组织成图表Line对象(线状图)(24小时)
	public LineDTO getColumnDateByTimeToday(Map<Long, Integer> maps,String startTime,String endTime,int id) {
		LineDTO column = new LineDTO();
		LineDataDTO dataDTO = new LineDataDTO();
		// 添加顶部标题 
		column.title = "时间分布统计查询";
		dataDTO.name = "发布";
		int k = 0;
		
		if (maps != null) {
			if(maps.size() != 0){
				//Iterator<Entry<Long, Integer>> iter = maps.entrySet().iterator();
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				int step = 0;;
				long key = 0;
				String str = "",day="";
				
				GregorianCalendar[] ga;
				try {
					ga = getBetweenDate(startTime, endTime,id);
					for(GregorianCalendar e:ga){
						int j = 0,value = 0;
						if(e.get(Calendar.HOUR_OF_DAY) < 10){
							day = "0"+e.get(Calendar.HOUR_OF_DAY)+ "时";
						}else{
							day = String.valueOf((e.get(Calendar.HOUR_OF_DAY)))+ "时";
						}

						//判断相同的日期并赋值
						for(Map.Entry<Long, Integer> entry : maps.entrySet()){
							key = entry.getKey();
							Date date = new Date(key);
							
							if(Integer.parseInt(sdf.format(date)) < 10){
								str = sdf.format(date) + "时";
							}else{
								str = sdf.format(date) + "时";
							}
							if(str.equals(day)){
								if(entry.getValue() == 0){
									k++;
								}
								value += entry.getValue();
//								column.categories.add(str); // 添加x坐标数据
//								dataDTO.data.add(Double.valueOf(value));// 添加y坐标数据 
//								if(value == 0){
//									k++;
//								}
//								break; 
							}else{
								j++;
							}
						}
						
						if(j == maps.entrySet().size()){//匹配不上的日期赋值0
							value = 0;
							column.categories.add(day); // 添加x坐标数据
							dataDTO.data.add(Double.valueOf(value));// 添加y坐标数据 
							k++;
						}else{
							column.categories.add(day); // 添加x坐标数据
							dataDTO.data.add(Double.valueOf(value));// 添加y坐标数据 
							//break; 
						}
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}  
			}else{
				column.test = -2;
			}
		}else{
			column.test = -2;
		}
//		if(k == column.categories.size()){
//			column.test = -2;
//		}
		column.series.add(dataDTO);
		return column;
	}
}
