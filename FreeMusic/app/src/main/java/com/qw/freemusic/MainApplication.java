package com.qw.freemusic;

import android.app.Application;
import android.content.Context;

/**
 * created by QY
 * description:
 */
public class MainApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
