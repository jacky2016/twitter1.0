package com.xunku.app.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.xunku.constant.PortalCST;

public class DateHelper {

	public static final int ONE_HOUR = 1000 * 60 * 60;
	public static final int ONE_DAY = ONE_HOUR * 24;
	public static final int ONE_MONTH = ONE_DAY * 30;

	public static void main(String[] args) {

		long[] lg = new long[] { 1400655634245l, 1400659234245l,
				1400662834245l, 1400666434245l, 1400670034245l, 1400673634245l,
				1400677234245l, 1400680834245l, 1400684434245l, 1400688034245l,
				1400691634245l, 1400695234245l, 1400698834245l, 1400702454245l,
				1400706024245l, 1400709624255l, 1400713224299l, 1400716899999l,
				1400720499999l, 1400724045632l, 1400727645632l, 1400731245632l,
				1400734845632l, 1400745645632l, 1400752845632l, 1400756445632l,
				1400763645632l, 1400767245632l, 1400770845632l, 1400781645632l,
				1400824845632l, 1400900445632l, 1400904045632l, 1401422445632l };

		Date[] dt = new Date[lg.length];

		for (int i = 0; i < lg.length; i++) {
			dt[i] = new Date(lg[i]);
			// System.out.println(lg[i]);
			// System.out.println(dt[i]);
			formatDateFirst(dt[i]);
			// System.out.println(getTimezoneHour(dt[i]));
			// System.out.println(getTimezoneOnlyHour(dt[i]));
		}

		long d1 = 1406080865000l;
		long d2 = 1406721600430l;
		long d3 = 1405915200032l;

		formatDateFirst(new Date(d1));
		formatDateFirst(new Date(d2));
		formatDateFirst(new Date(d3));

		Calendar c = Calendar.getInstance();
		c.set(2014, 8, 19, 0, 0, 0);

		System.out.println(c.getTimeInMillis());

		c.set(2014, 8, 19, 23, 59, 59);

		System.out.println(c.getTimeInMillis());
		formatDateFirst(new Date());

		System.out.println(new Date(1411487940000l));
		System.out.println(new Date(1411488000000l));
		System.out.println(new Date(1411574399999l));
		System.out.println(new Date(1414560880279l));
		System.out.println(new Date(1412857782855l));
		// 1411488000000
	}

	public static int getTimezoneOnlyHour(long date) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static int getTimezoneOnlyHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static long getTimezoneDay(Date date) {
		long lc = (date.getTime() / ONE_DAY) * ONE_DAY;
		return lc;
	}

	public static long getTimezoneDay(long time) {
		return (time / ONE_DAY) * ONE_DAY;
	}

	/**
	 * 获得该时间属于那个时间区间（按小时划分的时间区间）
	 * 
	 * @param date
	 * @return
	 */
	public static long getTimezoneHour(Date date) {
		long result = (date.getTime() / ONE_HOUR) * ONE_HOUR;
		return result;
	}

	public static long getTimezoneHour(long time) {
		return (time / ONE_HOUR) * ONE_HOUR;
	}

	/**
	 * 只获得当前时间的含小时的部分
	 * 
	 * @return
	 */
	public static long getTimezonHour() {
		return (System.currentTimeMillis() / ONE_HOUR);// * ONE_HOUR;
	}

	/**
	 * 获得本小时时间刻度
	 * 
	 * @param date
	 * @return
	 */
	public static long getTimezoneHour() {
		Date date = new Date();
		return getTimezoneHour(date);
	}

	/**
	 * 格式化指定时间为指定时间当天的第一秒
	 * 
	 * @param date
	 * @return
	 */
	public static long formatDateFirst(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// long l0 = c.getTimeInMillis();
		// System.out.println("t>" + l0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		long l1 = c.getTimeInMillis();// .getTime().getTime();
		// System.out.println("第一秒为：" + l1 + "->" + c.getTime());
		return l1;
		/*
		 * System.out.println(">" + l1);
		 * 
		 * c.set(Calendar.HOUR_OF_DAY, 23); c.set(Calendar.MINUTE, 59);
		 * c.set(Calendar.SECOND, 59); c.set(Calendar.MILLISECOND, 999); long l2 =
		 * c.getTime().getTime(); System.out.println("<" + l2);
		 * 
		 * System.out.println("->" + (l2 - l1)); return new
		 * SimpleDateFormat("yyyy-MM-dd ").format(date) +
		 * PortalCST.TIME_FIRST_SECOND;
		 */
	}

