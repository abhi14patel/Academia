package com.serosoft.academiassu.Modules.TimeLineView.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HP-HP on 19-07-2016.
 */
public class DateTimeUtils {

    public static String parseDateTime(String dateString, String originalFormat, String outputFromat){

        SimpleDateFormat formatter = new SimpleDateFormat(originalFormat, Locale.US);
        Date date = null;
        try {
            date = formatter.parse(dateString);

            SimpleDateFormat dateFormat=new SimpleDateFormat(outputFromat, new Locale("US"));

            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
