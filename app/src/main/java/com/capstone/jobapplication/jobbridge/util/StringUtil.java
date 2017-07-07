package com.capstone.jobapplication.jobbridge.util;

import org.joda.time.DateTime;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Aicun on 6/28/2017.
 */

public class StringUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatStringCurrency(String wage) {
        Locale locale = Locale.getDefault();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
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
}
