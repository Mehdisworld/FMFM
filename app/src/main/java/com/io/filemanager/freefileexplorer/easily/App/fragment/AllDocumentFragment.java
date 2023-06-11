package com.io.filemanager.freefileexplorer.easily.App.fragment;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.io.filemanager.freefileexplorer.easily.App.activity.StorageActivity;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.adapter.Document.AllDocumentAdapter;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.DisplayDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentCloseEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentFavouriteEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentFavouriteUpdateEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentSelectEvent;
import com.io.filemanager.freefileexplorer.easily.event.DocumentSortEvent;
import com.io.filemanager.freefileexplorer.easily.event.RenameEvent;
import com.io.filemanager.freefileexplorer.easily.model.DocumentModel;
import com.io.filemanager.freefileexplorer.easily.oncliclk.OnSelectedHome;
import com.io.filemanager.freefileexplorer.easily.utils.Constant;
import com.io.filemanager.freefileexplorer.easily.utils.PreferencesManager;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
import com.io.filemanager.freefileexplorer.easily.utils.StorageUtils;
import com.io.filemanager.freefileexplorer.easily.utils.Utils;
//import com.safedk.android.analytics.brandsafety.creatives.infos.CreativeInfo;
//import com.safedk.android.analytics.events.RedirectEvent;
//import com.safedk.android.utils.Logger;
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import net.lingala.zip4j.util.InternalZipConstants;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AllDocumentFragment extends Fragment {
    AllDocumentAdapter adapter;
    String compressPath;
    ArrayList<DocumentModel> documentList = new ArrayList<>();
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
    boolean isFileFromSdCard = false;
    @BindView(R.id.ll_bottom_option)
    LinearLayout llBottomOption;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
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
    @BindView(R.id.lout_send)
    LinearLayout loutSend;
    int pos = 0;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    String rootPath;
    String sdCardPath;
    int sdCardPermissionType = 0;
    OnSelectedHome selectedHome;
    int selected_Item = 0;
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
    String[] types = {".pdf", ".xml", ".java", ".php", ".html", ".htm", ".text/x-asm", ".pl", ".xls", ".xlsx", ".xld", ".xlc", ".ppt", ".pptx", ".ppsx", ".pptm", ".doc", ".docx", ".msg", ".odt", ".txt", ".tex", ".text", ".wpd", ".wps"};
    String zip_file_name;

    public static void safedk_FragmentActivity_startActivity_d3edafe57f96ad3eaceffb43aee7b6ff(FragmentActivity p0, Intent p1) {
        if (p1 != null) {
            p0.startActivity(p1);
        }
    }

    public AllDocumentFragment(OnSelectedHome onSelectedHome) {
        this.selectedHome = onSelectedHome;
    }

    public AllDocumentFragment() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        closeEvent();
        selectEvent();
        copyMoveEvent();
        documentDeleteEvent();
        renameEvent();
        sortEvent();
        favouriteEvent();
        favouriteUpdateEvent();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_document, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        initView();
        return inflate;
    }

    private void initView() {
        this.progressBar.setVisibility(0);
        this.imgMore.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_more_bottom));
        new Thread(new Runnable() {
            public final void run() {
                AllDocumentFragment.this.getList();
            }
        }).start();
        this.sdCardPath = Utils.getExternalStoragePath(getActivity(), true);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        this.loadingDialog.setCancelable(false);
        this.loadingDialog.setMessage("Delete file...");
        this.loadingDialog.setCanceledOnTouchOutside(false);
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
    }

    @OnClick({R.id.lout_send, R.id.lout_copy, R.id.lout_move, R.id.lout_delete, R.id.lout_more, R.id.lout_compress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lout_compress /*R.id.lout_compress*/:
                showCompressDialog();
                return;
            case R.id.lout_copy /*R.id.lout_copy*/:
                Constant.isCopyData = true;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_delete /*R.id.lout_delete*/:
                showDeleteDialog();
                return;
            case R.id.lout_more /*R.id.lout_more*/:
                showMoreOptionBottom();
                return;
            case R.id.lout_move /*R.id.lout_move*/:
                Constant.isCopyData = false;
                setCopyMoveOptinOn();
                return;
            case R.id.lout_send /*R.id.lout_send*/:
                sendFile();
                return;
            default:
                return;
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 300) {
            String sDCardTreeUri = PreferencesManager.getSDCardTreeUri(getActivity());
            Uri uri = null;
            Uri parse = sDCardTreeUri != null ? Uri.parse(sDCardTreeUri) : null;
            if (i2 == -1 && (uri = intent.getData()) != null) {
                PreferencesManager.setSDCardTreeUri(getActivity(), uri.toString());
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
                        getActivity().getContentResolver().takePersistableUriPermission(uri, flags);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (uri != null) {
                PreferencesManager.setSDCardTreeUri(getActivity(), parse.toString());
                int i4 = this.sdCardPermissionType;
                if (i4 == 1) {
                    setDeleteFile();
                } else if (i4 == 3) {
                    setcompress();
                }
            }
        }
    }

    
    public void sendFile() {
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        for (int i = 0; i < this.documentList.size(); i++) {
            if (this.documentList.get(i).isSelected()) {
                FragmentActivity activity = getActivity();
                arrayList.add(FileProvider.getUriForFile(activity, getActivity().getPackageName() + ".provider", new File(this.documentList.get(i).getPath())));
            }
        }
        intent.setType("*/*");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.addFlags(268435456);
        safedk_FragmentActivity_startActivity_d3edafe57f96ad3eaceffb43aee7b6ff(getActivity(), Intent.createChooser(intent, "Share with..."));
    }

    private void setCopyMoveOptinOn() {
        Constant.isFileFromSdCard = this.isFileFromSdCard;
        Constant.pastList = new ArrayList<>();
        ArrayList<DocumentModel> arrayList = this.documentList;
        if (!(arrayList == null || arrayList.size() == 0)) {
            for (int i = 0; i < this.documentList.size(); i++) {
                if (this.documentList.get(i).isSelected()) {
                    File file = new File(this.documentList.get(i).getPath());
                    if (file.exists()) {
                        Constant.pastList.add(file);
                    }
                }
            }
        }
        if (Constant.pastList == null || Constant.pastList.size() == 0) {
            Toast.makeText(getActivity(), "Please select file", 0).show();
            return;
        }
        setSelectionClose();
        Intent intent = new Intent(getActivity(), StorageActivity.class);
        intent.putExtra("type", "CopyMove");
        safedk_FragmentActivity_startActivity_d3edafe57f96ad3eaceffb43aee7b6ff(getActivity(), intent);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage((CharSequence) "Are you sure do you want to delete it?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) Html.fromHtml("<font color='#ffba00'>Yes</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (AllDocumentFragment.this.isFileFromSdCard) {
                    AllDocumentFragment.this.sdCardPermissionType = 1;
                    if (StorageUtils.checkFSDCardPermission(new File(AllDocumentFragment.this.sdCardPath), AllDocumentFragment.this.getActivity()) == 2) {
                        Toast.makeText(AllDocumentFragment.this.getActivity(), "Please give a permission for manager operation", 0).show();
                    } else {
                        AllDocumentFragment.this.setDeleteFile();
                    }
                } else {
                    AllDocumentFragment.this.setDeleteFile();
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
                AllDocumentFragment.this.deleteFile();
            }
        }).start();
    }

    public void setSelectionClose() {
        ArrayList<DocumentModel> arrayList = this.documentList;
        if (!(arrayList == null || arrayList.size() == 0)) {
            for (int i = 0; i < this.documentList.size(); i++) {
                this.documentList.get(i).setSelected(false);
                this.documentList.get(i).setCheckboxVisible(false);
            }
            this.adapter.notifyDataSetChanged();
            this.llBottomOption.setVisibility(8);
            this.selected_Item = 0;
        }
        this.selectedHome.OnSelected(true, false, 0);
    }

    
    public void setSelectedFile() {
        boolean z;
        int i;
        String str;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList<DocumentModel> arrayList3 = this.documentList;
        if (arrayList3 == null || arrayList3.size() == 0) {
            i = 0;
            z = false;
        } else {
            z = false;
            int i2 = 0;
            for (int i3 = 0; i3 < this.documentList.size(); i3++) {
                if (this.documentList.get(i3).isSelected()) {
                    i2++;
                    this.pos = i3;
                    if (this.documentList.get(i3).isFavorite()) {
                        arrayList.add(1);
                    } else {
                        arrayList2.add(0);
                    }
                    if (!z && (str = this.sdCardPath) != null && !str.equalsIgnoreCase("") && this.documentList.get(i3).getPath().contains(this.sdCardPath)) {
                        z = true;
                    }
                }
            }
            i = i2;
        }
        if ((arrayList2.size() != 0 || arrayList.size() == 0) && (arrayList2.size() == 0 || arrayList.size() != 0)) {
            this.isFileFromSdCard = z;
            this.selected_Item = i;
            this.llBottomOption.setVisibility(0);
            if (i == 0) {
                if (i == 0) {
                    this.llBottomOption.setVisibility(8);
                    this.selectedHome.OnSelected(true, false, i);
                    setSelectionClose();
                } else {
                    this.llBottomOption.setVisibility(0);
                    this.selectedHome.OnSelected(false, true, i, false, true, false);
                }
                if (i == 0) {
                    setInvisibleButton(this.loutSend, this.imgSend, this.txtTextSend);
                    setInvisibleButton(this.loutMove, this.imgMove, this.txtTextMove);
                    setInvisibleButton(this.loutDelete, this.imgDelete, this.txtTextDelete);
                    setInvisibleButton(this.loutCopy, this.imgCopy, this.txtTextCopy);
                    setInvisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
                    setInvisibleButton(this.loutCompress, this.imgCompress, this.txtTextCompress);
                    return;
                }
                setVisibleButton(this.loutSend, this.imgSend, this.txtTextSend);
                setVisibleButton(this.loutMove, this.imgMove, this.txtTextMove);
                setVisibleButton(this.loutDelete, this.imgDelete, this.txtTextDelete);
                setVisibleButton(this.loutCopy, this.imgCopy, this.txtTextCopy);
                setVisibleButton(this.loutMore, this.imgMore, this.txtTextMore);
                setVisibleButton(this.loutCompress, this.imgCompress, this.txtTextCompress);
                return;
            }
        }
        this.isFileFromSdCard = z;
        this.selected_Item = i;
        this.llBottomOption.setVisibility(0);
    }

    private void setVisibleButton(LinearLayout linearLayout, ImageView imageView, TextView textView) {
        linearLayout.setClickable(true);
        linearLayout.setEnabled(true);
        imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), PorterDuff.Mode.SRC_IN);
        textView.setTextColor(getResources().getColor(R.color.black));
    }

    private void setInvisibleButton(LinearLayout linearLayout, ImageView imageView, TextView textView) {
        linearLayout.setClickable(false);
        linearLayout.setEnabled(false);
        imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.invisible_color), PorterDuff.Mode.SRC_IN);
        textView.setTextColor(getResources().getColor(R.color.invisible_color));
    }

    
    public void setAdapter() {
        this.progressBar.setVisibility(8);
        ArrayList<DocumentModel> arrayList = this.documentList;
        if (arrayList == null || arrayList.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.llEmpty.setVisibility(0);
            return;
        }
        this.recyclerView.setVisibility(0);
        this.llEmpty.setVisibility(8);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AllDocumentAdapter allDocumentAdapter = new AllDocumentAdapter(getActivity(), this.documentList);
        this.adapter = allDocumentAdapter;
        this.recyclerView.setAdapter(allDocumentAdapter);
        this.adapter.setOnItemClickListener(new AllDocumentAdapter.ClickListener() {
            public void safedk_AllDocumentFragment_startActivity_d4cdb4d8555e48286b1eb0f57ebff6f5(AllDocumentFragment p0, Intent p1) {
                if (p1 != null) {
                    p0.startActivity(p1);
                }
            }

            public void onItemClick(int i, View view) {
                if (AllDocumentFragment.this.documentList.get(i).isCheckboxVisible()) {
                    if (AllDocumentFragment.this.documentList.get(i).isSelected()) {
                        AllDocumentFragment.this.documentList.get(i).setSelected(false);
                    } else {
                        AllDocumentFragment.this.documentList.get(i).setSelected(true);
                    }
                    AllDocumentFragment.this.adapter.notifyDataSetChanged();
                    AllDocumentFragment.this.setSelectedFile();
                    return;
                }
                File file = new File(AllDocumentFragment.this.documentList.get(i).getPath());
                Context applicationContext = AllDocumentFragment.this.getContext().getApplicationContext();
                Uri uriForFile = FileProvider.getUriForFile(applicationContext, AllDocumentFragment.this.getContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(uriForFile, Utils.getMimeTypeFromFilePath(file.getPath()));
                intent.addFlags(1);
                safedk_AllDocumentFragment_startActivity_d4cdb4d8555e48286b1eb0f57ebff6f5(AllDocumentFragment.this, Intent.createChooser(intent, "Open with"));
            }
        });
        this.adapter.setOnLongClickListener(new AllDocumentAdapter.LongClickListener() {
            public void onItemLongClick(int i, View view) {
                AllDocumentFragment.this.documentList.get(i).setSelected(true);
                if (!AllDocumentFragment.this.documentList.get(i).isCheckboxVisible()) {
                    for (int i2 = 0; i2 < AllDocumentFragment.this.documentList.size(); i2++) {
                        AllDocumentFragment.this.documentList.get(i2).setCheckboxVisible(true);
                    }
                }
                AllDocumentFragment.this.adapter.notifyDataSetChanged();
                AllDocumentFragment.this.setSelectedFile();
            }
        });
    }

    
    public void updateDeleteData(ArrayList<String> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            if (this.documentList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= this.documentList.size()) {
                            break;
                        } else if (arrayList.get(i).equalsIgnoreCase(this.documentList.get(i2).getPath())) {
                            this.documentList.remove(i2);
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            }
            AllDocumentAdapter allDocumentAdapter = this.adapter;
            if (allDocumentAdapter != null) {
                allDocumentAdapter.notifyDataSetChanged();
            }
            ArrayList<DocumentModel> arrayList2 = this.documentList;
            if (arrayList2 == null || arrayList2.size() == 0) {
                this.recyclerView.setVisibility(8);
                this.llEmpty.setVisibility(0);
                return;
            }
            this.recyclerView.setVisibility(0);
            this.llEmpty.setVisibility(8);
        }
    }

    public void deleteFile() {
        final ArrayList arrayList = new ArrayList();
        if (this.documentList != null) {
            for (int i = 0; i < this.documentList.size(); i++) {
                if (this.documentList.get(i).isSelected()) {
                    File file = new File(this.documentList.get(i).getPath());
                    if (StorageUtils.deleteFile(file, getActivity())) {
                        arrayList.add(file.getPath());
                        MediaScannerConnection.scanFile(getActivity(), new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                            }
                        });
                    }
                }
            }
        }
        if (this.documentList != null) {
            int i2 = 0;
            while (i2 < this.documentList.size()) {
                this.documentList.get(i2).setCheckboxVisible(false);
                if (this.documentList.get(i2).isSelected()) {
                    this.documentList.remove(i2);
                    if (i2 != 0) {
                        i2--;
                    }
                }
                i2++;
            }
            try {
                if (this.documentList.size() != 1 && 1 < this.documentList.size() && this.documentList.get(1).isSelected()) {
                    this.documentList.remove(1);
                }
                if (this.documentList.size() != 0 && this.documentList.get(0).isSelected()) {
                    this.documentList.remove(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                RxBus.getInstance().post(new DisplayDeleteEvent(arrayList));
                RxBus.getInstance().post(new DocumentDeleteEvent("ALLDoc", arrayList));
                AllDocumentFragment.this.selectedHome.OnSelected(true, false, 0);
                AllDocumentFragment.this.llBottomOption.setVisibility(8);
                if (AllDocumentFragment.this.adapter != null) {
                    AllDocumentFragment.this.adapter.notifyDataSetChanged();
                }
                if (AllDocumentFragment.this.loadingDialog != null && AllDocumentFragment.this.loadingDialog.isShowing()) {
                    AllDocumentFragment.this.loadingDialog.dismiss();
                }
                if (AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0) {
                    AllDocumentFragment.this.recyclerView.setVisibility(8);
                    AllDocumentFragment.this.llEmpty.setVisibility(0);
                } else {
                    AllDocumentFragment.this.recyclerView.setVisibility(0);
                    AllDocumentFragment.this.llEmpty.setVisibility(8);
                }
                Toast.makeText(AllDocumentFragment.this.getActivity(), "Delete file successfully", 0).show();
            }
        });
    }

    public void getList() {
        getDocumentList();
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (!(AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0)) {
                        int sortType = PreferencesManager.getSortType(AllDocumentFragment.this.getActivity());
                        if (sortType == 1) {
                            AllDocumentFragment.this.sortNameAscending();
                        } else if (sortType == 2) {
                            AllDocumentFragment.this.sortNameDescending();
                        } else if (sortType == 3) {
                            AllDocumentFragment.this.sortSizeDescending();
                        } else if (sortType == 4) {
                            AllDocumentFragment.this.sortSizeAscending();
                        } else if (sortType == 5) {
                            AllDocumentFragment.this.setDateWiseSortAs(true);
                        } else if (sortType == 6) {
                            AllDocumentFragment.this.setDateWiseSortAs(false);
                        } else {
                            AllDocumentFragment.this.sortNameAscending();
                        }
                    }
                    AllDocumentFragment.this.setAdapter();
                }
            });
        }
    }

    private void getDocumentList() {
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(getActivity());
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        Cursor query = getContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "_id", "date_modified", "_size", "mime_type"}, (String) null, (String[]) null, "LOWER(date_modified) DESC");
        if (query != null && query.moveToFirst()) {
            do {
                long j = query.getLong(query.getColumnIndex("_size"));
                String string = query.getString(query.getColumnIndex("_data"));
                Log.i("TAG", "getDocumentList: " + string);
                if (!(j == 0 || string == null || !contains(this.types, string))) {
                    String string2 = query.getString(query.getColumnIndex("_id"));
                    String string3 = query.getString(query.getColumnIndex("mime_type"));
                    long j2 = query.getLong(query.getColumnIndex("date_modified"));
                    DocumentModel documentModel = new DocumentModel();
                    documentModel.setName(string2);
                    documentModel.setPath(string);
                    documentModel.setSize(j);
                    documentModel.setDateValue(j2);
                    if (favouriteList.contains(string)) {
                        documentModel.setFavorite(true);
                    } else {
                        documentModel.setFavorite(false);
                    }
                    documentModel.setAppType(string3);
                    documentModel.setSelected(false);
                    documentModel.setCheckboxVisible(false);
                    this.documentList.add(documentModel);
                }
            } while (query.moveToNext());
            query.close();
        }
    }

    public boolean contains(String[] strArr, String str) {
        for (String endsWith : strArr) {
            if (str.endsWith(endsWith)) {
                return true;
            }
        }
        return false;
    }

    private void documentDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DocumentDeleteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DocumentDeleteEvent>() {
            public void call(DocumentDeleteEvent documentDeleteEvent) {
                if (documentDeleteEvent.getType() != null && !documentDeleteEvent.getType().equalsIgnoreCase("") && !documentDeleteEvent.getType().equalsIgnoreCase("ALLDoc") && documentDeleteEvent.getDeleteList() != null && documentDeleteEvent.getDeleteList().size() != 0) {
                    new ArrayList();
                    AllDocumentFragment.this.updateDeleteData(documentDeleteEvent.getDeleteList());
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void copyMoveEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(CopyMoveEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<CopyMoveEvent>() {
            public void call(CopyMoveEvent copyMoveEvent) {
                if (copyMoveEvent.getCopyMoveList() != null && copyMoveEvent.getCopyMoveList().size() != 0) {
                    new ArrayList();
                    ArrayList<File> copyMoveList = copyMoveEvent.getCopyMoveList();
                    if (copyMoveList == null) {
                        copyMoveList = new ArrayList<>();
                    }
                    ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(AllDocumentFragment.this.getActivity());
                    if (favouriteList == null) {
                        favouriteList = new ArrayList<>();
                    }
                    for (int i = 0; i < copyMoveList.size(); i++) {
                        File file = copyMoveList.get(i);
                        if (file.isFile() && file.getPath() != null) {
                            AllDocumentFragment allDocumentFragment = AllDocumentFragment.this;
                            if (allDocumentFragment.contains(allDocumentFragment.types, file.getPath())) {
                                DocumentModel documentModel = new DocumentModel();
                                documentModel.setName(file.getName());
                                documentModel.setPath(file.getPath());
                                documentModel.setSize(file.length());
                                documentModel.setDateValue(file.lastModified());
                                if (favouriteList.contains(file.getPath())) {
                                    documentModel.setFavorite(true);
                                } else {
                                    documentModel.setFavorite(false);
                                }
                                documentModel.setAppType(Utils.getMimeTypeFromFilePath(file.getPath()));
                                documentModel.setSelected(false);
                                documentModel.setCheckboxVisible(false);
                                AllDocumentFragment.this.documentList.add(0, documentModel);
                            }
                        }
                        if (AllDocumentFragment.this.adapter != null) {
                            AllDocumentFragment.this.adapter.notifyDataSetChanged();
                        } else {
                            AllDocumentFragment.this.setAdapter();
                        }
                        if (AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0) {
                            AllDocumentFragment.this.recyclerView.setVisibility(8);
                            AllDocumentFragment.this.llEmpty.setVisibility(0);
                        } else {
                            AllDocumentFragment.this.recyclerView.setVisibility(0);
                            AllDocumentFragment.this.llEmpty.setVisibility(8);
                        }
                        AllDocumentFragment allDocumentFragment2 = AllDocumentFragment.this;
                        allDocumentFragment2.setSort(PreferencesManager.getSortType(allDocumentFragment2.getActivity()));
                    }
                    if (copyMoveEvent.getDeleteList() != null && copyMoveEvent.getDeleteList().size() != 0) {
                        new ArrayList();
                        ArrayList<String> deleteList = copyMoveEvent.getDeleteList();
                        if (deleteList == null) {
                            deleteList = new ArrayList<>();
                        }
                        AllDocumentFragment.this.updateDeleteData(deleteList);
                    }
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void selectEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DocumentSelectEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DocumentSelectEvent>() {
            public void call(DocumentSelectEvent documentSelectEvent) {
                if (documentSelectEvent.getType().equalsIgnoreCase("ALLDoc")) {
                    if (documentSelectEvent.isSelected()) {
                        if (!(AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0)) {
                            for (int i = 0; i < AllDocumentFragment.this.documentList.size(); i++) {
                                if (AllDocumentFragment.this.documentList.get(i) != null) {
                                    AllDocumentFragment.this.documentList.get(i).setSelected(true);
                                }
                            }
                        }
                    } else if (!(AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0)) {
                        for (int i2 = 0; i2 < AllDocumentFragment.this.documentList.size(); i2++) {
                            AllDocumentFragment.this.documentList.get(i2).setSelected(false);
                        }
                    }
                    if (AllDocumentFragment.this.adapter != null) {
                        AllDocumentFragment.this.adapter.notifyDataSetChanged();
                    }
                    AllDocumentFragment.this.setSelectedFile();
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void closeEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DocumentCloseEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DocumentCloseEvent>() {
            public void call(DocumentCloseEvent documentCloseEvent) {
                if (documentCloseEvent.getType().equalsIgnoreCase("ALLDoc")) {
                    if (!(AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0)) {
                        for (int i = 0; i < AllDocumentFragment.this.documentList.size(); i++) {
                            AllDocumentFragment.this.documentList.get(i).setSelected(false);
                            AllDocumentFragment.this.documentList.get(i).setCheckboxVisible(false);
                        }
                    }
                    if (AllDocumentFragment.this.adapter != null) {
                        AllDocumentFragment.this.adapter.notifyDataSetChanged();
                    }
                    AllDocumentFragment.this.llBottomOption.setVisibility(8);
                }
                AllDocumentFragment.this.selected_Item = 0;
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void favouriteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DocumentFavouriteEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DocumentFavouriteEvent>() {
            public void call(DocumentFavouriteEvent documentFavouriteEvent) {
                if (documentFavouriteEvent.getType().equalsIgnoreCase("ALLDoc")) {
                    if (documentFavouriteEvent.isFavourite()) {
                        AllDocumentFragment.this.setFavourite();
                    } else {
                        AllDocumentFragment.this.setUnFavourite();
                    }
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    private void favouriteUpdateEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DocumentFavouriteUpdateEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DocumentFavouriteUpdateEvent>() {
            public void call(DocumentFavouriteUpdateEvent documentFavouriteUpdateEvent) {
                if (!documentFavouriteUpdateEvent.getType().equalsIgnoreCase("ALLDoc")) {
                    new ArrayList();
                    ArrayList<String> fileList = documentFavouriteUpdateEvent.getFileList();
                    if (fileList != null && fileList.size() != 0 && AllDocumentFragment.this.documentList != null && AllDocumentFragment.this.documentList.size() != 0) {
                        for (int i = 0; i < fileList.size(); i++) {
                            int i2 = 0;
                            while (i2 < AllDocumentFragment.this.documentList.size()) {
                                if (!AllDocumentFragment.this.documentList.get(i2).getPath().equalsIgnoreCase(fileList.get(i))) {
                                    i2++;
                                } else if (documentFavouriteUpdateEvent.isFavourite()) {
                                    AllDocumentFragment.this.documentList.get(i2).setFavorite(true);
                                } else {
                                    AllDocumentFragment.this.documentList.get(i2).setFavorite(false);
                                }
                            }
                        }
                        if (AllDocumentFragment.this.adapter != null) {
                            AllDocumentFragment.this.adapter.notifyDataSetChanged();
                        }
                        if (AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0) {
                            AllDocumentFragment.this.llEmpty.setVisibility(0);
                            AllDocumentFragment.this.recyclerView.setVisibility(8);
                            return;
                        }
                        AllDocumentFragment.this.llEmpty.setVisibility(8);
                        AllDocumentFragment.this.recyclerView.setVisibility(0);
                    }
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void setUnFavourite() {
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(getActivity());
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < this.documentList.size(); i2++) {
            if (this.documentList.get(i2).isSelected() && this.documentList.get(i2).isFavorite()) {
                this.documentList.get(i2).setFavorite(false);
                i++;
                arrayList.add(this.documentList.get(i2).getPath());
                if (favouriteList.contains(this.documentList.get(i2).getPath())) {
                    favouriteList.remove(this.documentList.get(i2).getPath());
                }
            }
            this.documentList.get(i2).setSelected(false);
            this.documentList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
        this.selectedHome.OnSelected(true, false, 0);
        String str = i == 1 ? " item removed from Favourites." : " items removed from Favourites.";
        FragmentActivity activity = getActivity();
        Toast.makeText(activity, i + str, 0).show();
        RxBus.getInstance().post(new DocumentFavouriteUpdateEvent("ALLDoc", false, arrayList));
        PreferencesManager.setFavouriteList(getActivity(), favouriteList);
    }

    
    public void setFavourite() {
        ArrayList<String> favouriteList = PreferencesManager.getFavouriteList(getActivity());
        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < this.documentList.size(); i2++) {
            if (this.documentList.get(i2).isSelected()) {
                if (!this.documentList.get(i2).isFavorite()) {
                    arrayList.add(this.documentList.get(i2).getPath());
                    favouriteList.add(0, this.documentList.get(i2).getPath());
                    i++;
                }
                this.documentList.get(i2).setFavorite(true);
            }
            this.documentList.get(i2).setSelected(false);
            this.documentList.get(i2).setCheckboxVisible(false);
        }
        this.adapter.notifyDataSetChanged();
        this.llBottomOption.setVisibility(8);
        this.selected_Item = 0;
        this.selectedHome.OnSelected(true, false, 0);
        String str = i == 1 ? " item added to Favourites." : " items added to Favourites.";
        FragmentActivity activity = getActivity();
        Toast.makeText(activity, i + str, 0).show();
        RxBus.getInstance().post(new DocumentFavouriteUpdateEvent("ALLDoc", true, arrayList));
        PreferencesManager.setFavouriteList(getActivity(), favouriteList);
    }

    private void showMoreOptionBottom() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), this.loutMore);
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
                    AllDocumentFragment.this.showDetailDialog();
                    return false;
                } else if (itemId == R.id.menu_rename) {
                    AllDocumentFragment.this.showRenameDialog();
                    return false;
                } else if (itemId != R.id.menu_share) {
                    return false;
                } else {
                    AllDocumentFragment.this.sendFile();
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    
    public void showDetailDialog() {
        AllDocumentFragment allDocumentFragment;
        final Dialog dialog = new Dialog(getActivity(), R.style.WideDialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(80);
        File file = new File(this.documentList.get(this.pos).getPath());
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
            textView5.setText(Formatter.formatShortFileSize(getActivity(), file.length()));
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
                    try {
                        allDocumentFragment = this;
                        try {
                            textView6.setText(allDocumentFragment.getDurationString((int) Long.parseLong(mediaMetadataRetriever.extractMetadata(9))));
                            textView4.setText(parseInt2 + "X" + parseInt);
                            linearLayout2.setVisibility(0);
                            linearLayout.setVisibility(0);
                        } catch (Exception unused) {
                        }
                    } catch (Exception unused2) {
                    }
                } catch (Exception e2) {
                    allDocumentFragment = this;
                    e2.printStackTrace();
                    linearLayout2.setVisibility(8);
                    linearLayout.setVisibility(8);
                    textView8.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
            allDocumentFragment = this;
        } else {
            allDocumentFragment = this;
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
        final File file = new File(this.documentList.get(this.pos).getPath());
        final Dialog dialog = new Dialog(getActivity(), R.style.WideDialog);
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
                    Toast.makeText(AllDocumentFragment.this.getActivity(), "New name can't be empty.", 0).show();
                } else if (appCompatEditText.getText().toString().equalsIgnoreCase(file.getName())) {
                    dialog.show();
                } else if (!file.isDirectory()) {
                    String[] split = appCompatEditText.getText().toString().split("\\.");
                    if (!split[split.length - 1].equalsIgnoreCase(filenameExtension)) {
                        final Dialog dialog = new Dialog(AllDocumentFragment.this.getActivity(), R.style.WideDialog);
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
                                AllDocumentFragment.this.reNameFile(file, appCompatEditText.getText().toString());
                            }
                        });
                        dialog.show();
                        return;
                    }
                    Log.e("", "rename");
                    dialog.dismiss();
                    AllDocumentFragment.this.reNameFile(file, appCompatEditText.getText().toString());
                } else {
                    dialog.dismiss();
                    AllDocumentFragment.this.reNameFile(file, appCompatEditText.getText().toString());
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
            z = StorageUtils.renameFile(file, str, getActivity());
        }
        if (z) {
            Log.e("LOG", "File renamed...");
            MediaScannerConnection.scanFile(getActivity(), new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
            this.documentList.get(this.pos).setPath(file2.getPath());
            this.documentList.get(this.pos).setName(file2.getName());
            this.adapter.notifyItemChanged(this.pos);
            Toast.makeText(getActivity(), "Rename file successfully", 0).show();
            RxBus.getInstance().post(new RenameEvent(file, file2));
            setSort(PreferencesManager.getSortType(getActivity()));
            return;
        }
        Log.e("LOG", "File not renamed...");
    }

    private void showRenameValidationDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.WideDialog);
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

    private void renameEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(RenameEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<RenameEvent>() {
            public void call(RenameEvent renameEvent) {
                if (renameEvent.getNewFile() != null && renameEvent.getOldFile() != null && renameEvent.getNewFile().exists()) {
                    if (AllDocumentFragment.this.documentList != null && AllDocumentFragment.this.documentList.size() != 0) {
                        int i = 0;
                        while (true) {
                            if (i >= AllDocumentFragment.this.documentList.size()) {
                                break;
                            } else if (renameEvent.getOldFile().getPath().equalsIgnoreCase(AllDocumentFragment.this.documentList.get(i).getPath())) {
                                AllDocumentFragment.this.documentList.get(i).setPath(renameEvent.getNewFile().getPath());
                                AllDocumentFragment.this.documentList.get(i).setName(renameEvent.getNewFile().getName());
                                break;
                            } else {
                                i++;
                            }
                        }
                    }
                    if (AllDocumentFragment.this.adapter != null) {
                        AllDocumentFragment.this.adapter.notifyDataSetChanged();
                    } else {
                        AllDocumentFragment.this.setAdapter();
                    }
                    if (AllDocumentFragment.this.documentList == null || AllDocumentFragment.this.documentList.size() == 0) {
                        AllDocumentFragment.this.recyclerView.setVisibility(8);
                        AllDocumentFragment.this.llEmpty.setVisibility(0);
                        return;
                    }
                    AllDocumentFragment.this.recyclerView.setVisibility(0);
                    AllDocumentFragment.this.llEmpty.setVisibility(8);
                }
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    
    public void sortNameAscending() {
        Collections.sort(this.documentList, new Comparator<DocumentModel>() {
            public int compare(DocumentModel documentModel, DocumentModel documentModel2) {
                return documentModel.getName().compareToIgnoreCase(documentModel2.getName());
            }
        });
    }

    
    public void sortNameDescending() {
        Collections.sort(this.documentList, new Comparator<DocumentModel>() {
            public int compare(DocumentModel documentModel, DocumentModel documentModel2) {
                return documentModel2.getName().compareToIgnoreCase(documentModel.getName());
            }
        });
    }

    
    public void sortSizeAscending() {
        Collections.sort(this.documentList, new Comparator<DocumentModel>() {
            public int compare(DocumentModel documentModel, DocumentModel documentModel2) {
                return Long.valueOf(documentModel.getSize()).compareTo(Long.valueOf(documentModel2.getSize()));
            }
        });
    }

    
    public void sortSizeDescending() {
        Collections.sort(this.documentList, new Comparator<DocumentModel>() {
            public int compare(DocumentModel documentModel, DocumentModel documentModel2) {
                return Long.valueOf(documentModel2.getSize()).compareTo(Long.valueOf(documentModel.getSize()));
            }
        });
    }

    
    public void setDateWiseSortAs(final boolean z) {
        Collections.sort(this.documentList, new Comparator<DocumentModel>() {
            public int compare(DocumentModel documentModel, DocumentModel documentModel2) {
                try {
                    if (z) {
                        return Long.valueOf(documentModel2.getDateValue()).compareTo(Long.valueOf(documentModel.getDateValue()));
                    }
                    return Long.valueOf(documentModel.getDateValue()).compareTo(Long.valueOf(documentModel2.getDateValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }

    private void sortEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DocumentSortEvent.class).subscribeOn(Schedulers.io()).distinctUntilChanged().subscribe(new Action1<DocumentSortEvent>() {
            public void call(DocumentSortEvent documentSortEvent) {
                AllDocumentFragment.this.setSort(documentSortEvent.getType());
            }
        }, (Action1<Throwable>) new Action1<Throwable>() {
            public void call(Throwable th) {
            }
        }));
    }

    public void setSort(int i) {
        switch (i) {
            case 1:
                ArrayList<DocumentModel> arrayList = this.documentList;
                if (arrayList != null && arrayList.size() != 0) {
                    sortNameAscending();
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case 2:
                ArrayList<DocumentModel> arrayList2 = this.documentList;
                if (arrayList2 != null && arrayList2.size() != 0) {
                    sortNameDescending();
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case 3:
                ArrayList<DocumentModel> arrayList3 = this.documentList;
                if (arrayList3 != null && arrayList3.size() != 0) {
                    sortSizeDescending();
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case 4:
                ArrayList<DocumentModel> arrayList4 = this.documentList;
                if (arrayList4 != null && arrayList4.size() != 0) {
                    sortSizeAscending();
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case 5:
                ArrayList<DocumentModel> arrayList5 = this.documentList;
                if (arrayList5 != null && arrayList5.size() != 0) {
                    setDateWiseSortAs(true);
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case 6:
                ArrayList<DocumentModel> arrayList6 = this.documentList;
                if (arrayList6 != null && arrayList6.size() != 0) {
                    setDateWiseSortAs(false);
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void showCompressDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.WideDialog);
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
                    if (new File(AllDocumentFragment.this.compressPath + InternalZipConstants.ZIP_FILE_SEPARATOR + str + ".zip").exists()) {
                        Toast.makeText(AllDocumentFragment.this.getActivity(), "File name already use", 0).show();
                        return;
                    }
                    AllDocumentFragment.this.zip_file_name = str;
                    dialog.dismiss();
                    if (AllDocumentFragment.this.isFileFromSdCard) {
                        AllDocumentFragment.this.sdCardPermissionType = 3;
                        if (StorageUtils.checkFSDCardPermission(new File(AllDocumentFragment.this.sdCardPath), AllDocumentFragment.this.getActivity()) == 2) {
                            Toast.makeText(AllDocumentFragment.this.getActivity(), "Please give a permission for manager operation", 0).show();
                        } else {
                            AllDocumentFragment.this.setcompress();
                        }
                    } else {
                        AllDocumentFragment.this.setcompress();
                    }
                } else {
                    Toast.makeText(AllDocumentFragment.this.getActivity(), AllDocumentFragment.this.getResources().getString(R.string.zip_validation), 0).show();
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
                    AllDocumentFragment.this.compressfile();
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
            file2 = new File(this.documentList.get(this.pos).getPath());
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
            for (int i = 0; i < this.documentList.size(); i++) {
                if (this.documentList.get(i) != null) {
                    DocumentModel documentModel = this.documentList.get(i);
                    if (documentModel.isSelected()) {
                        File file4 = new File(documentModel.getPath());
                        StorageUtils.copyFile(file4, new File(file2.getPath() + InternalZipConstants.ZIP_FILE_SEPARATOR + file4.getName()), getActivity());
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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AllDocumentFragment.this.setSelectionClose();
                if (str != null) {
                    if (AllDocumentFragment.this.loadingDialog.isShowing()) {
                        AllDocumentFragment.this.loadingDialog.dismiss();
                    }
                    Toast.makeText(AllDocumentFragment.this.getActivity(), "Compress file successfully", 0).show();
                    MediaScannerConnection.scanFile(AllDocumentFragment.this.getActivity(), new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                    RxBus.getInstance().post(new CopyMoveEvent(str));
                    if (AllDocumentFragment.this.selected_Item != 1) {
                        if (StorageUtils.deleteFile(file2, AllDocumentFragment.this.getActivity())) {
                            MediaScannerConnection.scanFile(AllDocumentFragment.this.getActivity(), new String[]{file2.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String str, Uri uri) {
                                }
                            });
                        }
                        if (file != null && StorageUtils.deleteFile(file, AllDocumentFragment.this.getActivity())) {
                            MediaScannerConnection.scanFile(AllDocumentFragment.this.getActivity(), new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
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
