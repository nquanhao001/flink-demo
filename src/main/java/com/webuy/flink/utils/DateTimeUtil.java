package com.webuy.flink.utils;


import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {


    public static String yearMonthDayHourMinutesOfChinese = "yyyy年MM月dd日 HH:mm";

    /**
     * 根据传入的时间来计算指定时间的前或者后的时间，根据timeUnit单位来计算。三个都必填
     * @param time     指定的时间
     * @param value    前或者后的值
     * @param timeUnit 时间单位。
     * @return
     */
    public static Date getTimeAfter(Date time, int value, TimeUnit timeUnit) {
        DateTime dateTime = new DateTime(time);
        switch (timeUnit) {
            case MILLISECONDS:
                return dateTime.plusMillis(value).toDate();
            case SECONDS:
                return dateTime.plusSeconds(value).toDate();
            case MINUTES:
                return dateTime.plusMinutes(value).toDate();
            case HOURS:
                return dateTime.plusHours(value).toDate();
            case DAYS:
                return dateTime.plusDays(value).toDate();
            default:
                throw new UnsupportedOperationException("timeUnit 只支持秒，分，小时，天");
        }
    }



    /**
     * 将DATE类型装换成字符串格式
     * @param date
     * @param type 要转换成的字符串格式，如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateToString(Date date, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(date);
    }

    public static Date strToDate(String time){
        try {
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fmt.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将DATE类型装换成字符串格式
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取上月第一天,LocalDateTime实现
     * @return
     */
    public static Date getLastMonthOneDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonthOneDay = LocalDateTime.of(now.getYear(),now.getMonthValue() - 1, 1, 0, 0);
        long timeMillis = lastMonthOneDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return new Date(timeMillis);
    }

    /**
     * 获取时间区间 字符串格式
     * @param start 开始时间
     * @param end  结束时间
     * @param connector 连接符号
     * @return 2019-19-11 11:11:11 - 2019-19-12 11:11:11                 
      */    
    public static String getDateSectionString(Date start, Date end, String connector) {
        return getDateSectionString(start,end,connector,"yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 获取时间区间 字符串格式
     * @param start 开始时间
     * @param end  结束时间
     * @param connector 连接符号
     * @param pattern 时间格式
     * @return 2019-19-11 11:11:11 - 2019-19-12 11:11:11
     */
    public static String getDateSectionString(Date start, Date end, String connector,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(start) + connector + sdf.format(end);
    }

    /**
     * 判断时间1 是否在 时间2 多少分钟之后
     * @param t1 时间1
     * @param t2 时间2
     * @param minutes 间隔时间
     * @return true or false
     */
    public static boolean judgeTime1IsAfterTime2(LocalDateTime t1,LocalDateTime t2,long minutes){
       return t1.isAfter(t2.plusMinutes(minutes));
    }
    /**
     * 判断时间1 是否在 时间2 多少分钟之后
     * @param d1 时间1
     * @param d2 时间2
     * @param minutes 间隔时间
     * @return true or false
     */
    public static boolean judgeTime1IsAfterTime2(Date d1,Date d2,long minutes){
        return judgeTime1IsAfterTime2(
                d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                minutes
        );
    }
    /**
     * 判断时间1 是否在 时间2 多少分钟之后
     * @param t1 时间1
     * @param d2 时间2
     * @param minutes 间隔时间
     * @return true or false
     */
    public static boolean judgeTime1IsAfterTime2(LocalDateTime t1,Date d2,long minutes){
        return judgeTime1IsAfterTime2(t1,d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),minutes);
    }

    /**
     * 获得当天0点时间
     * @return
     */
    public static Date getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }



    /**
     * 判断两个时间 是否是 同一年里的同一个月
     * @param d1  时间1
     * @param d2    时间2
     * @return false:不是同一个月份 true:是同一个月份
     */
    public static boolean judgeIsSameMonth(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    /**
     * 判断两个时间 是否是同一年里的同一天
     * @param d1 时间1
     * @param d2 时间2
     * @return false:不是同一天 true：是同一天
     */
    public static boolean judgeIsSameDay(Date d1,Date d2){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    //获取明天的开始时间
    public static Date getTimesMorningOfTomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesMorning());
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }


    public static Date getTimeByHour(Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getTomorrowTimeByHour(Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

}
