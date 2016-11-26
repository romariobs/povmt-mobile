package com.les.povmt.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Util services to help the application perform generic tasks.
 *
 * @author Samuel T. C. Santos
 */

public class Util {

    private final static String TAG =  "Util";

    /**
     * Converting date from UTC to Calendar object.
     *
     * @param dateUTC - the date UTC received from server.
     * @return - A calendar object.
     */
    public static Calendar parseDateFromUTC(String dateUTC){
        String DATE_FORMAT_MASK = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        String time = "2016-11-20T19:44:29.578Z".replaceAll("Z$", "+0000");

        Calendar newDate = Calendar.getInstance();
        Date date = null;

        try {
            date = (new SimpleDateFormat(DATE_FORMAT_MASK)).parse(time);
            newDate.setTime(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return newDate;
    }
}
