package com.io.filemanager.freefileexplorer.easily;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.io.filemanager.freefileexplorer.easily.utils.NotificationUtils;

import java.util.Locale;

import androidx.multidex.MultiDex;

public class MyOneApplication extends Application {


    private static AppOpenManager appOpenManager;

    public static SharedPreferences sharedPreferencesInApp;
    public static SharedPreferences.Editor editorInApp;

    @Override
    public void onCreate() {
        super.onCreate();



        //appOpenManager = new AppOpenManager(this);

        sharedPreferencesInApp = getSharedPreferences("my", MODE_PRIVATE);
        editorInApp = sharedPreferencesInApp.edit();


        Locale.getDefault().getDisplayLanguage();
        if (Build.VERSION.SDK_INT >= 26) {
            new NotificationUtils(this);
        }

    }

    public static void setuser_balance(Integer user_balance) {
        editorInApp.putInt("user_balance", user_balance).commit();
    }
    public static Integer getuser_balance() {
        return sharedPreferencesInApp.getInt("user_balance", 0);
    }

    public static void setuser_onetime(Integer user_onetime) {
        editorInApp.putInt("user_onetime", user_onetime).commit();
    }
    public static Integer getuser_onetime() {
        return sharedPreferencesInApp.getInt("user_onetime", 0);
    }

    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
