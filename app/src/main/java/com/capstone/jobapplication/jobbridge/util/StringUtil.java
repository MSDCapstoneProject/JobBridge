package com.capstone.jobapplication.jobbridge.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Aicun on 6/28/2017.
 */

public class StringUtil {

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
        return String.format("%1s/h",formattedWage);
    }

}
