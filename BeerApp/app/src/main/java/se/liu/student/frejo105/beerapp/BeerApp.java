package se.liu.student.frejo105.beerapp;

import android.app.Application;
import android.content.Context;

public class BeerApp extends Application {
    private static Context context;

    public static final String TESTED_COLOR = "#2196f3";
    public static final String UNTESTED_COLOR = "#ff4a4a";
    public static final int DEFAULT_DISTANCE = 20;
    public static final boolean DEFAULT_UNIT_KM = true;
    public static final boolean DEFAULT_INCLUDE_TESTED = true;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
