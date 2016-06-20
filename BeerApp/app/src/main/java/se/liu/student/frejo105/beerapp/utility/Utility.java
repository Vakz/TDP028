package se.liu.student.frejo105.beerapp.utility;

/**
 * Created by vakz on 2015-12-29.
 */
public class Utility {
    public static boolean isNullEmptyOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Formats a distance to a convenient format. If the distance is longer than 1000,
     * the returned distance will be in kilometers.
     * @param distance The distance to format
     * @return A string, rounded and in a proper unit
     */
    public static String formatDistanceString(double distance) {
        String unit = " m";
        distance = Math.round(distance);
        if (distance > 1000) {
            distance = Math.round(distance / 100) / 10;
            unit = " km";
        }
        return String.valueOf(distance) + unit;
    }
}
