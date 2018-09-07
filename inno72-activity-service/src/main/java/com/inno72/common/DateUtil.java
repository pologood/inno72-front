package com.inno72.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String PATTERN_YYYY_MM_DDHH_MM_SS ="yyyy-MM-dd HH:mm:ss";
    public static Date parse(String date,String pattern){
        DateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String format(Date date,String pattern){
        DateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
