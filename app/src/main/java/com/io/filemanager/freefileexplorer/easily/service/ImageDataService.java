package com.io.filemanager.freefileexplorer.easily.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.model.PhotoHeader;
import com.io.filemanager.freefileexplorer.easily.utils.NotificationUtils;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
//import com.safedk.android.analytics.events.RedirectEvent;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class ImageDataService extends Service {
    public static LinkedHashMap<String, ArrayList<PhotoData>> bucketimagesDataPhotoHashMap;
    public static boolean isComplete;
    public static List<Object> photoDataList = new ArrayList();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                startForeground(1, new NotificationCompat.Builder((Context) this, NotificationUtils.ANDROID_CHANNEL_ID).setContentTitle("").setPriority(1).setCategory(NotificationCompat.CATEGORY_SERVICE).setContentText("").build());
                stopForeground(STOP_FOREGROUND_REMOVE);
            }
        } catch (RuntimeException unused) {
        }
        photoDataList = new ArrayList();
        bucketimagesDataPhotoHashMap = new LinkedHashMap<>();
        isComplete = false;
        Observable.fromCallable(new Callable() {
            public final Object call() throws Exception {
                return ImageDataService.this.lambda$onStartCommand$0$ImageDataService();
            }
        }).subscribeOn(Schedulers.io()).doOnError(new Consumer() {
            public final void accept(Object obj) throws Exception {
                ImageDataService.this.lambda$onStartCommand$1$ImageDataService((Throwable) obj);
            }
        }).subscribe(new Consumer() {
            public final void accept(Object obj) throws Exception {
                ImageDataService.this.lambda$onStartCommand$2$ImageDataService((Boolean) obj);
            }
        });
        return super.onStartCommand(intent, i, i2);
    }

    public Boolean lambda$onStartCommand$0$ImageDataService() throws Exception {
        Log.e("ImageGet", "service photo getting start....");
        photoDataList.clear();
        bucketimagesDataPhotoHashMap.clear();
        photoDataList = getImagesList();
        return true;
    }

    public void lambda$onStartCommand$1$ImageDataService(Throwable th) throws Exception {
        isComplete = true;
        Intent intent = new Intent("LoardDataComplete");
        intent.putExtra("completed", true);
        sendBroadcast(intent);
    }

    public void lambda$onStartCommand$2$ImageDataService(Boolean bool) throws Exception {
        isComplete = true;
        Intent intent = new Intent("LoardDataComplete");
        intent.putExtra("completed", true);
        Log.e("ImageGet", "service photo set list....");
        sendBroadcast(intent);
    }

    public List<Object> getImagesList() {
        Uri uri;
        String str;
        ArrayList arrayList = new ArrayList();
        int i = Build.VERSION.SDK_INT;
        String str2 = "_data";
        String[] strArr = {str2, "date_modified", "_display_name", "_size"};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Images.Media.getContentUri("external");
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor query = getContentResolver().query(uri, strArr, (String) null, (String[]) null, "LOWER(date_modified) DESC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        if (query != null) {
            ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
            if (favouriteList == null) {
                favouriteList = new ArrayList<>();
            }
            query.moveToFirst();
            ArrayList arrayList2 = new ArrayList();
            query.moveToFirst();
            while (!query.isAfterLast()) {
                long j = query.getLong(query.getColumnIndex("_size"));
                if (j != 0) {
                    String string = query.getString(query.getColumnIndexOrThrow(str2));
                    String string2 = query.getString(query.getColumnIndex("_display_name"));
                    long j2 = query.getLong(query.getColumnIndex("date_modified")) * 1000;
                    String format = simpleDateFormat.format(Long.valueOf(j2));
                    str = str2;
                    PhotoData photoData = new PhotoData();
                    photoData.setFilePath(string);
                    photoData.setFileName(string2);
                    photoData.setSize(j);
                    photoData.setDateValue(j2);
                    if (favouriteList.contains(string)) {
                        photoData.setFavorite(true);
                    } else {
                        photoData.setFavorite(false);
                    }
                    arrayList2.add(photoData);
                    if (bucketimagesDataPhotoHashMap.containsKey(format)) {
                        ArrayList arrayList3 = bucketimagesDataPhotoHashMap.get(format);
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        }
                        arrayList3.add(photoData);
                        bucketimagesDataPhotoHashMap.put(format, arrayList3);
                    } else {
                        ArrayList arrayList4 = new ArrayList();
                        arrayList4.add(photoData);
                        bucketimagesDataPhotoHashMap.put(format, arrayList4);
                    }
                } else {
                    str = str2;
                }
                query.moveToNext();
                str2 = str;
            }
            query.close();
        }
        Set<String> keySet = bucketimagesDataPhotoHashMap.keySet();
        ArrayList arrayList5 = new ArrayList();
        arrayList5.addAll(keySet);
        arrayList.clear();
        for (int i2 = 0; i2 < arrayList5.size(); i2++) {
            ArrayList arrayList6 = bucketimagesDataPhotoHashMap.get(arrayList5.get(i2));
            if (!(arrayList6 == null || arrayList6.size() == 0)) {
                PhotoHeader photoHeader = new PhotoHeader();
                photoHeader.setTitle((String) arrayList5.get(i2));
                photoHeader.setPhotoList(arrayList6);
                arrayList.add(photoHeader);
                arrayList.addAll(arrayList6);
            }
        }
        return arrayList;
    }
}
