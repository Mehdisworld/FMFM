package com.io.filemanager.freefileexplorer.easily;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private static final String LOG_TAG = "AppOpenManager";

    private AppOpenAd appOpenAd = null;

    private Activity currentActivity;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private final MyOneApplication myOneApplication;

    private static boolean isShowingAd = false;

    private long loadTime = 0;

    /** Constructor */
    public AppOpenManager(MyOneApplication myApplication) {
        this.myOneApplication = myApplication;
        this.myOneApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /** Creates and returns ad request. */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /** Utility method that checks if ad exists and can be shown. */
    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /** Request an ad */
    public void fetchAd() {
        // Have unused ad, no need to fetch another.
        return;

    }

    public void showAdIfAvailable() {
        return;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        //AppOpne Ads WebApp Qureka
        //showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

}
