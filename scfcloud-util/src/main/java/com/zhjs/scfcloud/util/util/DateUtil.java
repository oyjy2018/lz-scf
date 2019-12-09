package com.zhjs.scfcloud.util.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-19 16:28
 *
 * @author liuchanghai
 * @create 2019-06-19 16:28
 * @since
 */

public class DateUtil {

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static String DATE_FORMAT_1 = "yyyy-MM-dd";
    private static String DATE_FORMAT_2 = "yyyyMMdd";

    private static String DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    private static String DATE_TIME_FORMAT_2 = "yyyyMMdd HHmmss";

    /**
     * 计算两个时间相差的天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getTimeDistance(Date beginDate , Date endDate ) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int)((endTime - beginTime) / (1000 * 60 * 60 *24));//先算出两时间的毫秒数之差大于一天的天数

        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);//使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);//再使endCalendar减去1天
        if(beginCalendar.get(Calendar.DAY_OF_MONTH)==endCalendar.get(Calendar.DAY_OF_MONTH))//比较两日期的DAY_OF_MONTH是否相等
            return betweenDays + 1;	//相等说明确实跨天了
        else
            return betweenDays + 0;	//不相等说明确实未跨天
    }

    /**
     * 计算两个日期相差的天数
     * 规则：开始日期取00:00:00 结束日期取23:59:59 即同一日期 相差天数为1天
     * @param beginDate 年月日格式
     * @param endDate
     * @return
     */
    public static int getDateDistance(Date beginDate, Date endDate ) {
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        int betweenDays = (int)((endTime - beginTime) / (1000 * 60 * 60 *24));//先算出两时间的毫秒数之差大于一天的天数
        return betweenDays + 1;
    }

    public static void main(String[] args) {
        System.out.println(JsonUtil.toJSON(getDateList("2019-10-28", "2019-11-9")));
    }

    /**
     * 计算时间差
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDatePoor(Date startDate, Date endDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;

        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;

        String result = "";
        if(day > 0){
            result += day + " 天";
        }
        if(hour > 0){
            result += hour + " 小时";
        }
        if(min > 0){
            result += min + " 分钟";
        }
        result += sec + " 秒";
        return result;
    }

    /**
     * yyyy-MM-dd格式化输出日期
     *
     * @param date   日期
     * @return 返回字符型日期
     */
    public static String format(Date date) {
        return format(date,DATE_FORMAT_1);
    }

    /**
     * 格式化输出日期
     *
     * @param date   日期
     * @param format 格式
     * @return 返回字符型日期
     */
    public static String format(Date date, String format) {
        try {
            if (date == null) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            logger.error("格式化输出日期",e);
            return null;
        }
    }

    /**
     * 生成指定日期
     * @param dateStr
     * @param format
     * @return
     */
    public static Date parse(String dateStr, String format){
        try{
            if (StringUtil.isEmpty(dateStr)) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        }catch (Exception e){
            logger.error("生成指定日期",e);
            return null;
        }
    }

    /**
     * 生成指定日期
     * @param dateStr
     * @param format
     * @return
     */
    public static Date parse(String dateStr){
        try{
            if (StringUtil.isEmpty(dateStr)) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        }catch (Exception e){
            logger.error("生成指定日期",e);
            return null;
        }
    }

    /**
     * 增减日期
     * @param date
     * @param type （1: 年；2：月；5：天；10：时；12：分；13：秒）
     * @param num
     * @return
     */
    public static Date add(Date date,int type, int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type,num);
        return calendar.getTime();
    }

    //获取今日日期字符串 yyyyMMdd格式
    public static String getTodayDateStr(String format) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String todayDateStr = sdf.format(d);
        //System.out.println("格式化后的日期：" + todayDateStr);
        return todayDateStr;
    }

    //获取某个日期的最后一秒的时间戳
    public static long getLastTimeForDate(Date date) {
        String dateStr = format(date,DATE_FORMAT_1);
        dateStr += " 23:59:59";
        return parse(dateStr,DATE_TIME_FORMAT_1).getTime();
    }

    //获取某个日期的最后一秒的时间
    public static Date getLastDateTimeForDate(Date date) {
        String dateStr = format(date,DATE_FORMAT_1);
        dateStr += " 23:59:59";
        return parse(dateStr,DATE_TIME_FORMAT_1);
    }

    /**
     * 获取两个日期之间的所有日期集合
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDateList(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 获取两个日期之间的所有日期集合（月）
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDateListByMonth(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.MONTH, +1);// 月份加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.MONTH, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 获取两个日期之间的所有日期（月）集合（日期为当月最后一天）
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDateListByMonthLastDay(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.MONTH, +1);// 月份加1(包含结束)
            while (tempStart.before(tempEnd)) {
                // 设定当前时间为每月一号
                tempStart.set(Calendar.DAY_OF_MONTH, 1);
                // 当前日历的天数上-1变成最大值 , 此方法不会改变指定字段之外的字段
                tempStart.roll(Calendar.DAY_OF_MONTH, -1);
                days.add(dateFormat1.format(tempStart.getTime()));
                tempStart.add(Calendar.MONTH, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }
}
