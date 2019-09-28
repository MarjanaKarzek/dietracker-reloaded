package de.karzek.diettracker.presentation.util;

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
public class StringUtils {
    public static String formatFloat(float value) {
        if (value % 1 == 0) {
            return "" + (int) value;
        } else
            return String.format("%.1f", value);
    }

    public static String formatInt(int value) {
        return "" + value;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.equals("");
    }
}
