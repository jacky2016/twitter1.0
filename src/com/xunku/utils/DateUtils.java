package com.xunku.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期工具类
 * 
 * @author wanghui
 */
public class DateUtils {
	public static void main(String[] args) {
		long time = 1409664026000l;
		System.out.println((new Date(time)));
	}

	public static long dateStringToInteger(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = 0;
		try {
			Date date = sdf.parse(dateString);
			time = (long) date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 功能描述<生成图片文件的名称>
	 * 
	 * @author wanghui
	 * @param void
	 * @return long
	 * @version twitter 1.0
	 * @date Apr 28, 201411:27:31 AM
	 */
	public static long getImageNameCode() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssmm");
		Date date = new Date();
		return Long.parseLong(sdf.format(date));
	}

	public static boolean compareToDate(String date) {
		boolean flag = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date dt1 = format.parse(date);
			Date dt2 = format.parse(nowDateFormat("yyyy-MM-dd HH:mm"));
			if (dt1.getTime() > dt2.getTime()) {
				flag = true;
			} else if (dt1.getTime() < dt2.getTime()) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static boolean compareToDate(Date date1, Date date2) {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = sdf.parse(sdf.format(date1));
			Date dt2 = sdf.parse(sdf.format(date2));
			if (dt1.getTime() > dt2.getTime()) {
				flag = true;
			} else if (dt1.getTime() < dt2.getTime()) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 将某一字符串格式化目标格式
	 * 
	 * @param dateString
	 *            需要进行格式化的字符串
	 * @param oldFormat
	 *            原始的格式
	 * @param newformat
	 *            格式化后的格式
	 * @return 格式化后的字符串
	 * @throws ParseException
	 */
	public static String StringDateFormat(String dateString, String oldFormat,
			String newformat) throws ParseException {
		SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat);
		SimpleDateFormat newDateFormat = new SimpleDateFormat(newformat);
		Date date = oldDateFormat.parse(dateString);
		return newDateFormat.format(date);
	}

	/**
	 * 或取当前时间格式化后的字符串
	 * 
	 * @return 格式化后的字符串
	 * @throws ParseException
	 */
	public static String nowDateFormat(String format) throws ParseException {
		Date d = new Date();// 获取时间
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 转换格式
		return sdf.format(d);
	}

	/**
	 * 获得当前日期前几天的日期
	 * 
	 * @param day
	 *            天数
	 * @param format
	 *            格式化规则
	 * @return 格式化后的字符串
	 */
	public static String getBeforNowDayToString(int day, String format) {
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.set(Calendar.DATE, now.get(Calendar.DATE) - (day - 1));
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(now.getTime());
	}

	// 实现日期加一天的方法
	public static String addDay(String s, int n) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Calendar cd = Calendar.getInstance();
			Date d = sdf.parse(s);
			cd.setTime(d);
			cd.add(Calendar.DATE, n);// 增加一天
			// cd.add(Calendar.MONTH, n);//增加一个月
			return sdf.format(cd.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 实现日期加一天的方法 @author: shaoqun
	public static String addDay2(String s, int n) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cd = Calendar.getInstance();
			Date d = sdf.parse(s);
			cd.setTime(d);
			cd.add(Calendar.DATE, n);// 增加一天
			// cd.add(Calendar.MONTH, n);//增加一个月
			return sdf.format(cd.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 计算开始时间与当前时间之间相差多少天
	 * 
	 * @param dateString
	 *            开始时间字符串
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int beforDays(String dateString) {
		Date now = new Date();
		Date beforDate;
		int day = 0;
		try {
			beforDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			day = (int) ((now.getTime() - beforDate.getTime()) / 1000 / 60 / 60 / 24) + 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 计算开始时间与当前时间之间相差多少天
	 * 
	 * @param dateString
	 *            开始时间字符串
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int beforDays(String startTime, String endTime)
			throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
		Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
		int day = (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24) + 1;
		return day;
	}

	public static int beforDays(Date startDate, Date endDate)
			throws ParseException {
		int day = (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24) + 1;
		return day;
	}

	/**
	 * 计算开始时间与当前时间之间相差多少天
	 * 
	 * @param beginDate
	 *            开始时间字符串
	 * @param endDate
	 *            结束时间字符串
	 * @return boolean true开始时间小于结束时间，false结束时间小于开始时间
	 * @throws ParseException
	 */
	public static boolean checkBegin_End(String beginDate, String endDate)
			throws ParseException {
		boolean flag = true;
		Date beforDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDate);
		Date endsDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
		flag = beforDate.getTime() <= endsDate.getTime();
		return flag;
	}

	/**
	 * 将string转换成date
	 * 
	 * @param formmat
	 *            原本string的格式
	 * @param date
	 *            需要进行转换的String
	 * @return Date
	 */
	public static Date stringToDate(String formmat, String date) {
		DateFormat format = new SimpleDateFormat(formmat);
		Date d = null;
		try {
			d = format.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	public static String StringDateFormat(String datetime)
			throws ParseException {
		String yy = datetime.substring(0, 4);
		String mm = datetime.substring(5, 7);
		String dd = datetime.substring(8, 10);
		String hh = datetime.substring(11, 13);
		String m = datetime.substring(14, 16);
		String s = datetime.substring(17, 19);
		String date = yy + "-" + mm + "-" + dd + " " + hh + ":" + m + ":" + s;
		return date;
	}

	public static String StringFormat(String datetime) throws ParseException {
		String yy = datetime.substring(0, 4);
		String mm = datetime.substring(5, 7);
		String dd = datetime.substring(8, 10);
		String hh = datetime.substring(11, 13);
		String m = datetime.substring(14, 16);
		// String s = datetime.substring(17, 19);
		String date = yy + "-" + mm + "-" + dd + " " + hh + ":" + m;
		return date;
	}

	/**
	 * 把日期标准化为yyyyMMdd格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null) {
			return dayformat.format(Calendar.getInstance().getTime());
		}
		return dayformat.format(date);

	}

	/**
	 * 把日期标准化为yyyy-MM-dd HH:mm格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateString(Date date) {
		SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dayformat.format(date);
	}

	/**
	 * add by wujian for long
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateString(long date) {
		return formatDateString(new Date(date));
	}

	/**
	 * 把日期标准化为yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dayformat.format(date);
	}

	/**
	 * 功能描述<根据日期段按天数分割日期段>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Map<String,String>
	 * @version twitter 1.0
	 * @date Apr 30, 20143:01:19 PM
	 */
	public static List<String> getDateAnalyze(String startTime, String endTime,
			int n) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = format.parse(startTime);
		Date endDate = format.parse(endTime);
		List<String> list = new ArrayList<String>();
		while (!startDate.after(endDate)) {
			String stime = format.format(startDate);
			list.add(stime);
			Calendar cd = Calendar.getInstance();
			cd.setTime(startDate);
			cd.add(Calendar.DATE, n);// 增加一天
			startDate = cd.getTime();
		}
		return list;
	}

	public static Map<Date, Date> getDateAnalyze(String startTime,
			String endTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = format.parse(startTime);
		Date endDate = format.parse(endTime);
		Map<Date, Date> map = new HashMap<Date, Date>();
		Date tempDate = null;
		while (!startDate.after(endDate)
				&& !DateUtils.compareToDate(startDate, endDate)) {
			Calendar cd = Calendar.getInstance();
			cd.setTime(startDate);
			cd.add(Calendar.DATE, 1);// 增加一天
			tempDate = cd.getTime();
			map.put(startDate, tempDate);
			startDate = tempDate;
		}
		return map;
	}

	public static int expireDay(int expiresin) {
		return (int) expiresin / 60 / 60 / 24;
	}

}
