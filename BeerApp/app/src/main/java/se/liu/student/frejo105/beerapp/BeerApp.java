package se.liu.student.frejo105.beerapp;

import android.app.Application;
import android.content.Context;

/**
 * This is used as a workaround to make it possible to retrieve local resources (strings, mostly)
 * without having to provide an application context in each case. The code is taken pretty much
 * verbatim from this SO post: http://stackoverflow.com/a/4391811
 */
public class BeerApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
