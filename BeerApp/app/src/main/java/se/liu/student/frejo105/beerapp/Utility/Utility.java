package se.liu.student.frejo105.beerapp.Utility;

/**
 * Created by vakz on 2015-12-29.
 */
public class Utility {
    public static boolean isNullEmptyOrWhitespace(String str) {
        return str == null ||  str.trim().isEmpty();
    }
}
