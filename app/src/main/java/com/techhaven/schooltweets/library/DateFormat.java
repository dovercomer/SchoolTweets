package com.techhaven.schooltweets.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Oluwayomi on 3/16/2016.
 */
public class DateFormat {
    /**
     * Converts Date class to a string representation, used for easy comparison and database lookup.
     */
    public static String getDbDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DB_DATE_FORMAT);
        return sdf.format(date);
    }

    public static String getDisplayDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DISPLACE_DATE_FORMAT);
        return sdf.format(date);
    }


    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Constant.DB_DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateFromDisplay(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Constant.DISPLACE_DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    }
}
