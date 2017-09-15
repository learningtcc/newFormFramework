/*
package com.drore.util;


import com.drore.exception.CustomException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

*/
/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: guide_management <br/>
 * 创建日期:  2017/3/8 10:29  <br/>
 * 作者:    wdz
 *//*

public class TimeUtil {
    private static DecimalFormat df = new DecimalFormat("######0.00");
    public static String yyyyMMdd = "yyyy-MM-dd";
    public static String yyyyMM = "yyyy-MM";
    public static String HHmmss = "HH:mm:ss";
    public static String HHmm = "HH:mm";
    public static String yyyyMMddHHmmssR = "yyyyMMddHHmmss";
    public static String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";

    */
/**
     * 功能描述：      获取周几                  <br/>
     * 作    者：xwb <br/>
     * 创建时间：2017年3月11日  下午12:04:51 <br/>
     *//*

    public static String getWeek(int day) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        date.setTime(date.getTime() - 1000 * 60 * 60 * 24 * day);
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String[] data = {"日", "一", "二", "三", "四", "五", "六"};
        return "周" + data[week - 1];

    }

    */
/**
     * 查询几天前的字符串日期
     *//*

    public static String DateBefor(int day) {
        Date now = new Date();
        long dayBefor = now.getTime() - day * 24 * 60 * 60 * 1000L;
        return form(new Date(dayBefor), yyyyMMdd);
    }

    */
/**
     * 功能描述： 比较两个日期相差的天数 <br/>
     * 作 者：xwb <br/>
     * 创建时间：2016年12月8日 下午6:18:22 <br/>
     *//*

    public static int daysBetween(String date1, String date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date1, yyyyMMdd));
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar.get(Calendar.YEAR);
        calendar.setTime(strToDate(date2, yyyyMMdd));
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);
        int year2 = calendar.get(Calendar.YEAR);
        int days = Math.abs(day1 - day2);// 天数绝对值
        if (year1 != year2) { // 不同年
            int years = 0;
            int maxYear = year1 > year2 ? year1 : year2;
            int minYear = year1 < year2 ? year1 : year2;
            for (int i = minYear; i < maxYear; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { // 闰年
                    years += 366;
                } else { // 不是闰年
                    years += 365;
                }
            }
            return years + days;
        } else {// 同年
            return days;
        }
    }

    */
/**
     * 计算两个时间段时长
     *
     * @param startTime 09:00
     * @param endTime   13:00
     *//*

    public static String calculationTime(String startTime, String endTime) {
        String startH = startTime.substring(0, startTime.indexOf(':'));
        String startM = startTime.substring(startTime.indexOf(':') + 1);
        double startHour = Double.valueOf(startH)
                + Double.valueOf(startM) / 60;
        String endH = endTime.substring(0, endTime.indexOf(':'));
        String endM = endTime.substring(endTime.indexOf(':') + 1);
        double endHour = Double.valueOf(endH) + Double.valueOf(endM)
                / 60;
        double between = endHour - startHour;
        String leng = String.valueOf(df.format(between));
        if ("00".equals(leng.substring(leng.indexOf('.') + 1, leng.length()))) {
            leng = leng.substring(0, leng.indexOf('.'));
        }
        return leng;
    }

    */
/**
     * 计算两个时间时长
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     *//*

    public static double hoursBetween(String startDateTime, String endDateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(yyyyMMddHHmmss);
        LocalDateTime sDateTime = LocalDateTime.parse(startDateTime, dateFormat);
        LocalDateTime eDateTime = LocalDateTime.parse(endDateTime, dateFormat);
        Duration duration = Duration.between(sDateTime, eDateTime);
        long m = duration.toMinutes();
        double d = DoubleUtil.div(Double.valueOf(m), 60.0, 2);
        return d;
    }

    */
/**
     * 比较时间 精度分钟 相等返回0 startTime小于endTime返回-1  startTime小于endTime返回大于endTime返回1
     *
     * @param startTime 09:00
     * @param endTime   13:00
     *//*

    public static int compare(String startTime, String endTime) {
        int n = startTime.compareTo(endTime);
        if (n < 0) {
            return -1;
        } else if (n == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    */
/**
     * 比较时间 精度秒 相等返回0 startTime小于endTime返回-1  startTime小于endTime返回大于endTime返回1
     *
     * @param startTime 09:00:00
     * @param endTime   13:00:00
     *//*

    public static int compareTime(String startTime, String endTime) {
        int n = startTime.compareTo(endTime);
        if (n < 0) {
            return -1;
        } else if (n == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    */
/**
     * 判断两者时间是否重合 重合返回true  1、开始时间不在此范围内，2结束时间不在此范围内，3开始时间小于且结束时间大于
     *
     * @param startTime   占用的时间段 09:00
     * @param endTime     占用的时间段 13:00
     * @param middleSTime 比较的时间段 09:00
     * @param middleETime 比较的时间段 13:00
     *//*

    public static boolean compare(String startTime, String endTime, String middleSTime, String middleETime) {
        if (middleSTime.compareTo(endTime) == 0 || middleETime.compareTo(startTime) == 0) {//这个表示开始时间等于结束时间 ，或者结束时间等于开始时间
            return false;
        }

        if (startTime.compareTo(middleSTime) <= 0 && middleSTime.compareTo(endTime) < 0) {//需要时间在导游开始服务到结束服务之间，表示已经重复了
            return true;
        }

        if (startTime.compareTo(middleETime) < 0 && middleETime.compareTo(endTime) <= 0) {//需要时间在导游服务结束时间前后，表示已经重复了
            return true;
        }

        if (middleSTime.compareTo(startTime) < 0 && middleETime.compareTo(endTime) > 0) {//需要时间在导游开始服务时间前后，表示已经重复了
            return true;
        }

        return false;


    }


    */
/**
     * 功能描述：   获取n小时后的时间字符串      time:09:00     h:1.5 小时                    <br/>
     * 作    者：xwb <br/>
     * 创建时间：2017年3月8日  下午1:46:46 <br/>
     *
     * @param time 格式  hh:mm
     *//*

    public static String backTime(String time, double h) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                yyyyMMddHHmm);
        Date date = null;
        try {
            date = dateFormat.parse("2017-01-01 " + time);
        } catch (ParseException e) {
            throw new CustomException("时间格式转换错误!" + time);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        double minute = DoubleUtil.mul(h, new Double(60));
        int min = 0;
        if (minute != 0) {
            min = (int) minute;
        }
        calendar.add(Calendar.MINUTE, min);
        String result = dateFormat.format(calendar.getTime());
        return result.split(" ")[1];

    }

    */
/**
     * 获取n小时后的时间
     *//*

    public static String backTimeHour(Date date, int h) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, h);
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        String dateStr = sdf.format(calendar.getTimeInMillis());
        return dateStr;
    }

    */
/**
     * java当前时间加n天
     *//*

    public static String backTimeDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        String dateStr = sdf.format(calendar.getTimeInMillis());
        return dateStr;
    }

    */
/**
     * 指定时间加n天
     *//*

    public static String backTimeDayEx(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        String dateStr = sdf.format(calendar.getTimeInMillis());
        return dateStr;
    }


    */
/**
     * 时间转换
     *//*

    public static String form(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    */
/**
     * 字符串转换为date
     *//*

    public static Date strToDate(String dateTime, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = simpleDateFormat.parse(dateTime);
            return date;
        } catch (ParseException e) {
            throw new CustomException("时间格式转换错误!" + dateTime);
        }

    }

    */
/**
     * 字符串转换为字符串
     *//*

    public static String strToStr(String dateTime, String oldStr, String newStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldStr);
        SimpleDateFormat newSimpleDateFormat = new SimpleDateFormat(newStr);
        try {
            Date date = simpleDateFormat.parse(dateTime);
            return newSimpleDateFormat.format(date);
        } catch (ParseException e) {
            throw new CustomException("时间格式转换错误!" + dateTime);
        }

    }

    */
/**
     * date 转换为string
     *//*

    public static String DateToStr(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    */
/**
     * 判断两个日期是否是同一天
     *//*

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    */
/**
     * 获取当月的第一天
     *//*

    public static String firstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;
    }

    */
/**
     * 获取当年的第一天
     *//*

    public static String firstDayOfYear() {
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd);
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        String first = format.format(getYearFirst(currentYear));
        return first;
    }

    */
/**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     *//*

    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    */
/**
     * 遍历计算两个日期之间天数
     *//*

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
        return dateList;
    }

    */
/**
     * list转换成
     *//*

    public static List<String> listDateToStr(List<Date> list, String pattern) {
        List<String> reList = new ArrayList<>();
        for (Date n : list) {
            reList.add(DateToStr(n, pattern));
        }
        return reList;
    }

    */
/**
     * 获取上个月第一天 yyyy-MM-dd
     *//*

    public static String getLastMonthFirst() {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -1);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
        String lastMonth = dft.format(cal.getTime());
        return lastMonth + "-01";
    }

    public static String getMonthFirst() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
        String lastMonth = dft.format(cal.getTime());
        return lastMonth + "-01";
    }

    */
/**
     * 获取上个月最后一天
     *//*

    public static String getLastMonthEnd() {
        SimpleDateFormat dft = new SimpleDateFormat(yyyyMMdd);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date strDateTo = calendar.getTime();
        return dft.format(strDateTo);
    }

    */
/**
     * 获取指定月份最后一天
     *
     * @param monthStr yyyy-MM
     *//*

    public static String getMonthEnd(String monthStr) {
        SimpleDateFormat dft = new SimpleDateFormat(yyyyMM);
        try {
            Date date = dft.parse(monthStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), 1);
            calendar.roll(Calendar.DATE, -1);
            DateFormat format = new SimpleDateFormat(yyyyMMdd);
            String res = format.format(calendar.getTime());
            return res;
        } catch (ParseException e) {
            throw new CustomException("时间格式转换错误!" + monthStr);
        }
    }


    */
/**
     * 获取对应年份每一个月份
     *
     * @param startMoth yyyy-MM
     * @param endMonth  yyyy-MM
     *//*

    public static List<String> getYearMonth(String startMoth, String endMonth) {
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM").parse(startMoth);//定义起始日期
            Date d2 = new SimpleDateFormat("yyyy-MM").parse(endMonth);//定义结束日期
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(d1);//设置日期起始时间
            List<String> list = new ArrayList<>();
            if (startMoth.equals(endMonth)) {
                list.add(startMoth);
                return list;
            }
            while (dd.getTime().before(d2)) {//判断是否到结束日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String str = sdf.format(dd.getTime());
                list.add(str);
                dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
            }
            list.add(endMonth);
            return list;
        } catch (ParseException e) {
            throw new CustomException("时间格式转换错误!" + startMoth + "  " + endMonth);
        }

    }

    */
/**
     * 获取n年度
     *//*

    public static String getYear(int coun) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, coun);
        Date y = c.getTime();
        String year = format.format(y);
        return year;
    }


    */
/**
     * 获取两个时间差
     *//*

    public static Map<String, Integer> getDatePoorHour(Date startDate, Date endDate) {
        long diff = startDate.getTime() - endDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        Map<String, Integer> map = new HashMap<>();
        map.put("days", (int) diffDays);
        map.put("hours", (int) diffHours);
        map.put("minutes", (int) diffMinutes);
        map.put("seconds", (int) diffSeconds);
        return map;
    }

    */
/**
     * 获取两个时间之间的时差
     *//*

    public static Double getDatePoorHour2(Date startDate, Date endDate) {
        long diff = startDate.getTime() - endDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        int days = (int) diffDays;
        int hours = (int) diffHours;
        int minutes = (int) diffMinutes;
        Double total = DoubleUtil.add(new Double(days * 24), new Double(hours));
        Double mtotal = DoubleUtil.div(new Double(minutes), new Double(60), 2);
        return DoubleUtil.add(total, mtotal);

    }


    */
/**
     * 如果时间早于那个小时，那么返回true,否则返回false
     *//*

    public static boolean compareHour(Map<String, Integer> map, int hour) {
        int days = map.get("days");
        int hours = map.get("hours");
        int totalHour = days * 24 + hours;//总共多长时间
        if (totalHour < 0) {//表示已经过了对应的时间，就毫无比较一样了
            return false;
        }


        if (totalHour >= hour) {
            return true;
        } else {
            return false;
        }

    }

    */
/**
     * 如果时间大于那个分钟，那么返回true,否则返回false
     *//*

    public static boolean compareMinuteMax(Map<String, Integer> map, int minute) {
        int minutes = map.get("minutes");
        int hours = map.get("hours");
        int totalMinute = hours * 60 + minutes;//总共多长时间
        if (totalMinute < 0) {//表示已经过了对应的时间，就毫无比较一样了
            return false;
        }


        if (totalMinute >= minute) {
            return true;
        } else {
            return false;
        }

    }

    */
/**
     * 如果时间早于那个分钟，那么返回true,否则返回false
     *//*

    public static boolean compareMinuteMin(Map<String, Integer> map, int minute) {
        int minutes = map.get("minutes");
        int hours = map.get("hours");
        int totalMinute = hours * 60 + minutes;//总共多长时间
        if (totalMinute < 0) {//表示已经过了对应的时间，就毫无比较一样了
            return false;
        }


        if (totalMinute < minute) {
            return true;
        } else {
            return false;
        }

    }

    */
/**
     * 如果时间早于那个分钟，那么返回true,否则返回false
     *//*

    public static boolean compareMinute(Map<String, Integer> map, int minMinute, int maxMinute) {
        int minutes = map.get("minutes");
        int hours = map.get("hours");
        int totalMinute = hours * 60 + minutes;//总共多长时间
        if (totalMinute < 0) {//表示已经过了对应的时间，就毫无比较一样了
            return false;
        }


        if (totalMinute >= minMinute && totalMinute < maxMinute) {
            return true;
        } else {
            return false;
        }

    }


    */
/**
     * 时间加n分钟
     *
     * @param time yyyy-MM-dd
     * @
     *//*

    public static String addMniutes(String time, int minutes) {
        Date date = strToDate(time, yyyyMMddHHmmss);

        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(date);
        nowTime.add(Calendar.MINUTE, minutes);
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        String dateStr = sdf.format(nowTime.getTimeInMillis());
        return dateStr;
    }

    //计算两个日期相差年数
    public static int yearDateDiff(String startDate, String endDate) {
        Calendar calBegin = Calendar.getInstance(); //获取日历实例
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(strToDate(startDate, "yyyy")); //字符串按照指定格式转化为日期
        calEnd.setTime(strToDate(endDate, "yyyy"));
        return calEnd.get(Calendar.YEAR) - calBegin.get(Calendar.YEAR);
    }

    */
/**
     * 按照指定格式获取当前系统时间
     *//*

    public static Date getSysTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String StringDate = sdf.format(date);
        try {
            Date time = sdf.parse(StringDate);
            return time;
        } catch (ParseException ex) {
            return new Date();
        }
    }

    */
/**
     * date1是否大于date2指定的分钟数
     * 计算两个时间之间的时间戳是否大于指定的分钟
     * 大于等于返回true ,小于返回false
     *//*

    public static boolean determineSize(Date date1, Date date2, int monut) {
        //得到两个时间相减的时间戳
        long days = date1.getTime() - date2.getTime();
        System.out.println("==========================>>两个时间段之间的时间戳值：" + days);
        long miaoshu = monut * 60 * 1000;
        if (days >= miaoshu) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {

        //   System.out.printf(calculationTime("09:00","13:00"));
        // System.out.printf(compareTime("12:00:05","12:00:05")+"");
        //System.out.printf(compare("12:00", "13:00", "10:00","14:00") + "");
        //System.out.printf(form(new Date(),HHmm));
        double ho = hoursBetween("2017-03-13 12:29:52", "2017-03-12 18:11:52");
        System.out.println((int) ho + 1);
        String str = "20170523114321";
        System.out.println("TimeUtil.main=="+strToStr(str,yyyyMMddHHmmssR,yyyyMMddHHmmss));
        // System.out.printf(firstDayOfMonth(yyyyMMdd));

        //List<Date> list = dateSplit(strToDate("2017-02-26",yyyyMMdd),strToDate("2017-03-10",yyyyMMdd));
        //  List<String>  tr = listDateToStr(list,yyyyMMdd);
//        System.out.printf(firstDayOfYear());
//        System.out.printf(String.valueOf(getYearMonth("2017-01","2017-12")));
//        System.out.println(daysBetween("2014-10-11", "2014-10-12"));

//        Date start = TimeUtil.strToDate("2017-03-11 12:29:52", TimeUtil.yyyyMMddHHmmss);
//        Date end = TimeUtil.strToDate("2017-03-11 14:39:51", TimeUtil.yyyyMMddHHmmss);
//        System.out.printf(addMniutes("2017-03-11 12:29:52", 10));
        // System.out.printf(getMonthFirst());
//        Map<String, Integer> map = getDatePoorHour(end, start);
//
//        System.out.printf(getDatePoorHour2(end, start)+"");

//        System.out.println(backTimeHour(new Date(), 24));
    }


}
*/
