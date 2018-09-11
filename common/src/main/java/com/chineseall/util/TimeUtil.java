package com.chineseall.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 15:11.
 */
public class TimeUtil {
    public static String getTodayToString() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static long getCurrentTimeStamp(){
        Date date = new Date();
        return date.getTime();
    }

    public static void main(String[] args) {
        System.out.println(getTodayToString());
        System.out.println(getCurrentTimeStamp());
    }
}
