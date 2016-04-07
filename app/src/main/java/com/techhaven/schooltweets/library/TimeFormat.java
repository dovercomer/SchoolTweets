package com.techhaven.schooltweets.library;

import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Oluwayomi on 2/23/2016.
 */
public class TimeFormat {
    public static String getFriendlyDayString(Date inputDate) {
        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"

        Date todayDate = new Date();

        String todayStr = DataContract.getDbDateString(todayDate);
        String dateStr = DataContract.getDbDateString(inputDate);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        cal.add(Calendar.MINUTE, -1);
        String MinFutureString = DataContract.getDbDateString(cal.getTime());
        cal.add(Calendar.MINUTE, 1);
        cal.add(Calendar.HOUR, -1);
        String HourFutureString = DataContract.getDbDateString(cal.getTime());
        cal.add(Calendar.HOUR, 1);
        cal.add(Calendar.DATE, -1);
        String DayFutureString = DataContract.getDbDateString(cal.getTime());
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -7);
        String WeekFutureString = DataContract.getDbDateString(cal.getTime());

        if (dateStr.compareTo(MinFutureString) > 0) {
            //SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return "now";//((todayDate.getTime()-inputDate.getTime())/1000)==1?"1 second ago":((todayDate.getTime()-inputDate.getTime())/1000)+" seconds ago";
        } else if (dateStr.compareTo(HourFutureString) > 0) {
            return ((todayDate.getTime() - inputDate.getTime()) / (1000 * 60)) == 1 ? "1 minute ago" : ((todayDate.getTime() - inputDate.getTime()) / (1000 * 60)) + " minutes ago";
        } else if (dateStr.compareTo(DayFutureString) > 0) {
            //Today, use the form "09:32"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("HH:mm");
            return shortenedDateFormat.format(inputDate);
        } else if (dateStr.compareTo(WeekFutureString) > 0) {
            // Within 7 Days, use the form "Mon 19:21"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE HH:mm");
            return shortenedDateFormat.format(inputDate);
        } else {

            // Otherwise, use the form "Mon Jun 3 2016"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
            return shortenedDateFormat.format(inputDate);
        }
    }
}
