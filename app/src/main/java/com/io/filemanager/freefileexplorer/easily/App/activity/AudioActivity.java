package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.adapter.AudioAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.AudioModel;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import net.lingala.zip4j.util.InternalZipConstants;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AudioActivity extends AppCompatActivity implements BottomListner {

    AudioAdapter adapter;
    ArrayList<AudioModel> audioList = new ArrayList<>();
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

    public static void safedk_AudioActivity_startActivity_57dd4446967ea61452c0587655047f4e(AudioActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_audio);
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
                AudioActivity.this.getAllAudioList();
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
        this.rootPath = file.getPath();
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
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(AudioActivity.this);
                bottomSheetFragment.show(AudioActivity.this.getSupportFragmentManager(), bottomSheetFragment.getTag());
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

    private void setCopyMoveOptinOn() {
        Constant.isFileFromSdCard = this.isFileFromSdCard;
        Constant.pastList = new ArrayList<>();
        for (int i = 0; i < this.audioList.size(); i++) {
            if (this.audioList.get(i).isSelected()) {
                File file = new File(this.audioList.get(i).getPath());
                if (file.exists()) {
                    Constant.pastList.add(file);
                }
            }
        }
        setSelectionClose();
        Intent intent = new Intent(this, StorageActivity.class);
        intent.putExtra("type", "CopyMove");
        safedk_AudioActivity_startActivity_57dd4446967ea61452c0587655047f4e(this, intent);
    }

    private void setUnFavourite() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        int i = 0;
        for (int i2 = 0; i2 < this.audioList.size(); i2++) {
            if (this.audioList.get(i2).isSelected() && this.audioList.get(i2).isFavorite()) {
                this.audioList.get(i2).setFavorite(false);
                i++;
                if (favouriteList.contains(this.audioList.get(i2).getPath())) {
                    favouriteList.remove(this.audioList.get(i2).getPath());
                }
            }
            this.audioList.get(i2).setSelected(false);
            this.audioList.get(i2).setCheckboxVisible(false);
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
        for (int i2 = 0; i2 < this.audioList.size(); i2++) {
            if (this.audioList.get(i2).isSelected()) {
                if (!this.audioList.get(i2).isFavorite()) {
                    favouriteList.add(0, this.audioList.get(i2).getPath());
                    i++;
                }
                this.audioList.get(i2).setFavorite(true);
            }
            this.audioList.get(i2).setSelected(false);
            this.audioList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
        String str = i == 1 ? " item added to Favourites." : " items added to Favourites.";
        Toast.makeText(this, i + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
    }

    
    public void setSelectionClose() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        for (int i = 0; i < this.audioList.size(); i++) {
            this.audioList.get(i).setSelected(false);
            this.audioList.get(i).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.loutSelected.setVisibility(8);
        this.loutToolbar.setVisibility(0);
    }

    private void selectEvent(boolean z) {
        if (z) {
            for (int i = 0; i < this.audioList.size(); i++) {
                this.audioList.get(i).setSelected(true);
            }
            this.adapter.notifyDataSetChanged();
            setSelectedFile();
            return;
        }
        for (int i2 = 0; i2 < this.audioList.size(); i2++) {
            this.audioList.get(i2).setSelected(false);
            this.audioList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
    }

    
    public void sendFile() {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        for (int i = 0; i < this.audioList.size(); i++) {
            if (this.audioList.get(i).isSelected()) {
                arrayList.add(FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(this.audioList.get(i).getPath())));
            }
        }
        intent.setType("*/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.addFlags(268435456);
        safedk_AudioActivity_startActivity_57dd4446967ea61452c0587655047f4e(this, Intent.createChooser(intent, "Share with..."));
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (AudioActivity.this.isFileFromSdCard) {
                    AudioActivity.this.sdCardPermissionType = 1;
                    if (StorageUtils.checkFSDCardPermission(new File(AudioActivity.this.sdCardPath), AudioActivity.this) == 2) {
                        Toast.makeText(AudioActivity.this, "Please give a permission for manager operation", 0).show();
                    } else {
                        AudioActivity.this.setDeleteFile();
                    }
                } else {
                    AudioActivity.this.setDeleteFile();
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
                AudioActivity.this.deleteFile();
            }
        }).start();
    }

    public void setAdapter() {
        this.progressBar.setVisibility(8);
        ArrayList<AudioModel> arrayList = this.audioList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AudioAdapter audioAdapter = new AudioAdapter(this, this.audioList);
        this.adapter = audioAdapter;
        this.recyclerView.setAdapter(audioAdapter);
        this.adapter.setOnItemClickListener(new AudioAdapter.ClickListener() {
            public void safedk_AudioActivity_startActivity_57dd4446967ea61452c0587655047f4e(AudioActivity p0, Intent p1) {
                if (p1 != null) {
                    p0.startActivity(p1);
                }
            }

            public void onItemClick(int i, View view) {
                if (AudioActivity.this.audioList.get(i).isCheckboxVisible()) {
                    if (AudioActivity.this.audioList.get(i).isSelected()) {
                        AudioActivity.this.audioList.get(i).setSelected(false);
                    } else {
                        AudioActivity.this.audioList.get(i).setSelected(true);
                    }
                    AudioActivity.this.adapter.notifyDataSetChanged();
                    AudioActivity.this.setSelectedFile();
                    return;
                }
                File file = new File(AudioActivity.this.audioList.get(i).getPath());
                AudioActivity audioActivity = AudioActivity.this;
                Uri uriForFile = FileProvider.getUriForFile(audioActivity, AudioActivity.this.getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(uriForFile, Utils.getMimeTypeFromFilePath(file.getPath()));
                intent.addFlags(1);
                safedk_AudioActivity_startActivity_57dd4446967ea61452c0587655047f4e(AudioActivity.this, Intent.createChooser(intent, "Open with"));
            }
        });
        this.adapter.setOnLongClickListener(new AudioAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                AudioActivity.this.audioList.get(i).setSelected(true);
                for (int i2 = 0; i2 < AudioActivity.this.audioList.size(); i2++) {
                    if (AudioActivity.this.audioList.get(i2) != null) {
                        AudioActivity.this.audioList.get(i2).setCheckboxVisible(true);
                    }
                }
                AudioActivity.this.adapter.notifyDataSetChanged();
                AudioActivity.this.setSelectedFile();
            }
        });
        new Thread(new Runnable() {
            public final void run() {
                AudioActivity.this.getSongAlbumImage();
            }
        }).start();
    }

    public void getSongAlbumImage() {
        ArrayList<AudioModel> arrayList = this.audioList;
        if (arrayList != null && arrayList.size() != 0) {
            for (int i = 0; i < this.audioList.size(); i++) {
                if (this.audioList.get(i) != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), this.audioList.get(i).getAlbumId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        this.audioList.get(i).setBitmap(bitmap);
                        int finalI = i;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (AudioActivity.this.adapter != null) {
                                    AudioActivity.this.adapter.notifyItemChanged(finalI);
                                }
                            }
                        });
                    }
                }
            }
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
        for (int i2 = 0; i2 < this.audioList.size(); i2++) {
            if (this.audioList.get(i2).isSelected()) {
                i++;
                this.pos = i2;
                if (this.audioList.get(i2).isFavorite()) {
                    arrayList.add(1);
                } else {
                    arrayList2.add(0);
                }
                if (!z3 && (str = this.sdCardPath) != null && !str.equalsIgnoreCase("") && this.audioList.get(i2).getPath().contains(this.sdCardPath)) {
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
            this.llBottomOption.setVisibility(8);
            OnSelected(true, false, i);
            setSelectionClose();
        } else {
            this.llBottomOption.setVisibility(0);
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
        } else {
            this.loutSelected.setVisibility(8);
        }
        AppCompatTextView appCompatTextView = this.txtSelect;
        appCompatTextView.setText(i + " selected");
    }

    public void getAllAudioList() {
        Cursor query = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "album", "artist", "album_id", "date_modified", "_size"}, (String) null, (String[]) null, "LOWER(date_modified) DESC");
        if (query != null) {
            ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
            if (favouriteList == null) {
                favouriteList = new ArrayList<>();
            }
            while (query.moveToNext()) {
                long j = query.getLong(query.getColumnIndex("_size"));
                if (j != 0) {
                    AudioModel audioModel = new AudioModel();
                    String string = query.getString(0);
                    String string2 = query.getString(1);
                    String string3 = query.getString(2);
                    Long valueOf = Long.valueOf(query.getLong(3));
                    long j2 = query.getLong(query.getColumnIndex("date_modified")) * 1000;
                    audioModel.setName(string.substring(string.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1));
                    audioModel.setAlbum(string2);
                    audioModel.setArtist(string3);
                    audioModel.setPath(string);
                    if (favouriteList.contains(string)) {
                        audioModel.setFavorite(true);
                    } else {
                        audioModel.setFavorite(false);
                    }
                    audioModel.setPlay(false);
                    audioModel.setSelected(false);
                    audioModel.setCheckboxVisible(false);
                    audioModel.setSize(j);
                    audioModel.setDateValue(j2);
                    audioModel.setAlbumId(valueOf.longValue());
                    this.audioList.add(audioModel);
                }
            }
            query.close();
        }
        runOnUiThread(new Runnable() {
            public void run() {
                if (!(AudioActivity.this.audioList == null || AudioActivity.this.audioList.size() == 0)) {
                    int sortType = PreferencesManager.getSortType(AudioActivity.this);
                    if (sortType == 1) {
                        AudioActivity.this.sortNameAscending();
                    } else if (sortType == 2) {
                        AudioActivity.this.sortNameDescending();
                    } else if (sortType == 3) {
                        AudioActivity.this.sortSizeDescending();
                    } else if (sortType == 4) {
                        AudioActivity.this.sortSizeAscending();
                    } else if (sortType == 5) {
                        AudioActivity.this.setDateWiseSortAs(true);
                    } else if (sortType == 6) {
                        AudioActivity.this.setDateWiseSortAs(false);
                    } else {
                        AudioActivity.this.sortNameAscending();
                    }
                }
                AudioActivity.this.setAdapter();
            }
        });
    }

    public void deleteFile() {
        if (this.audioList != null) {
            for (int i = 0; i < this.audioList.size(); i++) {
                if (this.audioList.get(i).isSelected()) {
                    File file = new File(this.audioList.get(i).getPath());
                    if (StorageUtils.deleteFile(file, this)) {
                        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                    }
                }
            }
        }
        if (this.audioList != null) {
            int i2 = 0;
            while (i2 < this.audioList.size()) {
                this.audioList.get(i2).setCheckboxVisible(false);
                if (this.audioList.get(i2).isSelected()) {
                    this.audioList.remove(i2);
                    if (i2 != 0) {
                        i2--;
                    }
                }
                i2++;
            }
            try {
                if (this.audioList.size() != 1 && 1 < this.audioList.size() && this.audioList.get(1).isSelected()) {
                    this.audioList.remove(1);
                }
                if (this.audioList.size() != 0 && this.audioList.get(0).isSelected()) {
                    this.audioList.remove(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                AudioActivity.this.OnSelected(true, false, 0);
                AudioActivity.this.llBottomOption.setVisibility(8);
                if (AudioActivity.this.adapter != null) {
                    AudioActivity.this.adapter.notifyDataSetChanged();
                }
                if (AudioActivity.this.loadingDialog != null && AudioActivity.this.loadingDialog.isShowing()) {
                    AudioActivity.this.loadingDialog.dismiss();
                }
                if (AudioActivity.this.audioList == null || AudioActivity.this.audioList.size() == 0) {
                    AudioActivity.this.recyclerView.setVisibility(8);
                    AudioActivity.this.llEmpty.setVisibility(0);
                } else {
                    AudioActivity.this.recyclerView.setVisibility(0);
                    AudioActivity.this.llEmpty.setVisibility(8);
                }
                Toast.makeText(AudioActivity.this, "Delete file successfully", 0).show();
            }
        });
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
                    ArrayList access$800 = AudioActivity.this.getAudioList();
                    for (int i = 0; i < copyMoveList.size(); i++) {
                        copyMoveList.get(i);
                        int i2 = 0;
                        while (true) {
                            if (i2 >= access$800.size()) {
                                break;
                            } else if (copyMoveList.get(i).getPath().equalsIgnoreCase(((AudioModel) access$800.get(i2)).getPath())) {
                                AudioModel audioModel = (AudioModel) access$800.get(i2);
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(AudioActivity.this.getContentResolver(), ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), audioModel.getAlbumId()));
                                } catch (FileNotFoundException unused) {
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                audioModel.setBitmap(bitmap);
                                AudioActivity.this.audioList.add(audioModel);
                                break;
                            } else {
                                i2++;
                            }
                        }
                    }
                    if (!(AudioActivity.this.audioList == null || AudioActivity.this.audioList.size() == 0)) {
                        int sortType = PreferencesManager.getSortType(AudioActivity.this);
                        if (sortType == 1) {
                            AudioActivity.this.sortNameAscending();
                        } else if (sortType == 2) {
                            AudioActivity.this.sortNameDescending();
                        } else if (sortType == 3) {
                            AudioActivity.this.sortSizeDescending();
                        } else if (sortType == 4) {
                            AudioActivity.this.sortSizeAscending();
                        } else if (sortType == 5) {
                            AudioActivity.this.setDateWiseSortAs(true);
                        } else if (sortType == 6) {
                            AudioActivity.this.setDateWiseSortAs(false);
                        } else {
                            AudioActivity.this.sortNameAscending();
                        }
                    }
                    if (AudioActivity.this.adapter != null) {
                        AudioActivity.this.adapter.notifyDataSetChanged();
                    } else {
                        AudioActivity.this.setAdapter();
                    }
                    if (AudioActivity.this.audioList == null || AudioActivity.this.audioList.size() == 0) {
                        AudioActivity.this.recyclerView.setVisibility(8);
                        AudioActivity.this.llEmpty.setVisibility(0);
                    } else {
                        AudioActivity.this.recyclerView.setVisibility(0);
                        AudioActivity.this.llEmpty.setVisibility(8);
                    }
                }
                if (copyMoveEvent.getDeleteList() != null && copyMoveEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    ArrayList<String> deleteList = copyMoveEvent.getDeleteList();
                    if (deleteList == null) {
                        deleteList = new ArrayList<>();
                    }
                    AudioActivity.this.updateDeleteData(deleteList);
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public ArrayList<AudioModel> getAudioList() {
        ArrayList<AudioModel> arrayList = new ArrayList<>();
        Cursor query = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "album", "artist", "album_id", "date_modified", "_size"}, (String) null, (String[]) null, "LOWER(date_modified) DESC");
        if (query != null) {
            while (query.moveToNext()) {
                long j = query.getLong(query.getColumnIndex("_size"));
                if (j != 0) {
                    AudioModel audioModel = new AudioModel();
                    String string = query.getString(0);
                    String string2 = query.getString(1);
                    String string3 = query.getString(2);
                    Long valueOf = Long.valueOf(query.getLong(3));
                    audioModel.setName(string.substring(string.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1));
                    audioModel.setAlbum(string2);
                    audioModel.setArtist(string3);
                    audioModel.setPath(string);
                    audioModel.setPlay(false);
                    audioModel.setSelected(false);
                    audioModel.setCheckboxVisible(false);
                    audioModel.setSize(j);
                    audioModel.setDateValue(query.getLong(query.getColumnIndex("date_modified")) * 1000);
                    audioModel.setAlbumId(valueOf.longValue());
                    arrayList.add(audioModel);
                }
            }
            query.close();
        }
        return arrayList;
    }

    
    public void updateDeleteData(ArrayList<String> arrayList) {
        ArrayList<AudioModel> arrayList2 = this.audioList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= this.audioList.size()) {
                        break;
                    } else if (this.audioList.get(i2).getPath().equalsIgnoreCase(arrayList.get(i))) {
                        this.audioList.remove(i2);
                        break;
                    } else {
                        i2++;
                    }
                }
            }
            AudioAdapter audioAdapter = this.adapter;
            if (audioAdapter != null) {
                audioAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }
            ArrayList<AudioModel> arrayList3 = this.audioList;
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
                    AudioActivity.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    AudioActivity.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    AudioActivity.this.sendFile();
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
        File file = new File(this.audioList.get(this.pos).getPath());
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
        final File file = new File(this.audioList.get(this.pos).getPath());
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
                    Toast.makeText(AudioActivity.this, "New name can't be empty.", 0).show();
                } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                    dialog.show();
                } else if (!file.isDirectory()) {
                    String[] split = appCompatEditText.getText().toString().split("\\.");
                    if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                        final Dialog dialog = new Dialog(AudioActivity.this, R.style.WideDialog);
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
                                AudioActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                            }
                        });
                        dialog.show();
                        return;
                    }
                    Log.e("", "rename");
                    dialog.dismiss();
                    AudioActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                } else {
                    dialog.dismiss();
                    AudioActivity.this.reNameFile(file, appCompatEditText.getText().toString());
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
            this.audioList.get(this.pos).setPath(file2.getPath());
            this.audioList.get(this.pos).setName(file2.getName());
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
        Collections.sort(this.audioList, new Comparator<AudioModel>() {
            public int compare(AudioModel audioModel, AudioModel audioModel2) {
                return audioModel.getPath().compareToIgnoreCase(audioModel2.getPath());
            }
        });
    }

    
    public void sortNameDescending() {
        Collections.sort(this.audioList, new Comparator<AudioModel>() {
            public int compare(AudioModel audioModel, AudioModel audioModel2) {
                return audioModel2.getPath().compareToIgnoreCase(audioModel.getPath());
            }
        });
    }

    
    public void sortSizeAscending() {
        Collections.sort(this.audioList, new Comparator<AudioModel>() {
            public int compare(AudioModel audioModel, AudioModel audioModel2) {
                return Long.valueOf(audioModel.getSize()).compareTo(Long.valueOf(audioModel2.getSize()));
            }
        });
    }

    
    public void sortSizeDescending() {
        Collections.sort(this.audioList, new Comparator<AudioModel>() {
            public int compare(AudioModel audioModel, AudioModel audioModel2) {
                return Long.valueOf(audioModel2.getSize()).compareTo(Long.valueOf(audioModel.getSize()));
            }
        });
    }

    
    public void setDateWiseSortAs(final boolean z) {
        Collections.sort(this.audioList, new Comparator<AudioModel>() {
            public int compare(AudioModel audioModel, AudioModel audioModel2) {
                if (z) {
                    return Long.valueOf(audioModel2.getDateValue()).compareTo(Long.valueOf(audioModel.getDateValue()));
                }
                return Long.valueOf(audioModel.getDateValue()).compareTo(Long.valueOf(audioModel2.getDateValue()));
            }
        });
    }

    public void onBottomClick(int i) {
        switch (i) {
            case 1:
                ArrayList<AudioModel> arrayList = this.audioList;
                if (arrayList != null && arrayList.size() != 0) {
                    sortNameAscending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 1);
                    return;
                }
                return;
            case 2:
                ArrayList<AudioModel> arrayList2 = this.audioList;
                if (arrayList2 != null && arrayList2.size() != 0) {
                    sortNameDescending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 2);
                    return;
                }
                return;
            case 3:
                ArrayList<AudioModel> arrayList3 = this.audioList;
                if (arrayList3 != null && arrayList3.size() != 0) {
                    sortSizeDescending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 3);
                    return;
                }
                return;
            case 4:
                ArrayList<AudioModel> arrayList4 = this.audioList;
                if (arrayList4 != null && arrayList4.size() != 0) {
                    sortSizeAscending();
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 4);
                    return;
                }
                return;
            case 5:
                ArrayList<AudioModel> arrayList5 = this.audioList;
                if (arrayList5 != null && arrayList5.size() != 0) {
                    setDateWiseSortAs(true);
                    this.adapter.notifyDataSetChanged();
                    PreferencesManager.saveToSortType(this, 5);
                    return;
                }
                return;
            case 6:
                ArrayList<AudioModel> arrayList6 = this.audioList;
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
                    if (new File(AudioActivity.this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(AudioActivity.this, "File name already use", 0).show();
                        return;
                    }
                    AudioActivity.this.zip_file_name = str;
                    dialog.dismiss();
                    if (AudioActivity.this.isFileFromSdCard) {
                        AudioActivity.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(AudioActivity.this.sdCardPath), AudioActivity.this) == 2) {
                            Toast.makeText(AudioActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            AudioActivity.this.setcompress();
                        }
                    } else {
                        AudioActivity.this.setcompress();
                    }
                } else {
                    AudioActivity audioActivity = AudioActivity.this;
                    Toast.makeText(audioActivity, audioActivity.getResources().getString(R.string.zip_validation), 0).show();
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
                    AudioActivity.this.compressfile();
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
            file2 = new File(this.audioList.get(this.pos).getPath());
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
            for (int i = 0; i < this.audioList.size(); i++) {
                if (this.audioList.get(i) != null) {
                    AudioModel audioModel = this.audioList.get(i);
                    if (audioModel.isSelected()) {
                        File file4 = new File(audioModel.getPath());
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
                AudioActivity.this.setSelectionClose();
                if (str != null) {
                    if (AudioActivity.this.loadingDialog.isShowing()) {
                        AudioActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(AudioActivity.this, "Compress file successfully", 0).show();
                    MediaScannerConnection.scanFile(AudioActivity.this, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str));
                    if (AudioActivity.this.selected_Item != 1) {
                        if (StorageUtils.deleteFile(file2, AudioActivity.this)) {
                            MediaScannerConnection.scanFile(AudioActivity.this, new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                        if (file != null && StorageUtils.deleteFile(file, AudioActivity.this)) {
                            MediaScannerConnection.scanFile(AudioActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
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
