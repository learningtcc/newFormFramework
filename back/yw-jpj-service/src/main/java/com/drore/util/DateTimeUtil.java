package com.drore.util;

import com.drore.exception.CustomException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:日期 工具类<br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月30日 下午7:53:57 <br/>
 * 作者: wdz
 */
public class DateTimeUtil {
	public static String yyyyMMdd = "yyyy-MM-dd";

	public static final String NORMALFORMAT = "yyyy-MM-dd";
	public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 获取当前时间之后的多少分钟时间
	 * @param minute 分钟
	 * @return
	 */
	public static Date nowTimeAfterMinute(int minute) {
		long curren = System.currentTimeMillis();
		curren += minute * 60 * 1000;
		Date da = new Date(curren);
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//		"yyyy-MM-dd HH:mm:ss");
//		System.out.println(dateFormat.format(da));
		return da;
	}
	/**
	 * date形式日期转String类型
	 * @param d
	 * @return
	 */
	public static String date2String(Date d)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(NORMALFORMAT);
		String dStr = sdf.format(d);
		return dStr;
	}

	/**
	 * 校验字符串格式
	 * @param time
	 * @param format
     */
	public static void checkStrFormat(String time,String format)
	{
		try {
			if(format.length()!=time.length()){
				throw new CustomException("时间格式错误!"+time+"应满足"+format+"格式!");
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date =sdf.parse(time);
			System.out.println("DateTimeUtil.checkStrFormat"+date);
		} catch (ParseException e) {
			throw new CustomException("时间格式错误!"+time+"应满足"+format+"格式!");
		}
	}



	/**
	 * 查询几天前的字符串日期
	 * @param d
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static String DateBefore(String d, int day) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(NORMALFORMAT);
		Date formatDay = sdf.parse(d);
		long dayBefore = formatDay.getTime() - day*24*60*60*1000L;
		
		return date2String(new Date(dayBefore));
	}


	/**
	 * 遍历计算两个日期之间天数
	 */
	public static List<Date> dateSplit(Date startDate, Date endDate)
			throws Exception {
		if (!startDate.before(endDate))
			throw new Exception("开始时间应该在结束时间之后");
		Long spi = endDate.getTime() - startDate.getTime();
		Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

		List<Date> dateList = new ArrayList<Date>();
		dateList.add(endDate);
		for (int i = 1; i <= step; i++) {
			dateList.add(new Date(dateList.get(i - 1).getTime()
					- (24 * 60 * 60 * 1000)));// 比上一天减一
		}
		Collections.reverse(dateList);
		return  dateList;
	}

	/**
	 * date 转换为string
	 */
	public static String dateToStr(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	/**
	 * list转换成
	 */
	public static List<String> listDateToStr(List<Date> list, String pattern) {
		List<String> reList = new ArrayList<>();
		for (Date n : list) {
			reList.add(dateToStr(n, pattern));
		}
		return reList;
	}

	/**
	 * 字符串转换为date
	 */
	public static Date strToDate(String dateTime, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			Date date = simpleDateFormat.parse(dateTime);
			return date;
		} catch (ParseException e) {
			throw new CustomException("时间格式转换错误!" + dateTime);
		}

	}

	/**
	 * 功能描述： 比较两个日期相差的天数 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2016年12月8日 下午6:18:22 <br/>
	 */
	public static int daysBetween(String date1, String date2) {
		int result = 0;

		Calendar calst = Calendar.getInstance();;
		Calendar caled = Calendar.getInstance();

		calst.setTime(strToDate(date1,yyyyMMdd));
		caled.setTime(strToDate(date2,yyyyMMdd));

		//设置时间为0时
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);
		//得到两个日期相差的天数
		int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;

		return days;
	}

	public static void main(String[] args){
	  System.out.println(daysBetween("2017-09-06","2018-08-01"));
	}


}
