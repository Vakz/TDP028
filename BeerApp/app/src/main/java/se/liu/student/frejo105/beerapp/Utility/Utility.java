package se.liu.student.frejo105.beerapp.Utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by vakz on 2015-12-29.
 */
public class Utility {
    public static boolean isNullEmptyOrWhitespace(String str) {
        return str == null ||  str.trim().isEmpty();
    }

    public static boolean inLandscapeMode(Context context) {
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        boolean widthLarger = dm.widthPixels > dm.heightPixels;
        return widthLarger;
    }
}
