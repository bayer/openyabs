/*
 * 
 * 
 */
package mpv5.utils.numberformat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import mpv5.logging.Log;

/**
 *
 *  This class provides useful number format methods
 */
public class FormatNumber {
    /**
     * Represents the default decimal format used in MP
     */
    public static String FORMAT_DECIMAL = "#,###,##0.00";
    /**
     * Check whether a text can be parsed to be a decimal number
     * @param text
     * @return If parsing would be successful
     */
    public  synchronized static boolean checkDezimal(String text) {
        if (parseDezimal(text) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * The default number format
     * @return
     */
    public  synchronized static NumberFormat getDefaultDecimalFormat() {
        return new DecimalFormat(FORMAT_DECIMAL);
    }

    /**
     * Formats a number to look like the users default locale decimal (+ rounding)
     * @param number
     * @return
     */
    public synchronized static String formatDezimal(Double number) {
        java.text.DecimalFormat n = (DecimalFormat) getDefaultDecimalFormat();
        n.setMaximumFractionDigits(2);
        return n.format(round(number));
    }

    /**
     * Round s a number up to two fraction digits {@link BigDecimal.ROUND_HALF_UP}
     * @param number
     * @return
     */
    public  synchronized static Double round(double number) {
        BigDecimal b = BigDecimal.valueOf(number);
        b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        return b.doubleValue();
    }

    /**
     * Tries to parse the given text to a number, using all available Locales
     * @param number
     * @return A double number or null if no matching number instance can be found
     */
    public  synchronized static Double parseDezimal(String number) {
        java.text.DecimalFormat n = (DecimalFormat) getDefaultDecimalFormat();
        n.setMaximumFractionDigits(2);
        Locale[] Locales;
        try {
            return n.parse(number).doubleValue();
        } catch (ParseException ex) {
            Log.Debug(FormatNumber.class, ex.getMessage());
            Locales = Locale.getAvailableLocales();
            for (int i = 0; i < Locales.length; i++) {
                Locale locale = Locales[i];
                try {
                    Number result = NumberFormat.getNumberInstance(locale).parse(number);
                    return result.doubleValue();
                } catch (ParseException parseException) {
                }
            }
            return null;
        }
    }

    /**
     * Formats a number to look like the users default locale decimal (+ rounding)
     * @param number
     * @return
     */
    public  synchronized static String formatDezimal(Float number) {
        java.text.DecimalFormat n = (DecimalFormat) getDefaultDecimalFormat();
        n.setMaximumFractionDigits(2);
        return n.format(round(Double.valueOf(number)));
    }

    /**
     * Formats a number to look like the users default locale currency (+ rounding)
     * @param betrag
     * @return
     */
    public  synchronized static String formatLokalCurrency(Double betrag) {
        NumberFormat n = NumberFormat.getCurrencyInstance();
        return n.format(round(betrag));
    }

    /**
     * Formats a number to look like the users default locale percent values (+ rounding)
     * @param number
     * @return
     */
    public  synchronized static String formatPercent(double number) {
        return NumberFormat.getPercentInstance().format(number);
    }

    /**
     * Checks if an object is in anyway compatible to be a number or a decimal number
     * @param number
     * @return
     */
    public  synchronized static boolean checkNumber(Object number) {
       if (number instanceof Long || number instanceof Integer ||
                   number instanceof Short || number instanceof Byte ||
                   number instanceof AtomicInteger ||
                   number instanceof AtomicLong ||
                   (number instanceof BigInteger &&
                    ((BigInteger)number).bitLength () < 64)) {
            return true;
        } else if (number instanceof BigDecimal) {
            return true;
        } else if (number instanceof BigInteger) {
            return true;
        } else if (number instanceof Number) {
            return true;
        } else {
            return checkDezimal(number.toString());
        }
    }

    /**
     * Tries to parse the given Object to a Double value
     * @param number
     * @return
     */
    public  synchronized static Double parseNumber(Object number) {
         if (number!=null) {
            if (number instanceof Number) {
                return ((Number) number).doubleValue();
            } else {
                try {
                    return Double.valueOf(number.toString());
                } catch (NumberFormatException numberFormatException) {
                    return parseDezimal(number.toString());
                }
            }
        }else {
            return 0d;
        }
    }
}
