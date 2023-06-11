package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.usage.StorageStatsManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.io.filemanager.freefileexplorer.easily.MyApp;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.adapter.RecentAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.ImageDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.InternalStorageFilesModel;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.service.ImageDataService;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
import com.io.filemanager.freefileexplorer.easily.utils.StorageUtils;
import com.io.filemanager.freefileexplorer.easily.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
/*import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.onesignal.OneSignal;
import com.safedk.android.analytics.events.RedirectEvent;
import com.safedk.android.utils.Logger;*/
//import com.willy.ratingbar.BaseRatingBar;
//import com.willy.ratingbar.RotationRatingBar;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.io.FileUtils;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {
    private static final String ONESIGNAL_APP_ID = "70d5c0aa-c9f3-4416-a7ec-e5c90ceb1fff";
    RecentAdapter adapter;
    @BindView(R.id.btn_internal_storage)
    LinearLayout btnInternalStorage;
    @BindView(R.id.btn_sd_card)
    LinearLayout btnSdCard;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.frame_layout_home)
    FrameLayout frameLayout;
    boolean isNativeADsShow = true;
    boolean isSdCard;
    @BindView(R.id.iv_more)
    AppCompatImageView ivMore;
    @BindView(R.id.lout_progress_bar)
    RelativeLayout loutProgressBar;
    

    boolean openScreen = false;
    float rating_count = 0.0f;
    ArrayList<InternalStorageFilesModel> recentList = new ArrayList<>();
    String rootPath;
    @BindView(R.id.rv_recent)
    RecyclerView rvRecent;
    @BindView(R.id.txt_header_title)
    TextView txtHeaderTitle;
    @BindView(R.id.txt_internal_storage)
    TextView txtInternalStorage;
    @BindView(R.id.txt_recent_file)
    TextView txtRecentFile;
    @BindView(R.id.txt_sd_card)
    TextView txtSdCard;
    @BindView(R.id.view_sd_card)
    View viewSdCard;

    public static void safedk_HomeActivity_startActivityForResult_method(HomeActivity p0, Intent p1, int p2) {
        MyApp.switcherLoadInter(p0);
        if (p1 != null) {
            p0.startActivityForResult(p1, p2);
        }
    }

    public static void safedk_HomeActivity_startActivity_secondMethod(HomeActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_home);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        //Mix Native Ads Call
        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_300);
        FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
        ImageView image = (ImageView) findViewById(R.id.banner_image);
        CardView qurekanative = (CardView) findViewById(R.id.qurekanative);
        RelativeLayout NativeAdContainer = (RelativeLayout) findViewById(R.id.nativeAds);
        RelativeLayout NativeAdsStartApp = (RelativeLayout) findViewById(R.id.sNativeAds);
        RecyclerView nativeMoPub = (RecyclerView) findViewById(R.id.nativemopub);
        FrameLayout maxNative = (FrameLayout) findViewById(R.id.max_native_ad_layout);
        //SplashLaunchActivity.MixNativeAdsCall(this, shimmerFrameLayout, frameLayout, image, qurekanative, NativeAdContainer, NativeAdsStartApp, nativeMoPub, maxNative);



        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
            }
        }, 1000);
        intView();
        displayDeleteEvent();
        copyMoveEvent();
        renameEvent();
        imageDeleteEvent();

    }


    public void intView() {
        this.rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        this.loutProgressBar.getLayoutParams().height = (Resources.getSystem().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen._4sdp)) / 4;
        this.loutProgressBar.requestLayout();
        setRecentAdapter();
        this.loutProgressBar.setVisibility(0);
        new Thread(new Runnable() {
            public final void run() {
                HomeActivity.this.getRecentData();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getRecentData() {
        getRecent();
        runOnUiThread(new Runnable() {
            public void run() {
                HomeActivity.this.loutProgressBar.setVisibility(8);
                if (HomeActivity.this.recentList == null || HomeActivity.this.recentList.size() == 0) {
                    HomeActivity.this.rvRecent.setVisibility(8);
                    HomeActivity.this.txtRecentFile.setVisibility(View.GONE);
                    return;
                }
                HomeActivity.this.rvRecent.setVisibility(0);
                HomeActivity.this.txtRecentFile.setVisibility(View.GONE);
            }
        });
    }

    private void getRecent() {
        Calendar instance = Calendar.getInstance();
        instance.set(6, instance.get(6) - 6);
        Date time = instance.getTime();
        Cursor query = getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "date_modified"}, (String) null, (String[]) null, "LOWER(date_modified) DESC");
        String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android";
        if (query != null) {
            if (query.getCount() <= 0 || !query.moveToFirst()) {
                query.close();
            }
            do {
                String string = query.getString(query.getColumnIndex("_data"));
                if (string != null && !str.toLowerCase().contains(string.toLowerCase()) && !string.toLowerCase().contains(str.toLowerCase())) {
                    File file = new File(string);
                    if (file.isFile() && time.compareTo(new Date(file.lastModified())) != 1 && !file.isDirectory()) {
                        String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(file.getPath());
                        InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                        internalStorageFilesModel.setFileName(file.getName());
                        internalStorageFilesModel.setFilePath(file.getPath());
                        if (file.isDirectory()) {
                            internalStorageFilesModel.setDir(true);
                        } else {
                            internalStorageFilesModel.setDir(false);
                        }
                        internalStorageFilesModel.setCheckboxVisible(false);
                        internalStorageFilesModel.setSelected(false);
                        internalStorageFilesModel.setMineType(mimeTypeFromFilePath);
                        this.recentList.add(internalStorageFilesModel);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (HomeActivity.this.adapter != null) {
                                    HomeActivity.this.adapter.notifyItemInserted(HomeActivity.this.recentList.size());
                                    HomeActivity.this.loutProgressBar.setVisibility(8);
                                }
                            }
                        });
                    }
                }
            } while (query.moveToNext());
            query.close();
        }
    }

    public void setRecentAdapter() {
        this.rvRecent.setLayoutManager(new LinearLayoutManager(this, 0, false));
        RecentAdapter recentAdapter = new RecentAdapter(this, this.recentList);
        this.adapter = recentAdapter;
        this.rvRecent.setAdapter(recentAdapter);
        this.adapter.setOnItemClickListener(new RecentAdapter.ClickListener() {
            public void onItemClick(int i, View view) {
                if (HomeActivity.this.recentList.get(i).isCheckboxVisible()) {
                    if (HomeActivity.this.recentList.get(i).isSelected()) {
                        HomeActivity.this.recentList.get(i).setSelected(false);
                    } else {
                        HomeActivity.this.recentList.get(i).setSelected(true);
                    }
                    HomeActivity.this.adapter.notifyDataSetChanged();
                    return;
                }
                InternalStorageFilesModel internalStorageFilesModel = HomeActivity.this.recentList.get(i);
                HomeActivity.this.openFile(new File(internalStorageFilesModel.getFilePath()), internalStorageFilesModel);
            }
        });
        this.adapter.setOnLongClickListener(new RecentAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
            }
        });
    }

    public void onResume() {
        super.onResume();
        setProgressData();
    }

    @OnClick({R.id.btn_Favourites, R.id.btn_apk, R.id.btn_audio, R.id.btn_document, R.id.btn_download, R.id.btn_image, R.id.btn_internal_storage, R.id.btn_sd_card, R.id.btn_video, R.id.btn_zip, R.id.iv_more, R.id.iv_search, R.id.lout_rate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Favourites:
                if (!this.openScreen) {
                    this.openScreen = true;
                    safedk_HomeActivity_startActivityForResult_method(this, new Intent(this, FavouriteActivity.class), 1);
                    return;
                }
                return;
            case R.id.btn_apk:
                if (!this.openScreen) {
                    this.openScreen = true;
                    safedk_HomeActivity_startActivityForResult_method(this, new Intent(this, ApkActivity.class), 1);
                    return;
                }
                return;
            case R.id.btn_audio:
                if (!this.openScreen) {
                    this.openScreen = true;
                    safedk_HomeActivity_startActivityForResult_method(this, new Intent(this, AudioActivity.class), 1);
                    return;
                }
                return;
            case R.id.btn_document:
                if (!this.openScreen) {
                    this.openScreen = true;
                    safedk_HomeActivity_startActivityForResult_method(this, new Intent(this, DocumentActivity.class), 1);
                    return;
                }
                return;
            case R.id.btn_download:
                if (!this.openScreen) {
                    this.openScreen = true;
                    Intent intent = new Intent(this, StorageActivity.class);
                    intent.putExtra("type", "Download");
                    safedk_HomeActivity_startActivityForResult_method(this, intent, 1);
                    return;
                }
                return;
            case R.id.btn_image:
                if (!this.openScreen) {
                    this.openScreen = true;
                    safedk_HomeActivity_startActivityForResult_method(this, new Intent(this, ImageActivity.class), 1);
                    return;
                }
                return;
            case R.id.btn_internal_storage:
                if (!this.openScreen) {
                    this.openScreen = true;
                    Intent intent2 = new Intent(this, StorageActivity.class);
                    intent2.putExtra("type", "Internal");
                    safedk_HomeActivity_startActivityForResult_method(this, intent2, 1);
                    return;
                }
                return;
            case R.id.btn_sd_card:
                if (!this.openScreen) {
                    this.openScreen = true;
                    Intent intent3 = new Intent(this, StorageActivity.class);
                    intent3.putExtra("type", "Sd card");
                    safedk_HomeActivity_startActivityForResult_method(this, intent3, 1);
                    return;
                }
                return;
            case R.id.btn_video:
                if (!this.openScreen) {
                    this.openScreen = true;
                    safedk_HomeActivity_startActivityForResult_method(this, new Intent(this, VideoActivity.class), 1);
                    return;
                }
                return;
            case R.id.btn_zip:
                if (!this.openScreen) {
                    this.openScreen = true;
                    safedk_HomeActivity_startActivityForResult_method(this, new Intent(this, ZipActivity.class), 1);
                    return;
                }
                return;
            case R.id.iv_more:
                Settings();
                return;
            case R.id.iv_search:
                Intent intentS = new Intent(this, SearchActivity.class);
                CallAds(intentS);
                //safedk_HomeActivity_startActivity_04da78e3e4079992aa593ea4c4eb9c76(this, new Intent(this, SearchActivity.class));
                return;
            case R.id.lout_rate:
                //rate app
                return;
            default:
                return;
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            this.openScreen = false;
        }
    }

    public void Settings() {

        Intent intent = new Intent(this, SettingActivity.class);
        CallAds(intent);

        safedk_HomeActivity_startActivity_secondMethod(this, new Intent(this, SettingActivity.class));
    }

    
    public void openFile(File file, InternalStorageFilesModel internalStorageFilesModel) {
        Uri uri;
        String mineType = internalStorageFilesModel.getMineType();
        if (mineType != null && (mineType.equalsIgnoreCase("image/jpeg") || mineType.equalsIgnoreCase("image/png") || mineType.equalsIgnoreCase("image/webp"))) {
            PhotoData photoData = new PhotoData();
            photoData.setFileName(internalStorageFilesModel.getFileName());
            photoData.setFilePath(internalStorageFilesModel.getFilePath());
            photoData.setFavorite(internalStorageFilesModel.isFavorite());
            Constant.displayImageList = new ArrayList();
            Constant.displayImageList.add(photoData);
            Intent intent = new Intent(this, DisplayImageActivity.class);
            intent.putExtra("pos", 0);
            //safedk_HomeActivity_startActivity_04da78e3e4079992aa593ea4c4eb9c76(this, intent);
            CallAds(intent);
        } else if (mineType != null && (mineType.equalsIgnoreCase("video/mp4") || mineType.equalsIgnoreCase("video/x-matroska"))) {
            PhotoData photoData2 = new PhotoData();
            photoData2.setFileName(internalStorageFilesModel.getFileName());
            photoData2.setFilePath(internalStorageFilesModel.getFilePath());
            Constant.displayVideoList = new ArrayList();
            Constant.displayVideoList.add(photoData2);
            Intent intent2 = new Intent(this, VideoPlayActivity.class);
            intent2.putExtra("pos", 0);
            //safedk_HomeActivity_startActivity_04da78e3e4079992aa593ea4c4eb9c76(this, intent2);
            CallAds(intent2);
        } else if (mineType != null && mineType.equalsIgnoreCase("application/vnd.android.package-archive")) {
            try {
                Intent intent3 = new Intent("android.intent.action.INSTALL_PACKAGE");
                String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(internalStorageFilesModel.getFilePath());
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(internalStorageFilesModel.getFilePath()));
                } else {
                    uri = Uri.fromFile(new File(internalStorageFilesModel.getFilePath()));
                }
                intent3.setFlags(1);
                intent3.setDataAndType(uri, mimeTypeFromFilePath);
                safedk_HomeActivity_startActivity_secondMethod(this, intent3);
            } catch (Exception unused) {
            }
        } else if (mineType == null || !mineType.equalsIgnoreCase("application/zip")) {
            Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent4 = new Intent();
            intent4.setAction("android.intent.action.VIEW");
            intent4.setDataAndType(uriForFile, Utils.getMimeTypeFromFilePath(file.getPath()));
            intent4.addFlags(1);
            safedk_HomeActivity_startActivity_secondMethod(this, Intent.createChooser(intent4, "Open with"));
        } else {
            File file2 = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
            if (!file2.exists()) {
                file2.mkdir();
            }
            File file3 = new File(file2.getPath() + "/.zipExtract");
            if (file3.exists() && StorageUtils.deleteFile(file3, this)) {
                MediaScannerConnection.scanFile(this, new String[]{file3.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
            }
            Intent intent5 = new Intent(this, OpenZipFileActivity.class);
            intent5.putExtra("ZipName", internalStorageFilesModel.getFileName());
            intent5.putExtra("ZipPath", internalStorageFilesModel.getFilePath());
            //safedk_HomeActivity_startActivity_04da78e3e4079992aa593ea4c4eb9c76(this, intent5);
            CallAds(intent5);
        }
    }

    private void CallAds(Intent intent) {
        SplashLaunchActivity.InterstitialAdsCall(this, intent);
    }

    private void setProgressData() {
        setInternalData();
        setSdCardData();
    }

    public void setSdCardData() {
        long j;
        long j2;
        int i;
        boolean externalMemoryAvailable = Utils.externalMemoryAvailable(this);
        this.isSdCard = externalMemoryAvailable;
        if (externalMemoryAvailable) {
            this.btnSdCard.setVisibility(0);
            this.viewSdCard.setVisibility(0);
            String externalStoragePath = Utils.getExternalStoragePath(this, true);
            if (externalStoragePath == null || externalStoragePath.equalsIgnoreCase("")) {
                this.btnSdCard.setVisibility(8);
                this.viewSdCard.setVisibility(8);
                return;
            }
            StatFs statFs = new StatFs(new File(externalStoragePath).getPath());
            if (Build.VERSION.SDK_INT < 18) {
                j2 = (long) statFs.getBlockSize();
                j = (long) statFs.getAvailableBlocks();
                i = statFs.getBlockCount();
            } else {
                j2 = (long) statFs.getBlockSize();
                j = (long) statFs.getAvailableBlocks();
                i = statFs.getBlockCount();
            }
            long j3 = ((long) i) * j2;
            TextView textView = this.txtSdCard;
            textView.setText(Formatter.formatShortFileSize(this, j3 - (j * j2)) + " / " + Formatter.formatShortFileSize(this, j3));
            StatFs statFs2 = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = (((long) statFs2.getBlockSize()) * ((long) statFs2.getBlockCount())) / 1048576;
            return;
        }
        this.btnSdCard.setVisibility(8);
        this.viewSdCard.setVisibility(8);
    }

    private void setInternalData() {
        if (Build.VERSION.SDK_INT >= 26) {
            StorageStatsManager storageStatsManager = (StorageStatsManager) getSystemService("storagestats");
            StorageManager storageManager = (StorageManager) getSystemService("storage");
            if (storageManager != null && storageStatsManager != null) {
                for (StorageVolume next : storageManager.getStorageVolumes()) {
                    String uuid = next.getUuid();
                    if (next.isPrimary()) {
                        UUID fromString = uuid == null ? StorageManager.UUID_DEFAULT : UUID.fromString(uuid);
                        try {
                            long totalBytes = storageStatsManager.getTotalBytes(fromString) - storageStatsManager.getFreeBytes(fromString);
                            TextView textView = this.txtInternalStorage;
                            textView.setText(Formatter.formatShortFileSize(this, totalBytes) + " / " + Formatter.formatShortFileSize(this, storageStatsManager.getTotalBytes(fromString)));
                        } catch (Exception unused) {
                            CallInternalStorageElse();
                        }
                    }
                }
                return;
            }
            return;
        }
        CallInternalStorageElse();
    }

    private void CallInternalStorageElse() {
        long j;
        long j2;
        int i;
        StatFs statFs = new StatFs(new File(String.valueOf(Environment.getDataDirectory())).getPath());
        if (Build.VERSION.SDK_INT < 18) {
            j2 = (long) statFs.getBlockSize();
            j = (long) statFs.getAvailableBlocks();
            i = statFs.getBlockCount();
        } else {
            j2 = (long) statFs.getBlockSize();
            j = (long) statFs.getAvailableBlocks();
            i = statFs.getBlockCount();
        }
        long j3 = ((long) i) * j2;
        long j4 = j3 - (j * j2);
        TextView textView = this.txtInternalStorage;
        textView.setText(Formatter.formatShortFileSize(this, j4) + " / " + Formatter.formatShortFileSize(this, j3));
    }

    
    public void getRecentList(File file) {
        boolean z;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String format = simpleDateFormat.format(Calendar.getInstance().getTime());
        Calendar instance = Calendar.getInstance();
        instance.add(5, -1);
        instance.getTime();
        String format2 = simpleDateFormat.format(instance.getTime());
        instance.add(5, -1);
        String format3 = simpleDateFormat.format(instance.getTime());
        instance.add(5, -1);
        String format4 = simpleDateFormat.format(instance.getTime());
        instance.add(5, -1);
        String format5 = simpleDateFormat.format(instance.getTime());
        instance.add(5, -1);
        String format6 = simpleDateFormat.format(instance.getTime());
        instance.add(5, -1);
        String format7 = simpleDateFormat.format(instance.getTime());
        File[] listFiles = file.listFiles();
        int length = listFiles.length;
        int i = 0;
        while (i < length) {
            File file2 = listFiles[i];
            File[] fileArr = listFiles;
            if (!file2.getPath().equalsIgnoreCase(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Android").getPath()) && !file2.getName().startsWith(".")) {
                if (file2.isFile()) {
                    String format8 = simpleDateFormat.format(Long.valueOf(file2.lastModified()));
                    String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(file2.getPath());
                    if (mimeTypeFromFilePath != null && (format8.equalsIgnoreCase(format) || format8.equalsIgnoreCase(format2) || format8.equalsIgnoreCase(format3) || format8.equalsIgnoreCase(format4) || format8.equalsIgnoreCase(format5) || format8.equalsIgnoreCase(format6) || format8.equalsIgnoreCase(format7))) {
                        InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                        internalStorageFilesModel.setFileName(file2.getName());
                        internalStorageFilesModel.setFilePath(file2.getPath());
                        if (file2.isDirectory()) {
                            internalStorageFilesModel.setDir(true);
                            z = false;
                        } else {
                            z = false;
                            internalStorageFilesModel.setDir(false);
                        }
                        internalStorageFilesModel.setCheckboxVisible(z);
                        internalStorageFilesModel.setSelected(z);
                        internalStorageFilesModel.setMineType(mimeTypeFromFilePath);
                        this.recentList.add(internalStorageFilesModel);
                    }
                } else if (file2.isDirectory()) {
                    getRecentList(file2.getAbsoluteFile());
                }
                i++;
                listFiles = fileArr;
            }
            i++;
            listFiles = fileArr;
        }
    }

    private void displayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DisplayDeleteEvent>() {
            public void call(DisplayDeleteEvent displayDeleteEvent) {
                if (!(displayDeleteEvent.getDeleteList() == null || displayDeleteEvent.getDeleteList().size() == 0)) {
                    new ArrayList();
                    HomeActivity.this.updateDeleteImageData(displayDeleteEvent.getDeleteList());
                }
                HomeActivity.this.updateFavoriteList();
                HomeActivity.this.startImageService();
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void imageDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(ImageDeleteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<ImageDeleteEvent>() {
            public void call(ImageDeleteEvent imageDeleteEvent) {
                if (imageDeleteEvent.getDeletePath() != null && !imageDeleteEvent.getDeletePath().equalsIgnoreCase("")) {
                    File file = new File(imageDeleteEvent.getDeletePath());
                    if (file.exists()) {
                        HomeActivity homeActivity = HomeActivity.this;
                        ContentResolver contentResolver = homeActivity.getContentResolver();
                        contentResolver.delete(FileProvider.getUriForFile(homeActivity, HomeActivity.this.getPackageName() + ".provider", file), (String) null, (String[]) null);
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MediaScannerConnection.scanFile(HomeActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                    }
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void updateFavoriteList() {
        new ArrayList();
        ArrayList arrayList = new ArrayList();
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        for (int i = 0; i < favouriteList.size(); i++) {
            File file = new File(favouriteList.get(i));
            if (file.exists()) {
                arrayList.add(file.getPath());
            }
        }
        PreferencesManager.setFavouriteList(this, arrayList);
    }

    
    public void updateDeleteImageData(ArrayList<String> arrayList) {
        ArrayList<InternalStorageFilesModel> arrayList2 = this.recentList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 < this.recentList.size()) {
                        if (this.recentList.get(i2).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                            break;
                        }
                        i2++;
                    }
                }
                this.recentList.remove(i2);
                try {
                    if (this.recentList.size() != 1 && 1 < this.recentList.size() && this.recentList.get(1).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        this.recentList.remove(1);
                    }
                    if (this.recentList.size() != 0 && this.recentList.get(0).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        this.recentList.remove(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RecentAdapter recentAdapter = this.adapter;
            if (recentAdapter != null) {
                recentAdapter.notifyDataSetChanged();
            }
            ArrayList<InternalStorageFilesModel> arrayList3 = this.recentList;
            if (arrayList3 == null || arrayList3.size() == 0) {
                this.rvRecent.setVisibility(8);
                this.txtRecentFile.setVisibility(8);
                return;
            }
            this.rvRecent.setVisibility(0);
            this.txtRecentFile.setVisibility(8);
        }
    }

    private void copyMoveEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CopyMoveEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<CopyMoveEvent>() {
            public void call(CopyMoveEvent copyMoveEvent) {
                boolean z;
                if (!(copyMoveEvent.getCopyMoveList() == null || copyMoveEvent.getCopyMoveList().size() == 0)) {
                    new ArrayList();
                    ArrayList<File> copyMoveList = copyMoveEvent.getCopyMoveList();
                    if (copyMoveList == null) {
                        copyMoveList = new ArrayList<>();
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    String format = simpleDateFormat.format(Calendar.getInstance().getTime());
                    Calendar instance = Calendar.getInstance();
                    instance.add(5, -1);
                    instance.getTime();
                    String format2 = simpleDateFormat.format(instance.getTime());
                    instance.add(5, -1);
                    String format3 = simpleDateFormat.format(instance.getTime());
                    instance.add(5, -1);
                    String format4 = simpleDateFormat.format(instance.getTime());
                    instance.add(5, -1);
                    String format5 = simpleDateFormat.format(instance.getTime());
                    instance.add(5, -1);
                    String format6 = simpleDateFormat.format(instance.getTime());
                    instance.add(5, -1);
                    String format7 = simpleDateFormat.format(instance.getTime());
                    boolean z2 = false;
                    for (int i = 0; i < copyMoveList.size(); i++) {
                        File file = copyMoveList.get(i);
                        if (!file.getName().startsWith(".")) {
                            if (file.isFile()) {
                                String format8 = simpleDateFormat.format(Long.valueOf(file.lastModified()));
                                String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(file.getPath());
                                if (mimeTypeFromFilePath != null && (format8.equalsIgnoreCase(format) || format8.equalsIgnoreCase(format2) || format8.equalsIgnoreCase(format3) || format8.equalsIgnoreCase(format4) || format8.equalsIgnoreCase(format5) || format8.equalsIgnoreCase(format6) || format8.equalsIgnoreCase(format7))) {
                                    InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                                    internalStorageFilesModel.setFileName(file.getName());
                                    internalStorageFilesModel.setFilePath(file.getPath());
                                    if (file.isDirectory()) {
                                        internalStorageFilesModel.setDir(true);
                                        z = false;
                                    } else {
                                        z = false;
                                        internalStorageFilesModel.setDir(false);
                                    }
                                    internalStorageFilesModel.setCheckboxVisible(z);
                                    internalStorageFilesModel.setSelected(z);
                                    internalStorageFilesModel.setMineType(mimeTypeFromFilePath);
                                    if ((mimeTypeFromFilePath != null && mimeTypeFromFilePath.equalsIgnoreCase("image/jpeg")) || mimeTypeFromFilePath.equalsIgnoreCase("image/png") || mimeTypeFromFilePath.equalsIgnoreCase("image/webp")) {
                                        z2 = true;
                                    }
                                    HomeActivity.this.recentList.add(0, internalStorageFilesModel);
                                }
                            } else if (file.isDirectory()) {
                                HomeActivity.this.getRecentList(file.getAbsoluteFile());
                            }
                        }
                    }
                    if (z2) {
                        HomeActivity.this.startImageService();
                    }
                    if (HomeActivity.this.adapter != null) {
                        HomeActivity.this.adapter.notifyDataSetChanged();
                    } else {
                        HomeActivity.this.setRecentAdapter();
                    }
                    if (HomeActivity.this.recentList == null || HomeActivity.this.recentList.size() == 0) {
                        HomeActivity.this.rvRecent.setVisibility(8);
                        HomeActivity.this.txtRecentFile.setVisibility(8);
                    } else {
                        HomeActivity.this.rvRecent.setVisibility(0);
                        HomeActivity.this.txtRecentFile.setVisibility(8);
                    }
                }
                if (!(copyMoveEvent.getDeleteList() == null || copyMoveEvent.getDeleteList().size() == 0)) {
                    new ArrayList();
                    ArrayList<String> deleteList = copyMoveEvent.getDeleteList();
                    if (deleteList == null) {
                        deleteList = new ArrayList<>();
                    }
                    HomeActivity.this.updateDeleteImageData(deleteList);
                }
                HomeActivity.this.updateFavoriteList();
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void startImageService() {
        if (!Constant.isOpenImage) {
            startService(new Intent(this, ImageDataService.class));
        }
    }

    private void renameEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(RenameEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<RenameEvent>() {
            public void call(RenameEvent renameEvent) {
                if (!(renameEvent.getNewFile() == null || renameEvent.getOldFile() == null || !renameEvent.getNewFile().exists())) {
                    new ArrayList();
                    ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(HomeActivity.this);
                    if (favouriteList == null) {
                        favouriteList = new ArrayList<>();
                    }
                    if (!(HomeActivity.this.recentList == null || HomeActivity.this.recentList.size() == 0)) {
                        int i = 0;
                        boolean z = false;
                        while (i < HomeActivity.this.recentList.size()) {
                            if (renameEvent.getOldFile().getPath().equalsIgnoreCase(HomeActivity.this.recentList.get(i).getFilePath())) {
                                HomeActivity.this.recentList.get(i).setFilePath(renameEvent.getNewFile().getPath());
                                HomeActivity.this.recentList.get(i).setFileName(renameEvent.getNewFile().getName());
                                String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(renameEvent.getNewFile().getPath());
                                if ((mimeTypeFromFilePath != null && mimeTypeFromFilePath.equalsIgnoreCase("image/jpeg")) || mimeTypeFromFilePath.equalsIgnoreCase("image/png") || mimeTypeFromFilePath.equalsIgnoreCase("image/webp")) {
                                    z = true;
                                }
                            } else {
                                i++;
                            }
                        }
                        if (favouriteList.contains(renameEvent.getOldFile().getPath())) {
                            favouriteList.remove(renameEvent.getOldFile().getPath());
                            favouriteList.add(renameEvent.getNewFile().getPath());
                        }
                        if (z) {
                            HomeActivity.this.startImageService();
                        }
                        PreferencesManager.setFavouriteList(HomeActivity.this, favouriteList);
                        if (HomeActivity.this.adapter == null) {
                            HomeActivity.this.adapter.notifyDataSetChanged();
                        } else {
                            HomeActivity.this.setRecentAdapter();
                        }
                        if (HomeActivity.this.recentList != null || HomeActivity.this.recentList.size() == 0) {
                            HomeActivity.this.rvRecent.setVisibility(8);
                            HomeActivity.this.txtRecentFile.setVisibility(8);
                        } else {
                            HomeActivity.this.rvRecent.setVisibility(0);
                            HomeActivity.this.txtRecentFile.setVisibility(8);
                        }
                    }
                    favouriteList.contains(renameEvent.getOldFile().getPath());
                    PreferencesManager.setFavouriteList(HomeActivity.this, favouriteList);
                    RecentAdapter recentAdapter = HomeActivity.this.adapter;
                    ArrayList<InternalStorageFilesModel> arrayList = HomeActivity.this.recentList;
                    HomeActivity.this.rvRecent.setVisibility(8);
                    HomeActivity.this.txtRecentFile.setVisibility(8);
                }
                HomeActivity.this.updateFavoriteList();
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }


}
