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
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.io.filemanager.freefileexplorer.easily.adapter.HeaderListAdapter;
import com.io.filemanager.freefileexplorer.easily.adapter.StorageAdapter;
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

public class StorageActivity extends AppCompatActivity implements BottomListner {
    static final boolean $assertionsDisabled = false;
    static int videoImage_code = 30;
    static int zip_open = 40;

    StorageAdapter adapter;
    
    public ArrayList<String> arrayListFilePaths = new ArrayList<>();
    ArrayList<InternalStorageFilesModel> backUpstorageList = new ArrayList<>();
    @BindView(R.id.btn_internal_storage)
    TextView btnInternalStorage;
    @BindView(R.id.btn_sd_card)
    TextView btnSdCard;
    String compressPath;
    @BindView(R.id.edt_search)
    EditText edtSearch;
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
    @BindView(R.id.img_past)
    ImageView imgPast;
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
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_close)
    AppCompatImageView ivClose;
    @BindView(R.id.iv_fav_fill)
    ImageView ivFavFill;
    @BindView(R.id.iv_fav_unfill)
    ImageView ivFavUnfill;
    @BindView(R.id.iv_list_grid)
    AppCompatImageView ivListGrid;
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
    @BindView(R.id.ll_paste_option)
    LinearLayout llPasteOption;
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
    @BindView(R.id.lout_past)
    LinearLayout loutPast;
    @BindView( R.id.lout_search_bar)
    RelativeLayout loutSearchBar;
    @BindView( R.id.lout_selected)
    RelativeLayout loutSelected;
    @BindView(R.id.lout_send)
    LinearLayout loutSend;
    @BindView(R.id.lout_storage_option)
    LinearLayout loutStorageOption;
    @BindView(R.id.lout_toolbar)
    RelativeLayout loutToolbar;
    ArrayList<File> pastList = new ArrayList<>();
    HeaderListAdapter pathAdapter;
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
    @BindView(R.id.txt_text_past)
    TextView txtTextPast;
    @BindView(R.id.txt_text_send)
    TextView txtTextSend;
    String zip_file_name;

    public static void safedk_StorageActivity_startActivityForResult_d04f8bfa4e74ebb540fb51c5a9b1220d(StorageActivity p0, Intent p1, int p2) {
        if (p1 != null) {
            p0.startActivityForResult(p1, p2);
        }
    }

    public static void safedk_StorageActivity_startActivity_cce8c718314155b16e3552877ac6d373(StorageActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_storage);
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
        Intent intent = getIntent();
        this.isSdCard = Utils.externalMemoryAvailable(this);
        String stringExtra = intent.getStringExtra("type");
        this.storage_type = stringExtra;
        if (stringExtra.equalsIgnoreCase("Internal")) {
            this.rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else if (this.storage_type.equalsIgnoreCase("Download")) {
            this.rootPath = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
        } else if (this.storage_type.equalsIgnoreCase("CopyMove")) {
            this.rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            this.rootPath = Utils.getExternalStoragePath(this, true);
        }
        this.sdCardPath = Utils.getExternalStoragePath(this, true);
        this.ivUncheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
        this.ivCheckAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        this.ivFavFill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_fill));
        this.ivFavUnfill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_unfill));
        this.imgMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_bottom));
        if (this.storage_type.equalsIgnoreCase("CopyMove")) {
            this.isFileFromSdCard = Constant.isFileFromSdCard;
            if (Constant.pastList == null || Constant.pastList.size() == 0) {
                this.llPasteOption.setVisibility(8);
            } else {
                this.llPasteOption.setVisibility(0);
                this.pastList.addAll(Constant.pastList);
            }
        } else {
            this.llPasteOption.setVisibility(8);
        }
        this.loutToolbar.setVisibility(0);
        this.loutSearchBar.setVisibility(8);
        this.isGrid = PreferencesManager.getDirList_Grid(this);
        this.isShowHidden = PreferencesManager.getShowHidden(this);
        this.ivCheckAll.setColorFilter(getResources().getColor(R.color.theme_color), PorterDuff.Mode.SRC_IN);
        if (this.isGrid) {
            this.ivListGrid.setImageDrawable(getResources().getDrawable(R.drawable.ic_list));
        } else {
            this.ivListGrid.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid));
        }
        setSearchBarData();
        getDataList();
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);
        this.loutStorageOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StorageActivity.this.setStorageOptionClose();
            }
        });
    }

    
    public void setStorageOptionClose() {
        if (this.loutStorageOption.getVisibility() == 0) {
            this.loutStorageOption.setVisibility(8);
        }
    }

    public void setSearchBarData() {
        this.edtSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String charSequence2 = charSequence.toString();
                Log.e("searchText addChange: ", charSequence2);
                StorageActivity.this.setSearch(charSequence2);
                if (i3 == 0) {
                    StorageActivity.this.ivClear.setVisibility(8);
                } else {
                    StorageActivity.this.ivClear.setVisibility(0);
                }
            }
        });
        this.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 3) {
                    return false;
                }
                Log.e("TAG", "edtSearch: " + StorageActivity.this.edtSearch.getText().toString());
                if (StorageActivity.this.edtSearch.getText().toString().isEmpty() || StorageActivity.this.edtSearch.getText().toString().trim().length() == 0) {
                    Toast.makeText(StorageActivity.this, "Enter file name", 0).show();
                    return true;
                }
                String trim = StorageActivity.this.edtSearch.getText().toString().trim();
                StorageActivity storageActivity = StorageActivity.this;
                storageActivity.hideSoftKeyboard(storageActivity.edtSearch);
                StorageActivity.this.setSearch(trim);
                return true;
            }
        });
    }

    public void getDataList() {
        this.progressBar.setVisibility(0);
        new Thread(new Runnable() {
            public final void run() {
                StorageActivity.this.getList();
            }
        }).start();
    }

    public void onBackPressed() {
        if (this.loutStorageOption.getVisibility() == 0) {
            setStorageOptionClose();
        } else if (this.loutSearchBar.getVisibility() == 0) {
            setsearchBack();
        } else if (this.loutSelected.getVisibility() == 0) {
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

    public void setSearch(String str) {
        Log.e("searchText: ", str);
        this.storageList.clear();
        for (int i = 0; i < this.backUpstorageList.size(); i++) {
            if (this.backUpstorageList.get(i).getFileName().toLowerCase().contains(str.toLowerCase())) {
                this.storageList.add(this.backUpstorageList.get(i));
            }
        }
        StorageAdapter storageAdapter = this.adapter;
        if (storageAdapter != null) {
            storageAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.btn_internal_storage, R.id.btn_sd_card, R.id.iv_back, R.id.iv_back_search, R.id.iv_clear, R.id.iv_close, R.id.iv_list_grid, R.id.iv_more, R.id.iv_search, R.id.ll_check_all, R.id.ll_favourite, R.id.lout_compress, R.id.lout_copy, R.id.lout_delete, R.id.lout_extract, R.id.lout_more, R.id.lout_move, R.id.lout_past, R.id.lout_past_cancel, R.id.lout_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_internal_storage /*2131296418*/:
                this.rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                this.arrayListFilePaths.clear();
                this.arrayListFilePaths.add(this.rootPath);
                refreshData();
                setStorageOptionClose();
                return;
            case R.id.btn_sd_card /*2131296428*/:
                this.rootPath = Utils.getExternalStoragePath(this, true);
                this.arrayListFilePaths.clear();
                this.arrayListFilePaths.add(this.rootPath);
                refreshData();
                setStorageOptionClose();
                return;
            case R.id.iv_back /*2131296682*/:
                onBackPressed();
                return;
            case R.id.iv_back_search /*2131296683*/:
                setsearchBack();
                return;
            case R.id.iv_clear /*2131296688*/:
                this.edtSearch.getText().clear();
                this.ivClear.setVisibility(8);
                setClear();
                if (!((InputMethodManager) getSystemService("input_method")).isAcceptingText()) {
                    showKeyboard();
                    return;
                }
                return;
            case R.id.iv_close /*2131296689*/:
                setSelectionClose();
                return;
            case R.id.iv_list_grid /*2131296702*/:
                if (this.isGrid) {
                    this.isGrid = false;
                    PreferencesManager.saveToDirList_Grid(this, false);
                    setRecyclerViewData();
                    this.ivListGrid.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid));
                    return;
                }
                this.isGrid = true;
                PreferencesManager.saveToDirList_Grid(this, true);
                setRecyclerViewData();
                this.ivListGrid.setImageDrawable(getResources().getDrawable(R.drawable.ic_list));
                return;
            case R.id.iv_more /*2131296703*/:
                setMoreMenu();
                return;
            case R.id.iv_search /*2131296709*/:
                this.loutToolbar.setVisibility(8);
                this.loutSearchBar.setVisibility(0);
                showKeyboard();
                this.backUpstorageList = new ArrayList<>();
                for (int i = 0; i < this.storageList.size(); i++) {
                    this.backUpstorageList.add(this.storageList.get(i));
                }
                return;
            case R.id.ll_check_all /*2131296746*/:
                if (this.isCheckAll) {
                    this.isCheckAll = false;
                    selectEvent(false);
                    this.ivCheckAll.setVisibility(8);
                    this.ivUncheck.setVisibility(0);
                    this.loutSelected.setVisibility(8);
                    this.loutToolbar.setVisibility(0);
                    return;
                }
                this.isCheckAll = true;
                selectEvent(true);
                this.ivCheckAll.setVisibility(0);
                this.ivUncheck.setVisibility(8);
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
            case R.id.lout_extract /*2131296770*/:
                showExtractDialog();
                return;
            case R.id.lout_more /*2131296774*/:
                showMoreOptionBottom();
                return;
            case R.id.lout_move /*2131296776*/:
                Constant.isCopyData = false;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_past /*2131296777*/:
                ArrayList<File> arrayList = this.pastList;
                if (arrayList != null && arrayList.size() != 0) {
                    if (this.isFileFromSdCard) {
                        this.sdCardPermissionType = 2;
                        if (StorageUtils.checkFSDCardPermission(new File(this.sdCardPath), this) == 2) {
                            Toast.makeText(this, "Please give a permission for manager operation", 0).show();
                            return;
                        } else {
                            setCopyMoveAction();
                            return;
                        }
                    } else {
                        setCopyMoveAction();
                        return;
                    }
                } else {
                    return;
                }
            case R.id.lout_past_cancel /*2131296778*/:
                Constant.pastList = new ArrayList<>();
                this.pastList = new ArrayList<>();
                this.llPasteOption.setVisibility(8);
                if (this.storage_type.equalsIgnoreCase("CopyMove")) {
                    finish();
                    return;
                }
                return;
            case R.id.lout_send /*2131296787*/:
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
        int i = 0;
        for (int i2 = 0; i2 < this.storageList.size(); i2++) {
            if (this.storageList.get(i2).isSelected() && this.storageList.get(i2).isFavorite()) {
                this.storageList.get(i2).setFavorite(false);
                i++;
                if (favouriteList.contains(this.storageList.get(i2).getFilePath())) {
                    favouriteList.remove(this.storageList.get(i2).getFilePath());
                }
            }
            this.storageList.get(i2).setSelected(false);
            this.storageList.get(i2).setCheckboxVisible(false);
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
                    StorageActivity.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    StorageActivity.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    StorageActivity.this.sendFile();
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
                    Toast.makeText(StorageActivity.this, "New name can't be empty.", 0).show();
                } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                    dialog.show();
                } else if (!file.isDirectory()) {
                    String[] split = appCompatEditText.getText().toString().split("\\.");
                    if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                        final Dialog dialog = new Dialog(StorageActivity.this, R.style.WideDialog);
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
                                StorageActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                            }
                        });
                        dialog.show();
                        return;
                    }
                    Log.e("", "rename");
                    dialog.dismiss();
                    StorageActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                } else {
                    dialog.dismiss();
                    StorageActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                }
            }
        });
        dialog.show();
    }

    public void setCopyMoveAction() {
        if (Constant.isCopyData) {
            ProgressDialog progressDialog = this.loadingDialog;
            if (progressDialog != null) {
                progressDialog.setMessage("Copy file");
                this.loadingDialog.show();
            }
            new Thread(new Runnable() {
                public void run() {
                    StorageActivity.this.copyFile();
                }
            }).start();
            return;
        }
        ProgressDialog progressDialog2 = this.loadingDialog;
        if (progressDialog2 != null) {
            progressDialog2.setMessage("Move file");
            this.loadingDialog.show();
        }
        new Thread(new Runnable() {
            public void run() {
                StorageActivity.this.moveFile();
            }
        }).start();
    }

    
    public void moveFile() {
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < this.pastList.size(); i++) {
            File file = this.pastList.get(i);
            if (file.isDirectory()) {
                file.getPath();
            } else {
                file.getParent();
            }
            File file2 = new File(this.rootPath);
            File file3 = this.pastList.get(i);
            try {
                if (file.isDirectory()) {
                    File file4 = new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file3.getName());
                    if (StorageUtils.moveFile(file3, file4, this)) {
                        arrayList.add(file4);
                        arrayList2.add(file4.getPath());
                    }
                    Log.e("move", "file is move");
                } else {
                    File file5 = new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file3.getName());
                    if (file5.exists()) {
                        String[] split = file3.getName().split("\\.");
                        String str = split[0];
                        String str2 = split[1];
                        File file6 = new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + str + "_" + System.currentTimeMillis() + "." + str2);
                        if (StorageUtils.moveFile(file3, file6, this)) {
                            arrayList.add(file6);
                            arrayList2.add(file.getPath());
                        }
                    } else if (StorageUtils.moveFile(file3, file5, this)) {
                        arrayList.add(file5);
                        arrayList2.add(file.getPath());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    public void run() {
                        if (StorageActivity.this.loadingDialog != null && StorageActivity.this.loadingDialog.isShowing()) {
                            StorageActivity.this.loadingDialog.dismiss();
                        }
                        Constant.pastList = new ArrayList<>();
                        StorageActivity.this.pastList = new ArrayList<>();
                        StorageActivity.this.llPasteOption.setVisibility(8);
                        Toast.makeText(StorageActivity.this, "Move file successfully", 0).show();
                        if (StorageActivity.this.storage_type.equalsIgnoreCase("CopyMove")) {
                            RxBus.getInstance().post(new CopyMoveEvent(arrayList, 2, arrayList2));
                            StorageActivity.this.finish();
                            return;
                        }
                        StorageActivity.this.refreshData();
                        StorageActivity.this.storageList.clear();
                        StorageActivity.this.getFilesList((String) StorageActivity.this.arrayListFilePaths.get(StorageActivity.this.arrayListFilePaths.size() - 1));
                        StorageActivity.this.adapter.notifyDataSetChanged();
                        if (StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0) {
                            StorageActivity.this.recyclerView.setVisibility(8);
                            StorageActivity.this.llEmpty.setVisibility(0);
                            return;
                        }
                        StorageActivity.this.recyclerView.setVisibility(0);
                        StorageActivity.this.llEmpty.setVisibility(8);
                    }
                }, 30);
            }
        });
    }

    
    public void copyFile() {
        File file = new File(this.rootPath);
        final ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.pastList.size(); i++) {
            File file2 = this.pastList.get(i);
            try {
                if (file2.isDirectory()) {
                    File file3 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file2.getName());
                    if (file3.exists()) {
                        this.folder_counter = 1;
                        File folderFile = folderFile(file2.getName(), file.getPath());
                        StorageUtils.copyFile(file2, folderFile, this);
                        arrayList.add(folderFile);
                    } else {
                        StorageUtils.copyFile(file2, file3, this);
                        arrayList.add(file3);
                    }
                } else {
                    File file4 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file2.getName());
                    if (file4.exists()) {
                        String[] split = file2.getName().split("\\.");
                        String str = split[0];
                        String str2 = split[1];
                        File file5 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + str + "_" + System.currentTimeMillis() + "." + str2);
                        StorageUtils.copyFile(file2, file5, this);
                        arrayList.add(file5);
                    } else {
                        StorageUtils.copyFile(file2, file4, this);
                        arrayList.add(file4);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Constant.pastList = new ArrayList<>();
                        StorageActivity.this.pastList = new ArrayList<>();
                        RxBus.getInstance().post(new CopyMoveEvent(arrayList, 1, new ArrayList()));
                        if (StorageActivity.this.storage_type.equalsIgnoreCase("CopyMove")) {
                            if (StorageActivity.this.loadingDialog != null && StorageActivity.this.loadingDialog.isShowing()) {
                                StorageActivity.this.loadingDialog.dismiss();
                            }
                            StorageActivity.this.llPasteOption.setVisibility(8);
                            Toast.makeText(StorageActivity.this, "Copy file successfully", 0).show();
                            StorageActivity.this.finish();
                            return;
                        }
                        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                            public void run() {
                                if (StorageActivity.this.loadingDialog != null && StorageActivity.this.loadingDialog.isShowing()) {
                                    StorageActivity.this.loadingDialog.dismiss();
                                }
                                StorageActivity.this.llPasteOption.setVisibility(8);
                                Toast.makeText(StorageActivity.this, "Copy file successfully", 0).show();
                                StorageActivity.this.refreshData();
                                StorageActivity.this.storageList.clear();
                                StorageActivity.this.getFilesList((String) StorageActivity.this.arrayListFilePaths.get(StorageActivity.this.arrayListFilePaths.size() - 1));
                                StorageActivity.this.adapter.notifyDataSetChanged();
                                if (StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0) {
                                    StorageActivity.this.recyclerView.setVisibility(8);
                                    StorageActivity.this.llEmpty.setVisibility(0);
                                    return;
                                }
                                StorageActivity.this.recyclerView.setVisibility(0);
                                StorageActivity.this.llEmpty.setVisibility(8);
                            }
                        }, 50);
                    }
                }, 20);
            }
        });
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

    
    public void refreshData() {
        ArrayList<String> arrayList;
        ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
        if (arrayList2 != null && arrayList2.size() != 0 && (arrayList = this.arrayListFilePaths) != null && arrayList.size() != 0) {
            this.storageList.clear();
            ArrayList<String> arrayList3 = this.arrayListFilePaths;
            getFilesList(arrayList3.get(arrayList3.size() - 1));
            this.adapter.notifyDataSetChanged();
            ArrayList<InternalStorageFilesModel> arrayList4 = this.storageList;
            if (arrayList4 == null || arrayList4.size() == 0) {
                this.recyclerView.setVisibility(8);
                this.llEmpty.setVisibility(0);
                return;
            }
            this.recyclerView.setVisibility(0);
            this.llEmpty.setVisibility(8);
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
        this.llPasteOption.setVisibility(0);
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
        safedk_StorageActivity_startActivity_cce8c718314155b16e3552877ac6d373(this, Intent.createChooser(intent, "Share with..."));
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (StorageActivity.this.isFileFromSdCard) {
                    StorageActivity.this.sdCardPermissionType = 1;
                    if (StorageUtils.checkFSDCardPermission(new File(StorageActivity.this.sdCardPath), StorageActivity.this) == 2) {
                        Toast.makeText(StorageActivity.this, "Please give a permission for manager operation", 0).show();
                    } else {
                        StorageActivity.this.setDeleteFile();
                    }
                } else {
                    StorageActivity.this.setDeleteFile();
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
                StorageActivity.this.deleteFile();
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

    private void setsearchBack() {
        View currentFocus;
        this.edtSearch.getText().clear();
        this.ivClear.setVisibility(8);
        if (((InputMethodManager) getSystemService("input_method")).isAcceptingText() && (currentFocus = getCurrentFocus()) != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        setClear();
        this.loutSearchBar.setVisibility(8);
        this.loutToolbar.setVisibility(0);
    }

    private void setClear() {
        this.storageList.clear();
        if (this.backUpstorageList != null) {
            for (int i = 0; i < this.backUpstorageList.size(); i++) {
                this.storageList.add(this.backUpstorageList.get(i));
            }
        }
        StorageAdapter storageAdapter = this.adapter;
        if (storageAdapter != null) {
            storageAdapter.notifyDataSetChanged();
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_create_folder) {
                    StorageActivity.this.showCreateFolder();
                } else if (itemId == R.id.menu_hidden) {
                    if (StorageActivity.this.isShowHidden) {
                        StorageActivity.this.isShowHidden = false;
                    } else {
                        StorageActivity.this.isShowHidden = true;
                    }
                    StorageActivity storageActivity = StorageActivity.this;
                    PreferencesManager.saveToShowHidden(storageActivity, storageActivity.isShowHidden);
                    StorageActivity.this.storageList.clear();
                    StorageActivity storageActivity2 = StorageActivity.this;
                    storageActivity2.getFilesList(storageActivity2.rootPath);
                } else if (itemId == R.id.menu_sort) {
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(StorageActivity.this);
                    bottomSheetFragment.show(StorageActivity.this.getSupportFragmentManager(), bottomSheetFragment.getTag());
                }
                return false;
            }
        });
        popupMenu.show();
    }

    
    public void showCreateFolder() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_create_folder);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(17);
        final AppCompatEditText appCompatEditText = (AppCompatEditText) dialog.findViewById(R.id.edt_file_name);
        ((LinearLayout) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!appCompatEditText.getText().toString().isEmpty()) {
                    String obj = appCompatEditText.getText().toString();
                    File file = new File(StorageActivity.this.rootPath + InternalZipConstants.ZIP_FILE_SEPARATOR + obj);
                    if (file.exists()) {
                        dialog.dismiss();
                        Toast.makeText(StorageActivity.this, "This file already exists", 1).show();
                        return;
                    }
                    dialog.dismiss();
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    StorageActivity.this.storageList.clear();
                    StorageActivity storageActivity = StorageActivity.this;
                    storageActivity.getFilesList(storageActivity.rootPath);
                    StorageActivity.this.adapter.notifyDataSetChanged();
                    return;
                }
                Toast.makeText(StorageActivity.this, "Enter folder name", 0).show();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setSortMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.ivMore);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_big_to_small /*2131296820*/:
                        if (!(StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0)) {
                            StorageActivity.this.sortSizeDescending();
                            StorageActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(StorageActivity.this, 3);
                            break;
                        }
                    case R.id.menu_name_ascending /*2131296828*/:
                        if (!(StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0)) {
                            StorageActivity.this.sortNameAscending();
                            StorageActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(StorageActivity.this, 1);
                            break;
                        }
                    case R.id.menu_name_descending /*2131296829*/:
                        if (!(StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0)) {
                            StorageActivity.this.sortNameDescending();
                            StorageActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(StorageActivity.this, 2);
                            break;
                        }
                    case R.id.menu_small_to_big /*2131296834*/:
                        if (!(StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0)) {
                            StorageActivity.this.sortSizeAscending();
                            StorageActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(StorageActivity.this, 4);
                            break;
                        }
                    case R.id.menu_time_newest /*2131296836*/:
                        if (!(StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0)) {
                            StorageActivity.this.setDateWiseSortAs(true);
                            StorageActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(StorageActivity.this, 5);
                            break;
                        }
                    case R.id.menu_time_oldest /*2131296837*/:
                        if (!(StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0)) {
                            StorageActivity.this.setDateWiseSortAs(false);
                            StorageActivity.this.adapter.notifyDataSetChanged();
                            PreferencesManager.saveToSortType(StorageActivity.this, 6);
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
        String str = this.rootPath;
        this.arrayListFilePaths.add(str);
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (this.isShowHidden) {
                    InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                    internalStorageFilesModel.setFileName(file.getName());
                    internalStorageFilesModel.setFilePath(file.getPath());
                    if (favouriteList.contains(file.getPath())) {
                        internalStorageFilesModel.setFavorite(true);
                    } else {
                        internalStorageFilesModel.setFavorite(false);
                    }
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
                StorageActivity.this.progressBar.setVisibility(8);
                StorageActivity.this.setHeaderData();
                StorageActivity.this.setRecyclerViewData();

            }
        });
    }

    
    public void getFilesList(String str) {
        new ArrayList();
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        this.rootPath = str;
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
        StorageAdapter storageAdapter = this.adapter;
        if (storageAdapter != null) {
            storageAdapter.notifyDataSetChanged();
        }
        HeaderListAdapter headerListAdapter = this.pathAdapter;
        if (headerListAdapter != null) {
            headerListAdapter.notifyDataSetChanged();
            setToPathPosition();
        }
        ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
        if (arrayList2 == null || arrayList2.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
        } else {
            this.recyclerView.setVisibility(0);
            this.llEmpty.setVisibility(8);
        }
        if (this.loutSearchBar.getVisibility() == 0) {
            this.backUpstorageList = new ArrayList<>();
            for (int i = 0; i < this.storageList.size(); i++) {
                this.backUpstorageList.add(this.storageList.get(i));
            }
        }
    }

    private void setToPathPosition() {
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
            safedk_StorageActivity_startActivityForResult_d04f8bfa4e74ebb540fb51c5a9b1220d(this, intent, videoImage_code);
        } else if (mineType != null && (mineType.equalsIgnoreCase("video/mp4") || mineType.equalsIgnoreCase("video/x-matroska"))) {
            PhotoData photoData2 = new PhotoData();
            photoData2.setFileName(internalStorageFilesModel.getFileName());
            photoData2.setFilePath(internalStorageFilesModel.getFilePath());
            Constant.displayVideoList = new ArrayList();
            Constant.displayVideoList.add(photoData2);
            Intent intent2 = new Intent(this, VideoPlayActivity.class);
            intent2.putExtra("pos", 0);
            Constant.arrayListFilePaths = this.arrayListFilePaths;
            safedk_StorageActivity_startActivityForResult_d04f8bfa4e74ebb540fb51c5a9b1220d(this, intent2, videoImage_code);
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
                safedk_StorageActivity_startActivity_cce8c718314155b16e3552877ac6d373(this, intent3);
            } catch (Exception unused) {
            }
        } else if (mineType == null || !mineType.equalsIgnoreCase("application/zip")) {
            Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent4 = new Intent();
            intent4.setAction("android.intent.action.VIEW");
            intent4.setDataAndType(uriForFile, Utils.getMimeTypeFromFilePath(file.getPath()));
            intent4.addFlags(1);
            safedk_StorageActivity_startActivity_cce8c718314155b16e3552877ac6d373(this, Intent.createChooser(intent4, "Open with"));
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
            safedk_StorageActivity_startActivityForResult_d04f8bfa4e74ebb540fb51c5a9b1220d(this, intent5, zip_open);
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
            StorageAdapter storageAdapter = new StorageAdapter(this, this.storageList, this.isGrid);
            this.adapter = storageAdapter;
            this.recyclerView.setAdapter(storageAdapter);
        } else {
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams2.setMargins(0, 0, 0, 0);
            this.recyclerView.setLayoutParams(layoutParams2);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            StorageAdapter storageAdapter2 = new StorageAdapter(this, this.storageList, this.isGrid);
            this.adapter = storageAdapter2;
            this.recyclerView.setAdapter(storageAdapter2);
        }
        this.adapter.setOnItemClickListener(new StorageAdapter.ClickListener() {
            public void onItemClick(int i, View view) {
                if (StorageActivity.this.storageList.get(i).isCheckboxVisible()) {
                    if (StorageActivity.this.storageList.get(i).isSelected()) {
                        StorageActivity.this.storageList.get(i).setSelected(false);
                    } else {
                        StorageActivity.this.storageList.get(i).setSelected(true);
                    }
                    StorageActivity.this.adapter.notifyDataSetChanged();
                    StorageActivity.this.setSelectedFile();
                    return;
                }
                InternalStorageFilesModel internalStorageFilesModel = StorageActivity.this.storageList.get(i);
                File file = new File(internalStorageFilesModel.getFilePath());
                if (StorageActivity.this.loutSearchBar.getVisibility() == 0 && ((InputMethodManager) StorageActivity.this.getSystemService("input_method")).isAcceptingText()) {
                    StorageActivity storageActivity = StorageActivity.this;
                    storageActivity.hideSoftKeyboard(storageActivity.edtSearch);
                }
                StorageActivity.this.openFile(file, internalStorageFilesModel);
            }
        });
        this.adapter.setOnLongClickListener(new StorageAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                if (StorageActivity.this.llPasteOption.getVisibility() == 8) {
                    StorageActivity.this.storageList.get(i).setSelected(true);
                    for (int i2 = 0; i2 < StorageActivity.this.storageList.size(); i2++) {
                        if (StorageActivity.this.storageList.get(i2) != null) {
                            StorageActivity.this.storageList.get(i2).setCheckboxVisible(true);
                        }
                    }
                    StorageActivity.this.adapter.notifyDataSetChanged();
                    StorageActivity.this.setSelectedFile();
                }
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
            this.llBottomOption.setVisibility(8);
            OnSelected(true, false, i);
            setSelectionClose();
        } else {
            this.llBottomOption.setVisibility(0);
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
            HeaderListAdapter headerListAdapter = new HeaderListAdapter(this, this.arrayListFilePaths);
            this.pathAdapter = headerListAdapter;
            this.rvHeader.setAdapter(headerListAdapter);
            this.pathAdapter.setOnItemClickListener(new HeaderListAdapter.ClickListener() {
                public void onItemClick(int i, View view) {
                    Log.e("path seleted:", (String) StorageActivity.this.arrayListFilePaths.get(i));
                    Log.e("onItemClick", "position: " + i);
                    int size = StorageActivity.this.arrayListFilePaths.size();
                    while (true) {
                        size--;
                        if (size > i) {
                            Log.e("onItemClick", "remove index: " + size);
                            StorageActivity.this.arrayListFilePaths.remove(size);
                        } else {
                            StorageActivity.this.storageList.clear();
                            StorageActivity storageActivity = StorageActivity.this;
                            storageActivity.getFilesList((String) storageActivity.arrayListFilePaths.get(StorageActivity.this.arrayListFilePaths.size() - 1));
                            return;
                        }
                    }
                }

                public void onItemHeaderClick(int i, View view) {
                    if (!StorageActivity.this.isSdCard) {
                        for (int size = StorageActivity.this.arrayListFilePaths.size() - 1; size > i; size += -1) {
                            Log.e("onItemClick", "remove index: " + size);
                            StorageActivity.this.arrayListFilePaths.remove(size);
                        }
                        StorageActivity.this.storageList.clear();
                        StorageActivity storageActivity = StorageActivity.this;
                        storageActivity.getFilesList((String) storageActivity.arrayListFilePaths.get(StorageActivity.this.arrayListFilePaths.size() - 1));
                    } else if (StorageActivity.this.loutStorageOption.getVisibility() == 0) {
                        StorageActivity.this.setStorageOptionClose();
                    } else {
                        StorageActivity.this.loutStorageOption.setVisibility(0);
                    }
                }

                public void onItemHomeClick(int i, View view) {
                    StorageActivity.this.finish();
                }
            });
        }
    }

    public void hideSoftKeyboard(EditText editText) {
        try {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).toggleSoftInputFromWindow(this.edtSearch.getApplicationWindowToken(), 2, 0);
        this.edtSearch.requestFocus();
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
            this.storageList.get(this.pos).setFilePath(file2.getPath());
            this.storageList.get(this.pos).setFileName(file2.getName());
            this.adapter.notifyItemChanged(this.pos);
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
                StorageActivity.this.OnSelected(true, false, 0);
                StorageActivity.this.llBottomOption.setVisibility(8);
                if (StorageActivity.this.adapter != null) {
                    StorageActivity.this.adapter.notifyDataSetChanged();
                }
                if (StorageActivity.this.loadingDialog != null && StorageActivity.this.loadingDialog.isShowing()) {
                    StorageActivity.this.loadingDialog.dismiss();
                }
                if (StorageActivity.this.storageList == null || StorageActivity.this.storageList.size() == 0) {
                    StorageActivity.this.recyclerView.setVisibility(8);
                    StorageActivity.this.llEmpty.setVisibility(0);
                } else {
                    StorageActivity.this.recyclerView.setVisibility(0);
                    StorageActivity.this.llEmpty.setVisibility(8);
                }
                Toast.makeText(StorageActivity.this, "Delete file successfully", 0).show();
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
                    StorageActivity.this.updateDeleteImageData(displayDeleteEvent.getDeleteList());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void displayFavoriteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayFavoriteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DisplayFavoriteEvent>() {
            public void call(DisplayFavoriteEvent displayFavoriteEvent) {
                if (displayFavoriteEvent.getFilePath() != null && !displayFavoriteEvent.getFilePath().equalsIgnoreCase("")) {
                    StorageActivity.this.setUpdateFavourite(displayFavoriteEvent.isFavorite(), displayFavoriteEvent.getFilePath());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void setUpdateFavourite(boolean z, String str) {
        ArrayList<InternalStorageFilesModel> arrayList = this.backUpstorageList;
        int i = 0;
        if (arrayList != null && arrayList.size() != 0) {
            int i2 = 0;
            while (true) {
                if (i2 < this.backUpstorageList.size()) {
                    InternalStorageFilesModel internalStorageFilesModel = this.backUpstorageList.get(i2);
                    if (internalStorageFilesModel.getFilePath() != null && internalStorageFilesModel.getFilePath().equalsIgnoreCase(str)) {
                        internalStorageFilesModel.setFavorite(z);
                        break;
                    }
                    i2++;
                } else {
                    break;
                }
            }
        }
        ArrayList<InternalStorageFilesModel> arrayList2 = this.storageList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            while (i < this.storageList.size()) {
                InternalStorageFilesModel internalStorageFilesModel2 = this.storageList.get(i);
                if (internalStorageFilesModel2.getFilePath() == null || !internalStorageFilesModel2.getFilePath().equalsIgnoreCase(str)) {
                    i++;
                } else {
                    internalStorageFilesModel2.setFavorite(z);
                    StorageAdapter storageAdapter = this.adapter;
                    if (storageAdapter != null) {
                        storageAdapter.notifyItemChanged(i);
                        return;
                    }
                    return;
                }
            }
        }
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
            StorageAdapter storageAdapter = this.adapter;
            if (storageAdapter != null) {
                storageAdapter.notifyDataSetChanged();
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
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.btn_cancel);
        LinearLayout linearLayout2 = (LinearLayout) dialog.findViewById(R.id.btn_ok);
        ((AppCompatTextView) dialog.findViewById(R.id.txt_title)).setText("Extract file");
        appCompatEditText.setHint("Enter extract file name");
        this.extractPath = this.rootPath;
        String str = this.sdCardPath;
        if (str != null && !str.equalsIgnoreCase("") && this.rootPath.contains(this.sdCardPath)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.extract_file));
            if (!file2.exists()) {
                file2.mkdirs();
            }
            this.extractPath = file2.getPath();
        }
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!appCompatEditText.getText().toString().isEmpty()) {
                    String obj = appCompatEditText.getText().toString();
                    if (new File(StorageActivity.this.extractPath + InternalZipConstants.ZIP_FILE_SEPARATOR + obj).exists()) {
                        Toast.makeText(StorageActivity.this, "File name already use", 0).show();
                        return;
                    }
                    StorageActivity.this.extract_file_name = obj;
                    dialog.dismiss();
                    if (StorageActivity.this.isFileFromSdCard) {
                        StorageActivity.this.sdCardPermissionType = 4;
                        if (StorageUtils.checkFSDCardPermission(new File(StorageActivity.this.sdCardPath), StorageActivity.this) == 2) {
                            Toast.makeText(StorageActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            StorageActivity.this.setExtract();
                        }
                    } else {
                        StorageActivity.this.setExtract();
                    }
                } else {
                    StorageActivity storageActivity = StorageActivity.this;
                    Toast.makeText(storageActivity, storageActivity.getResources().getString(R.string.extract_validation), 0).show();
                }
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
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
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.btn_cancel);
        LinearLayout linearLayout2 = (LinearLayout) dialog.findViewById(R.id.btn_ok);
        this.compressPath = this.rootPath;
        String str = this.sdCardPath;
        if (str != null && !str.equalsIgnoreCase("") && this.rootPath.contains(this.sdCardPath)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.compress_file));
            if (!file2.exists()) {
                file2.mkdirs();
            }
            this.compressPath = file2.getPath();
        }
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!appCompatEditText.getText().toString().isEmpty()) {
                    String str = appCompatEditText.getText().toString().split("\\.")[0];
                    if (new File(StorageActivity.this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(StorageActivity.this, "File name already use", 0).show();
                        return;
                    }
                    StorageActivity.this.zip_file_name = str;
                    dialog.dismiss();
                    if (StorageActivity.this.isFileFromSdCard) {
                        StorageActivity.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(StorageActivity.this.sdCardPath), StorageActivity.this) == 2) {
                            Toast.makeText(StorageActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            StorageActivity.this.setcompress();
                        }
                    } else {
                        StorageActivity.this.setcompress();
                    }
                } else {
                    StorageActivity storageActivity = StorageActivity.this;
                    Toast.makeText(storageActivity, storageActivity.getResources().getString(R.string.zip_validation), 0).show();
                }
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
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
        new Thread(new Runnable() {
            public final void run() {
                StorageActivity.this.extractfile();
            }
        }).start();
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
                StorageActivity.this.setSelectionClose();
                if (path != null) {
                    StorageActivity.this.storageList.clear();
                    StorageActivity storageActivity = StorageActivity.this;
                    storageActivity.getFilesList(storageActivity.rootPath);
                    if (StorageActivity.this.loadingDialog.isShowing()) {
                        StorageActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(StorageActivity.this, "Extract file successfully", 0).show();
                    MediaScannerConnection.scanFile(StorageActivity.this, new String[]{path}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
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
        new Thread(new Runnable() {
            public final void run() {
                try {
                    StorageActivity.this.compressfile();
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
                StorageActivity.this.setSelectionClose();
                if (str != null) {
                    MediaScannerConnection.scanFile(StorageActivity.this, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str));
                    if (StorageActivity.this.selected_Item != 1) {
                        boolean deleteFile = StorageUtils.deleteFile(file2, StorageActivity.this);
                        if (file != null && StorageUtils.deleteFile(file, StorageActivity.this)) {
                            MediaScannerConnection.scanFile(StorageActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                        if (deleteFile) {
                            MediaScannerConnection.scanFile(StorageActivity.this, new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                    }
                    StorageActivity.this.storageList.clear();
                    StorageActivity storageActivity = StorageActivity.this;
                    storageActivity.getFilesList(storageActivity.rootPath);
                    if (StorageActivity.this.loadingDialog.isShowing()) {
                        StorageActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(StorageActivity.this, "Compress file successfully", 0).show();
                }
            }
        });
    }
}
