package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.adapter.ApkAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.ApkModel;
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
import com.safedk.android.analytics.events.RedirectEvent;
import com.safedk.android.utils.Logger;*/
import butterknife.OnClick;
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import net.lingala.zip4j.util.InternalZipConstants;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ApkActivity extends AppCompatActivity implements BottomListner {
    private static final String TAG = "ApkActivity";

    ApkAdapter adapter;
    ArrayList<ApkModel> apkList = new ArrayList<>();
    String compressPath;
    String extractPath;
    @BindView(R.id.img_compress)
    ImageView imgCompress;
    @BindView(R.id.img_copy)
    ImageView imgCopy;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.img_move)
    ImageView imgMove;
    @BindView(R.id.img_send)
    ImageView imgSend;
    boolean isCheckAll = false;
    boolean isFileFromSdCard = false;
    @BindView(R.id.iv_check_all)
    ImageView ivCheckAll;
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
    int pos = 0;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    String rootPath;
    String sdCardPath;
    int sdCardPermissionType = 0;
    int selected_Item = 0;
    @BindView(R.id.txt_header_title)
    TextView txtHeaderTitle;
    @BindView(R.id.txt_select)
    AppCompatTextView txtSelect;
    @BindView(R.id.txt_text_compress)
    TextView txtTextCompress;
    @BindView(R.id.txt_text_copy)
    TextView txtTextCopy;
    @BindView(R.id.txt_text_delete)
    TextView txtTextDelete;
    @BindView(R.id.txt_text_more)
    TextView txtTextMore;
    @BindView(R.id.txt_text_move)
    TextView txtTextMove;
    @BindView(R.id.txt_text_send)
    TextView txtTextSend;
    String zip_file_name;

    public static void safedk_ApkActivity_startActivity_f8c4ea6be16c8d7a9019293e11c1e4a8(ApkActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_apk);
        ButterKnife.bind((Activity) this);

        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        intView();
        copyMoveEvent();

    }

    public void intView() {
        this.progressBar.setVisibility(0);
        new Thread(new Runnable() {
            public final void run() {
                ApkActivity.this.getApkData();
            }
        }).start();
        this.ivUncheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
        this.ivCheckAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        this.ivFavFill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_fill));
        this.ivFavUnfill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_unfill));
        this.imgMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_bottom));
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
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
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);
    }

    public void onBackPressed() {
        if (this.loutSelected.getVisibility() == 0) {
            setSelectionClose();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_close, R.id.iv_more, R.id.ll_check_all, R.id.ll_favourite, R.id.lout_compress, R.id.lout_copy, R.id.lout_delete, R.id.lout_more, R.id.lout_move, R.id.lout_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back /*2131296682*/:
                onBackPressed();
                return;
            case R.id.iv_close /*2131296689*/:
                setSelectionClose();
                return;
            case R.id.iv_more /*2131296703*/:
                setMoreMenu();
                return;
            case R.id.ll_check_all /*2131296746*/:
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
            case R.id.ll_favourite /*2131296750*/:
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
            case R.id.lout_compress /*2131296764*/:
                showCompressDialog();
                return;
            case R.id.lout_copy /*2131296765*/:
                Constant.isCopyData = true;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_delete /*2131296766*/:
                showDeleteDialog();
                return;
            case R.id.lout_more /*2131296774*/:
                showMoreOptionBottom();
                return;
            case R.id.lout_move /*2131296776*/:
                Constant.isCopyData = false;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_send /*2131296787*/:
                sendFile();
                return;
            default:
                return;
        }
    }

    public void setMoreMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.ivMore);
        popupMenu.getMenuInflater().inflate(R.menu.storage_menu, popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.menu_hidden).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menu_create_folder).setVisible(false);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() != R.id.menu_sort) {
                    return false;
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(ApkActivity.this);
                bottomSheetFragment.show(ApkActivity.this.getSupportFragmentManager(), bottomSheetFragment.getTag());
                return false;
            }
        });
        popupMenu.show();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 300) {
            String sDCardTreeUri = PreferencesManager.getSDCardTreeUri(this);
            Uri uri = null;
            Uri parse = sDCardTreeUri != null ? Uri.parse(sDCardTreeUri) : null;
            if (i2 == -1 && (uri = intent.getData()) != null) {
                PreferencesManager.setSDCardTreeUri(this, uri.toString());
                int i3 = this.sdCardPermissionType;
                if (i3 == 1) {
                    setDeleteFile();
                } else if (i3 == 3) {
                    setcompress();
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
                } else if (i4 == 3) {
                    setcompress();
                }
            }
        }
    }

    
    public void setSelectionClose() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        for (int i = 0; i < this.apkList.size(); i++) {
            this.apkList.get(i).setSelected(false);
            this.apkList.get(i).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
    }

    private void selectEvent(boolean z) {
        if (z) {
            for (int i = 0; i < this.apkList.size(); i++) {
                this.apkList.get(i).setSelected(true);
            }
            this.adapter.notifyDataSetChanged();
            setSelectedFile();
            return;
        }
        for (int i2 = 0; i2 < this.apkList.size(); i2++) {
            this.apkList.get(i2).setSelected(false);
            this.apkList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
    }

    private void setUnFavourite() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        int i = 0;
        for (int i2 = 0; i2 < this.apkList.size(); i2++) {
            if (this.apkList.get(i2).isSelected() && this.apkList.get(i2).isFavorite()) {
                this.apkList.get(i2).setFavorite(false);
                i++;
                if (favouriteList.contains(this.apkList.get(i2).getPath())) {
                    favouriteList.remove(this.apkList.get(i2).getPath());
                }
            }
            this.apkList.get(i2).setSelected(false);
            this.apkList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
        String str = i == 1 ? " item removed from Favourites." : " items removed from Favourites.";
        Toast.makeText(this, i + str, 0).show();
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
        for (int i2 = 0; i2 < this.apkList.size(); i2++) {
            if (this.apkList.get(i2).isSelected()) {
                if (!this.apkList.get(i2).isFavorite()) {
                    favouriteList.add(0, this.apkList.get(i2).getPath());
                    i++;
                }
                this.apkList.get(i2).setFavorite(true);
            }
            this.apkList.get(i2).setSelected(false);
            this.apkList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
        String str = i == 1 ? " item added to Favourites." : " items added to Favourites.";
        Toast.makeText(this, i + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
    }

    private void setCopyMoveOptinOn() {
        Constant.isFileFromSdCard = this.isFileFromSdCard;
        Constant.pastList = new ArrayList<>();
        for (int i = 0; i < this.apkList.size(); i++) {
            if (this.apkList.get(i).isSelected()) {
                File file = new File(this.apkList.get(i).getPath());
                if (file.exists()) {
                    Constant.pastList.add(file);
                }
            }
        }
        setSelectionClose();
        Intent intent = new Intent(this, StorageActivity.class);
        intent.putExtra("type", "CopyMove");
        safedk_ApkActivity_startActivity_f8c4ea6be16c8d7a9019293e11c1e4a8(this, intent);
    }

    
    public void sendFile() {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        for (int i = 0; i < this.apkList.size(); i++) {
            if (this.apkList.get(i).isSelected()) {
                arrayList.add(FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(this.apkList.get(i).getPath())));
            }
        }
        intent.setType("*/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.addFlags(268435456);
        safedk_ApkActivity_startActivity_f8c4ea6be16c8d7a9019293e11c1e4a8(this, Intent.createChooser(intent, "Share with..."));
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (ApkActivity.this.isFileFromSdCard) {
                    ApkActivity.this.sdCardPermissionType = 1;
                    if (StorageUtils.checkFSDCardPermission(new File(ApkActivity.this.sdCardPath), ApkActivity.this) == 2) {
                        Toast.makeText(ApkActivity.this, "Please give a permission for manager operation", 0).show();
                    } else {
                        ApkActivity.this.setDeleteFile();
                    }
                } else {
                    ApkActivity.this.setDeleteFile();
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
                ApkActivity.this.deleteFile();
            }
        }).start();
    }

    public void deleteFile() {
        if (this.apkList != null) {
            for (int i = 0; i < this.apkList.size(); i++) {
                if (this.apkList.get(i).isSelected()) {
                    File file = new File(this.apkList.get(i).getPath());
                    if (StorageUtils.deleteFile(file, this)) {
                        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                    }
                }
            }
        }
        if (this.apkList != null) {
            int i2 = 0;
            while (i2 < this.apkList.size()) {
                this.apkList.get(i2).setCheckboxVisible(false);
                if (this.apkList.get(i2).isSelected()) {
                    this.apkList.remove(i2);
                    if (i2 != 0) {
                        i2--;
                    }
                }
                i2++;
            }
            try {
                if (this.apkList.size() != 1 && 1 < this.apkList.size() && this.apkList.get(1).isSelected()) {
                    this.apkList.remove(1);
                }
                if (this.apkList.size() != 0 && this.apkList.get(0).isSelected()) {
                    this.apkList.remove(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                ApkActivity.this.OnSelected(true, false, 0);
                ApkActivity.this.llBottomOption.setVisibility(8);
                if (ApkActivity.this.adapter != null) {
                    ApkActivity.this.adapter.notifyDataSetChanged();
                }
                if (ApkActivity.this.loadingDialog != null && ApkActivity.this.loadingDialog.isShowing()) {
                    ApkActivity.this.loadingDialog.dismiss();
                }
                if (ApkActivity.this.apkList == null || ApkActivity.this.apkList.size() == 0) {
                    ApkActivity.this.recyclerView.setVisibility(8);
                    ApkActivity.this.llEmpty.setVisibility(0);
                } else {
                    ApkActivity.this.recyclerView.setVisibility(0);
                    ApkActivity.this.llEmpty.setVisibility(8);
                }
                Toast.makeText(ApkActivity.this, "Delete file successfully", 0).show();
            }
        });
    }

    public void setAdapter() {
        this.progressBar.setVisibility(8);
        ArrayList<ApkModel> arrayList = this.apkList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ApkAdapter apkAdapter = new ApkAdapter(this, this.apkList);
        this.adapter = apkAdapter;
        this.recyclerView.setAdapter(apkAdapter);
        this.adapter.setOnItemClickListener(new ApkAdapter.ClickListener() {
            public void safedk_ApkActivity_startActivity_f8c4ea6be16c8d7a9019293e11c1e4a8(ApkActivity p0, Intent p1) {
                if (p1 != null) {
                    p0.startActivity(p1);
                }
            }

            public void onItemClick(int i, View view) {
                Uri uri;
                if (ApkActivity.this.apkList.get(i).isCheckboxVisible()) {
                    if (ApkActivity.this.apkList.get(i).isSelected()) {
                        ApkActivity.this.apkList.get(i).setSelected(false);
                    } else {
                        ApkActivity.this.apkList.get(i).setSelected(true);
                    }
                    ApkActivity.this.adapter.notifyDataSetChanged();
                    ApkActivity.this.setSelectedFile();
                    return;
                }
                try {
                    File file = new File(ApkActivity.this.apkList.get(i).getPath());
                    ApkActivity apkActivity = ApkActivity.this;
                    FileProvider.getUriForFile(apkActivity, ApkActivity.this.getPackageName() + ".provider", file);
                    Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
                    String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(ApkActivity.this.apkList.get(i).getPath());
                    if (Build.VERSION.SDK_INT >= 24) {
                        ApkActivity apkActivity2 = ApkActivity.this;
                        uri = FileProvider.getUriForFile(apkActivity2, ApkActivity.this.getApplicationContext().getPackageName() + ".provider", new File(ApkActivity.this.apkList.get(i).getPath()));
                    } else {
                        uri = Uri.fromFile(new File(ApkActivity.this.apkList.get(i).getPath()));
                    }
                    intent.setFlags(1);
                    intent.setDataAndType(uri, mimeTypeFromFilePath);
                    safedk_ApkActivity_startActivity_f8c4ea6be16c8d7a9019293e11c1e4a8(ApkActivity.this, intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.adapter.setOnLongClickListener(new ApkAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                ApkActivity.this.apkList.get(i).setSelected(true);
                for (int i2 = 0; i2 < ApkActivity.this.apkList.size(); i2++) {
                    if (ApkActivity.this.apkList.get(i2) != null) {
                        ApkActivity.this.apkList.get(i2).setCheckboxVisible(true);
                    }
                }
                ApkActivity.this.adapter.notifyDataSetChanged();
                ApkActivity.this.setSelectedFile();
            }
        });
    }

    
    public void setSelectedFile() {
        boolean z;
        boolean z2;
        String str;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        boolean z3 = false;
        for (int i2 = 0; i2 < this.apkList.size(); i2++) {
            if (this.apkList.get(i2).isSelected()) {
                i++;
                this.pos = i2;
                if (this.apkList.get(i2).isFavorite()) {
                    arrayList.add(1);
                } else {
                    arrayList2.add(0);
                }
                if (!z3 && (str = this.sdCardPath) != null && !str.equalsIgnoreCase("") && this.apkList.get(i2).getPath().contains(this.sdCardPath)) {
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
        if (i == 0) {
            setInvisibleButton(this.loutSend, this.imgSend, this.txtTextSend);
            setInvisibleButton(this.loutMove, this.imgMove, this.txtTextMove);
            setInvisibleButton(this.loutDelete, this.imgDelete, this.txtTextDelete);
            setInvisibleButton(this.loutCopy, this.imgCopy, this.txtTextCopy);
            setInvisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
            setInvisibleButton(this.loutCompress, this.imgCompress, this.txtTextCompress);
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
        if (z2) {
            this.llFavourite.setVisibility(0);
            if (z) {
                this.ivFavFill.setVisibility(0);
                this.ivFavUnfill.setVisibility(8);
                return;
            }
            this.ivFavFill.setVisibility(8);
            this.ivFavUnfill.setVisibility(0);
            return;
        }
        this.llFavourite.setVisibility(8);
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

    public void OnSelected(boolean z, boolean z2, int i) {
        if (z) {
            this.loutToolbar.setVisibility(0);
        } else {
            this.loutToolbar.setVisibility(8);
        }
        if (z2) {
            this.loutSelected.setVisibility(0);
            this.llBottomOption.setVisibility(0);
        } else {
            this.loutSelected.setVisibility(8);
            this.llBottomOption.setVisibility(8);
        }
        AppCompatTextView appCompatTextView = this.txtSelect;
        appCompatTextView.setText(i + " selected");
    }

    public void getApkData() {
        Log.i(TAG, "getApkData: ");
        this.apkList = getApkList();
        Log.i(TAG, "getApkData: apkList:" + this.apkList.size());
        runOnUiThread(new Runnable() {
            public void run() {
                if (!(ApkActivity.this.apkList == null || ApkActivity.this.apkList.size() == 0)) {
                    int sortType = PreferencesManager.getSortType(ApkActivity.this);
                    if (sortType == 1) {
                        ApkActivity.this.sortNameAscending();
                    } else if (sortType == 2) {
                        ApkActivity.this.sortNameDescending();
                    } else if (sortType == 3) {
                        ApkActivity.this.sortSizeDescending();
                    } else if (sortType == 4) {
                        ApkActivity.this.sortSizeAscending();
                    } else if (sortType == 5) {
                        ApkActivity.this.setDateWiseSortAs(true);
                    } else if (sortType == 6) {
                        ApkActivity.this.setDateWiseSortAs(false);
                    } else {
                        ApkActivity.this.sortNameAscending();
                    }
                }
                ApkActivity.this.setAdapter();
            }
        });
    }

    public ArrayList<ApkModel> getApkList() {
        Log.i(TAG, "getApkList: ");
        ArrayList<ApkModel> arrayList = new ArrayList<>();
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        Cursor query = getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        if (query != null && query.getCount() > 0 && query.moveToFirst()) {
            Log.i(TAG, "getApkList: first if");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            query.moveToFirst();
            while (!query.isAfterLast()) {
                String string = query.getString(query.getColumnIndex("_data"));
                Log.i(TAG, "getApkList: while:" + string);
                if (string != null && string.endsWith(".apk")) {
                    Log.i(TAG, "getApkList: second if");
                    File file = new File(string);
                    if (!file.getName().startsWith(".") && file.length() != 0) {
                        Log.i(TAG, "getApkList: third if");
                        ApkModel apkModel = new ApkModel();
                        apkModel.setApkName(file.getName());
                        apkModel.setPath(file.getPath());
                        apkModel.setSelected(false);
                        apkModel.setSize(file.length());
                        try {
                            apkModel.setDate(simpleDateFormat.parse(simpleDateFormat.format(Long.valueOf(file.lastModified()))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        apkModel.setCheckboxVisible(false);
                        if (favouriteList.contains(file.getPath())) {
                            apkModel.setFavorite(true);
                        } else {
                            apkModel.setFavorite(false);
                        }
                        arrayList.add(apkModel);
                    }
                }
                query.moveToNext();
            }
        }
        query.close();
        return arrayList;
    }

    private void copyMoveEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CopyMoveEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<CopyMoveEvent>() {
            public void call(CopyMoveEvent copyMoveEvent) {
                if (!(copyMoveEvent.getCopyMoveList() == null || copyMoveEvent.getCopyMoveList().size() == 0 || copyMoveEvent.getType() == 3)) {
                    new ArrayList();
                    ArrayList<File> copyMoveList = copyMoveEvent.getCopyMoveList();
                    if (copyMoveList == null) {
                        copyMoveList = new ArrayList<>();
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                    for (int i = 0; i < copyMoveList.size(); i++) {
                        File file = copyMoveList.get(i);
                        PackageManager packageManager = ApkActivity.this.getPackageManager();
                        if (!Formatter.formatShortFileSize(ApkActivity.this, file.length()).equalsIgnoreCase("0 B")) {
                            ApkModel apkModel = new ApkModel();
                            apkModel.setApkName(file.getName());
                            apkModel.setPath(file.getPath());
                            apkModel.setSelected(false);
                            apkModel.setCheckboxVisible(false);
                            apkModel.setSize(file.length());
                            try {
                                apkModel.setDate(simpleDateFormat.parse(simpleDateFormat.format(Long.valueOf(file.lastModified()))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            file.getName();
                            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(file.getPath(), 0);
                            apkModel.setPackageName(packageArchiveInfo.packageName);
                            try {
                                apkModel.setAppIcon(ApkActivity.this.getPackageManager().getApplicationIcon(packageArchiveInfo.packageName));
                            } catch (PackageManager.NameNotFoundException e2) {
                                e2.printStackTrace();
                                apkModel.setAppIcon(packageArchiveInfo.applicationInfo.loadIcon(packageManager));
                            }
                            ApkActivity.this.apkList.add(0, apkModel);
                            ApkActivity.this.adapter.notifyDataSetChanged();
                        }
                    }
                    if (!(ApkActivity.this.apkList == null || ApkActivity.this.apkList.size() == 0)) {
                        int sortType = PreferencesManager.getSortType(ApkActivity.this);
                        if (sortType == 1) {
                            ApkActivity.this.sortNameAscending();
                        } else if (sortType == 2) {
                            ApkActivity.this.sortNameDescending();
                        } else if (sortType == 3) {
                            ApkActivity.this.sortSizeDescending();
                        } else if (sortType == 4) {
                            ApkActivity.this.sortSizeAscending();
                        } else if (sortType == 5) {
                            ApkActivity.this.setDateWiseSortAs(true);
                        } else if (sortType == 6) {
                            ApkActivity.this.setDateWiseSortAs(false);
                        } else {
                            ApkActivity.this.sortNameAscending();
                        }
                    }
                    if (ApkActivity.this.adapter != null) {
                        ApkActivity.this.adapter.notifyDataSetChanged();
                    } else {
                        ApkActivity.this.setAdapter();
                    }
                    if (ApkActivity.this.apkList == null || ApkActivity.this.apkList.size() == 0) {
                        ApkActivity.this.recyclerView.setVisibility(8);
                        ApkActivity.this.llEmpty.setVisibility(0);
                    } else {
                        ApkActivity.this.recyclerView.setVisibility(0);
                        ApkActivity.this.llEmpty.setVisibility(8);
                    }
                }
                if (copyMoveEvent.getDeleteList() != null && copyMoveEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    ArrayList<String> deleteList = copyMoveEvent.getDeleteList();
                    if (deleteList == null) {
                        deleteList = new ArrayList<>();
                    }
                    ApkActivity.this.updateDeleteData(deleteList);
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void updateDeleteData(ArrayList<String> arrayList) {
        ArrayList<ApkModel> arrayList2 = this.apkList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= this.apkList.size()) {
                        break;
                    } else if (this.apkList.get(i2).getPath().equalsIgnoreCase(arrayList.get(i))) {
                        this.apkList.remove(i2);
                        break;
                    } else {
                        i2++;
                    }
                }
            }
            ApkAdapter apkAdapter = this.adapter;
            if (apkAdapter != null) {
                apkAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }
            ArrayList<ApkModel> arrayList3 = this.apkList;
            if (arrayList3 == null || arrayList3.size() == 0) {
                this.recyclerView.setVisibility(8);
                this.llEmpty.setVisibility(0);
                return;
            }
            this.recyclerView.setVisibility(0);
            this.llEmpty.setVisibility(8);
        }
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_details) {
                    ApkActivity.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    ApkActivity.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    ApkActivity.this.sendFile();
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    
    public void showDetailDialog() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(80);
        File file = new File(this.apkList.get(this.pos).getPath());
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
        final File file = new File(this.apkList.get(this.pos).getPath());
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
                    Toast.makeText(ApkActivity.this, "New name can't be empty.", 0).show();
                } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                    dialog.show();
                } else if (!file.isDirectory()) {
                    String[] split = appCompatEditText.getText().toString().split("\\.");
                    if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                        final Dialog dialog = new Dialog(ApkActivity.this, R.style.WideDialog);
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
                                ApkActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                            }
                        });
                        dialog.show();
                        return;
                    }
                    Log.e("", "rename");
                    dialog.dismiss();
                    ApkActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                } else {
                    dialog.dismiss();
                    ApkActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                }
            }
        });
        dialog.show();
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
            this.apkList.get(this.pos).setPath(file2.getPath());
            this.apkList.get(this.pos).setApkName(file2.getName());
            this.adapter.notifyItemChanged(this.pos);
            Toast.makeText(this, "Rename file successfully", 0).show();
            RxBus.getInstance().post(new RenameEvent(file, file2));
            return;
        }
        Log.e("LOG", "File not renamed...");
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

    
    public void sortNameAscending() {
        Collections.sort(this.apkList, new Comparator<ApkModel>() {
            public int compare(ApkModel apkModel, ApkModel apkModel2) {
                return apkModel.getApkName().compareToIgnoreCase(apkModel2.getApkName());
            }
        });
    }

    
    public void sortNameDescending() {
        Collections.sort(this.apkList, new Comparator<ApkModel>() {
            public int compare(ApkModel apkModel, ApkModel apkModel2) {
                return apkModel2.getApkName().compareToIgnoreCase(apkModel.getApkName());
            }
        });
    }

    
    public void sortSizeAscending() {
        Collections.sort(this.apkList, new Comparator<ApkModel>() {
            public int compare(ApkModel apkModel, ApkModel apkModel2) {
                return Long.valueOf(apkModel.getSize()).compareTo(Long.valueOf(apkModel2.getSize()));
            }
        });
    }

    
    public void sortSizeDescending() {
        Collections.sort(this.apkList, new Comparator<ApkModel>() {
            public int compare(ApkModel apkModel, ApkModel apkModel2) {
                return Long.valueOf(apkModel2.getSize()).compareTo(Long.valueOf(apkModel.getSize()));
            }
        });
    }

    
    public void setDateWiseSortAs(final boolean z) {
        Collections.sort(this.apkList, new Comparator<ApkModel>() {
            public int compare(ApkModel apkModel, ApkModel apkModel2) {
                try {
                    if (z) {
                        return apkModel2.getDate().compareTo(apkModel.getDate());
                    }
                    return apkModel.getDate().compareTo(apkModel2.getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }

    public void onBottomClick(int i) {
        switch (i) {
            case 1:
                ArrayList<ApkModel> arrayList = this.apkList;
                if (arrayList != null && arrayList.size() != 0) {
                    sortNameAscending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 1);
                    return;
                }
                return;
            case 2:
                ArrayList<ApkModel> arrayList2 = this.apkList;
                if (arrayList2 != null && arrayList2.size() != 0) {
                    sortNameDescending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 2);
                    return;
                }
                return;
            case 3:
                ArrayList<ApkModel> arrayList3 = this.apkList;
                if (arrayList3 != null && arrayList3.size() != 0) {
                    sortSizeDescending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 3);
                    return;
                }
                return;
            case 4:
                ArrayList<ApkModel> arrayList4 = this.apkList;
                if (arrayList4 != null && arrayList4.size() != 0) {
                    sortSizeAscending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 4);
                    return;
                }
                return;
            case 5:
                ArrayList<ApkModel> arrayList5 = this.apkList;
                if (arrayList5 != null && arrayList5.size() != 0) {
                    setDateWiseSortAs(true);
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 5);
                    return;
                }
                return;
            case 6:
                ArrayList<ApkModel> arrayList6 = this.apkList;
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
                    if (new File(ApkActivity.this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(ApkActivity.this, "File name already use", 0).show();
                        return;
                    }
                    ApkActivity.this.zip_file_name = str;
                    dialog.dismiss();
                    if (ApkActivity.this.isFileFromSdCard) {
                        ApkActivity.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(ApkActivity.this.sdCardPath), ApkActivity.this) == 2) {
                            Toast.makeText(ApkActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            ApkActivity.this.setcompress();
                        }
                    } else {
                        ApkActivity.this.setcompress();
                    }
                } else {
                    ApkActivity apkActivity = ApkActivity.this;
                    Toast.makeText(apkActivity, apkActivity.getResources().getString(R.string.zip_validation), 0).show();
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

    
    public void setcompress() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Compress file...");
            this.loadingDialog.show();
        }
        new Thread(new Runnable() {
            public final void run() {
                try {
                    ApkActivity.this.compressfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    
    public void compressfile() throws IOException {
        final File file;
        final File file2;
        final String str;
        String str2 = this.zip_file_name;
        if (this.selected_Item == 1) {
            file2 = new File(this.apkList.get(this.pos).getPath());
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
            for (int i = 0; i < this.apkList.size(); i++) {
                if (this.apkList.get(i) != null) {
                    ApkModel apkModel = this.apkList.get(i);
                    if (apkModel.isSelected()) {
                        File file4 = new File(apkModel.getPath());
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
                ApkActivity.this.setSelectionClose();
                if (str != null) {
                    if (ApkActivity.this.loadingDialog.isShowing()) {
                        ApkActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(ApkActivity.this, "Compress file successfully", 0).show();
                    MediaScannerConnection.scanFile(ApkActivity.this, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str));
                    if (ApkActivity.this.selected_Item != 1) {
                        if (StorageUtils.deleteFile(file2, ApkActivity.this)) {
                            MediaScannerConnection.scanFile(ApkActivity.this, new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                        //File file = file;
                        if (file != null && StorageUtils.deleteFile(file, ApkActivity.this)) {
                            MediaScannerConnection.scanFile(ApkActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
