package com.serosoft.academiassu.Helpers;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class AcademiaApp extends Application {

    public static boolean IS_UPDATE = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
    }
}
