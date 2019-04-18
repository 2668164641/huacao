package com.qttx.toolslibrary.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author huang
 * @date 2017/10/20
 * 时间工具类
 */

public class DateUtils {

    public static Date date = null;
    public static DateFormat dateFormat = null;
    public static Calendar calendar = null;

    /**
     * 功能描述：格式化日期
     * @param dateStr
     *            String 字符型日期
     * @param format
     *            String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        try {
            dateFormat = new SimpleDateFormat(format);
            String dt = dateStr.replaceAll("-", "/");
            if ((!dt.equals("")) && (dt.length() < format.length())) {
                dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
                        "0");
            }
            date = (Date) dateFormat.parse(dt);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr
     *            String 字符型日期：YYYY-MM-DD 格式
     * @return Date
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy/MM/dd");
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date
     *            Date 日期
     * @param format
     *            String 格式
     * @return 返回字符型日期
     */
    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 功能描述：
     *
     * @param date
     *            Date 日期
     * @return
     */
    public static String format(Date date) {
        return format(date, "yyyy/MM/dd");
    }

    /**
     * 功能描述：返回年份
     *
     * @param date
     *            Date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 功能描述：返回月份
     *
     * @param date
     *            Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日份
     *
     * @param date
     *            Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能描述：返回小时
     *
     * @param date
     *            日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分钟
     *
     * @param date
     *            日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date
     *            Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 功能描述：返回毫秒
     *
     * @param date
     *            日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 功能描述：返回字符型日期
     *
     * @param date
     *            日期
     * @return 返回字符型日期 yyyy/MM/dd 格式
     */
    public static String getDate(Date date) {
        return format(date, "yyyy/MM/dd");
    }

