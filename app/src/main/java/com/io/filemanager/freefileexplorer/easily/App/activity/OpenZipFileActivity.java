package com.io.filemanager.freefileexplorer.easily.App.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.adapter.ZipHeaderListAdapter;
import com.io.filemanager.freefileexplorer.easily.adapter.ZipStorageAdapter;
import com.io.filemanager.freefileexplorer.easily.model.InternalStorageFilesModel;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.StorageUtils;
import com.io.filemanager.freefileexplorer.easily.utils.Utils;
/*import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;*/
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.lingala.zip4j.util.InternalZipConstants;

public class OpenZipFileActivity extends AppCompatActivity {

    ZipStorageAdapter adapter;
    ArrayList<String> arrayListFilePaths = new ArrayList<>();
    @BindView(R.id.btn_cancel)
    AppCompatTextView btnCancel;
    @BindView(R.id.btn_extract)
    AppCompatTextView btnExtract;
    String extract_file_name;
    String extract_path;
    File file;
    String headerName;
    boolean isCheckAll = true;
    boolean isShowHidden = false;
    @BindView(R.id.iv_back)
    AppCompatImageView ivBack;
    @BindView(R.id.iv_check_all)
    ImageView ivCheckAll;
    @BindView(R.id.iv_close)
    AppCompatImageView ivClose;
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
    ProgressDialog loadingDialog;
    @BindView(R.id.lout_selected)
    RelativeLayout loutSelected;
    @BindView(R.id.lout_toolbar)
    RelativeLayout loutToolbar;
    String path;
    ZipHeaderListAdapter pathAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    String rootPath;
    @BindView(R.id.rv_header)
    RecyclerView rvHeader;
    String sdCardPath;
    int selected_Item = 0;
    ArrayList<InternalStorageFilesModel> storageList = new ArrayList<>();
    @BindView(R.id.txt_header_title)
    TextView txtHeaderTitle;
    @BindView(R.id.txt_select)
    AppCompatTextView txtSelect;
    ArrayList<InternalStorageFilesModel> zipFileList = new ArrayList<>();
    int zip_counter = 0;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_open_zip_file);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        initView();


    }

    private void initView() {
        Intent intent = getIntent();
        this.headerName = intent.getStringExtra("ZipName");
        this.path = intent.getStringExtra("ZipPath");
        this.txtHeaderTitle.setText(this.headerName);
        this.isShowHidden = PreferencesManager.getShowHidden(this);
        this.llFavourite.setVisibility(8);
        this.ivUncheck.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
        this.ivCheckAll.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_btn_selected));
        this.ivClose.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        this.progressBar.setVisibility(0);
        this.sdCardPath = Utils.getExternalStoragePath(this, true);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);
        this.llBottomOption.setVisibility(8);
        if (this.isCheckAll) {
            this.ivCheckAll.setVisibility(0);
        } else {
            this.ivCheckAll.setVisibility(8);
        }
        new Thread(new Runnable() {
            public final void run() {
                OpenZipFileActivity.this.getZipOpen();
            }
        }).start();
    }

    private HashMap<String, List<String>> openZipFile(String str) {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        try {
            Enumeration<? extends ZipEntry> entries = new ZipFile(str).entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                Log.e("ZipFileOpenTop", "file name: " + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    String[] split = zipEntry.getName().split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    int length = split.length;
                    String str2 = split[0];
                    zipEntry.getName().lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    String str3 = split[split.length - 1];
                    Log.e("ZipFileOpenDir", "length: " + split.length);
                    if (length == 2) {
                        String str4 = split[split.length - 1];
                        Log.e("ZipFileOpenDir", "directory name: " + str2 + "      fileName: " + str3 + "      lastname: " + str4);
                        if (!hashMap.containsKey(str2)) {
                            List list = hashMap.get(str2);
                            if (list == null) {
                                list = new ArrayList();
                            }
                            if (str3 != null && !str3.equalsIgnoreCase("") && !list.contains(str3)) {
                                list.add(str3);
                            }
                            hashMap.put(str2, list);
                            Log.e("ZipFile", "directory name is: " + str2);
                        } else {
                            List list2 = hashMap.get(str2);
                            if (list2 == null) {
                                list2 = new ArrayList();
                            }
                            if (str3 != null && !str3.equalsIgnoreCase("") && !list2.contains(str3)) {
                                list2.add(str3);
                            }
                            hashMap.put(str2, list2);
                        }
                    }
                } else {
                    String name = zipEntry.getName();
                    name.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    String[] split2 = name.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    int length2 = split2.length;
                    Log.e("ZipFileOpen", "length: " + split2.length);
                    if (length2 == 2 || length2 == 1) {
                        String str5 = split2[0];
                        String str6 = split2[split2.length - 1];
                        String str7 = split2[split2.length - 1];
                        Log.e("ZipFileOpen", "directory name: " + str5 + "    fileName: " + str6 + "    lastname: " + str7);
                        if (!hashMap.containsKey(str5)) {
                            List list3 = hashMap.get(str5);
                            if (list3 == null) {
                                list3 = new ArrayList();
                            }
                            if (str6 != null && !str6.equalsIgnoreCase("") && !list3.contains(str6)) {
                                list3.add(str6);
                            }
                            hashMap.put(str5, list3);
                        } else {
                            List list4 = hashMap.get(str5);
                            if (list4 == null) {
                                list4 = new ArrayList();
                            }
                            if (str6 != null && !str6.equalsIgnoreCase("") && !list4.contains(str6)) {
                                list4.add(str6);
                            }
                            hashMap.put(str5, list4);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public void onBackPressed() {
        if (this.arrayListFilePaths.size() == 1) {
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

    @OnClick({R.id.btn_cancel, R.id.btn_extract, R.id.iv_back, R.id.iv_close, R.id.ll_check_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel /*2131296409*/:
                finish();
                return;
            case R.id.btn_extract /*2131296415*/:
                if (this.selected_Item != 0) {
                    setExtractZip();
                    return;
                } else {
                    Toast.makeText(this, "please select file", 0).show();
                    return;
                }
            case R.id.iv_back /*2131296682*/:
            case R.id.iv_close /*2131296689*/:
                onBackPressed();
                return;
            case R.id.ll_check_all /*2131296746*/:
                if (this.isCheckAll) {
                    this.isCheckAll = false;
                    selectEvent(false);
                    this.ivCheckAll.setVisibility(8);
                    return;
                }
                this.isCheckAll = true;
                selectEvent(true);
                this.ivCheckAll.setVisibility(0);
                return;
            default:
                return;
        }
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
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
        OnSelected(false, true, 0);
    }

    public void setExtractZip() {
        this.extract_path = new File(this.path).getParent();
        String str = this.sdCardPath;
        if (str != null && !str.equalsIgnoreCase("") && this.path.contains(this.sdCardPath)) {
            File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.extract_file));
            if (!file3.exists()) {
                file3.mkdirs();
            }
            this.extract_path = file3.getPath();
        }
        String str2 = this.headerName.split("\\.")[0];
        if (new File(this.extract_path + InternalZipConstants.ZIP_FILE_SEPARATOR + str2).exists()) {
            showExtractDialog(str2);
            return;
        }
        this.extract_file_name = str2;
        setExtract();
    }

    public void showExtractDialog(final String str) {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_rename_zip);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(80);
        ((AppCompatTextView) dialog.findViewById(R.id.txt_msg)).setText(getResources().getString(R.string.str_extract_validation_1) + " " + str + " " + getResources().getString(R.string.str_extract_validation_2));
        ((LinearLayout) dialog.findViewById(R.id.btn_skip)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(OpenZipFileActivity.this, "Extraction cancelled", 0).show();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btn_replace)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OpenZipFileActivity.this.extract_file_name = str;
                File file = new File(OpenZipFileActivity.this.extract_path + InternalZipConstants.ZIP_FILE_SEPARATOR + str);
                dialog.dismiss();
                if (OpenZipFileActivity.this.loadingDialog != null && !OpenZipFileActivity.this.loadingDialog.isShowing()) {
                    OpenZipFileActivity.this.loadingDialog.setMessage("Extract file...");
                    OpenZipFileActivity.this.loadingDialog.show();
                }
                if (StorageUtils.deleteFile(file, OpenZipFileActivity.this)) {
                    MediaScannerConnection.scanFile(OpenZipFileActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                            OpenZipFileActivity.this.setExtract();
                        }
                    });
                }
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btn_rename)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                OpenZipFileActivity openZipFileActivity = OpenZipFileActivity.this;
                File access$100 = openZipFileActivity.createUnZipFile(str, openZipFileActivity.extract_path);
                OpenZipFileActivity.this.extract_file_name = access$100.getName();
                OpenZipFileActivity.this.setExtract();
            }
        });
        dialog.show();
    }

    
    public File createUnZipFile(String str, String str2) {
        this.zip_counter = 1;
        File file2 = new File(str2 + InternalZipConstants.ZIP_FILE_SEPARATOR + str);
        if (file2.exists()) {
            return UnZipFile(str, str2);
        }
        file2.mkdir();
        return file2;
    }

    private File UnZipFile(String str, String str2) {
        File file2 = new File(str2 + InternalZipConstants.ZIP_FILE_SEPARATOR + str + "(" + this.zip_counter + ")");
        if (!file2.exists()) {
            file2.mkdir();
            return file2;
        }
        this.zip_counter++;
        return UnZipFile(str, str2);
    }

    public void getZipOpen() {
        File file2 = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + getResources().getString(R.string.app_name));
        if (!file2.exists()) {
            file2.mkdir();
        }
        File file3 = new File(file2.getPath() + "/.zipExtract");
        this.file = file3;
        if (!file3.exists()) {
            this.file.mkdir();
        }
        this.rootPath = this.file.getPath();
        HashMap<String, List<String>> openZipFile = openZipFile(this.path);
        Set<String> keySet = openZipFile.keySet();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(keySet);
        boolean z = false;
        int i = 0;
        while (i < arrayList.size()) {
            if (arrayList.size() == 1) {
                String[] split = ((String) arrayList.get(i)).split("\\.");
                String str = split[split.length - 1];
                String mimeTypeFromFilePath = Utils.getMimeTypeFromFilePath((String) arrayList.get(i));
                InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                internalStorageFilesModel.setFileName((String) arrayList.get(i));
                internalStorageFilesModel.setFilePath((String) arrayList.get(i));
                internalStorageFilesModel.setFavorite(z);
                internalStorageFilesModel.setCheckboxVisible(true);
                internalStorageFilesModel.setSelected(true);
                if (mimeTypeFromFilePath == null) {
                    List list = openZipFile.get(arrayList.get(i));
                    if (list == null) {
                        list = new ArrayList();
                    }
                    int i2 = 0;
                    while (i2 < list.size()) {
                        InternalStorageFilesModel internalStorageFilesModel2 = new InternalStorageFilesModel();
                        internalStorageFilesModel2.setFileName((String) list.get(i2));
                        internalStorageFilesModel2.setFilePath((String) list.get(i2));
                        internalStorageFilesModel2.setFavorite(z);
                        this.selected_Item = this.selected_Item;
                        ((String) list.get(i2)).split("\\.");
                        String mimeTypeFromFilePath2 = Utils.getMimeTypeFromFilePath((String) list.get(i2));
                        if (mimeTypeFromFilePath2 != null) {
                            internalStorageFilesModel2.setDir(z);
                        } else if (str == null || str.equalsIgnoreCase((String) arrayList.get(i))) {
                            internalStorageFilesModel2.setDir(z);
                        } else {
                            internalStorageFilesModel2.setDir(z);
                        }
                        Log.e("listkeys", "name" + ((String) arrayList.get(i)) + " type " + mimeTypeFromFilePath);
                        internalStorageFilesModel2.setMineType(mimeTypeFromFilePath2);
                        this.storageList.add(internalStorageFilesModel2);
                        i2++;
                        z = false;
                    }
                } else {
                    internalStorageFilesModel.setDir(z);
                    internalStorageFilesModel.setMineType(mimeTypeFromFilePath);
                    this.selected_Item++;
                    this.storageList.add(internalStorageFilesModel);
                }
            } else {
                InternalStorageFilesModel internalStorageFilesModel3 = new InternalStorageFilesModel();
                internalStorageFilesModel3.setFileName((String) arrayList.get(i));
                internalStorageFilesModel3.setFilePath((String) arrayList.get(i));
                internalStorageFilesModel3.setFavorite(false);
                internalStorageFilesModel3.setCheckboxVisible(true);
                internalStorageFilesModel3.setSelected(true);
                this.selected_Item++;
                String[] split2 = ((String) arrayList.get(i)).split("\\.");
                String str2 = split2[split2.length - 1];
                String mimeTypeFromFilePath3 = Utils.getMimeTypeFromFilePath((String) arrayList.get(i));
                if (mimeTypeFromFilePath3 != null) {
                    internalStorageFilesModel3.setDir(false);
                } else if (str2 == null || str2.equalsIgnoreCase((String) arrayList.get(i))) {
                    internalStorageFilesModel3.setDir(true);
                } else {
                    internalStorageFilesModel3.setDir(false);
                }
                Log.e("listkeys", "name" + ((String) arrayList.get(i)) + " type " + mimeTypeFromFilePath3);
                internalStorageFilesModel3.setMineType(mimeTypeFromFilePath3);
                this.storageList.add(internalStorageFilesModel3);
            }
            i++;
            z = false;
        }
        this.arrayListFilePaths.add(this.rootPath);
        runOnUiThread(new Runnable() {
            public void run() {
                OpenZipFileActivity.this.progressBar.setVisibility(8);
                OpenZipFileActivity.this.setHeaderData();
                OpenZipFileActivity.this.setRecyclerViewData();
            }
        });
    }

    
    public void setRecyclerViewData() {
        ArrayList<InternalStorageFilesModel> arrayList = this.storageList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            this.llBottomOption.setVisibility(8);
            if (this.selected_Item == 0) {
                this.llBottomOption.setVisibility(8);
            } else {
                this.llBottomOption.setVisibility(0);
            }
            OnSelected(true, false, this.selected_Item);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
        if (this.selected_Item == 0) {
            this.llBottomOption.setVisibility(8);
        } else {
            this.llBottomOption.setVisibility(0);
        }
        this.llBottomOption.setVisibility(0);
        OnSelected(false, true, this.selected_Item);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(0, 0, 0, 0);
        this.recyclerView.setLayoutParams(layoutParams);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ZipStorageAdapter zipStorageAdapter = new ZipStorageAdapter(this, this.storageList, false);
        this.adapter = zipStorageAdapter;
        this.recyclerView.setAdapter(zipStorageAdapter);
        this.adapter.setOnItemClickListener(new ZipStorageAdapter.ClickListener() {
            public void onItemClick(int i, View view) {
                if (OpenZipFileActivity.this.storageList.get(i).isCheckboxVisible()) {
                    if (OpenZipFileActivity.this.storageList.get(i).isSelected()) {
                        OpenZipFileActivity.this.storageList.get(i).setSelected(false);
                    } else {
                        OpenZipFileActivity.this.storageList.get(i).setSelected(true);
                    }
                    OpenZipFileActivity.this.adapter.notifyDataSetChanged();
                    OpenZipFileActivity.this.setSelectedFile();
                }
            }
        });
        this.adapter.setOnLongClickListener(new ZipStorageAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
            }
        });
    }

    
    public void setSelectedFile() {
        int size = this.storageList.size();
        new ArrayList();
        new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < this.storageList.size(); i2++) {
            if (this.storageList.get(i2).isSelected()) {
                i++;
            }
        }
        this.llBottomOption.setVisibility(0);
        OnSelected(false, true, i);
        this.selected_Item = i;
        if (i == 0) {
            this.llBottomOption.setVisibility(8);
        } else {
            this.llBottomOption.setVisibility(0);
        }
        if (size == this.selected_Item) {
            this.isCheckAll = true;
        } else {
            this.isCheckAll = false;
        }
        if (this.isCheckAll) {
            this.ivCheckAll.setVisibility(0);
        } else {
            this.ivCheckAll.setVisibility(8);
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
            ZipHeaderListAdapter zipHeaderListAdapter = new ZipHeaderListAdapter(this, this.arrayListFilePaths);
            this.pathAdapter = zipHeaderListAdapter;
            this.rvHeader.setAdapter(zipHeaderListAdapter);
            this.pathAdapter.setOnItemClickListener(new ZipHeaderListAdapter.ClickListener() {
                public void onItemHeaderClick(int i, View view) {
                }

                public void onItemClick(int i, View view) {
                    Log.e("path seleted:", OpenZipFileActivity.this.arrayListFilePaths.get(i));
                    Log.e("onItemClick", "position: " + i);
                    int size = OpenZipFileActivity.this.arrayListFilePaths.size();
                    while (true) {
                        size--;
                        if (size > i) {
                            Log.e("onItemClick", "remove index: " + size);
                            OpenZipFileActivity.this.arrayListFilePaths.remove(size);
                        } else {
                            OpenZipFileActivity.this.storageList.clear();
                            OpenZipFileActivity openZipFileActivity = OpenZipFileActivity.this;
                            openZipFileActivity.getFilesList(openZipFileActivity.arrayListFilePaths.get(OpenZipFileActivity.this.arrayListFilePaths.size() - 1));
                            return;
                        }
                    }
                }
            });
        }
    }

    private void getList() {
        new ArrayList();
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(this);
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        File[] listFiles = new File(this.rootPath).listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (this.isShowHidden) {
                    InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                    internalStorageFilesModel.setFileName(file2.getName());
                    internalStorageFilesModel.setFilePath(file2.getPath());
                    if (favouriteList.contains(file2.getPath())) {
                        internalStorageFilesModel.setFavorite(true);
                    } else {
                        internalStorageFilesModel.setFavorite(false);
                    }
                    if (file2.isDirectory()) {
                        internalStorageFilesModel.setDir(true);
                    } else {
                        internalStorageFilesModel.setDir(false);
                    }
                    internalStorageFilesModel.setCheckboxVisible(false);
                    internalStorageFilesModel.setSelected(false);
                    internalStorageFilesModel.setMineType(Utils.getMimeTypeFromFilePath(file2.getPath()));
                    this.storageList.add(internalStorageFilesModel);
                } else if (!file2.getName().startsWith(".")) {
                    InternalStorageFilesModel internalStorageFilesModel2 = new InternalStorageFilesModel();
                    internalStorageFilesModel2.setFileName(file2.getName());
                    internalStorageFilesModel2.setFilePath(file2.getPath());
                    if (file2.isDirectory()) {
                        internalStorageFilesModel2.setDir(true);
                    } else {
                        internalStorageFilesModel2.setDir(false);
                    }
                    if (favouriteList.contains(file2.getPath())) {
                        internalStorageFilesModel2.setFavorite(true);
                    } else {
                        internalStorageFilesModel2.setFavorite(false);
                    }
                    internalStorageFilesModel2.setCheckboxVisible(false);
                    internalStorageFilesModel2.setSelected(false);
                    internalStorageFilesModel2.setMineType(Utils.getMimeTypeFromFilePath(file2.getPath()));
                    this.storageList.add(internalStorageFilesModel2);
                }
            }
        }
        runOnUiThread(new Runnable() {
            public void run() {
                OpenZipFileActivity.this.progressBar.setVisibility(8);
                OpenZipFileActivity.this.setHeaderData();
                OpenZipFileActivity.this.setRecyclerViewData();
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
            for (File file2 : listFiles) {
                if (this.isShowHidden) {
                    InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                    internalStorageFilesModel.setFileName(file2.getName());
                    internalStorageFilesModel.setFilePath(file2.getPath());
                    if (file2.isDirectory()) {
                        internalStorageFilesModel.setDir(true);
                    } else {
                        internalStorageFilesModel.setDir(false);
                    }
                    if (favouriteList.contains(file2.getPath())) {
                        internalStorageFilesModel.setFavorite(true);
                    } else {
                        internalStorageFilesModel.setFavorite(false);
                    }
                    internalStorageFilesModel.setCheckboxVisible(false);
                    internalStorageFilesModel.setSelected(false);
                    internalStorageFilesModel.setMineType(Utils.getMimeTypeFromFilePath(file2.getPath()));
                    this.storageList.add(internalStorageFilesModel);
                } else if (!file2.getName().startsWith(".")) {
                    InternalStorageFilesModel internalStorageFilesModel2 = new InternalStorageFilesModel();
                    internalStorageFilesModel2.setFileName(file2.getName());
                    internalStorageFilesModel2.setFilePath(file2.getPath());
                    if (file2.isDirectory()) {
                        internalStorageFilesModel2.setDir(true);
                    } else {
                        internalStorageFilesModel2.setDir(false);
                    }
                    if (favouriteList.contains(file2.getPath())) {
                        internalStorageFilesModel2.setFavorite(true);
                    } else {
                        internalStorageFilesModel2.setFavorite(false);
                    }
                    internalStorageFilesModel2.setCheckboxVisible(false);
                    internalStorageFilesModel2.setSelected(false);
                    internalStorageFilesModel2.setMineType(Utils.getMimeTypeFromFilePath(file2.getPath()));
                    this.storageList.add(internalStorageFilesModel2);
                }
            }
        }
        ZipStorageAdapter zipStorageAdapter = this.adapter;
        if (zipStorageAdapter != null) {
            zipStorageAdapter.notifyDataSetChanged();
        }
        ZipHeaderListAdapter zipHeaderListAdapter = this.pathAdapter;
        if (zipHeaderListAdapter != null) {
            zipHeaderListAdapter.notifyDataSetChanged();
            setToPathPosition();
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

    private void setToPathPosition() {
        RecyclerView recyclerView2 = this.rvHeader;
        recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount() - 1);
    }

    
    public void setExtract() {
        ProgressDialog progressDialog = this.loadingDialog;
        if (progressDialog != null && !progressDialog.isShowing()) {
            this.loadingDialog.setMessage("Extract file...");
            this.loadingDialog.show();
        }
        new Thread(new Runnable() {
            public final void run() {
                OpenZipFileActivity.this.extractfile();
            }
        }).start();
    }

    public void extractfile() {
        File file2 = new File(this.extract_path + InternalZipConstants.ZIP_FILE_SEPARATOR + this.extract_file_name);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(this.path);
        new ZipArchive();
        ZipArchive.unzip(file3.getPath(), file2.getPath(), "");
        final String path2 = file2.getPath();
        runOnUiThread(new Runnable() {
            public void run() {
                String str = path2;
                if (str != null) {
                    MediaScannerConnection.scanFile(OpenZipFileActivity.this, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    if (OpenZipFileActivity.this.isCheckAll) {
                        if (OpenZipFileActivity.this.loadingDialog.isShowing()) {
                            OpenZipFileActivity.this.loadingDialog.dismiss();
                        }
                        Toast.makeText(OpenZipFileActivity.this, "Extract file successfully", 0).show();
                        OpenZipFileActivity.this.setResult(-1);
                        OpenZipFileActivity.this.finish();
                        return;
                    }
                    OpenZipFileActivity.this.getZipDataList(path2);
                    if (!(OpenZipFileActivity.this.zipFileList == null || OpenZipFileActivity.this.zipFileList.size() == 0)) {
                        for (int i = 0; i < OpenZipFileActivity.this.storageList.size(); i++) {
                            if (!OpenZipFileActivity.this.storageList.get(i).isSelected()) {
                                int i2 = 0;
                                while (i2 < OpenZipFileActivity.this.zipFileList.size()) {
                                    if (OpenZipFileActivity.this.zipFileList.get(i2).getFileName().equalsIgnoreCase(OpenZipFileActivity.this.storageList.get(i).getFileName())) {
                                        File file = new File(OpenZipFileActivity.this.zipFileList.get(i2).getFilePath());
                                        if (StorageUtils.deleteFile(file, OpenZipFileActivity.this)) {
                                            MediaScannerConnection.scanFile(OpenZipFileActivity.this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                                public void onScanCompleted(String str, Uri uri) {
                                                }
                                            });
                                        }
                                    } else {
                                        i2++;
                                    }
                                }
                            }
                        }
                    }
                    if (OpenZipFileActivity.this.loadingDialog.isShowing()) {
                        OpenZipFileActivity.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(OpenZipFileActivity.this, "Extract file successfully", 0).show();
                    OpenZipFileActivity.this.setResult(-1);
                    OpenZipFileActivity.this.finish();
                }
            }
        });
    }

    
    public void getZipDataList(String str) {
        this.zipFileList = new ArrayList<>();
        getZipList(str);
    }

    private void getZipList(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                InternalStorageFilesModel internalStorageFilesModel = new InternalStorageFilesModel();
                internalStorageFilesModel.setFileName(file2.getName());
                internalStorageFilesModel.setFilePath(file2.getPath());
                if (file2.isDirectory()) {
                    internalStorageFilesModel.setDir(true);
                } else {
                    internalStorageFilesModel.setDir(false);
                }
                internalStorageFilesModel.setCheckboxVisible(false);
                internalStorageFilesModel.setSelected(false);
                internalStorageFilesModel.setMineType(Utils.getMimeTypeFromFilePath(file2.getPath()));
                this.zipFileList.add(internalStorageFilesModel);
                if (file2.isDirectory()) {
                    getZipList(file2.getPath());
                }
            }
        }
    }
}
