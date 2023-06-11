package com.io.filemanager.freefileexplorer.easily.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import com.io.filemanager.freefileexplorer.easily.R;


public class Method {
    public static boolean isDownload = true;
    public static boolean loginBack;
    public static boolean personalizationAd;
    public Activity activity;
    public SharedPreferences.Editor editor;
    private String filename;
    private String firstTime = "firstTime";
    public String loginType = "loginType";
    private final String myPreference = "login";

    public SharedPreferences pref;
    public String pref_login = "pref_login";
    public String profileId = "profileId";
    public String show_login = "show_login";
    public String themSetting = "them";
    public String userImage = "userImage";

    public Method(Activity activity2) {
        this.activity = activity2;
        SharedPreferences sharedPreferences = activity2.getSharedPreferences("login", 0);
        this.pref = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void login() {
        if (!this.pref.getBoolean(this.firstTime, false)) {
            this.editor.putBoolean(this.pref_login, false);
            this.editor.putBoolean(this.firstTime, true);
            this.editor.commit();
        }
    }

    public boolean isLogin() {
        return this.pref.getBoolean(this.pref_login, false);
    }

    public String getLoginType() {
        return this.pref.getString(this.loginType, "");
    }

    public String userId() {
        return this.pref.getString(this.profileId, (String) null);
    }

    public String getUserImage() {
        return this.pref.getString(this.userImage, "");
    }

    public String getDeviceId() {
        try {
            return Settings.Secure.getString(this.activity.getContentResolver(), "android_id");
        } catch (Exception unused) {
            return "NotFound";
        }
    }

    public void forceRTLIfSupported() {
        if (this.activity.getResources().getString(R.string.isRTL).equals("true")) {
            this.activity.getWindow().getDecorView().setLayoutDirection(1);
        }
    }

    public String themMode() {
        return this.pref.getString(this.themSetting, "system");
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.activity.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }

    public int getScreenWidth() {
        Display defaultDisplay = ((WindowManager) this.activity.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        point.x = defaultDisplay.getWidth();
        point.y = defaultDisplay.getHeight();
        return point.x;
    }

    public String webViewText() {
        if (isDarkMode()) {
            return Constant.webViewTextDark;
        }
        return Constant.webViewText;
    }

    public String webViewLink() {
        if (isDarkMode()) {
            return Constant.webViewLinkDark;
        }
        return Constant.webViewLink;
    }

    public String imageGalleryToolBar() {
        if (isDarkMode()) {
            return Constant.darkGallery;
        }
        return Constant.lightGallery;
    }

    public String imageGalleryProgressBar() {
        if (isDarkMode()) {
            return Constant.progressBarDarkGallery;
        }
        return Constant.progressBarLightGallery;
    }

    public boolean isDarkMode() {
        return (this.activity.getResources().getConfiguration().uiMode & 48) == 32;
    }
}
