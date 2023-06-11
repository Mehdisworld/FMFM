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
import com.io.filemanager.freefileexplorer.easily.adapter.VideoAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.model.PhotoHeader;
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
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.io.IOException;
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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class VideoActivity extends AppCompatActivity implements BottomListner {

    VideoAdapter adapter;
    LinkedHashMap<String, ArrayList<PhotoData>> bucketVideoMap = new LinkedHashMap<>();
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
    @BindView(R.id.iv_back)
    AppCompatImageView ivBack;
    public List<Object> videoList = new ArrayList();


    public static void safedk_VideoActivity_startActivity_90c5d16e603d58752f60e156304a6eda(VideoActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_video);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        intView();
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);
        copyMoveEvent();
        displayDeleteEvent();

    }

    public void intView() {
        this.progressBar.setVisibility(0);
        new Thread(new Runnable() {
            public final void run() {
                VideoActivity.this.getVideoList();
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
    }

    
    public void setAdapter() {
        this.progressBar.setVisibility(8);
        List<Object> list = this.videoList;
        if (list == null || list.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
        GridLayoutManager gridLayoutManager = new GridLayoutManager((Context) this, 4, 1, false);
        this.recyclerView.setLayoutManager(gridLayoutManager);
        VideoAdapter videoAdapter = new VideoAdapter(this, this.videoList);
        this.adapter = videoAdapter;
        this.recyclerView.setAdapter(videoAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int i) {
                return (VideoActivity.this.adapter.getItemViewType(i) == 1 || VideoActivity.this.adapter.getItemViewType(i) == 3) ? 4 : 1;
            }
        });
        this.adapter.setOnItemClickListener(new VideoAdapter.ClickListener() {
            public void safedk_VideoActivity_startActivity_90c5d16e603d58752f60e156304a6eda(VideoActivity p0, Intent p1) {
                if (p1 != null) {
                    p0.startActivity(p1);
                }
            }

            public void onItemClick(int i, View view) {
                try {
                    if (VideoActivity.this.videoList.get(i) instanceof PhotoData) {
                        PhotoData photoData = (PhotoData) VideoActivity.this.videoList.get(i);
                        if (photoData.isCheckboxVisible()) {
                            if (photoData.isSelected()) {
                                photoData.setSelected(false);
                            } else {
                                photoData.setSelected(true);
                            }
                            VideoActivity.this.adapter.notifyItemChanged(i);
                            VideoActivity.this.setSelectedFile();
                            return;
                        }
                        int i2 = -1;
                        ArrayList arrayList = new ArrayList();
                        for (int i3 = 0; i3 < VideoActivity.this.videoList.size(); i3++) {
                            if (VideoActivity.this.videoList.get(i3) instanceof PhotoData) {
                                arrayList.add((PhotoData) VideoActivity.this.videoList.get(i3));
                                if (i == i3) {
                                    i2 = arrayList.size() - 1;
                                }
                            }
                        }
                        Constant.displayVideoList = new ArrayList();
                        Constant.displayVideoList.addAll(arrayList);
                        Intent intent = new Intent(VideoActivity.this, VideoPlayActivity.class);
                        intent.putExtra("pos", i2);
                        safedk_VideoActivity_startActivity_90c5d16e603d58752f60e156304a6eda(VideoActivity.this, intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.adapter.setOnLongClickListener(new VideoAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                if (VideoActivity.this.videoList.get(i) instanceof PhotoData) {
                    PhotoData photoData = (PhotoData) VideoActivity.this.videoList.get(i);
                    for (int i2 = 0; i2 < VideoActivity.this.videoList.size(); i2++) {
                        if (VideoActivity.this.videoList.get(i2) != null && (VideoActivity.this.videoList.get(i2) instanceof PhotoData)) {
                            ((PhotoData) VideoActivity.this.videoList.get(i2)).setCheckboxVisible(true);
                        }
                    }
                    photoData.setCheckboxVisible(true);
                    photoData.setSelected(true);
                    VideoActivity.this.adapter.notifyDataSetChanged();
                    VideoActivity.this.setSelectedFile();
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
            case R.id.lout_more:
                showMoreOptionBottom();
                return;
            case R.id.lout_move:
                Constant.isCopyData = false;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_send:
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
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(VideoActivity.this);
                bottomSheetFragment.show(VideoActivity.this.getSupportFragmentManager(), bottomSheetFragment.getTag());
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
        OnSelected(true, false, 0);
    }

    private void selectEvent(boolean z) {
        if (z) {
            for (int i = 0; i < this.videoList.size(); i++) {
                if (this.videoList.get(i) != null && (this.videoList.get(i) instanceof PhotoData)) {
                    ((PhotoData) this.videoList.get(i)).setSelected(true);
                }
            }
            this.adapter.notifyDataSetChanged();
            setSelectedFile();
            return;
        }
        for (int i2 = 0; i2 < this.videoList.size(); i2++) {
            if (this.videoList.get(i2) != null && (this.videoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.videoList.get(i2);
                photoData.setSelected(false);
                photoData.setCheckboxVisible(false);
            }
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
        for (int i2 = 0; i2 < this.videoList.size(); i2++) {
            if (this.videoList.get(i2) != null && (this.videoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.videoList.get(i2);
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
        VideoAdapter videoAdapter = this.adapter;
        if (videoAdapter != null) {
            videoAdapter.notifyDataSetChanged();
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
        for (int i2 = 0; i2 < this.videoList.size(); i2++) {
            if (this.videoList.get(i2) != null && (this.videoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.videoList.get(i2);
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
        VideoAdapter videoAdapter = this.adapter;
        if (videoAdapter != null) {
            videoAdapter.notifyDataSetChanged();
        }
        this.selected_Item = 0;
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        OnSelected(true, false, 0);
        String str = i == 1 ? " item added to Favourites." : " items added to Favourites.";
        Toast.makeText(this, i + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
    }

    private void setCopyMoveOptinOn() {
        String str;
        if (this.videoList != null) {
            Constant.isFileFromSdCard = this.isFileFromSdCard;
            Constant.pastList = new ArrayList<>();
            boolean z = false;
            for (int i = 0; i < this.videoList.size(); i++) {
                if (this.videoList.get(i) != null && (this.videoList.get(i) instanceof PhotoData)) {
                    PhotoData photoData = (PhotoData) this.videoList.get(i);
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
            safedk_VideoActivity_startActivity_90c5d16e603d58752f60e156304a6eda(this, intent);
        }
    }

    
    public void setSelectedFile() {
        boolean z;
        boolean z2;
        String str;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        boolean z3 = false;
        for (int i2 = 0; i2 < this.videoList.size(); i2++) {
            if (this.videoList.get(i2) != null && (this.videoList.get(i2) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.videoList.get(i2);
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
            setVisibleButton(this.loutCompress, this.imgCompress, this.txtTextCompress);
            setVisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
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

    
    public void shareFile() {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        for (int i = 0; i < this.videoList.size(); i++) {
            if (this.videoList.get(i) != null && (this.videoList.get(i) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.videoList.get(i);
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
        safedk_VideoActivity_startActivity_90c5d16e603d58752f60e156304a6eda(this, Intent.createChooser(intent, "Share with..."));
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

    private void setClose() {
        for (int i = 0; i < this.videoList.size(); i++) {
            if (this.videoList.get(i) != null && (this.videoList.get(i) instanceof PhotoData)) {
                PhotoData photoData = (PhotoData) this.videoList.get(i);
                photoData.setCheckboxVisible(false);
                photoData.setSelected(false);
            }
        }
        VideoAdapter videoAdapter = this.adapter;
        if (videoAdapter != null) {
            videoAdapter.notifyDataSetChanged();
        }
        this.selected_Item = 0;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (VideoActivity.this.isFileFromSdCard) {
                    VideoActivity.this.sdCardPermissionType = 1;
                    if (StorageUtils.checkFSDCardPermission(new File(VideoActivity.this.sdCardPath), VideoActivity.this) == 2) {
                        Toast.makeText(VideoActivity.this, "Please give a permission for manager operation", 0).show();
                    } else {
                        VideoActivity.this.setDeleteFile();
                    }
                } else {
                    VideoActivity.this.setDeleteFile();
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
                    VideoActivity.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    VideoActivity.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    VideoActivity.this.shareFile();
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    
    public void setDeleteFile() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Delete file...");
            this.loadingDialog.show();
        }
        new Thread(new Runnable() {
            public final void run() {
                VideoActivity.this.deleteFile();
            }
        }).start();
    }

    public void deleteFile() {
        if (this.videoList != null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < this.videoList.size(); i++) {
                if (this.videoList.get(i) != null && (this.videoList.get(i) instanceof PhotoData)) {
                    PhotoData photoData = (PhotoData) this.videoList.get(i);
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
            while (i2 < this.videoList.size()) {
                if (this.videoList.get(i2) != null && (this.videoList.get(i2) instanceof PhotoData) && ((PhotoData) this.videoList.get(i2)).isSelected()) {
                    boolean z = i2 != 0 && (this.videoList.get(i2 + -1) instanceof PhotoHeader);
                    boolean z2 = i2 < this.videoList.size() + -2 && (this.videoList.get(i2 + 1) instanceof PhotoHeader);
                    if (z && z2) {
                        this.videoList.remove(i2);
                        this.videoList.remove(i2 - 1);
                    } else if (i2 != this.videoList.size() - 1) {
                        this.videoList.remove(i2);
                    } else if (z) {
                        this.videoList.remove(i2);
                        this.videoList.remove(i2 - 1);
                    } else {
                        this.videoList.remove(i2);
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
                    if (VideoActivity.this.loadingDialog != null && VideoActivity.this.loadingDialog.isShowing()) {
                        VideoActivity.this.loadingDialog.dismiss();
                    }
                    VideoActivity.this.selected_Item = 0;
                    VideoActivity.this.OnSelected(true, false, 0);
                    VideoActivity.this.adapter.notifyDataSetChanged();
                    Toast.makeText(VideoActivity.this, "Delete video successfully", 0).show();
                }
            });
        }
    }

    public void getVideoList() {
        Uri uri;
        this.bucketVideoMap = new LinkedHashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Video.Media.getContentUri("external");
        } else {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor query = getContentResolver().query(uri, new String[]{"_data", "_display_name", "_data", "duration", "date_modified", "_size"}, (String) null, (String[]) null, "LOWER(date_modified) DESC");
        if (query != null) {
            ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
            if (favouriteList == null) {
                favouriteList = new ArrayList<>();
            }
            ArrayList<String> arrayList = favouriteList;
            int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("duration");
            query.moveToFirst();
            while (!query.isAfterLast()) {
                String string = query.getString(query.getColumnIndexOrThrow("_data"));
                String string2 = query.getString(query.getColumnIndexOrThrow("_display_name"));
                long j = query.getLong(query.getColumnIndex("_size"));
                long j2 = query.getLong(query.getColumnIndex("date_modified")) * 1000;
                String format = simpleDateFormat.format(Long.valueOf(j2));
                String format2 = simpleDateFormat2.format(Long.valueOf(j2));
                String durationString = getDurationString(query.getInt(columnIndexOrThrow2));
                SimpleDateFormat simpleDateFormat3 = simpleDateFormat;
                PhotoData photoData = new PhotoData();
                photoData.setFilePath(string);
                photoData.setFileName(string2);
                photoData.setThumbnails(query.getString(columnIndexOrThrow));
                photoData.setDuration(durationString);
                photoData.setSize(j);
                try {
                    photoData.setDate(simpleDateFormat2.parse(format2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (arrayList.contains(string)) {
                    photoData.setFavorite(true);
                } else {
                    photoData.setFavorite(false);
                }
                if (this.bucketVideoMap.containsKey(format)) {
                    ArrayList arrayList2 = this.bucketVideoMap.get(format);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add(photoData);
                    this.bucketVideoMap.put(format, arrayList2);
                } else {
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(photoData);
                    this.bucketVideoMap.put(format, arrayList3);
                }
                query.moveToNext();
                simpleDateFormat = simpleDateFormat3;
            }
            query.close();
        }
        setSortData();
        runOnUiThread(new Runnable() {
            public void run() {
                Log.e("video", "set Adapter data---");
                VideoActivity.this.setAdapter();
            }
        });
    }


    private void setSortData() {
        int sortType = PreferencesManager.getSortType(this);
        Set<String> keySet = this.bucketVideoMap.keySet();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(keySet);
        this.videoList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList arrayList2 = this.bucketVideoMap.get(arrayList.get(i));
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
                    this.videoList.add(photoHeader);
                    this.videoList.addAll(arrayList3);
                }
            }
        }
        VideoAdapter videoAdapter = this.adapter;
        if (videoAdapter != null) {
            videoAdapter.notifyDataSetChanged();
            this.progressBar.setVisibility(8);
        }
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

    private void copyMoveEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CopyMoveEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<CopyMoveEvent>() {
            public void call(CopyMoveEvent copyMoveEvent) {
                if (!(copyMoveEvent.getCopyMoveList() == null || copyMoveEvent.getCopyMoveList().size() == 0 || copyMoveEvent.getType() == 3)) {
                    new ArrayList();
                    ArrayList<File> copyMoveList = copyMoveEvent.getCopyMoveList();
                    if (copyMoveList == null) {
                        copyMoveList = new ArrayList<>();
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    ArrayList arrayList = new ArrayList();
                    String str = "";
                    for (int i = 0; i < copyMoveList.size(); i++) {
                        File file = copyMoveList.get(i);
                        if (!file.isDirectory() && file.exists()) {
                            PhotoData photoData = new PhotoData();
                            String format = simpleDateFormat.format(Long.valueOf(file.lastModified()));
                            photoData.setFilePath(file.getPath());
                            photoData.setFileName(file.getName());
                            arrayList.add(photoData);
                            str = format;
                        }
                    }
                    if (arrayList.size() != 0) {
                        if (VideoActivity.this.bucketVideoMap.containsKey(str)) {
                            ArrayList arrayList2 = VideoActivity.this.bucketVideoMap.get(str);
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList();
                            }
                            arrayList2.addAll(arrayList);
                            ArrayList arrayList3 = new ArrayList();
                            switch (PreferencesManager.getSortType(VideoActivity.this)) {
                                case 1:
                                    arrayList3 = VideoActivity.this.sortNameAscending(arrayList2);
                                    break;
                                case 2:
                                    arrayList3 = VideoActivity.this.sortNameDescending(arrayList2);
                                    break;
                                case 3:
                                    arrayList3 = VideoActivity.this.sortSizeDescending(arrayList2);
                                    break;
                                case 4:
                                    arrayList3 = VideoActivity.this.sortSizeAscending(arrayList2);
                                    break;
                                case 5:
                                    arrayList3 = VideoActivity.this.setDateWiseSortAs(true, arrayList2);
                                    break;
                                case 6:
                                    arrayList3 = VideoActivity.this.setDateWiseSortAs(false, arrayList2);
                                    break;
                            }
                            VideoActivity.this.bucketVideoMap.put(str, arrayList3);
                        } else {
                            ArrayList arrayList4 = new ArrayList();
                            arrayList4.addAll(arrayList);
                            ArrayList arrayList5 = new ArrayList();
                            switch (PreferencesManager.getSortType(VideoActivity.this)) {
                                case 1:
                                    arrayList5 = VideoActivity.this.sortNameAscending(arrayList4);
                                    break;
                                case 2:
                                    arrayList5 = VideoActivity.this.sortNameDescending(arrayList4);
                                    break;
                                case 3:
                                    arrayList5 = VideoActivity.this.sortSizeDescending(arrayList4);
                                    break;
                                case 4:
                                    arrayList5 = VideoActivity.this.sortSizeAscending(arrayList4);
                                    break;
                                case 5:
                                    arrayList5 = VideoActivity.this.setDateWiseSortAs(true, arrayList4);
                                    break;
                                case 6:
                                    arrayList5 = VideoActivity.this.setDateWiseSortAs(false, arrayList4);
                                    break;
                            }
                            LinkedHashMap linkedHashMap = new LinkedHashMap();
                            linkedHashMap.put(str, arrayList5);
                            linkedHashMap.putAll(VideoActivity.this.bucketVideoMap);
                            VideoActivity.this.bucketVideoMap = new LinkedHashMap<>();
                            VideoActivity.this.bucketVideoMap.putAll(linkedHashMap);
                        }
                        VideoActivity.this.videoList.clear();
                        Set<String> keySet = VideoActivity.this.bucketVideoMap.keySet();
                        ArrayList arrayList6 = new ArrayList();
                        arrayList6.addAll(keySet);
                        for (int i2 = 0; i2 < arrayList6.size(); i2++) {
                            ArrayList arrayList7 = VideoActivity.this.bucketVideoMap.get(arrayList6.get(i2));
                            if (!(arrayList7 == null || arrayList7.size() == 0)) {
                                PhotoHeader photoHeader = new PhotoHeader();
                                photoHeader.setTitle((String) arrayList6.get(i2));
                                photoHeader.setPhotoList(arrayList7);
                                VideoActivity.this.videoList.add(photoHeader);
                                VideoActivity.this.videoList.addAll(arrayList7);
                            }
                        }
                        if (VideoActivity.this.adapter != null) {
                            VideoActivity.this.adapter.notifyDataSetChanged();
                        } else {
                            VideoActivity.this.setAdapter();
                        }
                        if (VideoActivity.this.videoList == null || VideoActivity.this.videoList.size() == 0) {
                            VideoActivity.this.recyclerView.setVisibility(8);
                            VideoActivity.this.llEmpty.setVisibility(0);
                        } else {
                            VideoActivity.this.recyclerView.setVisibility(0);
                            VideoActivity.this.llEmpty.setVisibility(8);
                        }
                    }
                }
                if (copyMoveEvent.getDeleteList() != null && copyMoveEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    VideoActivity.this.updateDeleteImageData(copyMoveEvent.getDeleteList());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void updateDeleteImageData(ArrayList<String> arrayList) {
        List<Object> list = this.videoList;
        if (list != null && list.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (i2 < this.videoList.size()) {
                    if ((this.videoList.get(i2) instanceof PhotoData) && ((PhotoData) this.videoList.get(i2)).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        boolean z = i2 != 0 && (this.videoList.get(i2 + -1) instanceof PhotoHeader);
                        boolean z2 = i2 < this.videoList.size() + -2 && (this.videoList.get(i2 + 1) instanceof PhotoHeader);
                        if (z && z2) {
                            this.videoList.remove(i2);
                            this.videoList.remove(i2 - 1);
                        } else if (i2 != this.videoList.size() - 1) {
                            this.videoList.remove(i2);
                        } else if (z) {
                            this.videoList.remove(i2);
                            this.videoList.remove(i2 - 1);
                        } else {
                            this.videoList.remove(i2);
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
            VideoAdapter videoAdapter = this.adapter;
            if (videoAdapter != null) {
                videoAdapter.notifyDataSetChanged();
            }
            List<Object> list2 = this.videoList;
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

    public void updateMainList(ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (arrayList == null || arrayList.size() == 0 || arrayList2 == null || arrayList2.size() == 0) {
            Set<String> keySet = this.bucketVideoMap.keySet();
            ArrayList arrayList3 = new ArrayList();
            arrayList3.addAll(keySet);
            for (int i = 0; i < arrayList.size(); i++) {
                for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                    ArrayList arrayList4 = this.bucketVideoMap.get(arrayList3.get(i2));
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
                            this.bucketVideoMap.remove(str);
                        } else {
                            this.bucketVideoMap.put(str, arrayList4);
                        }
                    }
                }
            }
            return;
        }
        for (int i4 = 0; i4 < arrayList2.size(); i4++) {
            String str2 = arrayList2.get(i4);
            if (str2 != null && !str2.equalsIgnoreCase("") && this.bucketVideoMap.containsKey(str2)) {
                ArrayList arrayList5 = this.bucketVideoMap.get(str2);
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
                    this.bucketVideoMap.remove(str2);
                } else {
                    this.bucketVideoMap.put(str2, arrayList5);
                }
            }
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
                        return photoData2.getDate().compareTo(photoData.getDate());
                    }
                    return photoData.getDate().compareTo(photoData2.getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        return arrayList;
    }

    
    public void showDetailDialog() {
        if (this.videoList.get(this.pos) instanceof PhotoData) {
            final Dialog dialog = new Dialog(this, R.style.WideDialog);
            dialog.requestWindowFeature(1);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_details);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setGravity(80);
            File file = new File(((PhotoData) this.videoList.get(this.pos)).getFilePath());
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
        if (this.videoList.get(this.pos) instanceof PhotoData) {
            final File file = new File(((PhotoData) this.videoList.get(this.pos)).getFilePath());
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
                        Toast.makeText(VideoActivity.this, "New name can't be empty.", 0).show();
                    } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                        dialog.show();
                    } else if (!file.isDirectory()) {
                        String[] split = appCompatEditText.getText().toString().split("\\.");
                        if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                            final Dialog dialog = new Dialog(VideoActivity.this, R.style.WideDialog);
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
                                    VideoActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                                }
                            });
                            dialog.show();
                            return;
                        }
                        Log.e("", "rename");
                        dialog.dismiss();
                        VideoActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                    } else {
                        dialog.dismiss();
                        VideoActivity.this.reNameFile(file, appCompatEditText.getText().toString());
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
            if (this.videoList.get(this.pos) instanceof PhotoData) {
                PhotoData photoData = (PhotoData) this.videoList.get(this.pos);
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

    private void displayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DisplayDeleteEvent>() {
            public void call(DisplayDeleteEvent displayDeleteEvent) {
                if (displayDeleteEvent.getDeleteList() != null && displayDeleteEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    VideoActivity.this.updateDeleteImageData(displayDeleteEvent.getDeleteList());
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
                VideoActivity.this.progressBar.setVisibility(0);
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
                    if (new File(VideoActivity.this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(VideoActivity.this, "File name already use", 0).show();
                        return;
                    }
                    VideoActivity.this.zip_file_name = str;
                    dialog.dismiss();
                    if (VideoActivity.this.isFileFromSdCard) {
                        VideoActivity.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(VideoActivity.this.sdCardPath), VideoActivity.this) == 2) {
                            Toast.makeText(VideoActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            VideoActivity.this.setcompress();
                        }
                    } else {
                        VideoActivity.this.setcompress();
                    }
                } else {
                    VideoActivity videoActivity = VideoActivity.this;
                    Toast.makeText(videoActivity, videoActivity.getResources().getString(R.string.zip_validation), 0).show();
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
                    VideoActivity.this.compressfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void compressfile() throws IOException {
        File file = null;
        String str;
        File file2 = null;
        String str2 = this.zip_file_name;
        File file3 = null;
        if (this.selected_Item != 1) {
            File file4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
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
            for (int i = 0; i < this.videoList.size(); i++) {
                if (this.videoList.get(i) != null && (this.videoList.get(i) instanceof PhotoData)) {
                    PhotoData photoData = (PhotoData) this.videoList.get(i);
                    if (photoData.isSelected()) {
                        File file5 = new File(photoData.getFilePath());
                        StorageUtils.copyFile(file5, new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file5.getName()), this);
                    }
                }
            }
        } else if (this.videoList.get(this.pos) instanceof PhotoData) {
            file2 = new File(((PhotoData) this.videoList.get(this.pos)).getFilePath());
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
            File finalFile = file3;
            String finalStr = str3;
            File finalFile2 = file;
            runOnUiThread(new Runnable() {
                public void run() {
                    VideoActivity.this.setSelectionClose();
                    if (finalFile == null) {
                        Toast.makeText(VideoActivity.this, "Error", 0).show();
                    } else if (finalStr != null) {
                        if (VideoActivity.this.loadingDialog.isShowing()) {
                            VideoActivity.this.loadingDialog.dismiss();
                        }
                        Toast.makeText(VideoActivity.this, "Compress file successfully", 0).show();
                        MediaScannerConnection.scanFile(VideoActivity.this, new String[]{finalStr}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                        RxBus.getInstance().post(new CopyMoveEvent(finalStr));
                        if (VideoActivity.this.selected_Item != 1) {
                            if (StorageUtils.deleteFile(finalFile, VideoActivity.this)) {
                                MediaScannerConnection.scanFile(VideoActivity.this, new String[]{finalFile.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String str, Uri uri) {
                                    }
                                });
                            }

                            if (finalFile2 != null && StorageUtils.deleteFile(finalFile2, VideoActivity.this)) {
                                MediaScannerConnection.scanFile(VideoActivity.this, new String[]{finalFile2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
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
        File finalFile3 = file;
        runOnUiThread(new Runnable() {
            public void run() {
                VideoActivity.this.setSelectionClose();
                if (finalFile1 == null) {
                    Toast.makeText(VideoActivity.this, "Error", 0).show();
                } else if (str32 != null) {
                    if (VideoActivity.this.loadingDialog.isShowing()) {
                        VideoActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(VideoActivity.this, "Compress file successfully", 0).show();
                    MediaScannerConnection.scanFile(VideoActivity.this, new String[]{str32}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str32));
                    if (VideoActivity.this.selected_Item != 1) {
                        if (StorageUtils.deleteFile(finalFile1, VideoActivity.this)) {
                            MediaScannerConnection.scanFile(VideoActivity.this, new String[]{finalFile1.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }

                        if (finalFile3 != null && StorageUtils.deleteFile(finalFile3, VideoActivity.this)) {
                            MediaScannerConnection.scanFile(VideoActivity.this, new String[]{finalFile3.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
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
