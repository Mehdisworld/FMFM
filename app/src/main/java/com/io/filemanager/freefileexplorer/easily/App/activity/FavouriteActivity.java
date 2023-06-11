package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.adapter.FavouriteAdapter;
import com.io.filemanager.freefileexplorer.easily.adapter.FavouriteHeaderListAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayFavoriteEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.InternalStorageFilesModel;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.oncliclk.BottomListner;
import com.io.filemanager.freefileexplorer.easily.utils.BottomSheetFragment;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
import com.io.filemanager.freefileexplorer.easily.utils.StorageUtils;
import com.io.filemanager.freefileexplorer.easily.utils.Utils;
/*import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.safedk.android.analytics.brandsafety.creatives.infos.CreativeInfo;
import com.safedk.android.utils.Logger;*/
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import net.lingala.zip4j.util.InternalZipConstants;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FavouriteActivity extends AppCompatActivity implements BottomListner {
    static int videoImage_code = 30;
    static int zip_open = 40;
    FavouriteAdapter adapter;
    
    public ArrayList<String> arrayListFilePaths = new ArrayList<>();
    ArrayList<InternalStorageFilesModel> backUpstorageList = new ArrayList<>();
    String compressPath;
    String extractPath;
    String extract_file_name;
    int folder_counter = 1;
    @BindView(R.id.img_compress)
    ImageView imgCompress;
    @BindView(R.id.img_copy)
    ImageView imgCopy;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.img_extract)
    ImageView imgExtract;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.img_move)
    ImageView imgMove;
    @BindView(R.id.img_send)
    ImageView imgSend;
    boolean isCheckAll = false;
    boolean isDir = false;
    boolean isFileFromSdCard = false;
    boolean isGrid = false;
    boolean isSdCard = false;
    boolean isShowHidden = false;
    @BindView(R.id.iv_check_all)
    ImageView ivCheckAll;
    @BindView(R.id.iv_close)
    AppCompatImageView ivClose;
    @BindView(R.id.iv_fav_fill)
    ImageView ivFavFill;
    @BindView(R.id.iv_fav_unfill)
    ImageView ivFavUnfill;
    @BindView(R.id.iv_more)
    AppCompatImageView ivMore;
    @BindView(R.id.iv_uncheck)
    ImageView ivUncheck;
    @BindView(R.id.ll_bottom_option)
    LinearLayout llBottomOption;
    @BindView(R.id.ll_check_all)
    RelativeLayout llCheckAll;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_favourite)
    RelativeLayout llFavourite;
    @BindView(R.id.ll_banner)
    FrameLayout ll_banner;
    ProgressDialog loadingDialog;
    @BindView(R.id.lout_compress)
    LinearLayout loutCompress;
    @BindView(R.id.lout_copy)
    LinearLayout loutCopy;
    @BindView(R.id.lout_delete)
    LinearLayout loutDelete;
    @BindView(R.id.lout_extract)
    LinearLayout loutExtract;
    @BindView(R.id.lout_more)
    LinearLayout loutMore;
    @BindView(R.id.lout_move)
    LinearLayout loutMove;
    @BindView(R.id.lout_selected)
    RelativeLayout loutSelected;
    @BindView(R.id.lout_send)
    LinearLayout loutSend;
    @BindView(R.id.lout_toolbar)
    RelativeLayout loutToolbar;
    String mainRootPath;
    ArrayList<File> pastList = new ArrayList<>();
    FavouriteHeaderListAdapter pathAdapter;
    int pos = 0;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    String rootPath;
    @BindView(R.id.rv_header)
    RecyclerView rvHeader;
    String sdCardPath;
    int sdCardPermissionType = 0;
    int selected_Item = 0;
    ArrayList<InternalStorageFilesModel> storageList = new ArrayList<>();
    String storage_type;
    @BindView(R.id.txt_select)
    AppCompatTextView txtSelect;
    @BindView(R.id.txt_text_compress)
    TextView txtTextCompress;
    @BindView(R.id.txt_text_copy)
    TextView txtTextCopy;
    @BindView(R.id.txt_text_delete)
    TextView txtTextDelete;
    @BindView(R.id.txt_text_extract)
    TextView txtTextExtract;
    @BindView(R.id.txt_text_more)
    TextView txtTextMore;
    @BindView(R.id.txt_text_move)
    TextView txtTextMove;
    @BindView(R.id.txt_text_send)
    TextView txtTextSend;
    String zip_file_name;

    public static void safedk_FavouriteActivity_startActivityForResult_4b497934248c561fe71634bbfaf8bc4b(FavouriteActivity p0, Intent p1, int p2) {
        if (p1 != null) {
            p0.startActivityForResult(p1, p2);
        }
    }

    public static void safedk_FavouriteActivity_startActivity_3461e3a02d4572bd1fcf13589088aeb0(FavouriteActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void setCopyMoveAction() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_favourite);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        intView();
        displayDeleteEvent();
        displayFavoriteEvent();
    }

    public void onResume() {
        super.onResume();
        this.isSdCard = Utils.externalMemoryAvailable(this);
    }

    public void intView() {
        getIntent();
        this.isSdCard = Utils.externalMemoryAvailable(this);
        this.mainRootPath = "Favourites";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        this.ivUncheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
        this.ivCheckAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        this.ivFavFill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_fill));
        this.ivFavUnfill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_unfill));
        this.imgMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_bottom));
        this.rootPath = file.getPath();
        File file2 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.compress_file));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.extract_file));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        this.compressPath = file2.getPath();
        this.extractPath = file3.getPath();
        this.sdCardPath = Utils.getExternalStoragePath(this, true);
        this.loutToolbar.setVisibility(0);
        this.isGrid = PreferencesManager.getDirList_Grid(this);
        this.isShowHidden = PreferencesManager.getShowHidden(this);
        this.ivCheckAll.setColorFilter(getResources().getColor(R.color.theme_color), PorterDuff.Mode.SRC_IN);
        getDataList();
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);
    }

    private void displayFavoriteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayFavoriteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DisplayFavoriteEvent>() {
            public void call(DisplayFavoriteEvent displayFavoriteEvent) {
                if (displayFavoriteEvent.getFilePath() != null && !displayFavoriteEvent.getFilePath().equalsIgnoreCase("")) {
                    FavouriteActivity.this.storageList.clear();
                    FavouriteActivity favouriteActivity = FavouriteActivity.this;
                    favouriteActivity.getFilesList((String) favouriteActivity.arrayListFilePaths.get(FavouriteActivity.this.arrayListFilePaths.size() - 1));
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    public void getDataList() {
        this.progressBar.setVisibility(0);
        new Thread(new Runnable() {
            public final void run() {
                FavouriteActivity.this.getList();
            }
        }).start();
    }

    public void onBackPressed() {
        if (this.loutSelected.getVisibility() == 0) {
            setSelectionClose();
        } else if (this.arrayListFilePaths.size() == 1) {
            super.onBackPressed();
        } else if (this.arrayListFilePaths.size() == 0 || this.arrayListFilePaths.size() == 1) {
            super.onBackPressed();
        } else {
            ArrayList<String> arrayList = this.arrayListFilePaths;
            arrayList.remove(arrayList.size() - 1);
            this.storageList.clear();
            ArrayList<String> arrayList2 = this.arrayListFilePaths;
            getFilesList(arrayList2.get(arrayList2.size() - 1));
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_close, R.id.iv_more, R.id.ll_check_all, R.id.ll_favourite, R.id.lout_compress, R.id.lout_copy, R.id.lout_delete, R.id.lout_extract, R.id.lout_more, R.id.lout_move, R.id.lout_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                return;
            case R.id.iv_close:
                setSelectionClose();
                return;
            case R.id.iv_more:
                setMoreMenu();
                return;
            case R.id.ll_check_all:
                if (this.isCheckAll) {
                    this.isCheckAll = false;
                    selectEvent(false);
                    this.ivCheckAll.setVisibility(8);
                    this.loutSelected.setVisibility(8);
                    this.loutToolbar.setVisibility(0);
                    return;
                }
                this.isCheckAll = true;
                selectEvent(true);
                this.ivCheckAll.setVisibility(0);
                return;
            case R.id.ll_favourite:
                if (this.selected_Item != 0) {
                    if (this.ivFavFill.getVisibility() == 0) {
                        setUnFavourite();
                        return;
                    } else {
                        setFavourite();
                        return;
                    }
                } else {
                    return;
                }
            case R.id.lout_compress:
                showCompressDialog();
                return;
            case R.id.lout_copy:
                Constant.isCopyData = true;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_delete:
                showDeleteDialog();
                return;
            case R.id.lout_extract:
                showExtractDialog();
                return;
            case R.id.lout_more:
                showMoreOptionBottom();
                return;
            case R.id.lout_move:
                Constant.isCopyData = false;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_send:
                sendFile();
                return;
            default:
                return;
        }
    }

    private void setUnFavourite() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        while (true) {
            boolean z = true;
            if (i >= this.storageList.size()) {
                break;
            }
            if (!this.storageList.get(i).isSelected() || !this.storageList.get(i).isFavorite()) {
                z = false;
            } else {
                this.storageList.get(i).setFavorite(false);
                i2++;
                if (favouriteList.contains(this.storageList.get(i).getFilePath())) {
                    favouriteList.remove(this.storageList.get(i).getFilePath());
                }
            }
            this.storageList.get(i).setSelected(false);
            this.storageList.get(i).setCheckboxVisible(false);
            if (z) {
                arrayList.add(this.storageList.get(i));
            }
            i++;
        }
        if (this.mainRootPath.equalsIgnoreCase("Favourites")) {
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                this.storageList.remove(arrayList.get(i3));
            }
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
        ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
        if (arrayList2 == null || arrayList2.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
        } else {
            this.recyclerView.setVisibility(0);
            this.llEmpty.setVisibility(8);
        }
        String str = i2 == 1 ? " item removed from Favourites." : " items removed from Favourites.";
        Toast.makeText(this, i2 + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
    }

    private void setFavourite() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        int i = 0;
        for (int i2 = 0; i2 < this.storageList.size(); i2++) {
            if (this.storageList.get(i2).isSelected()) {
                if (!this.storageList.get(i2).isFavorite()) {
                    favouriteList.add(0, this.storageList.get(i2).getFilePath());
                    i++;
                }
                this.storageList.get(i2).setFavorite(true);
            }
            this.storageList.get(i2).setSelected(false);
            this.storageList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
        String str = i == 1 ? " item added to Favourites." : " items added to Favourites.";
        Toast.makeText(this, i + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
    }

    private void showMoreOptionBottom() {
        PopupMenu popupMenu = new PopupMenu(this, this.loutMore);
        popupMenu.getMenuInflater().inflate(R.menu.storage_more_menu, popupMenu.getMenu());
        if (this.selected_Item == 1) {
            popupMenu.getMenu().findItem(R.id.menu_rename).setVisible(true);
            popupMenu.getMenu().findItem(R.id.menu_details).setVisible(true);
        } else {
            popupMenu.getMenu().findItem(R.id.menu_rename).setVisible(false);
            popupMenu.getMenu().findItem(R.id.menu_details).setVisible(false);
        }
        if (this.isDir) {
            popupMenu.getMenu().findItem(R.id.menu_share).setVisible(false);
        } else {
            popupMenu.getMenu().findItem(R.id.menu_share).setVisible(true);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_details) {
                    FavouriteActivity.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    FavouriteActivity.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    FavouriteActivity.this.sendFile();
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == videoImage_code && i2 == -1) {
            if (Constant.arrayListFilePaths != null) {
                this.arrayListFilePaths = Constant.arrayListFilePaths;
                this.storageList.clear();
                ArrayList<String> arrayList = this.arrayListFilePaths;
                getFilesList(arrayList.get(arrayList.size() - 1));
                setHeaderData();
                setRecyclerViewData();
            }
        } else if (i == zip_open && i2 == -1) {
            refreshData();
        } else if (i == 300) {
            String sDCardTreeUri = PreferencesManager.getSDCardTreeUri(this);
            Uri uri = null;
            Uri parse = sDCardTreeUri != null ? Uri.parse(sDCardTreeUri) : null;
            if (i2 == -1 && (uri = intent.getData()) != null) {
                PreferencesManager.setSDCardTreeUri(this, uri.toString());
                int i3 = this.sdCardPermissionType;
                if (i3 == 1) {
                    setDeleteFile();
                } else if (i3 == 2) {
                    setCopyMoveAction();
                } else if (i3 == 3) {
                    setcompress();
                } else if (i3 == 4) {
                    setExtract();
                }
            }
            if (i2 == -1) {
                try {
                    int flags = intent.getFlags() & 3;
                    if (Build.VERSION.SDK_INT >= 19) {
                        getContentResolver().takePersistableUriPermission(uri, flags);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (uri != null) {
                PreferencesManager.setSDCardTreeUri(this, parse.toString());
                int i4 = this.sdCardPermissionType;
                if (i4 == 1) {
                    setDeleteFile();
                } else if (i4 == 2) {
                    setCopyMoveAction();
                } else if (i4 == 3) {
                    setcompress();
                } else if (i4 == 4) {
                    setExtract();
                }
            }
        }
    }

    
    public void showDetailDialog() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(80);
        File file = new File(this.storageList.get(this.pos).getFilePath());
        TextView textView = (TextView) dialog.findViewById(R.id.txt_title);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txt_format);
        TextView textView3 = (TextView) dialog.findViewById(R.id.txt_time);
        TextView textView4 = (TextView) dialog.findViewById(R.id.txt_resolution);
        TextView textView5 = (TextView) dialog.findViewById(R.id.txt_file_size);
        TextView textView6 = (TextView) dialog.findViewById(R.id.txt_duration);
        TextView textView7 = (TextView) dialog.findViewById(R.id.txt_path);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.lout_duration);
        LinearLayout linearLayout2 = (LinearLayout) dialog.findViewById(R.id.lout_resolution);
        linearLayout2.setVisibility(8);
        linearLayout.setVisibility(8);
        TextView textView8 = (TextView) dialog.findViewById(R.id.btn_ok);
        if (file.exists()) {
            textView.setText(file.getName());
            textView7.setText(file.getPath());
            String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(file.getPath());
            textView2.setText(mimeTypeFromFilePath);
            textView5.setText(Formatter.formatShortFileSize(this, file.length()));
            textView3.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm a").format(Long.valueOf(file.lastModified())));
            if (file.isDirectory()) {
                linearLayout2.setVisibility(8);
                linearLayout.setVisibility(8);
            } else if (mimeTypeFromFilePath != null && mimeTypeFromFilePath.contains("image")) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    int i = options.outHeight;
                    int i2 = options.outWidth;
                    textView4.setText(i2 + " x " + i);
                    linearLayout2.setVisibility(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    linearLayout2.setVisibility(8);
                }
            } else if (mimeTypeFromFilePath != null && mimeTypeFromFilePath.contains("video")) {
                try {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(file.getPath());
                    String extractMetadata = mediaMetadataRetriever.extractMetadata(19);
                    extractMetadata.getClass();
                    int parseInt = Integer.parseInt(extractMetadata);
                    String extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
                    extractMetadata2.getClass();
                    int parseInt2 = Integer.parseInt(extractMetadata2);
                    textView6.setText(getDurationString((int) Long.parseLong(mediaMetadataRetriever.extractMetadata(9))));
                    textView4.setText(parseInt2 + "X" + parseInt);
                    linearLayout2.setVisibility(0);
                    linearLayout.setVisibility(0);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    linearLayout2.setVisibility(8);
                    linearLayout.setVisibility(8);
                }
            }
        }
        textView8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String getDurationString(int i) {
        String str;
        String str2;
        String str3;
        long j = (long) i;
        long hours = TimeUnit.MILLISECONDS.toHours(j);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(j);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j));
        if (minutes < 10) {
            str = "0" + minutes;
        } else {
            str = String.valueOf(minutes);
        }
        if (seconds < 10) {
            str2 = "0" + seconds;
        } else {
            str2 = String.valueOf(seconds);
        }
        if (hours < 10) {
            str3 = "0" + hours;
        } else {
            str3 = String.valueOf(hours);
        }
        if (hours == 0) {
            return str + ":" + str2;
        }
        return str3 + ":" + str + ":" + str2;
    }

    
    public void showRenameDialog() {
        setSelectionClose();
        final File file = new File(this.storageList.get(this.pos).getFilePath());
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_rename);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(17);
        final AppCompatEditText appCompatEditText = (AppCompatEditText) dialog.findViewById(R.id.edt_file_name);
        appCompatEditText.setText(file.getName());
        ((LinearLayout) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String filenameExtension = Utils.getFilenameExtension(file.getName());
                if (appCompatEditText.getText().toString().isEmpty()) {
                    Toast.makeText(FavouriteActivity.this, "New name can't be empty.", 0).show();
                } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                    dialog.show();
                } else if (!file.isDirectory()) {
                    String[] split = appCompatEditText.getText().toString().split("\\.");
                    if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                        final Dialog dialog = new Dialog(FavouriteActivity.this, R.style.WideDialog);
                        dialog.requestWindowFeature(1);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_rename_validation);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        dialog.getWindow().setGravity(17);
                        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                dialog.dismiss();
                                appCompatEditText.setText(file.getName());
                            }
                        });
                        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                dialog.dismiss();
                                dialog.dismiss();
                                FavouriteActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                            }
                        });
                        dialog.show();
                        return;
                    }
                    Log.e("", "rename");
                    dialog.dismiss();
                    FavouriteActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                } else {
                    dialog.dismiss();
                    FavouriteActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                }
            }
        });
        dialog.show();
    }

    private void setVisibleButton(LinearLayout linearLayout, ImageView imageView, TextView textView) {
        linearLayout.setClickable(true);
        linearLayout.setEnabled(true);
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        textView.setTextColor(getResources().getColor(R.color.black));
    }

    private void setInvisibleButton(LinearLayout linearLayout, ImageView imageView, TextView textView) {
        linearLayout.setClickable(false);
        linearLayout.setEnabled(false);
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.invisible_color), PorterDuff.Mode.SRC_IN);
        textView.setTextColor(getResources().getColor(R.color.invisible_color));
    }

    private void refreshData() {
        ArrayList<String> arrayList;
        ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
        if (arrayList2 != null && arrayList2.size() != 0 && (arrayList = this.arrayListFilePaths) != null && arrayList.size() != 0) {
            this.storageList.clear();
            ArrayList<String> arrayList3 = this.arrayListFilePaths;
            getFilesList(arrayList3.get(arrayList3.size() - 1));
        }
    }

    private void setCopyMoveOptinOn() {
        Constant.isFileFromSdCard = this.isFileFromSdCard;
        Constant.pastList = new ArrayList<>();
        for (int i = 0; i < this.storageList.size(); i++) {
            if (this.storageList.get(i).isSelected()) {
                File file = new File(this.storageList.get(i).getFilePath());
                if (file.exists()) {
                    this.pastList.add(file);
                    Constant.pastList.add(file);
                }
            }
        }
        setSelectionClose();
        Intent intent = new Intent(this, StorageActivity.class);
        intent.putExtra("type", "CopyMove");
        //safedk_FavouriteActivity_startActivity_3461e3a02d4572bd1fcf13589088aeb0(this, intent);
        SplashLaunchActivity.InterstitialAdsCall(this, intent);
    }

    
    public void sendFile() {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        for (int i = 0; i < this.storageList.size(); i++) {
            if (this.storageList.get(i).isSelected() && !this.storageList.get(i).isDir()) {
                arrayList.add(FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(this.storageList.get(i).getFilePath())));
            }
        }
        intent.setType("*/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.addFlags(268435456);
        safedk_FavouriteActivity_startActivity_3461e3a02d4572bd1fcf13589088aeb0(this, Intent.createChooser(intent, "Share with..."));
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (FavouriteActivity.this.isFileFromSdCard) {
                    FavouriteActivity.this.sdCardPermissionType = 1;
                    if (StorageUtils.checkFSDCardPermission(new File(FavouriteActivity.this.sdCardPath), FavouriteActivity.this) == 2) {
                        Toast.makeText(FavouriteActivity.this, "Please give a permission for manager operation", 0).show();
                    } else {
                        FavouriteActivity.this.setDeleteFile();
                    }
                } else {
                    FavouriteActivity.this.setDeleteFile();
                }
            }
        });
        builder.setNegativeButton((CharSequence) Html.fromHtml("<font color='#ffba00'>No</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    
    public void setDeleteFile() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Delete file...");
            this.loadingDialog.show();
        }
        new Thread(new Runnable() {
            public final void run() {
                FavouriteActivity.this.deleteFile();
            }
        }).start();
    }

    
    public void setSelectionClose() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        for (int i = 0; i < this.storageList.size(); i++) {
            this.storageList.get(i).setSelected(false);
            this.storageList.get(i).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
    }

    private void setClear() {
        this.storageList.clear();
        if (this.backUpstorageList != null) {
            for (int i = 0; i < this.backUpstorageList.size(); i++) {
                this.storageList.add(this.backUpstorageList.get(i));
            }
        }
        FavouriteAdapter favouriteAdapter = this.adapter;
        if (favouriteAdapter != null) {
            favouriteAdapter.notifyDataSetChanged();
        } else {
            setRecyclerViewData();
        }
        ArrayList<InternalStorageFilesModel> arrayList = this.storageList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
    }

    public void setMoreMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.ivMore);
        popupMenu.getMenuInflater().inflate(R.menu.storage_menu, popupMenu.getMenu());
        if (this.isShowHidden) {
            popupMenu.getMenu().findItem(R.id.menu_hidden).setTitle("Don't show hidden files");
        } else {
            popupMenu.getMenu().findItem(R.id.menu_hidden).setTitle("Show hidden files");
        }
        popupMenu.getMenu().findItem(R.id.menu_create_folder).setVisible(false);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_hidden) {
                    if (FavouriteActivity.this.isShowHidden) {
                        FavouriteActivity.this.isShowHidden = false;
                    } else {
                        FavouriteActivity.this.isShowHidden = true;
                    }
                    FavouriteActivity favouriteActivity = FavouriteActivity.this;
                    PreferencesManager.saveToShowHidden(favouriteActivity, favouriteActivity.isShowHidden);
                    FavouriteActivity.this.storageList.clear();
                    FavouriteActivity favouriteActivity2 = FavouriteActivity.this;
                    favouriteActivity2.getFilesList(favouriteActivity2.mainRootPath);
                } else if (itemId == R.id.menu_sort) {
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(FavouriteActivity.this);
                    bottomSheetFragment.show(FavouriteActivity.this.getSupportFragmentManager(), bottomSheetFragment.getTag());
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void setSortMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.ivMore);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_big_to_small:
                        if (!(FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0)) {
                            FavouriteActivity.this.sortSizeDescending();
                            FavouriteActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(FavouriteActivity.this, 3);
                            break;
                        }
                    case R.id.menu_name_ascending:
                        if (!(FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0)) {
                            FavouriteActivity.this.sortNameAscending();
                            FavouriteActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(FavouriteActivity.this, 1);
                            break;
                        }
                    case R.id.menu_name_descending:
                        if (!(FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0)) {
                            FavouriteActivity.this.sortNameDescending();
                            FavouriteActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(FavouriteActivity.this, 2);
                            break;
                        }
                    case R.id.menu_small_to_big:
                        if (!(FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0)) {
                            FavouriteActivity.this.sortSizeAscending();
                            FavouriteActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(FavouriteActivity.this, 4);
                            break;
                        }
                    case R.id.menu_time_newest:
                        if (!(FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0)) {
                            FavouriteActivity.this.setDateWiseSortAs(true);
                            FavouriteActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(FavouriteActivity.this, 5);
                            break;
                        }
                    case R.id.menu_time_oldest:
                        if (!(FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0)) {
                            FavouriteActivity.this.setDateWiseSortAs(false);
                            FavouriteActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(FavouriteActivity.this, 6);
                            break;
                        }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    
    public void sortNameAscending() {
        Collections.sort(this.storageList, new Comparator<InternalStorageFilesModel>() {
            public int compare(InternalStorageFilesModel internalStorageFilesModel, InternalStorageFilesModel internalStorageFilesModel2) {
                return new File(internalStorageFilesModel.getFilePath()).getName().compareToIgnoreCase(new File(internalStorageFilesModel2.getFilePath()).getName());
            }
        });
    }

    
    public void sortNameDescending() {
        Collections.sort(this.storageList, new Comparator<InternalStorageFilesModel>() {
            public int compare(InternalStorageFilesModel internalStorageFilesModel, InternalStorageFilesModel internalStorageFilesModel2) {
                return new File(internalStorageFilesModel2.getFilePath()).getName().compareToIgnoreCase(new File(internalStorageFilesModel.getFilePath()).getName());
            }
        });
    }

    
    public void sortSizeAscending() {
        Collections.sort(this.storageList, new Comparator<InternalStorageFilesModel>() {
            public int compare(InternalStorageFilesModel internalStorageFilesModel, InternalStorageFilesModel internalStorageFilesModel2) {
                return Long.valueOf(new File(internalStorageFilesModel.getFilePath()).length()).compareTo(Long.valueOf(new File(internalStorageFilesModel2.getFilePath()).length()));
            }
        });
    }

    
    public void sortSizeDescending() {
        Collections.sort(this.storageList, new Comparator<InternalStorageFilesModel>() {
            public int compare(InternalStorageFilesModel internalStorageFilesModel, InternalStorageFilesModel internalStorageFilesModel2) {
                return Long.valueOf(new File(internalStorageFilesModel2.getFilePath()).length()).compareTo(Long.valueOf(new File(internalStorageFilesModel.getFilePath()).length()));
            }
        });
    }

    
    public void setDateWiseSortAs(final boolean z) {
        Collections.sort(this.storageList, new Comparator<InternalStorageFilesModel>() {
            public int compare(InternalStorageFilesModel internalStorageFilesModel, InternalStorageFilesModel internalStorageFilesModel2) {
                Date date;
                File file = new File(internalStorageFilesModel.getFilePath());
                File file2 = new File(internalStorageFilesModel2.getFilePath());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                String format = simpleDateFormat.format(Long.valueOf(file.lastModified()));
                String format2 = simpleDateFormat.format(Long.valueOf(file2.lastModified()));
                Date date2 = null;
                try {
                    Date parse = simpleDateFormat.parse(format);
                    try {
                        date2 = simpleDateFormat.parse(format2);
                    } catch (ParseException unused) {
                    }
                    date = date2;
                    date2 = parse;
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = null;
                }
                if (!z) {
                    return date.compareTo(date2);
                }
                return date2.compareTo(date);
            }
        });
    }

    private void selectEvent(boolean z) {
        if (z) {
            for (int i = 0; i < this.storageList.size(); i++) {
                this.storageList.get(i).setSelected(true);
            }
            this.adapter.notifyDataSetChanged();
            setSelectedFile();
            return;
        }
        for (int i2 = 0; i2 < this.storageList.size(); i2++) {
            this.storageList.get(i2).setSelected(false);
            this.storageList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
    }

    public void getList() {
        new ArrayList();
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        this.arrayListFilePaths.add(this.mainRootPath);
        if (favouriteList != null) {
            for (int i = 0; i < favouriteList.size(); i++) {
                File file = new File(favouriteList.get(i));
                if (file.exists()) {
                    if (this.isShowHidden) {
                        InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                        internalStorageFilesModel.setFileName(file.getName());
                        internalStorageFilesModel.setFilePath(file.getPath());
                        internalStorageFilesModel.setFavorite(true);
                        if (file.isDirectory()) {
                            internalStorageFilesModel.setDir(true);
                        } else {
                            internalStorageFilesModel.setDir(false);
                        }
                        internalStorageFilesModel.setCheckboxVisible(false);
                        internalStorageFilesModel.setSelected(false);
                        internalStorageFilesModel.setMineType(Utils.getMimeTypeFromFilePath(file.getPath()));
                        this.storageList.add(internalStorageFilesModel);
                    } else if (!file.getName().startsWith(".")) {
                        InternalStorageFilesModel internalStorageFilesModel2 = new InternalStorageFilesModel();
                        internalStorageFilesModel2.setFileName(file.getName());
                        internalStorageFilesModel2.setFilePath(file.getPath());
                        internalStorageFilesModel2.setFavorite(true);
                        if (file.isDirectory()) {
                            internalStorageFilesModel2.setDir(true);
                        } else {
                            internalStorageFilesModel2.setDir(false);
                        }
                        internalStorageFilesModel2.setCheckboxVisible(false);
                        internalStorageFilesModel2.setSelected(false);
                        internalStorageFilesModel2.setMineType(Utils.getMimeTypeFromFilePath(file.getPath()));
                        this.storageList.add(internalStorageFilesModel2);
                    }
                }
            }
        }
        ArrayList<InternalStorageFilesModel> arrayList = this.storageList;
        if (!(arrayList == null || arrayList.size() == 0)) {
            int sortType = PreferencesManager.getSortType(this);
            if (sortType == 1) {
                sortNameAscending();
            } else if (sortType == 2) {
                sortNameDescending();
            } else if (sortType == 3) {
                sortSizeDescending();
            } else if (sortType == 4) {
                sortSizeAscending();
            } else if (sortType == 5) {
                setDateWiseSortAs(true);
            } else if (sortType == 6) {
                setDateWiseSortAs(false);
            } else {
                sortNameAscending();
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                FavouriteActivity.this.progressBar.setVisibility(8);
                if (FavouriteActivity.this.adapter != null) {
                    FavouriteActivity.this.adapter.notifyDataSetChanged();
                } else {
                    FavouriteActivity.this.setRecyclerViewData();
                }
                if (FavouriteActivity.this.pathAdapter != null) {
                    FavouriteActivity.this.pathAdapter.notifyDataSetChanged();
                    FavouriteActivity.this.setToPathPosition();
                } else {
                    FavouriteActivity.this.setHeaderData();
                }
                if (FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0) {
                    FavouriteActivity.this.recyclerView.setVisibility(8);
                    FavouriteActivity.this.llEmpty.setVisibility(0);
                } else {
                    FavouriteActivity.this.recyclerView.setVisibility(0);
                    FavouriteActivity.this.llEmpty.setVisibility(8);
                }

            }
        });
    }

    public void getFilesList(String str) {
        new ArrayList();
        this.mainRootPath = str;
        if (str.equalsIgnoreCase("Favourites")) {
            ArrayList<String> arrayList = this.arrayListFilePaths;
            if (arrayList != null) {
                arrayList.remove(this.mainRootPath);
            }
            getDataList();
            return;
        }
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (this.isShowHidden) {
                    InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                    internalStorageFilesModel.setFileName(file.getName());
                    internalStorageFilesModel.setFilePath(file.getPath());
                    if (file.isDirectory()) {
                        internalStorageFilesModel.setDir(true);
                    } else {
                        internalStorageFilesModel.setDir(false);
                    }
                    if (favouriteList.contains(file.getPath())) {
                        internalStorageFilesModel.setFavorite(true);
                    } else {
                        internalStorageFilesModel.setFavorite(false);
                    }
                    internalStorageFilesModel.setCheckboxVisible(false);
                    internalStorageFilesModel.setSelected(false);
                    internalStorageFilesModel.setMineType(Utils.getMimeTypeFromFilePath(file.getPath()));
                    this.storageList.add(internalStorageFilesModel);
                } else if (!file.getName().startsWith(".")) {
                    InternalStorageFilesModel internalStorageFilesModel2 = new InternalStorageFilesModel();
                    internalStorageFilesModel2.setFileName(file.getName());
                    internalStorageFilesModel2.setFilePath(file.getPath());
                    if (file.isDirectory()) {
                        internalStorageFilesModel2.setDir(true);
                    } else {
                        internalStorageFilesModel2.setDir(false);
                    }
                    if (favouriteList.contains(file.getPath())) {
                        internalStorageFilesModel2.setFavorite(true);
                    } else {
                        internalStorageFilesModel2.setFavorite(false);
                    }
                    internalStorageFilesModel2.setCheckboxVisible(false);
                    internalStorageFilesModel2.setSelected(false);
                    internalStorageFilesModel2.setMineType(Utils.getMimeTypeFromFilePath(file.getPath()));
                    this.storageList.add(internalStorageFilesModel2);
                }
            }
        }
        ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
        if (!(arrayList2 == null || arrayList2.size() == 0)) {
            int sortType = PreferencesManager.getSortType(this);
            if (sortType == 1) {
                sortNameAscending();
            } else if (sortType == 2) {
                sortNameDescending();
            } else if (sortType == 3) {
                sortSizeDescending();
            } else if (sortType == 4) {
                sortSizeAscending();
            } else if (sortType == 5) {
                setDateWiseSortAs(true);
            } else if (sortType == 6) {
                setDateWiseSortAs(false);
            } else {
                sortNameAscending();
            }
        }
        FavouriteAdapter favouriteAdapter = this.adapter;
        if (favouriteAdapter != null) {
            favouriteAdapter.notifyDataSetChanged();
        }
        FavouriteHeaderListAdapter favouriteHeaderListAdapter = this.pathAdapter;
        if (favouriteHeaderListAdapter != null) {
            favouriteHeaderListAdapter.notifyDataSetChanged();
            setToPathPosition();
        }
        ArrayList<InternalStorageFilesModel> arrayList3 = this.storageList;
        if (arrayList3 == null || arrayList3.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
    }

    
    public void setToPathPosition() {
        RecyclerView recyclerView2 = this.rvHeader;
        recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount() - 1);
    }

    
    public void openFile(File file, InternalStorageFilesModel internalStorageFilesModel) {
        Uri uri;
        String mineType = internalStorageFilesModel.getMineType();
        if (file.isDirectory()) {
            if (file.canRead()) {
                for (int i = 0; i < this.storageList.size(); i++) {
                    if (this.storageList.get(i) != null) {
                        this.storageList.get(i).setSelected(false);
                    }
                }
                this.adapter.notifyDataSetChanged();
                this.storageList.clear();
                this.arrayListFilePaths.add(internalStorageFilesModel.getFilePath());
                getFilesList(internalStorageFilesModel.getFilePath());
                return;
            }
            Toast.makeText(this, "Folder can't be read!", 0).show();
        } else if (mineType != null && (mineType.equalsIgnoreCase("image/jpeg") || mineType.equalsIgnoreCase("image/png") || mineType.equalsIgnoreCase("image/webp"))) {
            PhotoData photoData = new PhotoData();
            photoData.setFileName(internalStorageFilesModel.getFileName());
            photoData.setFilePath(internalStorageFilesModel.getFilePath());
            photoData.setFavorite(internalStorageFilesModel.isFavorite());
            Constant.displayImageList = new ArrayList();
            Constant.displayImageList.add(photoData);
            Intent intent = new Intent(this, DisplayImageActivity.class);
            intent.putExtra("pos", 0);
            Constant.arrayListFilePaths = this.arrayListFilePaths;
            safedk_FavouriteActivity_startActivityForResult_4b497934248c561fe71634bbfaf8bc4b(this, intent, videoImage_code);
        } else if (mineType != null && (mineType.equalsIgnoreCase("video/mp4") || mineType.equalsIgnoreCase("video/x-matroska"))) {
            PhotoData photoData2 = new PhotoData();
            photoData2.setFileName(internalStorageFilesModel.getFileName());
            photoData2.setFilePath(internalStorageFilesModel.getFilePath());
            Constant.displayVideoList = new ArrayList();
            Constant.displayVideoList.add(photoData2);
            Intent intent2 = new Intent(this, VideoPlayActivity.class);
            intent2.putExtra("pos", 0);
            Constant.arrayListFilePaths = this.arrayListFilePaths;
            safedk_FavouriteActivity_startActivityForResult_4b497934248c561fe71634bbfaf8bc4b(this, intent2, videoImage_code);
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
                safedk_FavouriteActivity_startActivity_3461e3a02d4572bd1fcf13589088aeb0(this, intent3);
            } catch (Exception unused) {
            }
        } else if (mineType == null || !mineType.equalsIgnoreCase("application/zip")) {
            Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent4 = new Intent();
            intent4.setAction("android.intent.action.VIEW");
            intent4.setDataAndType(uriForFile, Utils.getMimeTypeFromFilePath(file.getPath()));
            intent4.addFlags(1);
            safedk_FavouriteActivity_startActivity_3461e3a02d4572bd1fcf13589088aeb0(this, Intent.createChooser(intent4, "Open with"));
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
            safedk_FavouriteActivity_startActivityForResult_4b497934248c561fe71634bbfaf8bc4b(this, intent5, zip_open);
        }
    }

    
    public void setRecyclerViewData() {
        ArrayList<InternalStorageFilesModel> arrayList = this.storageList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
        if (this.isGrid) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen._6sdp), 0, getResources().getDimensionPixelSize(R.dimen._6sdp), 0);
            this.recyclerView.setLayoutParams(layoutParams);
            this.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            FavouriteAdapter favouriteAdapter = new FavouriteAdapter(this, this.storageList, this.isGrid);
            this.adapter = favouriteAdapter;
            this.recyclerView.setAdapter(favouriteAdapter);
        } else {
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams2.setMargins(0, 0, 0, 0);
            this.recyclerView.setLayoutParams(layoutParams2);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            FavouriteAdapter favouriteAdapter2 = new FavouriteAdapter(this, this.storageList, this.isGrid);
            this.adapter = favouriteAdapter2;
            this.recyclerView.setAdapter(favouriteAdapter2);
        }
        this.adapter.setOnItemClickListener(new FavouriteAdapter.ClickListener() {
            public void onItemClick(int i, View view) {
                if (FavouriteActivity.this.storageList.get(i).isCheckboxVisible()) {
                    if (FavouriteActivity.this.storageList.get(i).isSelected()) {
                        FavouriteActivity.this.storageList.get(i).setSelected(false);
                    } else {
                        FavouriteActivity.this.storageList.get(i).setSelected(true);
                    }
                    FavouriteActivity.this.adapter.notifyDataSetChanged();
                    FavouriteActivity.this.setSelectedFile();
                    return;
                }
                InternalStorageFilesModel internalStorageFilesModel = FavouriteActivity.this.storageList.get(i);
                FavouriteActivity.this.openFile(new File(internalStorageFilesModel.getFilePath()), internalStorageFilesModel);
            }
        });
        this.adapter.setOnLongClickListener(new FavouriteAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                FavouriteActivity.this.storageList.get(i).setSelected(true);
                for (int i2 = 0; i2 < FavouriteActivity.this.storageList.size(); i2++) {
                    if (FavouriteActivity.this.storageList.get(i2) != null) {
                        FavouriteActivity.this.storageList.get(i2).setCheckboxVisible(true);
                    }
                }
                FavouriteActivity.this.adapter.notifyDataSetChanged();
                FavouriteActivity.this.setSelectedFile();
            }
        });
    }

    
    public void setSelectedFile() {
        boolean z;
        boolean z2;
        String str;
        String mineType;
        this.isDir = false;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        boolean z3 = false;
        boolean z4 = false;
        for (int i2 = 0; i2 < this.storageList.size(); i2++) {
            if (this.storageList.get(i2).isSelected()) {
                this.pos = i2;
                i++;
                if (this.storageList.get(i2).isDir()) {
                    this.isDir = true;
                }
                if (this.storageList.get(i2).isFavorite()) {
                    arrayList.add(1);
                } else {
                    arrayList2.add(0);
                }
                if (!z4 && (mineType = this.storageList.get(i2).getMineType()) != null && mineType.equalsIgnoreCase("application/zip")) {
                    z4 = true;
                }
                if (!z3 && (str = this.sdCardPath) != null && !str.equalsIgnoreCase("") && this.storageList.get(i2).getFilePath().contains(this.sdCardPath)) {
                    z3 = true;
                }
            }
        }
        if (arrayList2.size() != 0 || arrayList.size() == 0) {
            z2 = arrayList2.size() != 0 && arrayList.size() == 0;
            z = false;
        } else {
            z2 = true;
            z = true;
        }
        this.isFileFromSdCard = z3;
        if (i == 0) {
            OnSelected(true, false, i);
            setSelectionClose();
        } else {
            OnSelected(false, true, i);
        }
        this.selected_Item = i;
        if (i != 1) {
            this.loutExtract.setVisibility(8);
            this.loutCompress.setVisibility(0);
        } else if (z4) {
            this.loutCompress.setVisibility(8);
            this.loutExtract.setVisibility(0);
        } else {
            this.loutExtract.setVisibility(8);
            this.loutCompress.setVisibility(0);
        }
        if (i == 0) {
            setInvisibleButton(this.loutSend, this.imgSend, this.txtTextSend);
            setInvisibleButton(this.loutMove, this.imgMove, this.txtTextMove);
            setInvisibleButton(this.loutDelete, this.imgDelete, this.txtTextDelete);
            setInvisibleButton(this.loutCopy, this.imgCopy, this.txtTextCopy);
            setInvisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
            setInvisibleButton(this.loutCompress, this.imgCompress, this.txtTextCompress);
            setInvisibleButton(this.loutExtract, this.imgExtract, this.txtTextExtract);
            this.ivFavUnfill.setVisibility(8);
            this.ivFavFill.setVisibility(8);
            this.llFavourite.setVisibility(8);
            return;
        }
        setVisibleButton(this.loutSend, this.imgSend, this.txtTextSend);
        setVisibleButton(this.loutMove, this.imgMove, this.txtTextMove);
        setVisibleButton(this.loutDelete, this.imgDelete, this.txtTextDelete);
        setVisibleButton(this.loutCopy, this.imgCopy, this.txtTextCopy);
        setVisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
        setVisibleButton(this.loutCompress, this.imgCompress, this.txtTextCompress);
        setVisibleButton(this.loutExtract, this.imgExtract, this.txtTextExtract);
        if (z2) {
            this.llFavourite.setVisibility(0);
            if (z) {
                this.ivFavFill.setVisibility(0);
                this.ivFavUnfill.setVisibility(8);
            } else {
                this.ivFavFill.setVisibility(8);
                this.ivFavUnfill.setVisibility(0);
            }
        } else {
            this.llFavourite.setVisibility(8);
        }
        if (i == 1) {
            setVisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
        } else if (this.isDir) {
            setInvisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
        } else {
            setVisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
        }
    }

    public void OnSelected(boolean z, boolean z2, int i) {
        if (z) {
            this.loutToolbar.setVisibility(0);
        } else {
            this.loutToolbar.setVisibility(8);
        }
        if (z2) {
            this.loutSelected.setVisibility(0);
        } else {
            this.loutSelected.setVisibility(8);
        }
        AppCompatTextView appCompatTextView = this.txtSelect;
        appCompatTextView.setText(i + " selected");
    }

    public void setHeaderData() {
        ArrayList<String> arrayList = this.arrayListFilePaths;
        if (arrayList != null && arrayList.size() != 0) {
            this.rvHeader.setLayoutManager(new LinearLayoutManager(this, 0, false));
            FavouriteHeaderListAdapter favouriteHeaderListAdapter = new FavouriteHeaderListAdapter(this, this.arrayListFilePaths);
            this.pathAdapter = favouriteHeaderListAdapter;
            this.rvHeader.setAdapter(favouriteHeaderListAdapter);
            this.pathAdapter.setOnItemClickListener(new FavouriteHeaderListAdapter.ClickListener() {
                public void onItemHeaderClick(int i, View view) {
                }

                public void onItemClick(int i, View view) {
                    Log.e("path seleted:", (String) FavouriteActivity.this.arrayListFilePaths.get(i));
                    Log.e("onItemClick", "position: " + i);
                    int size = FavouriteActivity.this.arrayListFilePaths.size();
                    while (true) {
                        size--;
                        if (size > i) {
                            Log.e("onItemClick", "remove index: " + size);
                            FavouriteActivity.this.arrayListFilePaths.remove(size);
                        } else {
                            FavouriteActivity.this.storageList.clear();
                            FavouriteActivity favouriteActivity = FavouriteActivity.this;
                            favouriteActivity.getFilesList((String) favouriteActivity.arrayListFilePaths.get(FavouriteActivity.this.arrayListFilePaths.size() - 1));
                            return;
                        }
                    }
                }

                public void onItemHomeClick(int i, View view) {
                    FavouriteActivity.this.finish();
                }
            });
        }
    }

    
    public void reNameFile(File file, String str) {
        boolean z;
        File file2 = new File(file.getParent() + InternalZipConstants.ZIP_FILE_SEPARATOR + str);
        StringBuilder sb = new StringBuilder();
        sb.append("file name: ");
        sb.append(file.getPath());
        Log.e("1", sb.toString());
        Log.e(ExifInterface.GPS_MEASUREMENT_2D, "file2 name: " + file2.getPath());
        if (file2.exists()) {
            Log.e("rename", "File already exists!");
            showRenameValidationDialog();
            return;
        }
        String str2 = this.sdCardPath;
        if (str2 == null || str2.equalsIgnoreCase("") || !file.getPath().contains(this.sdCardPath)) {
            z = file.renameTo(file2);
        } else {
            z = StorageUtils.renameFile(file, str, this);
        }
        if (z) {
            Log.e("LOG", "File renamed...");
            MediaScannerConnection.scanFile(this, new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
            String filePath = this.storageList.get(this.pos).getFilePath();
            this.storageList.get(this.pos).setFilePath(file2.getPath());
            this.storageList.get(this.pos).setFileName(file2.getName());
            this.adapter.notifyItemChanged(this.pos);
            new ArrayList();
            ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
            if (favouriteList == null) {
                favouriteList = new ArrayList<>();
            }
            if (favouriteList.contains(filePath)) {
                favouriteList.remove(filePath);
                favouriteList.add(file2.getPath());
                PreferencesManager.setFavouriteList(this, favouriteList);
            }
            RxBus.getInstance().post(new RenameEvent(file, file2));
            Toast.makeText(this, "Rename file successfully", 0).show();
            return;
        }
        Log.e("LOG", "File not renamed...");
    }

    public void deleteFile() {
        if (this.storageList != null) {
            for (int i = 0; i < this.storageList.size(); i++) {
                if (this.storageList.get(i).isSelected()) {
                    File file = new File(this.storageList.get(i).getFilePath());
                    if (StorageUtils.deleteFile(file, this)) {
                        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                    }
                }
            }
        }
        if (this.storageList != null) {
            int i2 = 0;
            while (i2 < this.storageList.size()) {
                this.storageList.get(i2).setCheckboxVisible(false);
                if (this.storageList.get(i2).isSelected()) {
                    this.storageList.remove(i2);
                    if (i2 != 0) {
                        i2--;
                    }
                }
                i2++;
            }
            try {
                if (this.storageList.size() != 1 && 1 < this.storageList.size() && this.storageList.get(1).isSelected()) {
                    this.storageList.remove(1);
                }
                if (this.storageList.size() != 0 && this.storageList.get(0).isSelected()) {
                    this.storageList.remove(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                FavouriteActivity.this.OnSelected(true, false, 0);
                FavouriteActivity.this.llBottomOption.setVisibility(8);
                if (FavouriteActivity.this.adapter != null) {
                    FavouriteActivity.this.adapter.notifyDataSetChanged();
                }
                if (FavouriteActivity.this.loadingDialog != null && FavouriteActivity.this.loadingDialog.isShowing()) {
                    FavouriteActivity.this.loadingDialog.dismiss();
                }
                if (FavouriteActivity.this.storageList == null || FavouriteActivity.this.storageList.size() == 0) {
                    FavouriteActivity.this.recyclerView.setVisibility(8);
                    FavouriteActivity.this.llEmpty.setVisibility(0);
                } else {
                    FavouriteActivity.this.recyclerView.setVisibility(0);
                    FavouriteActivity.this.llEmpty.setVisibility(8);
                }
                Toast.makeText(FavouriteActivity.this, "Delete file successfully", 0).show();
            }
        });
    }

    private File folderFile(String str, String str2) {
        File file = new File(str2 + InternalZipConstants.ZIP_FILE_SEPARATOR + str + "(" + this.folder_counter + ")");
        if (!file.exists()) {
            return file;
        }
        this.folder_counter++;
        return folderFile(str, str2);
    }

    private void displayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DisplayDeleteEvent>() {
            public void call(DisplayDeleteEvent displayDeleteEvent) {
                if (displayDeleteEvent.getDeleteList() != null && displayDeleteEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    FavouriteActivity.this.updateDeleteImageData(displayDeleteEvent.getDeleteList());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void updateDeleteImageData(ArrayList<String> arrayList) {
        ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 < this.storageList.size()) {
                        if (this.storageList.get(i2).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                            break;
                        }
                        i2++;
                    }
                }
                this.storageList.remove(i2);
                try {
                    if (this.storageList.size() != 1 && 1 < this.storageList.size() && this.storageList.get(1).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        this.storageList.remove(1);
                    }
                    if (this.storageList.size() != 0 && this.storageList.get(0).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        this.storageList.remove(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FavouriteAdapter favouriteAdapter = this.adapter;
            if (favouriteAdapter != null) {
                favouriteAdapter.notifyDataSetChanged();
            }
            ArrayList<InternalStorageFilesModel> arrayList3 = this.storageList;
            if (arrayList3 == null || arrayList3.size() == 0) {
                this.recyclerView.setVisibility(8);
                this.llEmpty.setVisibility(0);
                return;
            }
            this.recyclerView.setVisibility(0);
            this.llEmpty.setVisibility(8);
        }
    }

    private void showRenameValidationDialog() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_rename_same_name_validation);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(17);
        ((LinearLayout) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onBottomClick(int i) {
        switch (i) {
            case 1:
                ArrayList<InternalStorageFilesModel> arrayList = this.storageList;
                if (arrayList != null && arrayList.size() != 0) {
                    sortNameAscending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 1);
                    return;
                }
                return;
            case 2:
                ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
                if (arrayList2 != null && arrayList2.size() != 0) {
                    sortNameDescending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 2);
                    return;
                }
                return;
            case 3:
                ArrayList<InternalStorageFilesModel> arrayList3 = this.storageList;
                if (arrayList3 != null && arrayList3.size() != 0) {
                    sortSizeDescending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 3);
                    return;
                }
                return;
            case 4:
                ArrayList<InternalStorageFilesModel> arrayList4 = this.storageList;
                if (arrayList4 != null && arrayList4.size() != 0) {
                    sortSizeAscending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 4);
                    return;
                }
                return;
            case 5:
                ArrayList<InternalStorageFilesModel> arrayList5 = this.storageList;
                if (arrayList5 != null && arrayList5.size() != 0) {
                    setDateWiseSortAs(true);
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 5);
                    return;
                }
                return;
            case 6:
                ArrayList<InternalStorageFilesModel> arrayList6 = this.storageList;
                if (arrayList6 != null && arrayList6.size() != 0) {
                    setDateWiseSortAs(false);
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 6);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void showExtractDialog() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_compress);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(17);
        final AppCompatEditText appCompatEditText = (AppCompatEditText) dialog.findViewById(R.id.edt_file_name);
        ((AppCompatTextView) dialog.findViewById(R.id.txt_title)).setText("Extract file");
        appCompatEditText.setHint("Enter extract file name");
        ((LinearLayout) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!appCompatEditText.getText().toString().isEmpty()) {
                    String obj = appCompatEditText.getText().toString();
                    if (new File(FavouriteActivity.this.extractPath + InternalZipConstants.ZIP_FILE_SEPARATOR + obj).exists()) {
                        Toast.makeText(FavouriteActivity.this, "File name already use", 0).show();
                        return;
                    }
                    FavouriteActivity.this.extract_file_name = obj;
                    dialog.dismiss();
                    if (FavouriteActivity.this.isFileFromSdCard) {
                        FavouriteActivity.this.sdCardPermissionType = 4;
                        if (StorageUtils.checkFSDCardPermission(new File(FavouriteActivity.this.sdCardPath), FavouriteActivity.this) == 2) {
                            Toast.makeText(FavouriteActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            FavouriteActivity.this.setExtract();
                        }
                    } else {
                        FavouriteActivity.this.setExtract();
                    }
                } else {
                    FavouriteActivity favouriteActivity = FavouriteActivity.this;
                    Toast.makeText(favouriteActivity, favouriteActivity.getResources().getString(R.string.extract_validation), 0).show();
                }
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showCompressDialog() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_compress);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(17);
        final AppCompatEditText appCompatEditText = (AppCompatEditText) dialog.findViewById(R.id.edt_file_name);
        ((LinearLayout) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!appCompatEditText.getText().toString().isEmpty()) {
                    String str = appCompatEditText.getText().toString().split("\\.")[0];
                    if (new File(FavouriteActivity.this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(FavouriteActivity.this, "File name already use", 0).show();
                        return;
                    }
                    FavouriteActivity.this.zip_file_name = str;
                    dialog.dismiss();
                    if (FavouriteActivity.this.isFileFromSdCard) {
                        FavouriteActivity.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(FavouriteActivity.this.sdCardPath), FavouriteActivity.this) == 2) {
                            Toast.makeText(FavouriteActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            FavouriteActivity.this.setcompress();
                        }
                    } else {
                        FavouriteActivity.this.setcompress();
                    }
                } else {
                    FavouriteActivity favouriteActivity = FavouriteActivity.this;
                    Toast.makeText(favouriteActivity, favouriteActivity.getResources().getString(R.string.zip_validation), 0).show();
                }
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    
    public void setExtract() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Extract file...");
            this.loadingDialog.show();
        }
    }

    public void extractfile() {
        File file = new File(this.extractPath + InternalZipConstants.ZIP_FILE_SEPARATOR + this.extract_file_name);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(this.storageList.get(this.pos).getFilePath());
        new ZipArchive();
        ZipArchive.unzip(file2.getPath(), file.getPath(), "");
        final String path = file.getPath();
        runOnUiThread(new Runnable() {
            public void run() {
                FavouriteActivity.this.setSelectionClose();
                if (path != null) {
                    FavouriteActivity.this.storageList.clear();
                    FavouriteActivity favouriteActivity = FavouriteActivity.this;
                    favouriteActivity.getFilesList(favouriteActivity.mainRootPath);
                    if (FavouriteActivity.this.loadingDialog.isShowing()) {
                        FavouriteActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(FavouriteActivity.this, "Extract file successfully", 0).show();
                    MediaScannerConnection.scanFile(FavouriteActivity.this, new String[]{path}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                }
            }
        });
    }

    
    public void setcompress() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Compress file...");
            this.loadingDialog.show();
        }
    }

    public void compressfile() throws IOException {
        final File file;
        final File file2;
        final String str;
        String str2 = this.zip_file_name;
        if (this.selected_Item == 1) {
            file2 = new File(this.storageList.get(this.pos).getFilePath());
            if (!file2.exists()) {
                file2.mkdir();
            }
            file = null;
        } else {
            File file3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
            if (!file3.exists()) {
                file3.mkdirs();
            }
            file = new File(file3.getPath() + "/.ZIP");
            if (!file.exists()) {
                file.mkdirs();
            }
            file2 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + str2);
            if (!file2.exists()) {
                file2.mkdir();
            }
            for (int i = 0; i < this.storageList.size(); i++) {
                if (this.storageList.get(i) != null) {
                    InternalStorageFilesModel internalStorageFilesModel = this.storageList.get(i);
                    if (internalStorageFilesModel.isSelected()) {
                        File file4 = new File(internalStorageFilesModel.getFilePath());
                        StorageUtils.copyFile(file4, new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file4.getName()), this);
                    }
                }
            }
        }
        if (this.selected_Item == 1) {
            str = this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str2 + ".zip";
        } else {
            str = this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + file2.getName() + ".zip";
        }
        ZipArchive.zip(file2.getPath(), str, "");
        runOnUiThread(new Runnable() {
            public void run() {
                FavouriteActivity.this.setSelectionClose();
                if (str != null) {
                    MediaScannerConnection.scanFile(FavouriteActivity.this, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str));
                    if (FavouriteActivity.this.selected_Item != 1) {
                        boolean deleteFile = StorageUtils.deleteFile(file2, FavouriteActivity.this);
                        if (file != null && StorageUtils.deleteFile(file, FavouriteActivity.this)) {
                            MediaScannerConnection.scanFile(FavouriteActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                        if (deleteFile) {
                            MediaScannerConnection.scanFile(FavouriteActivity.this, new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                    }
                    FavouriteActivity.this.storageList.clear();
                    FavouriteActivity favouriteActivity = FavouriteActivity.this;
                    favouriteActivity.getFilesList(favouriteActivity.mainRootPath);
                    if (FavouriteActivity.this.loadingDialog.isShowing()) {
                        FavouriteActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(FavouriteActivity.this, "Compress file successfully", 0).show();
                }
            }
        });
    }
}
