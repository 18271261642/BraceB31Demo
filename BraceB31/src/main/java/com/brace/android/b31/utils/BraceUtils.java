package com.brace.android.b31.utils;

import android.content.Context;

import com.brace.android.b31.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin
 * Date 2019/11/6
 */
public class BraceUtils {

    // 字符串的非空
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 获取当前时间，格式为 :yyyy-MM-dd
     */
    public static String getCurrentDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dateFormat.format(now);
    }


    /**
     * 获取当前时间，格式为 :yyyy-MM-dd
     */
    public static String getCurrentDate(String format) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(now);
    }


    /**
     * 比较两个日期的大小 yyyy-MM-dd HH:MM:SS格式
     *
     * @param currDay 当前日期
     * @param comDay  需要比较的日期
     * @return
     */
    public static boolean comPariDateDetail(String currDay, String comDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            //转换成long类型
            long currLongDay = sdf.parse(currDay).getTime();
            //需要比较的时间
            long comLongDay = sdf.parse(comDay).getTime();

            return comLongDay > currLongDay;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 日期相差多少，string类型
     *
     * @param startTime
     * @param endTime
     * @return 毫秒
     */
    public static int intervalTimeStr(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            long startLongTime = sdf.parse(startTime).getTime();
            long endLongTime = sdf.parse(endTime).getTime();

            //结束日期减开始日期
            long diffDay = endLongTime - startLongTime;
            return (int) (diffDay > 0 ? diffDay / 60000 : diffDay == 0 ? 0 : diffDay / 60000);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }


    /**
     * 根据当前日期获取左右一天的日期
     *
     * @param date 条件日期
     * @param left true_前一天 false_后一天
     * @return 计算后的日期
     */
    public static String obtainAroundDate(String date, boolean left) {
        if (date.equals(obtainFormatDate(0)) && !left) {
            return date;//如果传入的日期==今天,而且要求后一天数据,直接返回今天
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date dateTemp = null;
        try {
            dateTemp = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateTemp == null) return "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTemp);// 初始化日历
        calendar.add(Calendar.DATE, left ? -1 : 1);
        dateTemp = calendar.getTime();
        return sdf.format(dateTemp);
    }

    /**
     * 根据类型获取指定日期
     *
     * @param type 0_今天 1_昨天 2_前天
     * @return "yyyy-MM-dd"
     */
    public static String obtainFormatDate(int type) {
        Date date = new Date();// 当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);// 初始化日历
        calendar.add(Calendar.DATE, 0 - type);// 0,-1,-2
        date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(date);
    }



    static String[] timeStr = new String[]{"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00",
            "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00",
            "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00",
            "22:30", "23:00", "23:30"};


    public static Map<String, Object> setHalfDateMap() {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < timeStr.length; i++) {
            map.put(timeStr[i], 0 + "");
        }
        return map;

    }



    /**
     * 计算手环闹钟的重复时间
     *
     * @param repeat 000000
     * @return 周日..周一
     */
    public static String obtainAlarmDate(Context context, String repeat) {
        if (repeat == null || repeat.length() != 7 || repeat.equals("0000000")) {
            return context.getResources().getString(R.string.repeat_once);
        }
        String[] week = context.getResources().getStringArray(R.array.WeekItems);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (repeat.charAt(i) == '1') {
                builder.append(week[i]);
                builder.append(",");
            }
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }



    /**
     * 两个double相乘
     *
     * @param v1 param1
     * @param v2 param2
     * @return
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }

}
