package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import androidx.appcompat.widget.Toolbar;
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
import com.io.filemanager.freefileexplorer.easily.adapter.StorageAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayFavoriteEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.InternalStorageFilesModel;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
import com.io.filemanager.freefileexplorer.easily.utils.StorageUtils;
import com.io.filemanager.freefileexplorer.easily.utils.Utils;
/*import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.safedk.android.analytics.brandsafety.creatives.infos.CreativeInfo;
import com.safedk.android.analytics.events.RedirectEvent;
import com.safedk.android.utils.Logger;*/
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import net.lingala.zip4j.util.InternalZipConstants;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    static final boolean $assertionsDisabled = false;
    StorageAdapter adapter;
    @BindView(R.id.edt_search)
    EditText edtSearch;
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
    boolean isFileFromSdCard = false;
    boolean isLaodBanner = false;
    @BindView(R.id.iv_back_search)
    ImageView ivBackSearch;
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
    int pos = 0;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    String rootPath;
    String sdCardPath;
    int sdCardPermissionType = 0;
    ArrayList<InternalStorageFilesModel> searchDataList = new ArrayList<>();
    ArrayList<InternalStorageFilesModel> searchList = new ArrayList<>();
    int selected_Item = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

    public static void safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(SearchActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_search);
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

    public void intView() {
        this.searchList = new ArrayList<>();
        this.ivUncheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
        this.ivCheckAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        this.ivFavFill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_fill));
        this.ivFavUnfill.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_unfill));
        this.imgMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_bottom));
        setSearchBarData();
        setAdapter();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.compress_file));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        this.rootPath = file2.getPath();
        this.progressBar.setVisibility(0);
        new Thread(new Runnable() {
            public final void run() {
                SearchActivity.this.getDataList();
            }
        }).start();
        this.sdCardPath = Utils.getExternalStoragePath(this, true);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);
        copyMoveEvent();
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
                SearchActivity.this.setSearch(charSequence2);
                if (i3 == 0) {
                    SearchActivity.this.ivClear.setVisibility(8);
                } else {
                    SearchActivity.this.ivClear.setVisibility(0);
                }
            }
        });
        this.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 3) {
                    return false;
                }
                Log.e("TAG", "edtSearch: " + SearchActivity.this.edtSearch.getText().toString());
                if (SearchActivity.this.edtSearch.getText().toString().isEmpty() || SearchActivity.this.edtSearch.getText().toString().trim().length() == 0) {
                    Toast.makeText(SearchActivity.this, "Enter file name", 0).show();
                    return true;
                }
                String trim = SearchActivity.this.edtSearch.getText().toString().trim();
                SearchActivity searchActivity = SearchActivity.this;
                searchActivity.hideSoftKeyboard(searchActivity.edtSearch);
                SearchActivity.this.setSearch(trim);
                return true;
            }
        });
    }

    public void setSearch(String str) {
        Log.e("searchText: ", str);
        this.searchDataList.clear();
        for (int i = 0; i < this.searchList.size(); i++) {
            if (this.searchList.get(i).getFileName().toLowerCase().contains(str.toLowerCase())) {
                this.searchDataList.add(this.searchList.get(i));
            }
        }
        StorageAdapter storageAdapter = this.adapter;
        if (storageAdapter != null) {
            storageAdapter.notifyDataSetChanged();
        } else {
            setAdapter();
        }
        ArrayList<InternalStorageFilesModel> arrayList = this.searchDataList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
    }

    public void getDataList() {
        getSearchList();
        runOnUiThread(new Runnable() {
            public void run() {
                SearchActivity.this.progressBar.setVisibility(8);
                if (SearchActivity.this.searchDataList == null || SearchActivity.this.searchDataList.size() == 0) {
                    SearchActivity.this.recyclerView.setVisibility(8);
                    SearchActivity.this.llEmpty.setVisibility(0);
                    return;
                }
                SearchActivity.this.recyclerView.setVisibility(0);
                SearchActivity.this.llEmpty.setVisibility(8);
            }
        });
    }

    public void setAdapter() {
        this.progressBar.setVisibility(8);
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StorageAdapter storageAdapter = new StorageAdapter(this, this.searchDataList, false);
        this.adapter = storageAdapter;
        this.recyclerView.setAdapter(storageAdapter);
        this.adapter.setOnItemClickListener(new StorageAdapter.ClickListener() {
            public void onItemClick(int i, View view) {
                if (SearchActivity.this.searchDataList.get(i).isCheckboxVisible()) {
                    if (SearchActivity.this.searchDataList.get(i).isSelected()) {
                        SearchActivity.this.searchDataList.get(i).setSelected(false);
                    } else {
                        SearchActivity.this.searchDataList.get(i).setSelected(true);
                    }
                    SearchActivity.this.adapter.notifyDataSetChanged();
                    SearchActivity.this.setSelectedFile();
                    return;
                }
                InternalStorageFilesModel internalStorageFilesModel = SearchActivity.this.searchDataList.get(i);
                SearchActivity.this.openFile(new File(internalStorageFilesModel.getFilePath()), internalStorageFilesModel);
            }
        });
        this.adapter.setOnLongClickListener(new StorageAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                SearchActivity.this.searchDataList.get(i).setSelected(true);
                for (int i2 = 0; i2 < SearchActivity.this.searchDataList.size(); i2++) {
                    if (SearchActivity.this.searchDataList.get(i2) != null) {
                        SearchActivity.this.searchDataList.get(i2).setCheckboxVisible(true);
                    }
                }
                SearchActivity.this.adapter.notifyDataSetChanged();
                SearchActivity.this.setSelectedFile();
            }
        });
    }

    public void onBackPressed() {
        View currentFocus;
        if (this.loutSelected.getVisibility() == 0) {
            setSelectionClose();
            return;
        }
        if (((InputMethodManager) getSystemService("input_method")).isAcceptingText() && (currentFocus = getCurrentFocus()) != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        super.onBackPressed();
    }

    @OnClick({R.id.iv_back_search, R.id.iv_clear, R.id.iv_close, R.id.ll_check_all, R.id.ll_favourite, R.id.lout_compress, R.id.lout_copy, R.id.lout_delete, R.id.lout_more, R.id.lout_move, R.id.lout_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_search /*2131296683*/:
                onBackPressed();
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

    private void setUnFavourite() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        int i = 0;
        for (int i2 = 0; i2 < this.searchDataList.size(); i2++) {
            if (this.searchDataList.get(i2).isSelected() && this.searchDataList.get(i2).isFavorite()) {
                this.searchDataList.get(i2).setFavorite(false);
                i++;
                if (favouriteList.contains(this.searchDataList.get(i2).getFilePath())) {
                    favouriteList.remove(this.searchDataList.get(i2).getFilePath());
                }
            }
            this.searchDataList.get(i2).setSelected(false);
            this.searchDataList.get(i2).setCheckboxVisible(false);
        }
        runOnUiThread(new Runnable() {
            public void run() {
                SearchActivity.this.adapter.notifyDataSetChanged();
                SearchActivity.this.llBottomOption.setVisibility(8);
                SearchActivity.this.loutSelected.setVisibility(8);
                SearchActivity.this.loutToolbar.setVisibility(0);
            }
        });
        String str = i == 1 ? " item removed from Favourites." : " items removed from Favourites.";
        Toast.makeText(this, i + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
        ArrayList<InternalStorageFilesModel> arrayList = this.searchList;
        if (arrayList != null && arrayList.size() != 0) {
            for (int i3 = 0; i3 < this.searchList.size(); i3++) {
                if (favouriteList != null) {
                    try {
                        if (favouriteList.contains(this.searchList.get(i3).getFilePath())) {
                            this.searchList.get(i3).setFavorite(true);
                        } else {
                            this.searchList.get(i3).setFavorite(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setFavourite() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        int i = 0;
        for (int i2 = 0; i2 < this.searchDataList.size(); i2++) {
            if (this.searchDataList.get(i2).isSelected()) {
                if (!this.searchDataList.get(i2).isFavorite()) {
                    favouriteList.add(0, this.searchDataList.get(i2).getFilePath());
                    i++;
                }
                this.searchDataList.get(i2).setFavorite(true);
            }
            this.searchDataList.get(i2).setSelected(false);
            this.searchDataList.get(i2).setCheckboxVisible(false);
        }
        runOnUiThread(new Runnable() {
            public void run() {
                SearchActivity.this.adapter.notifyDataSetChanged();
                SearchActivity.this.llBottomOption.setVisibility(8);
                SearchActivity.this.loutSelected.setVisibility(8);
                SearchActivity.this.loutToolbar.setVisibility(0);
            }
        });
        String str = i == 1 ? " item added to Favourites." : " items added to Favourites.";
        Toast.makeText(this, i + str, 0).show();
        PreferencesManager.setFavouriteList(this, favouriteList);
        ArrayList<InternalStorageFilesModel> arrayList = this.searchList;
        if (arrayList != null && arrayList.size() != 0) {
            for (int i3 = 0; i3 < this.searchList.size(); i3++) {
                if (favouriteList != null) {
                    try {
                        if (favouriteList.contains(this.searchList.get(i3).getFilePath())) {
                            this.searchList.get(i3).setFavorite(true);
                        } else {
                            this.searchList.get(i3).setFavorite(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setCopyMoveOptinOn() {
        Constant.isFileFromSdCard = this.isFileFromSdCard;
        Constant.pastList = new ArrayList<>();
        for (int i = 0; i < this.searchDataList.size(); i++) {
            if (this.searchDataList.get(i).isSelected()) {
                File file = new File(this.searchDataList.get(i).getFilePath());
                if (file.exists()) {
                    Constant.pastList.add(file);
                }
            }
        }
        setSelectionClose();
        Intent intent = new Intent(this, StorageActivity.class);
        intent.putExtra("type", "CopyMove");
        //safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(this, intent);
        CallAds(intent);
    }

    private void CallAds(Intent intent) {
        SplashLaunchActivity.InterstitialAdsCall(this, intent);
    }

    private void setClear() {
        this.searchDataList.clear();
        if (this.searchList != null) {
            for (int i = 0; i < this.searchList.size(); i++) {
                this.searchDataList.add(this.searchList.get(i));
            }
        }
        StorageAdapter storageAdapter = this.adapter;
        if (storageAdapter != null) {
            storageAdapter.notifyDataSetChanged();
        } else {
            setAdapter();
        }
        ArrayList<InternalStorageFilesModel> arrayList = this.searchDataList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
    }

    
    public void setSelectionClose() {
        this.isCheckAll = false;
        this.ivCheckAll.setVisibility(8);
        for (int i = 0; i < this.searchDataList.size(); i++) {
            this.searchDataList.get(i).setSelected(false);
            this.searchDataList.get(i).setCheckboxVisible(false);
        }
        runOnUiThread(new Runnable() {
            public void run() {
                if (SearchActivity.this.adapter != null) {
                    SearchActivity.this.adapter.notifyDataSetChanged();
                }
                SearchActivity.this.llBottomOption.setVisibility(8);
                SearchActivity.this.loutSelected.setVisibility(8);
                SearchActivity.this.loutToolbar.setVisibility(0);
            }
        });
    }

    
    public void sendFile() {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        for (int i = 0; i < this.searchDataList.size(); i++) {
            if (this.searchDataList.get(i).isSelected() && !this.searchDataList.get(i).isDir()) {
                arrayList.add(FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(this.searchDataList.get(i).getFilePath())));
            }
        }
        intent.setType("*/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.addFlags(268435456);
        safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(this, Intent.createChooser(intent, "Share with..."));
    }

    private void selectEvent(boolean z) {
        if (z) {
            for (int i = 0; i < this.searchDataList.size(); i++) {
                this.searchDataList.get(i).setSelected(true);
            }
            this.adapter.notifyDataSetChanged();
            setSelectedFile();
            return;
        }
        for (int i2 = 0; i2 < this.searchDataList.size(); i2++) {
            this.searchDataList.get(i2).setSelected(false);
            this.searchDataList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                SearchActivity.this.sdCardPermissionType = 1;
                if (!SearchActivity.this.isFileFromSdCard) {
                    SearchActivity.this.setDeleteFile();
                } else if (StorageUtils.checkFSDCardPermission(new File(SearchActivity.this.sdCardPath), SearchActivity.this) == 2) {
                    Toast.makeText(SearchActivity.this, "Please give a permission for manager operation", 0).show();
                } else {
                    SearchActivity.this.setDeleteFile();
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

    
    public void setSelectedFile() {
        boolean z;
        boolean z2;
        String str;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        boolean z3 = false;
        boolean z4 = false;
        for (int i2 = 0; i2 < this.searchDataList.size(); i2++) {
            if (this.searchDataList.get(i2).isSelected()) {
                i++;
                this.pos = i2;
                if (this.searchDataList.get(i2).isDir()) {
                    z4 = true;
                }
                if (!z3 && (str = this.sdCardPath) != null && !str.equalsIgnoreCase("") && this.searchDataList.get(i2).getFilePath().contains(this.sdCardPath)) {
                    z3 = true;
                }
                if (this.searchDataList.get(i2).isFavorite()) {
                    arrayList.add(1);
                } else {
                    arrayList2.add(0);
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
            setInvisibleButton(this.loutExtract, this.imgExtract, this.txtTextExtract);
            this.ivFavUnfill.setVisibility(8);
            this.ivFavFill.setVisibility(8);
            this.ivFavUnfill.setVisibility(8);
            this.ivFavFill.setVisibility(8);
            this.llFavourite.setVisibility(8);
        } else {
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
        }
        if (z4) {
            this.imgSend.setColorFilter(ContextCompat.getColor(this, R.color.invisible_color), PorterDuff.Mode.SRC_IN);
            this.txtTextSend.setTextColor(getResources().getColor(R.color.invisible_color));
            this.loutSend.setClickable(false);
            this.loutSend.setEnabled(false);
            return;
        }
        this.imgSend.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        this.txtTextSend.setTextColor(getResources().getColor(R.color.black));
        this.loutSend.setClickable(true);
        this.loutSend.setEnabled(true);
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

    
    public void openFile(File file, InternalStorageFilesModel internalStorageFilesModel) {
        Uri uri;
        String mineType = internalStorageFilesModel.getMineType();
        if (file.isDirectory()) {
            return;
        }
        if (mineType != null && (mineType.equalsIgnoreCase("image/jpeg") || mineType.equalsIgnoreCase("image/png") || mineType.equalsIgnoreCase("image/webp"))) {
            PhotoData photoData = new PhotoData();
            photoData.setFileName(internalStorageFilesModel.getFileName());
            photoData.setFilePath(internalStorageFilesModel.getFilePath());
            photoData.setFavorite(internalStorageFilesModel.isFavorite());
            Constant.displayImageList = new ArrayList();
            Constant.displayImageList.add(photoData);
            Intent intent = new Intent(this, DisplayImageActivity.class);
            intent.putExtra("pos", 0);
            //safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(this, intent);
            CallAds(intent);
        } else if (mineType != null && (mineType.equalsIgnoreCase("video/mp4") || mineType.equalsIgnoreCase("video/x-matroska"))) {
            PhotoData photoData2 = new PhotoData();
            photoData2.setFileName(internalStorageFilesModel.getFileName());
            photoData2.setFilePath(internalStorageFilesModel.getFilePath());
            Constant.displayVideoList = new ArrayList();
            Constant.displayVideoList.add(photoData2);
            Intent intent2 = new Intent(this, VideoPlayActivity.class);
            intent2.putExtra("pos", 0);
            //safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(this, intent2);
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
                safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(this, intent3);
            } catch (Exception unused) {
            }
        } else if (mineType == null || !mineType.equalsIgnoreCase("application/zip")) {
            Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent4 = new Intent();
            intent4.setAction("android.intent.action.VIEW");
            intent4.setDataAndType(uriForFile, Utils.getMimeTypeFromFilePath(file.getPath()));
            intent4.addFlags(1);
            safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(this, Intent.createChooser(intent4, "Open with"));
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
            //safedk_SearchActivity_startActivity_c7baab9652a95c14c45b3e0883a0620a(this, intent5);
            CallAds(intent5);
        }
    }

    private void getSearchList() {
        Calendar instance = Calendar.getInstance();
        instance.set(6, instance.get(6) - 6);
        instance.getTime();
        Cursor query = getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "date_modified"}, (String) null, (String[]) null, "LOWER(date_modified) DESC");
        String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android";
        if (query != null) {
            new ArrayList();
            ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
            if (favouriteList == null) {
                favouriteList = new ArrayList<>();
            }
            if (query.getCount() <= 0 || !query.moveToFirst()) {
                query.close();
            }
            do {
                String string = query.getString(query.getColumnIndex("_data"));
                if (string != null && !str.toLowerCase().contains(string.toLowerCase()) && !string.toLowerCase().contains(str.toLowerCase())) {
                    File file = new File(string);
                    if (file.isFile()) {
                        String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(file.getPath());
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
                        internalStorageFilesModel.setMineType(mimeTypeFromFilePath);
                        this.searchList.add(internalStorageFilesModel);
                        if (this.edtSearch.getText().toString().length() == 0) {
                            this.searchDataList.add(internalStorageFilesModel);
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (SearchActivity.this.edtSearch.getText().toString().length() == 0 && SearchActivity.this.adapter != null) {
                                    SearchActivity.this.adapter.notifyItemInserted(SearchActivity.this.searchDataList.size());
                                    SearchActivity.this.progressBar.setVisibility(8);
                                    if (SearchActivity.this.searchDataList != null && SearchActivity.this.searchDataList.size() > 10) {
                                    }
                                }
                            }
                        });
                    }
                }
            } while (query.moveToNext());
            query.close();
        }
    }

    private void displayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DisplayDeleteEvent>() {
            public void call(DisplayDeleteEvent displayDeleteEvent) {
                if (displayDeleteEvent.getDeleteList() != null && displayDeleteEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    SearchActivity.this.updateDeleteImageData(displayDeleteEvent.getDeleteList());
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
                    SearchActivity.this.setUpdateFavourite(displayFavoriteEvent.isFavorite(), displayFavoriteEvent.getFilePath());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void setUpdateFavourite(boolean z, String str) {
        ArrayList<InternalStorageFilesModel> arrayList = this.searchList;
        int i = 0;
        if (arrayList != null && arrayList.size() != 0) {
            int i2 = 0;
            while (true) {
                if (i2 < this.searchList.size()) {
                    InternalStorageFilesModel internalStorageFilesModel = this.searchList.get(i2);
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
        ArrayList<InternalStorageFilesModel> arrayList2 = this.searchDataList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            while (i < this.searchDataList.size()) {
                InternalStorageFilesModel internalStorageFilesModel2 = this.searchDataList.get(i);
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
        ArrayList<InternalStorageFilesModel> arrayList2 = this.searchList;
        if (arrayList2 != null && arrayList2.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= this.searchList.size()) {
                        break;
                    } else if (this.searchList.get(i2).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        this.searchList.remove(i2);
                        break;
                    } else {
                        i2++;
                    }
                }
                try {
                    if (this.searchList.size() != 1 && 1 < this.searchList.size() && this.searchList.get(1).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        this.searchList.remove(1);
                    }
                    if (this.searchList.size() != 0 && this.searchList.get(0).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                        this.searchList.remove(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayList<InternalStorageFilesModel> arrayList3 = this.searchDataList;
                if (!(arrayList3 == null || arrayList3.size() == 0)) {
                    int i3 = 0;
                    while (true) {
                        if (i3 < this.searchDataList.size()) {
                            if (this.searchDataList.get(i3).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                                break;
                            }
                            i3++;
                        }
                    }
                    this.searchDataList.remove(i3);
                    try {
                        if (this.searchDataList.size() != 1 && 1 < this.searchDataList.size() && this.searchDataList.get(1).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                            this.searchDataList.remove(1);
                        }
                        if (this.searchDataList.size() != 0 && this.searchDataList.get(0).getFilePath().equalsIgnoreCase(arrayList.get(i))) {
                            this.searchDataList.remove(0);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            StorageAdapter storageAdapter = this.adapter;
            if (storageAdapter != null) {
                storageAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }
            ArrayList<InternalStorageFilesModel> arrayList4 = this.searchDataList;
            if (arrayList4 == null || arrayList4.size() == 0) {
                this.recyclerView.setVisibility(8);
                this.llEmpty.setVisibility(0);
                return;
            }
            this.recyclerView.setVisibility(0);
            this.llEmpty.setVisibility(8);
        }
    }

    
    public void setDeleteFile() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Delete file...");
            this.loadingDialog.show();
        }
        new Thread(new Runnable() {
            public final void run() {
                SearchActivity.this.deleteFile();
            }
        }).start();
    }

    public void deleteFile() {
        ArrayList<InternalStorageFilesModel> arrayList;
        if (this.searchDataList != null) {
            for (int i = 0; i < this.searchDataList.size(); i++) {
                if (this.searchDataList.get(i).isSelected()) {
                    File file = new File(this.searchDataList.get(i).getFilePath());
                    if (StorageUtils.deleteFile(file, this)) {
                        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                    }
                }
            }
        }
        ArrayList arrayList2 = new ArrayList();
        if (this.searchDataList != null) {
            int i2 = 0;
            while (i2 < this.searchDataList.size()) {
                this.searchDataList.get(i2).setCheckboxVisible(false);
                if (this.searchDataList.get(i2).isSelected()) {
                    arrayList2.add(this.searchDataList.get(i2).getFilePath());
                    this.searchDataList.remove(i2);
                    if (i2 != 0) {
                        i2--;
                    }
                }
                i2++;
            }
            try {
                if (this.searchDataList.size() != 1 && 1 < this.searchDataList.size() && this.searchDataList.get(1).isSelected()) {
                    arrayList2.add(this.searchDataList.get(1).getFilePath());
                    this.searchDataList.remove(1);
                }
                if (this.searchDataList.size() != 0 && this.searchDataList.get(0).isSelected()) {
                    arrayList2.add(this.searchDataList.get(0).getFilePath());
                    this.searchDataList.remove(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!(arrayList2.size() == 0 || (arrayList = this.searchList) == null || arrayList.size() == 0)) {
            for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                int i4 = 0;
                while (true) {
                    if (i4 < this.searchList.size()) {
                        if (this.searchList.get(i4).getFilePath().equalsIgnoreCase((String) arrayList2.get(i3))) {
                            break;
                        }
                        i4++;
                    }
                }
                this.searchList.remove(i4);
                try {
                    if (this.searchList.size() != 1 && 1 < this.searchList.size() && this.searchList.get(1).getFilePath().equalsIgnoreCase((String) arrayList2.get(i3))) {
                        this.searchList.remove(1);
                    }
                    if (this.searchList.size() != 0 && this.searchList.get(0).getFilePath().equalsIgnoreCase((String) arrayList2.get(i3))) {
                        this.searchList.remove(0);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                SearchActivity.this.OnSelected(true, false, 0);
                SearchActivity.this.llBottomOption.setVisibility(8);
                if (SearchActivity.this.adapter != null) {
                    SearchActivity.this.adapter.notifyDataSetChanged();
                }
                if (SearchActivity.this.loadingDialog != null && SearchActivity.this.loadingDialog.isShowing()) {
                    SearchActivity.this.loadingDialog.dismiss();
                }
                if (SearchActivity.this.searchDataList == null || SearchActivity.this.searchDataList.size() == 0) {
                    SearchActivity.this.recyclerView.setVisibility(8);
                    SearchActivity.this.llEmpty.setVisibility(0);
                } else {
                    SearchActivity.this.recyclerView.setVisibility(0);
                    SearchActivity.this.llEmpty.setVisibility(8);
                }
                Toast.makeText(SearchActivity.this, "Delete file successfully", 0).show();
            }
        });
    }

    private void copyMoveEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CopyMoveEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<CopyMoveEvent>() {
            public void call(CopyMoveEvent copyMoveEvent) {
                ArrayList arrayList = new ArrayList();
                if (!(copyMoveEvent.getCopyMoveList() == null || copyMoveEvent.getCopyMoveList().size() == 0)) {
                    new ArrayList();
                    ArrayList<File> copyMoveList = copyMoveEvent.getCopyMoveList();
                    if (copyMoveList == null) {
                        copyMoveList = new ArrayList<>();
                    }
                    for (int i = 0; i < copyMoveList.size(); i++) {
                        File file = copyMoveList.get(i);
                        if (!file.getName().startsWith(".")) {
                            if (file.isFile()) {
                                String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(file.getPath());
                                if (mimeTypeFromFilePath != null) {
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
                                    SearchActivity.this.searchList.add(0, internalStorageFilesModel);
                                    arrayList.add(internalStorageFilesModel);
                                }
                            } else {
                                file.isDirectory();
                            }
                        }
                    }
                    if (arrayList.size() != 0) {
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            if (((InternalStorageFilesModel) arrayList.get(i2)).getFileName().toLowerCase().contains(SearchActivity.this.edtSearch.getText().toString().toLowerCase())) {
                                SearchActivity.this.searchDataList.add(0, (InternalStorageFilesModel) arrayList.get(i2));
                            }
                        }
                    }
                    if (SearchActivity.this.adapter != null) {
                        SearchActivity.this.adapter.notifyDataSetChanged();
                    } else {
                        SearchActivity.this.setAdapter();
                    }
                    if (SearchActivity.this.searchDataList == null || SearchActivity.this.searchDataList.size() == 0) {
                        SearchActivity.this.recyclerView.setVisibility(8);
                        SearchActivity.this.llEmpty.setVisibility(0);
                    } else {
                        SearchActivity.this.recyclerView.setVisibility(0);
                        SearchActivity.this.llEmpty.setVisibility(8);
                    }
                }
                if (copyMoveEvent.getDeleteList() != null && copyMoveEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    ArrayList<String> deleteList = copyMoveEvent.getDeleteList();
                    if (deleteList == null) {
                        deleteList = new ArrayList<>();
                    }
                    SearchActivity.this.updateDeleteImageData(deleteList);
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
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
                    SearchActivity.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    SearchActivity.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    SearchActivity.this.sendFile();
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
        File file = new File(this.searchDataList.get(this.pos).getFilePath());
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

    
    public void showRenameDialog() {
        setSelectionClose();
        final File file = new File(this.searchDataList.get(this.pos).getFilePath());
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
                    Toast.makeText(SearchActivity.this, "New name can't be empty.", 0).show();
                } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                    dialog.show();
                } else if (!file.isDirectory()) {
                    String[] split = appCompatEditText.getText().toString().split("\\.");
                    if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                        final Dialog dialog = new Dialog(SearchActivity.this, R.style.WideDialog);
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
                                SearchActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                            }
                        });
                        dialog.show();
                        return;
                    }
                    Log.e("", "rename");
                    dialog.dismiss();
                    SearchActivity.this.reNameFile(file, appCompatEditText.getText().toString());
                } else {
                    dialog.dismiss();
                    SearchActivity.this.reNameFile(file, appCompatEditText.getText().toString());
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
            this.searchDataList.get(this.pos).setFilePath(file2.getPath());
            this.searchDataList.get(this.pos).setFileName(file2.getName());
            this.adapter.notifyItemChanged(this.pos);
            RxBus.getInstance().post(new RenameEvent(file, file2));
            Toast.makeText(this, "Rename file successfully", 0).show();
            ArrayList<InternalStorageFilesModel> arrayList = this.searchList;
            if (arrayList != null && arrayList.size() != 0) {
                for (int i = 0; i < this.searchList.size(); i++) {
                    if (this.searchList.get(i).getFilePath().equalsIgnoreCase(file.getPath())) {
                        this.searchList.get(i).setFilePath(file2.getPath());
                        this.searchList.get(i).setFileName(file2.getName());
                        return;
                    }
                }
                return;
            }
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
                    if (new File(SearchActivity.this.rootPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(SearchActivity.this, "File name already use", 0).show();
                        return;
                    }
                    SearchActivity.this.zip_file_name = str;
                    dialog.dismiss();
                    if (SearchActivity.this.isFileFromSdCard) {
                        SearchActivity.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(SearchActivity.this.sdCardPath), SearchActivity.this) == 2) {
                            Toast.makeText(SearchActivity.this, "Please give a permission for manager operation", 0).show();
                        } else {
                            SearchActivity.this.setcompress();
                        }
                    } else {
                        SearchActivity.this.setcompress();
                    }
                } else {
                    SearchActivity searchActivity = SearchActivity.this;
                    Toast.makeText(searchActivity, searchActivity.getResources().getString(R.string.zip_validation), 0).show();
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
                    SearchActivity.this.compressfile();
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
            file2 = new File(this.searchDataList.get(this.pos).getFilePath());
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
            for (int i = 0; i < this.searchDataList.size(); i++) {
                if (this.searchDataList.get(i) != null) {
                    InternalStorageFilesModel internalStorageFilesModel = this.searchDataList.get(i);
                    if (internalStorageFilesModel.isSelected()) {
                        File file4 = new File(internalStorageFilesModel.getFilePath());
                        StorageUtils.copyFile(file4, new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file4.getName()), this);
                    }
                }
            }
        }
        if (this.selected_Item == 1) {
            str = this.rootPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str2 + ".zip";
        } else {
            str = this.rootPath + InternalZipConstants.ZIP_FILE_SEPARATOR + file2.getName() + ".zip";
        }
        ZipArchive.zip(file2.getPath(), str, "");
        runOnUiThread(new Runnable() {
            public void run() {
                SearchActivity.this.setSelectionClose();
                if (str != null) {
                    MediaScannerConnection.scanFile(SearchActivity.this, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str));
                    if (SearchActivity.this.selected_Item != 1) {
                        boolean deleteFile = StorageUtils.deleteFile(file2, SearchActivity.this);
                        if (file != null && StorageUtils.deleteFile(file, SearchActivity.this)) {
                            MediaScannerConnection.scanFile(SearchActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                        if (deleteFile) {
                            MediaScannerConnection.scanFile(SearchActivity.this, new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                    }
                    File file2 = new File(str);
                    String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath(file2.getPath());
                    InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                    internalStorageFilesModel.setFileName(file2.getName());
                    internalStorageFilesModel.setFilePath(file2.getPath());
                    internalStorageFilesModel.setFavorite(false);
                    if (file2.isDirectory()) {
                        internalStorageFilesModel.setDir(true);
                    } else {
                        internalStorageFilesModel.setDir(false);
                    }
                    internalStorageFilesModel.setCheckboxVisible(false);
                    internalStorageFilesModel.setSelected(false);
                    internalStorageFilesModel.setMineType(mimeTypeFromFilePath);
                    SearchActivity.this.searchList.add(internalStorageFilesModel);
                    if (SearchActivity.this.edtSearch.getText().toString().length() == 0) {
                        SearchActivity.this.searchDataList.add(internalStorageFilesModel);
                    }
                    if (SearchActivity.this.adapter != null) {
                        SearchActivity.this.adapter.notifyDataSetChanged();
                    }
                    if (SearchActivity.this.loadingDialog.isShowing()) {
                        SearchActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(SearchActivity.this, "Compress file successfully", 0).show();
                }
            }
        });
    }
}
