package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.adapter.PhotoAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayFavoriteEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.model.PhotoHeader;
import com.io.filemanager.freefileexplorer.easily.oncliclk.BottomListner;
import com.io.filemanager.freefileexplorer.easily.service.ImageDataService;
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
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.safedk.android.analytics.brandsafety.creatives.infos.CreativeInfo;
import com.safedk.android.analytics.events.RedirectEvent;
import com.safedk.android.utils.Logger;*/
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import net.lingala.zip4j.util.InternalZipConstants;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ImageActivity extends AppCompatActivity implements BottomListner {
    

    PhotoAdapter adapter;
    LinkedHashMap<String, ArrayList<PhotoData>> bucketimagesDataPhotoHashMap;
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
    public List<Object> photoList = new ArrayList();
    public int retryAttempt;

    public static void safedk_ImageActivity_startActivity_00c2e4bab27661c937f72a4f90ef2198(ImageActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    static int access$108(ImageActivity imageActivity) {
        int i = imageActivity.retryAttempt;
        imageActivity.retryAttempt = i + 1;
        return i;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_image);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        Constant.isOpenImage = true;
        intView();
        displayDeleteEvent();
        displayFavoriteEvent();
        copyMoveEvent();
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);

    }

    public void onDestroy() {
        Constant.isOpenImage = false;
        super.onDestroy();
    }

    public void intView() {
        this.progressBar.setVisibility(0);
        this.ivUncheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
        this.ivCheckAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        this.ivFavFill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_fill));
        this.ivFavUnfill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_unfill));
        this.imgMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_bottom));
        new Thread(new Runnable() {
            public final void run() {
                ImageActivity.this.getImageData();
            }
        }).start();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        this.rootPath = file.getPath();
        File file2 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getString(R.string.compress_file));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getString(R.string.extract_file));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        this.compressPath = file2.getPath();
        this.extractPath = file3.getPath();
        this.sdCardPath = Utils.getExternalStoragePath(this, true);
    }

    public void getImageData() {
        this.photoList = new ArrayList();
        this.bucketimagesDataPhotoHashMap = new LinkedHashMap<>();
        do {
        } while (!ImageDataService.isComplete);
        runOnUiThread(new Runnable() {
            public void run() {
                ImageActivity.this.photoList = new ArrayList();
                ImageActivity.this.bucketimagesDataPhotoHashMap.clear();
                ImageActivity.this.bucketimagesDataPhotoHashMap = ImageDataService.bucketimagesDataPhotoHashMap;
                ImageActivity.this.setSortData();
                ImageActivity.this.setAdapter();

            }
        });
    }

    
    public void setAdapter() {
        this.progressBar.setVisibility(8);
        List<Object> list = this.photoList;
        if (list == null || list.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
        GridLayoutManager gridLayoutManager = new GridLayoutManager((Context) this, 4, 1, false);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.adapter = new PhotoAdapter(this, this.photoList);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int i) {
                return ImageActivity.this.adapter.getItemViewType(i) == 1 ? 4 : 1;
            }
        });
        this.recyclerView.hasFixedSize();
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setOnItemClickListener(new PhotoAdapter.ClickListener() {
            public void safedk_ImageActivity_startActivity_00c2e4bab27661c937f72a4f90ef2198(ImageActivity p0, Intent p1) {
                if (p1 != null) {
                    p0.startActivity(p1);
                }
            }

            public void onItemClick(int i, View view) {
                try {
                    if (ImageActivity.this.photoList.get(i) instanceof PhotoData) {
                        PhotoData photoData = (PhotoData) ImageActivity.this.photoList.get(i);
                        if (photoData.isCheckboxVisible()) {
                            if (photoData.isSelected()) {
                                photoData.setSelected(false);
                            } else {
                                photoData.setSelected(true);
                            }
                            ImageActivity.this.adapter.notifyItemChanged(i);
                            ImageActivity.this.setSelectedFile();
                            return;
                        }
                        int i2 = -1;
                        ArrayList arrayList = new ArrayList();
                        for (int i3 = 0; i3 < ImageActivity.this.photoList.size(); i3++) {
                            if (ImageActivity.this.photoList.get(i3) instanceof PhotoData) {
                                arrayList.add((PhotoData) ImageActivity.this.photoList.get(i3));
                                if (i == i3) {
                                    i2 = arrayList.size() - 1;
                                }
                            }
                        }
                        Constant.displayImageList = new ArrayList();
                        Constant.displayImageList.addAll(arrayList);
                        Intent intent = new Intent(ImageActivity.this, DisplayImageActivity.class);
                        intent.putExtra("pos", i2);
                        safedk_ImageActivity_startActivity_00c2e4bab27661c937f72a4f90ef2198(ImageActivity.this, intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.adapter.setOnLongClickListener(new PhotoAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                try {
                    if (ImageActivity.this.photoList.get(i) instanceof PhotoData) {
                        PhotoData photoData = (PhotoData) ImageActivity.this.photoList.get(i);
                        for (int i2 = 0; i2 < ImageActivity.this.photoList.size(); i2++) {
                            if (ImageActivity.this.photoList.get(i2) != null && (ImageActivity.this.photoList.get(i2) instanceof PhotoData)) {
                                ((PhotoData) ImageActivity.this.photoList.get(i2)).setCheckboxVisible(true);
                            }
                        }
                        photoData.setCheckboxVisible(true);
                        photoData.setSelected(true);
                        ImageActivity.this.adapter.notifyDataSetChanged();
                        ImageActivity.this.setSelectedFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                shareFile();
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
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(ImageActivity.this);
                bottomSheetFragment.show(ImageActivity.this.getSupportFragmentManager(), bottomSheetFragment.getTag());
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
        setClose();
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        OnSelected(true, false, 0);
    }

    
    public void setSelectedFile() {
        boolean z;
        boolean z2;
        String str;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        boolean z3 = false;
        int i = 0;
        for (int i2 = 0; i2 < this.photoList.size(); i2++) {
            if (this.photoList.get(i2) != null && (this.photoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.photoList.get(i2);
                if (photoData.isSelected()) {
                    i++;
                    this.pos = i2;
                    if (photoData.isFavorite()) {
                        arrayList.add(1);
                    } else {
                        arrayList2.add(0);
                    }
                    if (!z3 && (str = this.sdCardPath) != null && !str.equalsIgnoreCase("") && photoData.getFilePath().contains(this.sdCardPath)) {
                        z3 = true;
                    }
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
            setInvisibleButton(this.loutSend, this.imgSend, this.txtTextSend);
            setInvisibleButton(this.loutMove, this.imgMove, this.txtTextMove);
            setInvisibleButton(this.loutDelete, this.imgDelete, this.txtTextDelete);
            setInvisibleButton(this.loutCopy, this.imgCopy, this.txtTextCopy);
            setInvisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
            setInvisibleButton(this.loutCompress, this.imgCompress, this.txtTextCompress);
            if (arrayList2.size() != 0 || arrayList.size() == 0) {
                arrayList2.size();
            }
            this.llFavourite.setVisibility(8);
        } else {
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
                } else {
                    this.ivFavFill.setVisibility(8);
                    this.ivFavUnfill.setVisibility(0);
                }
            } else {
                this.llFavourite.setVisibility(8);
            }
        }
        if (i == 0) {
            OnSelected(true, false, i);
            setSelectionClose();
        } else {
            OnSelected(false, true, i);
        }
        this.selected_Item = i;
    }

    private void setUnFavourite() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        int i = 0;
        for (int i2 = 0; i2 < this.photoList.size(); i2++) {
            if (this.photoList.get(i2) != null && (this.photoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.photoList.get(i2);
                if (photoData.isSelected() && photoData.isFavorite()) {
                    photoData.setFavorite(false);
                    i++;
                    if (favouriteList.contains(photoData.getFilePath())) {
                        favouriteList.remove(photoData.getFilePath());
                    }
                }
                photoData.setCheckboxVisible(false);
                photoData.setSelected(false);
            }
        }
        PhotoAdapter photoAdapter = this.adapter;
        if (photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
        }
        this.selected_Item = 0;
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        OnSelected(true, false, 0);
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
        for (int i2 = 0; i2 < this.photoList.size(); i2++) {
            if (this.photoList.get(i2) != null && (this.photoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.photoList.get(i2);
                if (photoData.isSelected()) {
                    if (!photoData.isFavorite()) {
                        favouriteList.add(0, photoData.getFilePath());
                        i++;
                    }
                    photoData.setFavorite(true);
                }
                photoData.setCheckboxVisible(false);
                photoData.setSelected(false);
            }
        }
        PhotoAdapter photoAdapter = this.adapter;
        if (photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
        }
        this.selected_Item = 0;
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        OnSelected(true, false, 0);
        String str = i == 1 ? " item added to Favourites." : " items added to Favourites.";
        Toast.makeText(this, i + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
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

    private void setCopyMoveOptinOn() {
        String str;
        if (this.photoList != null) {
            Constant.isFileFromSdCard = this.isFileFromSdCard;
            Constant.pastList = new ArrayList<>();
            boolean z = false;
            for (int i = 0; i < this.photoList.size(); i++) {
                if (this.photoList.get(i) != null && (this.photoList.get(i) instanceof PhotoData)) {
                    PhotoData photoData = (PhotoData) this.photoList.get(i);
                    if (photoData.isSelected()) {
                        File file = new File(photoData.getFilePath());
                        if (file.exists()) {
                            Constant.pastList.add(file);
                            if (!z && (str = this.sdCardPath) != null && !str.equalsIgnoreCase("") && file.getPath().contains(this.sdCardPath)) {
                                z = true;
                            }
                        }
                    }
                }
            }
            setSelectionClose();
            Intent intent = new Intent(this, StorageActivity.class);
            intent.putExtra("type", "CopyMove");
            safedk_ImageActivity_startActivity_00c2e4bab27661c937f72a4f90ef2198(this, intent);
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
            this.llBottomOption.setVisibility(0);
        } else {
            this.loutSelected.setVisibility(8);
            this.llBottomOption.setVisibility(8);
        }
        AppCompatTextView appCompatTextView = this.txtSelect;
        appCompatTextView.setText(i + " selected");
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
                    ImageActivity.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    ImageActivity.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    ImageActivity.this.shareFile();
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void setClose() {
        for (int i = 0; i < this.photoList.size(); i++) {
            if (this.photoList.get(i) != null && (this.photoList.get(i) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.photoList.get(i);
                photoData.setCheckboxVisible(false);
                photoData.setSelected(false);
            }
        }
        PhotoAdapter photoAdapter = this.adapter;
        if (photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
        }
        this.selected_Item = 0;
    }

    
    public void setDeleteFile() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Delete file...");
            this.loadingDialog.show();
        }
        new Thread(new Runnable() {
            public final void run() {
                ImageActivity.this.deleteFile();
            }
        }).start();
    }

    private void selectEvent(boolean z) {
        if (z) {
            for (int i = 0; i < this.photoList.size(); i++) {
                if (this.photoList.get(i) != null && (this.photoList.get(i) instanceof PhotoData)) {
                    ((PhotoData) this.photoList.get(i)).setSelected(true);
                }
            }
            this.adapter.notifyDataSetChanged();
            setSelectedFile();
            return;
        }
        for (int i2 = 0; i2 < this.photoList.size(); i2++) {
            if (this.photoList.get(i2) != null && (this.photoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.photoList.get(i2);
                photoData.setSelected(false);
                photoData.setCheckboxVisible(false);
            }
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
    }

    
    public void showDetailDialog() {
        if (this.photoList.get(this.pos) instanceof PhotoData) {
            final Dialog dialog = new Dialog(this, R.style.WideDialog);
            dialog.requestWindowFeature(1);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_details);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setGravity(80);
            File file = new File(((PhotoData) this.photoList.get(this.pos)).getFilePath());
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
    }

    
    public void showRenameDialog() {
        setSelectionClose();
        if (this.photoList.get(this.pos) instanceof PhotoData) {
            final File file = new File(((PhotoData) this.photoList.get(this.pos)).getFilePath());
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
                        Toast.makeText(ImageActivity.this, "New name can't be empty.", 0).show();
                    } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                        dialog.show();
                    } else if (!file.isDirectory()) {
                        String[] split = appCompatEditText.getText().toString().split("\\.");
                        if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                            final Dialog dialog = new Dialog(ImageActivity.this, R.style.WideDialog);
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
                                    ImageActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                                }
                            });
                            dialog.show();
                            return;
                        }
                        Log.e("", "rename");
                        dialog.dismiss();
                        ImageActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                    } else {
                        dialog.dismiss();
                        ImageActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                    }
                }
            });
            dialog.show();
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
            if (this.photoList.get(this.pos) instanceof PhotoData) {
                PhotoData photoData = (PhotoData) this.photoList.get(this.pos);
                photoData.setFilePath(file2.getPath());
                photoData.setFileName(file2.getName());
            }
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

    
    public void shareFile() {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        for (int i = 0; i < this.photoList.size(); i++) {
            if (this.photoList.get(i) != null && (this.photoList.get(i) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.photoList.get(i);
                if (photoData.isSelected()) {
                    arrayList.add(FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(photoData.getFilePath())));
                }
            }
        }
        intent.setType("*/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.addFlags(268435456);
        safedk_ImageActivity_startActivity_00c2e4bab27661c937f72a4f90ef2198(this, Intent.createChooser(intent, "Share with..."));
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (ImageActivity.this.isFileFromSdCard) {
                    ImageActivity.this.sdCardPermissionType = 1;
                    if (StorageUtils.checkFSDCardPermission(new File(ImageActivity.this.sdCardPath), ImageActivity.this) == 2) {
                        Toast.makeText(ImageActivity.this, "Please give a permission for manager operation", 0).show();
                    } else {
                        ImageActivity.this.setDeleteFile();
                    }
                } else {
                    ImageActivity.this.setDeleteFile();
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

    public void deleteFile() {
        if (this.photoList != null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < this.photoList.size(); i++) {
                if (this.photoList.get(i) != null && (this.photoList.get(i) instanceof PhotoData)) {
                    PhotoData photoData = (PhotoData) this.photoList.get(i);
                    if (photoData.isSelected()) {
                        File file = new File(photoData.getFilePath());
                        String format = new SimpleDateFormat("MMM dd, yyyy").format(Long.valueOf(file.lastModified()));
                        if (StorageUtils.deleteFile(file, this)) {
                            arrayList.add(photoData.getFilePath());
                            arrayList2.add(format);
                            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                    } else {
                        photoData.setCheckboxVisible(false);
                    }
                }
            }
            int i2 = 0;
            while (i2 < this.photoList.size()) {
                if (this.photoList.get(i2) != null && (this.photoList.get(i2) instanceof PhotoData) && ((PhotoData) this.photoList.get(i2)).isSelected()) {
                    boolean z = i2 != 0 && (this.photoList.get(i2 + -1) instanceof PhotoHeader);
                    boolean z2 = i2 < this.photoList.size() + -2 && (this.photoList.get(i2 + 1) instanceof PhotoHeader);
                    if (z && z2) {
                        this.photoList.remove(i2);
                        this.photoList.remove(i2 - 1);
                    } else if (i2 != this.photoList.size() - 1) {
                        this.photoList.remove(i2);
                    } else if (z) {
                        this.photoList.remove(i2);
                        this.photoList.remove(i2 - 1);
                    } else {
                        this.photoList.remove(i2);
                    }
                    if (i2 != 0) {
                        i2--;
                    }
                }
                i2++;
            }
            updateMainList(arrayList, arrayList2);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ImageActivity.this.loadingDialog != null && ImageActivity.this.loadingDialog.isShowing()) {
                        ImageActivity.this.loadingDialog.dismiss();
                    }
                    ImageActivity.this.selected_Item = 0;
                    ImageActivity.this.OnSelected(true, false, 0);
                    ImageActivity.this.adapter.notifyDataSetChanged();
                    Toast.makeText(ImageActivity.this, "Delete image successfully", 0).show();
                }
            });
        }
    }

    public void updateMainList(ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (arrayList == null || arrayList.size() == 0 || arrayList2 == null || arrayList2.size() == 0) {
            Set<String> keySet = this.bucketimagesDataPhotoHashMap.keySet();
            ArrayList arrayList3 = new ArrayList();
            arrayList3.addAll(keySet);
            for (int i = 0; i < arrayList.size(); i++) {
                for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                    ArrayList arrayList4 = this.bucketimagesDataPhotoHashMap.get(arrayList3.get(i2));
                    String str = (String) arrayList3.get(i2);
                    if (!(arrayList4 == null || arrayList4.size() == 0)) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= arrayList4.size()) {
                                break;
                            }
                            try {
                                if (((PhotoData) arrayList4.get(i3)).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                                    arrayList4.remove(i3);
                                    break;
                                }
                                i3++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (arrayList4 == null || arrayList4.size() == 0) {
                            this.bucketimagesDataPhotoHashMap.remove(str);
                        } else {
                            this.bucketimagesDataPhotoHashMap.put(str, arrayList4);
                        }
                    }
                }
            }
            return;
        }
        for (int i4 = 0; i4 < arrayList2.size(); i4++) {
            String str2 = arrayList2.get(i4);
            if (str2 != null && !str2.equalsIgnoreCase("") && this.bucketimagesDataPhotoHashMap.containsKey(str2)) {
                ArrayList arrayList5 = this.bucketimagesDataPhotoHashMap.get(str2);
                if (arrayList5 == null) {
                    arrayList5 = new ArrayList();
                }
                int i5 = 0;
                while (true) {
                    if (i5 >= arrayList5.size()) {
                        break;
                    }
                    try {
                        if (((PhotoData) arrayList5.get(i5)).getFilePath().equalsIgnoreCase(arrayList.get(i4))) {
                            arrayList5.remove(i5);
                            break;
                        }
                        i5++;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (arrayList5 == null || arrayList5.size() == 0) {
                    this.bucketimagesDataPhotoHashMap.remove(str2);
                } else {
                    this.bucketimagesDataPhotoHashMap.put(str2, arrayList5);
                }
            }
        }
    }

    private void getImagesList() {
        Uri uri;
        String str;
        this.photoList = new ArrayList();
        this.bucketimagesDataPhotoHashMap = new LinkedHashMap<>();
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
            ArrayList arrayList = new ArrayList();
            query.moveToFirst();
            while (!query.isAfterLast()) {
                long j = query.getLong(query.getColumnIndex("_size"));
                if (j != 0) {
                    String string = query.getString(query.getColumnIndexOrThrow(str2));
                    String string2 = query.getString(query.getColumnIndex("_display_name"));
                    Log.e("imagesize", "size: " + j);
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
                    arrayList.add(photoData);
                    if (this.bucketimagesDataPhotoHashMap.containsKey(format)) {
                        ArrayList arrayList2 = this.bucketimagesDataPhotoHashMap.get(format);
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        arrayList2.add(photoData);
                        this.bucketimagesDataPhotoHashMap.put(format, arrayList2);
                    } else {
                        ArrayList arrayList3 = new ArrayList();
                        arrayList3.add(photoData);
                        this.bucketimagesDataPhotoHashMap.put(format, arrayList3);
                    }
                } else {
                    str = str2;
                }
                query.moveToNext();
                str2 = str;
            }
            query.close();
        }
        setSortData();
        runOnUiThread(new Runnable() {
            public void run() {
                ImageActivity.this.setAdapter();
            }
        });
    }

    
    public void setSortData() {
        int sortType = PreferencesManager.getSortType(this);
        Set<String> keySet = this.bucketimagesDataPhotoHashMap.keySet();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(keySet);
        this.photoList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList arrayList2 = this.bucketimagesDataPhotoHashMap.get(arrayList.get(i));
            if (!(arrayList2 == null || arrayList2.size() == 0)) {
                PhotoHeader photoHeader = new PhotoHeader();
                photoHeader.setTitle((String) arrayList.get(i));
                ArrayList<PhotoData> arrayList3 = new ArrayList<>();
                switch (sortType) {
                    case 1:
                        arrayList3 = sortNameAscending(arrayList2);
                        break;
                    case 2:
                        arrayList3 = sortNameDescending(arrayList2);
                        break;
                    case 3:
                        arrayList3 = sortSizeDescending(arrayList2);
                        break;
                    case 4:
                        arrayList3 = sortSizeAscending(arrayList2);
                        break;
                    case 5:
                        arrayList3 = setDateWiseSortAs(true, arrayList2);
                        break;
                    case 6:
                        arrayList3 = setDateWiseSortAs(false, arrayList2);
                        break;
                }
                if (!(arrayList3 == null || arrayList3.size() == 0)) {
                    photoHeader.setPhotoList(arrayList3);
                    this.photoList.add(photoHeader);
                    this.photoList.addAll(arrayList3);
                }
            }
        }
        PhotoAdapter photoAdapter = this.adapter;
        if (photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
            this.progressBar.setVisibility(8);
        }
    }

    
    public ArrayList<PhotoData> sortNameAscending(ArrayList<PhotoData> arrayList) {
        Collections.sort(arrayList, new Comparator<PhotoData>() {
            public int compare(PhotoData photoData, PhotoData photoData2) {
                return photoData.getFileName().compareToIgnoreCase(photoData2.getFileName());
            }
        });
        return arrayList;
    }

    
    public ArrayList<PhotoData> sortNameDescending(ArrayList<PhotoData> arrayList) {
        Collections.sort(arrayList, new Comparator<PhotoData>() {
            public int compare(PhotoData photoData, PhotoData photoData2) {
                return photoData2.getFileName().compareToIgnoreCase(photoData.getFileName());
            }
        });
        return arrayList;
    }

    
    public ArrayList<PhotoData> sortSizeAscending(ArrayList<PhotoData> arrayList) {
        Collections.sort(arrayList, new Comparator<PhotoData>() {
            public int compare(PhotoData photoData, PhotoData photoData2) {
                return Long.valueOf(photoData.getSize()).compareTo(Long.valueOf(photoData2.getSize()));
            }
        });
        return arrayList;
    }

    
    public ArrayList<PhotoData> sortSizeDescending(ArrayList<PhotoData> arrayList) {
        Collections.sort(arrayList, new Comparator<PhotoData>() {
            public int compare(PhotoData photoData, PhotoData photoData2) {
                return Long.valueOf(photoData2.getSize()).compareTo(Long.valueOf(photoData.getSize()));
            }
        });
        return arrayList;
    }

    
    public ArrayList<PhotoData> setDateWiseSortAs(final boolean z, ArrayList<PhotoData> arrayList) {
        Collections.sort(arrayList, new Comparator<PhotoData>() {
            public int compare(PhotoData photoData, PhotoData photoData2) {
                try {
                    if (z) {
                        return Long.valueOf(photoData2.getDateValue()).compareTo(Long.valueOf(photoData.getDateValue()));
                    }
                    return Long.valueOf(photoData.getDateValue()).compareTo(Long.valueOf(photoData2.getDateValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        return arrayList;
    }

    private void displayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1<DisplayDeleteEvent>() {
            public void call(DisplayDeleteEvent displayDeleteEvent) {
                if (displayDeleteEvent.getDeleteList() != null && displayDeleteEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    ImageActivity.this.updateDeleteImageData(displayDeleteEvent.getDeleteList());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void displayFavoriteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayFavoriteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1<DisplayFavoriteEvent>() {
            public void call(DisplayFavoriteEvent displayFavoriteEvent) {
                if (displayFavoriteEvent.getFilePath() != null && !displayFavoriteEvent.getFilePath().equalsIgnoreCase("")) {
                    ImageActivity.this.setUpdateFavourite(displayFavoriteEvent.isFavorite(), displayFavoriteEvent.getFilePath());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void setUpdateFavourite(boolean z, String str) {
        List<Object> list = this.photoList;
        if (list != null && list.size() != 0) {
            for (int i = 0; i < this.photoList.size(); i++) {
                if (this.photoList.get(i) instanceof PhotoData) {
                    PhotoData photoData = (PhotoData) this.photoList.get(i);
                    if (photoData.getFilePath() != null && photoData.getFilePath().equalsIgnoreCase(str)) {
                        photoData.setFavorite(z);
                        PhotoAdapter photoAdapter = this.adapter;
                        if (photoAdapter != null) {
                            photoAdapter.notifyItemChanged(i);
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    
    public void updateDeleteImageData(ArrayList<String> arrayList) {
        List<Object> list = this.photoList;
        if (list != null && list.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (i2 < this.photoList.size()) {
                    if ((this.photoList.get(i2) instanceof PhotoData) && ((PhotoData) this.photoList.get(i2)).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        boolean z = i2 != 0 && (this.photoList.get(i2 + -1) instanceof PhotoHeader);
                        boolean z2 = i2 < this.photoList.size() + -2 && (this.photoList.get(i2 + 1) instanceof PhotoHeader);
                        if (z && z2) {
                            this.photoList.remove(i2);
                            this.photoList.remove(i2 - 1);
                        } else if (i2 != this.photoList.size() - 1) {
                            this.photoList.remove(i2);
                        } else if (z) {
                            this.photoList.remove(i2);
                            this.photoList.remove(i2 - 1);
                        } else {
                            this.photoList.remove(i2);
                        }
                        if (i2 != 0) {
                            i2--;
                        }
                        if (i == arrayList.size() - 1) {
                            break;
                        }
                    }
                    i2++;
                }
            }
            PhotoAdapter photoAdapter = this.adapter;
            if (photoAdapter != null) {
                photoAdapter.notifyDataSetChanged();
            }
            List<Object> list2 = this.photoList;
            if (list2 == null || list2.size() == 0) {
                this.recyclerView.setVisibility(8);
                this.llEmpty.setVisibility(0);
            } else {
                this.recyclerView.setVisibility(0);
                this.llEmpty.setVisibility(8);
            }
            updateMainList(arrayList, new ArrayList());
        }
    }

    private void copyMoveEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CopyMoveEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1<CopyMoveEvent>() {
            public void call(CopyMoveEvent copyMoveEvent) {
                if (!(copyMoveEvent.getCopyMoveList() == null || copyMoveEvent.getCopyMoveList().size() == 0 || copyMoveEvent.getType() == 3)) {
                    new ArrayList();
                    ArrayList<File> copyMoveList = copyMoveEvent.getCopyMoveList();
                    if (copyMoveList == null) {
                        copyMoveList = new ArrayList<>();
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                    ArrayList arrayList = new ArrayList();
                    String str = "";
                    for (int i = 0; i < copyMoveList.size(); i++) {
                        File file = copyMoveList.get(i);
                        if (!file.isDirectory() && file.exists()) {
                            PhotoData photoData = new PhotoData();
                            String format = simpleDateFormat.format(Long.valueOf(file.lastModified()));
                            String format2 = simpleDateFormat2.format(Long.valueOf(file.lastModified()));
                            photoData.setFilePath(file.getPath());
                            photoData.setFileName(file.getName());
                            photoData.setSize(file.length());
                            try {
                                photoData.setDate(simpleDateFormat2.parse(format2));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            arrayList.add(photoData);
                            str = format;
                        }
                    }
                    if (arrayList.size() != 0) {
                        if (ImageActivity.this.bucketimagesDataPhotoHashMap.containsKey(str)) {
                            ArrayList arrayList2 = ImageActivity.this.bucketimagesDataPhotoHashMap.get(str);
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList();
                            }
                            arrayList2.addAll(arrayList);
                            ArrayList arrayList3 = new ArrayList();
                            switch (PreferencesManager.getSortType(ImageActivity.this)) {
                                case 1:
                                    arrayList3 = ImageActivity.this.sortNameAscending(arrayList2);
                                    break;
                                case 2:
                                    arrayList3 = ImageActivity.this.sortNameDescending(arrayList2);
                                    break;
                                case 3:
                                    arrayList3 = ImageActivity.this.sortSizeDescending(arrayList2);
                                    break;
                                case 4:
                                    arrayList3 = ImageActivity.this.sortSizeAscending(arrayList2);
                                    break;
                                case 5:
                                    arrayList3 = ImageActivity.this.setDateWiseSortAs(true, arrayList2);
                                    break;
                                case 6:
                                    arrayList3 = ImageActivity.this.setDateWiseSortAs(false, arrayList2);
                                    break;
                            }
                            ImageActivity.this.bucketimagesDataPhotoHashMap.put(str, arrayList3);
                        } else {
                            ArrayList arrayList4 = new ArrayList();
                            arrayList4.addAll(arrayList);
                            ArrayList arrayList5 = new ArrayList();
                            if (arrayList4.size() != 0 && arrayList4.size() != 1) {
                                switch (PreferencesManager.getSortType(ImageActivity.this)) {
                                    case 1:
                                        arrayList5 = ImageActivity.this.sortNameAscending(arrayList4);
                                        break;
                                    case 2:
                                        arrayList5 = ImageActivity.this.sortNameDescending(arrayList4);
                                        break;
                                    case 3:
                                        arrayList5 = ImageActivity.this.sortSizeDescending(arrayList4);
                                        break;
                                    case 4:
                                        arrayList5 = ImageActivity.this.sortSizeAscending(arrayList4);
                                        break;
                                    case 5:
                                        arrayList5 = ImageActivity.this.setDateWiseSortAs(true, arrayList4);
                                        break;
                                    case 6:
                                        arrayList5 = ImageActivity.this.setDateWiseSortAs(false, arrayList4);
                                        break;
                                }
                            } else {
                                arrayList5.addAll(arrayList4);
                            }
                            try {
                                LinkedHashMap linkedHashMap = new LinkedHashMap();
                                linkedHashMap.put(str, arrayList5);
                                linkedHashMap.putAll(ImageActivity.this.bucketimagesDataPhotoHashMap);
                                ImageActivity.this.bucketimagesDataPhotoHashMap = new LinkedHashMap<>();
                                ImageActivity.this.bucketimagesDataPhotoHashMap.putAll(linkedHashMap);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                        ImageActivity.this.photoList.clear();
                        Set<String> keySet = ImageActivity.this.bucketimagesDataPhotoHashMap.keySet();
                        ArrayList arrayList6 = new ArrayList();
                        arrayList6.addAll(keySet);
                        for (int i2 = 0; i2 < arrayList6.size(); i2++) {
                            ArrayList arrayList7 = ImageActivity.this.bucketimagesDataPhotoHashMap.get(arrayList6.get(i2));
                            if (!(arrayList7 == null || arrayList7.size() == 0)) {
                                PhotoHeader photoHeader = new PhotoHeader();
                                photoHeader.setTitle((String) arrayList6.get(i2));
                                photoHeader.setPhotoList(arrayList7);
                                ImageActivity.this.photoList.add(photoHeader);
                                ImageActivity.this.photoList.addAll(arrayList7);
                            }
                        }
                        ImageActivity.this.setAdapter();
                        if (ImageActivity.this.photoList == null || ImageActivity.this.photoList.size() == 0) {
                            ImageActivity.this.recyclerView.setVisibility(8);
                            ImageActivity.this.llEmpty.setVisibility(0);
                        } else {
                            ImageActivity.this.recyclerView.setVisibility(0);
                            ImageActivity.this.llEmpty.setVisibility(8);
                        }
                    }
                }
                if (copyMoveEvent.getDeleteList() != null && copyMoveEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    ImageActivity.this.updateDeleteImageData(copyMoveEvent.getDeleteList());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    public void onBottomClick(int i) {
        PreferencesManager.saveToSortType(this, i);
        runOnUiThread(new Runnable() {
            public void run() {
                ImageActivity.this.progressBar.setVisibility(0);
            }
        });
        setSortData();
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
                    if (new File(ImageActivity.this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(ImageActivity.this, "File name already use", 0).show();
                        return;
                    }
                    ImageActivity.this.zip_file_name = str;
                    dialog.dismiss();
                    if (ImageActivity.this.isFileFromSdCard) {
                        ImageActivity.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(ImageActivity.this.sdCardPath), ImageActivity.this) == 2) {
                            Toast.makeText(ImageActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            ImageActivity.this.setcompress();
                        }
                    } else {
                        ImageActivity.this.setcompress();
                    }
                } else {
                    ImageActivity imageActivity = ImageActivity.this;
                    Toast.makeText(imageActivity, imageActivity.getResources().getString(R.string.zip_validation), 0).show();
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
                ImageActivity.this.compressfile();
            }
        }).start();
    }

    public void compressfile() {
        File file = null;
        String str;
        File file2 = null;
        String str2 = this.zip_file_name;
        File file3 = null;
        if (this.selected_Item != 1) {
            File file4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getString(R.string.app_name));
            if (!file4.exists()) {
                file4.mkdirs();
            }
            file3 = new File(file4.getPath() + "/.ZIP");
            if (!file3.exists()) {
                file3.mkdirs();
            }
            file2 = new File(file3.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + str2);
            if (!file2.exists()) {
                file2.mkdir();
            }
            for (int i = 0; i < this.photoList.size(); i++) {
                if (this.photoList.get(i) != null && (this.photoList.get(i) instanceof PhotoData)) {
                    PhotoData photoData = (PhotoData) this.photoList.get(i);
                    if (photoData.isSelected()) {
                        File file5 = new File(photoData.getFilePath());
                        StorageUtils.copyFile1(file5, new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file5.getName()), this);
                    }
                }
            }
        } else if (this.photoList.get(this.pos) instanceof PhotoData) {
            file2 = new File(((PhotoData) this.photoList.get(this.pos)).getFilePath());
            if (!file2.exists()) {
                file2.mkdir();
            }
        } else {
            file = null;
            String str3 = "";
            if (file3 != null) {
                if (this.selected_Item == 1) {
                    str = this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str2 + ".zip";
                } else {
                    str = this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + file3.getName() + ".zip";
                }
                ZipArchive.zip(file3.getPath(), str, str3);
                str3 = str;
            }
            String finalStr = str3;
            File finalFile = file3;
            File finalFile3 = file;
            runOnUiThread(new Runnable() {
                public void run() {
                    ImageActivity.this.setSelectionClose();
                    if (finalFile == null) {
                        Toast.makeText(ImageActivity.this, "Error", 0).show();
                    } else if (finalStr != null) {
                        if (ImageActivity.this.loadingDialog.isShowing()) {
                            ImageActivity.this.loadingDialog.dismiss();
                        }
                        Toast.makeText(ImageActivity.this, "Compress file successfully", 0).show();
                        MediaScannerConnection.scanFile(ImageActivity.this, new String[]{finalStr}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                        RxBus.getInstance().post(new CopyMoveEvent(finalStr));
                        if (ImageActivity.this.selected_Item != 1) {
                            if (StorageUtils.deleteFile(finalFile, ImageActivity.this)) {
                                MediaScannerConnection.scanFile(ImageActivity.this, new String[]{finalFile.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String str, Uri uri) {
                                    }
                                });
                            }

                            if (finalFile3 != null && StorageUtils.deleteFile(finalFile3, ImageActivity.this)) {
                                MediaScannerConnection.scanFile(ImageActivity.this, new String[]{finalFile3.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String str, Uri uri) {
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
        File file6 = file3;
        file3 = file2;
        //file = file6;
        final String str32 = "";
        if (file3 != null) {
        }
        File finalFile1 = file3;
        File finalFile2 = file;
        runOnUiThread(new Runnable() {
            public void run() {
                ImageActivity.this.setSelectionClose();
                if (finalFile1 == null) {
                    Toast.makeText(ImageActivity.this, "Error", 0).show();
                } else if (str32 != null) {
                    if (ImageActivity.this.loadingDialog.isShowing()) {
                        ImageActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(ImageActivity.this, "Compress file successfully", 0).show();
                    MediaScannerConnection.scanFile(ImageActivity.this, new String[]{str32}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str32));
                    if (ImageActivity.this.selected_Item != 1) {
                        if (StorageUtils.deleteFile(finalFile1, ImageActivity.this)) {
                            MediaScannerConnection.scanFile(ImageActivity.this, new String[]{finalFile1.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }

                        if (finalFile2 != null && StorageUtils.deleteFile(finalFile2, ImageActivity.this)) {
                            MediaScannerConnection.scanFile(ImageActivity.this, new String[]{finalFile2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
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
