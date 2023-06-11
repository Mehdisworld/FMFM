package com.io.filemanager.freefileexplorer.easily.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

public class NotificationUtils extends ContextWrapper {

    public static final String ANDROID_CHANNEL_ID = "com.aperlo.file.manager.explorer.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "Load_data_MY_FILE_Channel";
    private NotificationManager mManager;

    public NotificationUtils(Context context) {
        super(context);
        createChannels();
    }

    public void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(ANDROID_CHANNEL_ID, ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.setLockscreenVisibility(1);
        getManager().createNotificationChannel(notificationChannel);
    }

    private NotificationManager getManager() {
        if (this.mManager == null) {
            this.mManager = (NotificationManager) getSystemService("notification");
        }
        return this.mManager;
    }

}
