package com.capstone.jobapplication.jobbridge.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Aicun on 6/28/2017.
 */

public class StringUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Locale locale = Locale.getDefault();
    private static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    public static String formatStringCurrency(String wage) {
        String formatted = null;
        try {
            double w = Double.parseDouble(wage);
            formatted = currencyFormatter.format(w);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return formatted;
    }

    public static String formatWage(String wage) {
        if(wage == null) wage = "0";
        String formattedWage = formatStringCurrency(wage);
        return String.format("%1s/h", formattedWage);
    }

    public static Date formateDateTime(String dateTimeString) {
        Date dt = null;
        try {
            dt = sdf.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String formatLocation(String city, String province) {
        return String.format("%s, %s", city, province);
    }

    public static String formatAddress(String street, String city, String province, String country, String postalCode) {
        return String.format("%s, %s, %s, %s, %s", street, city, province, country, postalCode);
    }

    public static boolean isNightShift(String startTime) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date start = parser.parse(startTime);
            Date nightStart = parser.parse("18:00");
            Date nightEnd = parser.parse("08:00");

            if (start.after(nightStart) || start.before(nightEnd)) {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }
}