    /**
     * 功能描述：返回字符型时间
     *
     * @param date
     *            Date 日期
     * @return 返回字符型时间 HH:mm:ss 格式
     */
    public static String getTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date
     *            Date 日期
     * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
     */
    public static String getDateTime(Date date) {
        return format(date, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * 功能描述：日期相加
     *
     * @param date
     *            Date 日期
     * @param day
     *            int 天数
     * @return 返回相加后的日期
     */
    public static Date addDate(Date date, int day) {
        calendar = Calendar.getInstance();
        long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    /**
     * 功能描述：日期相减
     *
     * @param date
     *            Date 日期
     * @param date1
     *            Date 日期
     * @return 返回相减后的日期
     */
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 功能描述：取得指定月份的第一天
     *
     * @param strdate
     *            String 字符型日期
     * @return String yyyy-MM-dd 格式
     */
    public static String getMonthBegin(String strdate) {
        date = parseDate(strdate);
        return format(date, "yyyy-MM") + "-01";
    }
    public static String getStringDateFormat(long date) {
        Date currentTime = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static String getStringDateFormat(long date,String formart) {
        Date currentTime = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat(formart);
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 功能描述：取得指定月份的最后一天
     *
     * @param strdate
     *            String 字符型日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String getMonthEnd(String strdate) {
        date = parseDate(getMonthBegin(strdate));
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return formatDate(calendar.getTime());
    }

    /**
     * 功能描述：常用的格式化日期
     *
     * @param date
     *            Date 日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, "yyyy-MM-dd");
    }

    /**
     * 将短时间格式字符串yyyy-MM-dd 转换为long
     *
     * @param strDate
     * @return
     */
    public static Long shortStrToLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }

    /**
     * 功能描述：以指定的格式来格式化日期
     *
     * @param date
     *            Date 日期
     * @param format
     *            String 格式
     * @return String 日期字符串
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取某天是星期几
     * @param date
     * @return
     */
    public static String getMonthDayWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);    //获取年
        int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        int week = c.get(Calendar.DAY_OF_WEEK);

        String weekStr = null;

        switch (week) {

            case Calendar.SUNDAY:
                weekStr = "周日";
                break;

            case Calendar.MONDAY:
                weekStr = "周一";
                break;

            case Calendar.TUESDAY:
                weekStr = "周二";
                break;

            case Calendar.WEDNESDAY:
                weekStr = "周三";
                break;

            case Calendar.THURSDAY:
                weekStr = "周四";
                break;

            case Calendar.FRIDAY:
                weekStr = "周五";
                break;

            case Calendar.SATURDAY:
                weekStr = "周六";
                break;
            default:
        }

        return month + "月" + day + "日"  + "(" + weekStr + ")";
    }

    /**
     * 日期字符串转换为日期
     *
     * @param date 日期字符串
     * @param pattern 格式
     * @return 日期
     */
    public static Date formatStringByFormat(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
             LogUtils.printException(e);
            return null;
        }
    }

    /**
     * 获得口头时间字符串，如今天，昨天等
     *
     * @param d 时间格式为yyyy-MM-dd HH:mm:ss
     * @return 口头时间字符串
     */
    public static String getTimeInterval(String d) {
        Date date = formatStringByFormat(d, "yyyy-MM-dd HH:mm:ss");
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH);
        int nowWeek = now.get(Calendar.WEEK_OF_MONTH);
        int nowDay = now.get(Calendar.DAY_OF_WEEK);
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMinute = now.get(Calendar.MINUTE);

        Calendar ca = Calendar.getInstance();
        if(date!=null)
        {
            ca.setTime(date);
        }
        else
        {
            ca.setTime(new Date());
        }
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int week = ca.get(Calendar.WEEK_OF_MONTH);
        int day = ca.get(Calendar.DAY_OF_WEEK);
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        int minute = ca.get(Calendar.MINUTE);
        if (year != nowYear) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //不同年份
            return sdf.format(date);
        } else {
            if (month != nowMonth) {
                //不同月份
                SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
                return sdf.format(date);
            } else {
                if (week != nowWeek) {
                    //不同周
                    SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
                    return sdf.format(date);
                } else if (day != nowDay) {
                    if (day + 1 == nowDay) {
                        return "昨天" + formatDateByFormat(date, "HH:mm");
                    }
                    if (day + 2 == nowDay) {
                        return "前天" + formatDateByFormat(date, "HH:mm");
                    }
                    //不同天
                    SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
                    return sdf.format(date);
                } else {
                    //同一天
                    int hourGap = nowHour - hour;
                    if (hourGap == 0)//1小时内
                    {
                        if (nowMinute - minute < 1) {
                            return "刚刚";
                        } else {
                            return (nowMinute - minute) + "分钟前";
                        }
                    } else if (hourGap >= 1 && hourGap <= 12) {
                        return hourGap + "小时前";
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                        return sdf.format(date);
                    }
                }
            }
        }
    }

    /**
     * 日期字符串转换为日期
     *
     * @param date 日期字符串
     * @param pattern 格式
     * @return 日期
     */
    public static String reformatTime(String date, String pattern) {
        String fmt = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simple = new SimpleDateFormat(pattern);
        Date old = parseToDate(date, fmt);
        return simple.format(old);
    }

    /**
     * 字符串转换成日期.
     * @param dateString 日期字符
     * @param pattern 格式化.
     * @return
     */
    public static Date parseToDate(String dateString,String pattern){

        if(pattern==null || "".equals(pattern)){
            pattern="yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {

        }
        return new Date();
    }

    public static String getTimeInterval(Date d){
        String date = format(d, "yyyy-MM-dd HH:mm:ss");
        return getTimeInterval(date);
    }

    /**
     * 获取生肖
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 生肖
     */
    public static String getChineseZodiac(final String time) {
        return getChineseZodiac(string2Date(time, DEFAULT_FORMAT));
    }

    /**
     * 获取生肖
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 生肖
     */
    public static String getChineseZodiac(final String time, final DateFormat format) {
        return getChineseZodiac(string2Date(time, format));
    }

    /**
     * 获取生肖
     *
     * @param date Date类型时间
     * @return 生肖
     */
    public static String getChineseZodiac(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
    }

    /**
     * 获取生肖
     *
     * @param millis 毫秒时间戳
     * @return 生肖
     */
    public static String getChineseZodiac(final long millis) {
        return getChineseZodiac(millis2Date(millis));
    }

    /**
     * 获取生肖
     *
     * @param year 年
     * @return 生肖
     */
    public static String getChineseZodiac(final int year) {
        return CHINESE_ZODIAC[year % 12];
    }
    private static final String[] CHINESE_ZODIAC = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    private static final String[] ZODIAC = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    private static final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    /**
     * 获取星座
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 生肖
     */
    public static String getZodiac(final String time) {
        return getZodiac(string2Date(time, DEFAULT_FORMAT));
    }

    /**
     * 获取星座
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 生肖
     */
    public static String getZodiac(final String time, final DateFormat format) {
        return getZodiac(string2Date(time, format));
    }

    /**
     * 获取星座
     *
     * @param date Date类型时间
     * @return 星座
     */
    public static String getZodiac(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getZodiac(month, day);
    }

    /**
     * 获取星座
     *
     * @param millis 毫秒时间戳
     * @return 星座
     */
    public static String getZodiac(final long millis) {
        return getZodiac(millis2Date(millis));
    }

    /**
     * 获取星座
     *
     * @param month 月
     * @param day   日
     * @return 星座
     */
    public static String getZodiac(final int month, final int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month - 1]
                ? month - 1
                : (month + 10) % 12];
    }
    /**
     * 将时间戳转为Date类型
     *
     * @param millis 毫秒时间戳
     * @return Date类型时间
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }
    /**
     * 将时间字符串转为Date类型
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return Date类型
     */
    public static Date string2Date(final String time, final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
             LogUtils.printException(e);
        }
        return null;
    }
    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取年龄
     * @param date
     * @return
     */
    public static int getAge(String date) {
        if (date.contains("_"))
        {
            date= date.replaceAll("-", "/");
        }
        Date dateOfBirth = parseDate(date);
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException(
                        "Can't be born in the future");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
        }
        return age;
    }
    /**
     * 毫秒转化
     */
    public static String formatmillTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day : "" + day; // 天
        String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
        String strSecond = second < 10 ? "0" + second : "" + second;// 秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;// 毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;

        return strMinute + ":" + strSecond;
    }
}