	/**
	 * 格式化指定时间为指定时间当天的最后一秒
	 * 
	 * @param date
	 * @return
	 */
	public static long formatDateLast(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// long l0 = c.getTimeInMillis();
		// System.out.println("t>" + l0);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		long l1 = c.getTimeInMillis();// .getTime().getTime();
		// System.out.println("最后一秒为：" + l1 + "->" + c.getTime());
		return l1;
	}

	/**
	 * 获得今天的第一秒
	 * 
	 * @return
	 */
	public static long formatTodayFirst() {
		return formatDateFirst(new Date());
	}

	/**
	 * 计算两个时间之间相差多少天
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getDistanceDays(long time1, long time2) {

		return (time2 - time1) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 获得今天的最后一秒
	 * 
	 * @return
	 */
	public static long formatTodayLast() {
		return formatDateLast(new Date());
	}

	/**
	 * 获得昨天的第一秒
	 * 
	 * @return
	 */
	public static long formatYesterdayFirst() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return formatDateFirst(cal.getTime());
	}

	/**
	 * 获指定时间的昨天的第一秒
	 * 
	 * @return
	 */
	public static long formatYesterdayFirst(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		return formatDateFirst(cal.getTime());
	}

	/**
	 * 获得前天的第一秒
	 * 
	 * @return
	 */
	public static long formatBeforeYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		return formatDateFirst(cal.getTime());
	}

	/**
	 * 获得指定时间的前一天的第一秒
	 * 
	 * @param date
	 * @return
	 */
	public static long formatBeforeYesterday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -2);
		return formatDateFirst(cal.getTime());
	}

	/**
	 * 获得昨天的最后一秒
	 * 
	 * @return
	 */
	public static long formatYesterdayLast() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return formatDateLast(cal.getTime());
	}

	/**
	 * 获得指定时间的刻钟表示
	 * 
	 * @param date
	 * @return
	 */
	public static String getQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		// 取出来小时和分钟计算出描述

		int minutes = cal.get(Calendar.MINUTE);
		int quarter = (hours * 4) + ((minutes / 15) + 1);
		String strQuarter = String.valueOf(quarter);
		String result = new SimpleDateFormat(PortalCST.FORMAT_QUARTER_TIME)
				.format(date);
		if (strQuarter.length() == 1) {
			strQuarter = "0" + strQuarter;
		}
		return result + strQuarter;
	}

	/**
	 * 计算开始和结束时间之间相差多少天，结束时间一定要大于开始时间
	 * <p>
	 * 如果结束时间小于等于开始时间则返回0
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int beforeDays(Date startTime, Date endTime) {

		if (endTime.compareTo(startTime) <= 0) {
			return 0;
		}

		long diff = endTime.getTime() - startTime.getTime();
		long day = diff / (1000 * 24 * 60 * 60);
		return (int) day;
	}

	/**
	 * 将日期格式化为数据库可接受的日期格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDBTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.999");
		String dd = format.format(date);
		return dd;
	}

	public static String formatDBTime(long date) {
		return formatDBTime(new Date(date));
	}

	/**
	 * 格式化当前时间格式yyyyMMddHH
	 * 
	 * @return
	 */
	public static String formatDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
		String dd = format.format(new Date());
		return dd;
	}

	/**
	 * 格式化当前时间格式MM/dd
	 * 
	 * @return
	 */
	public static String formatDateChar(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd");
		String dd = format.format(date);
		return dd;
	}

	public static String formatDateChar2(Date date) {
		return new SimpleDateFormat("MM-dd HH:mm").format(date);
	}

	/**
	 * 输出的日期格式yyyy-MM-dd HH:m
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
	}

	public static String formatDate(long date) {
		return formatDate(new Date(date));
	}

	/**
	 * 输出的日期格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate2(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static String formatDate2(long date) {
		return formatDate(new Date(date));
	}

	/**
	 * 默认最后一次刷新时间
	 */
	public static Date getLastRefreshDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(1979, 8, 30, 12, 00);
		return cal.getTime();
	}

	/**
	 * 获得SQL的时间
	 * 
	 * @param date
	 * @return
	 */
	public static java.sql.Timestamp getSqlTime(Date date) {
		return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * 在指定的时间上增加指定的天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

	public static long[] calScale4Push(int type, int pcount) {

		long[] result = new long[2];
		switch (type) {
		case 2:
			result[0] = formatTodayFirst();
			result[1] = result[0] + ONE_DAY * pcount;
			break;
		case 3:
			result[0] = new TimeHelper().getMondayOFWeek();
			result[1] = result[0] + ONE_DAY * 7 * pcount;
			break;
		case 4:
			result[0] = new TimeHelper().getFirstDayOfMonth();
			result[1] = result[0] + ONE_MONTH * pcount;
			break;
		}

		return result;
	}
}
