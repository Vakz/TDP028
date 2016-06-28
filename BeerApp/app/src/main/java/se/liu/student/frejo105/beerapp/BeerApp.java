package se.liu.student.frejo105.beerapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by vakz on 2016-06-20.
 */
public class BeerApp extends Application {
    private static Context context;

    public static final String TESTED_COLOR = "#2196f3";
    public static final String UNTESTED_COLOR = "#ff4a4a";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
