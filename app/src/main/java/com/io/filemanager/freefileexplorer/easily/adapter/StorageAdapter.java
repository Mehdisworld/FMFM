package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.model.InternalStorageFilesModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
//import com.onesignal.OSInAppMessageContentKt;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StorageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_AD = 2;
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_ITEM_GRID = 1;
    
    public static ClickListener listener;
    
    public static LongClickListener longClickListener;
    Context context;
    ArrayList<InternalStorageFilesModel> internalStorageList = new ArrayList<>();
    boolean isGrid = false;

    public interface ClickListener {
        void onItemClick(int i, View view);
    }

    public interface LongClickListener {
        void onItemLongClick(int i, View view);
    }


    public StorageAdapter(Context context2, ArrayList<InternalStorageFilesModel> arrayList, boolean z) {
        this.context = context2;
        this.internalStorageList = arrayList;
        this.isGrid = z;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        listener = clickListener;
    }

    public void setOnLongClickListener(LongClickListener longClickListener2) {
        longClickListener = longClickListener2;
    }

    public int getItemViewType(int i) {
        if (this.internalStorageList.get(i) == null) {
            return 2;
        }
        return this.isGrid ? 1 : 0;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_storage_list, viewGroup, false));
        }
        if (i != 2 && i == 1) {
            return new GridHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_storage_grid, viewGroup, false));
        }
        throw new RuntimeException("there is no type that matches the type " + i + " + make sure your using types correctly");
    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int i2;
        String str;
        int i3 = i;
        try {
            int itemViewType = getItemViewType(i3);
            String str2 = "application/vnd.android.package-archive";
            String str3 = "pptm";
            if (itemViewType == 0) {
                try {
                    final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
                    InternalStorageFilesModel internalStorageFilesModel = this.internalStorageList.get(i3);
                    String str4 = "application/rar";
                    String str5 = "application/zip";
                    viewHolder2.txtFolderName.setText(internalStorageFilesModel.getFileName());
                    File file = new File(internalStorageFilesModel.getFilePath());
                    String filenameExtension = com.io.filemanager.freefileexplorer.easily.utils.Utils.getFilenameExtension(file.getName());
                    String str6 = "video/3gpp";
                    viewHolder2.iv_image.setVisibility(8);
                    if (this.internalStorageList.get(i3).isCheckboxVisible()) {
                        viewHolder2.ll_check.setVisibility(0);
                        if (this.internalStorageList.get(i3).isSelected()) {
                            viewHolder2.ivCheck.setVisibility(0);
                            viewHolder2.ivUncheck.setVisibility(8);
                        } else {
                            viewHolder2.ivUncheck.setVisibility(0);
                            viewHolder2.ivCheck.setVisibility(8);
                        }
                    } else {
                        viewHolder2.ll_check.setVisibility(8);
                    }
                    if (file.isDirectory()) {
                        int filesList = getFilesList(file.getPath());
                        AppCompatTextView appCompatTextView = viewHolder2.txtFolderItem;
                        appCompatTextView.setText(filesList + " item");
                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_folder));
                        viewHolder2.ivFolder.setVisibility(0);
                        viewHolder2.cardView.setVisibility(8);
                    } else {
                        String mineType = internalStorageFilesModel.getMineType();
                        if (mineType != null) {
                            Log.e("storage", "type: " + mineType + "  name: " + file.getName());
                            if (!mineType.equalsIgnoreCase("image/jpeg") && !mineType.equalsIgnoreCase("image/png") && !mineType.equalsIgnoreCase("image/webp")) {
                                if (!mineType.equalsIgnoreCase("video/mp4") && !mineType.equalsIgnoreCase("video/x-matroska")) {
                                    if (!mineType.equalsIgnoreCase("audio/mpeg") && !mineType.equalsIgnoreCase("audio/aac") && !mineType.equalsIgnoreCase("audio/ogg") && !mineType.equalsIgnoreCase(str6)) {
                                        if (mineType.equalsIgnoreCase(str5)) {
                                            viewHolder2.ivFolder.setVisibility(0);
                                            viewHolder2.cardView.setVisibility(8);
                                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_zip_file));
                                        } else if (mineType.equalsIgnoreCase(str4)) {
                                            viewHolder2.ivFolder.setVisibility(0);
                                            viewHolder2.cardView.setVisibility(8);
                                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_rar_file));
                                        } else if (mineType.equalsIgnoreCase(str2)) {
                                            viewHolder2.iv_image.setVisibility(0);
                                            viewHolder2.ivFolder.setVisibility(8);
                                            viewHolder2.cardView.setVisibility(8);
                                            viewHolder2.iv_image.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_apk_file));
                                            PackageManager packageManager = this.context.getPackageManager();
                                            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(internalStorageFilesModel.getFilePath(), 0);
                                            viewHolder2.iv_image.setImageDrawable(this.context.getPackageManager().getApplicationIcon(packageArchiveInfo.packageName));
                                            ((RequestBuilder) Glide.with(this.context).load(this.context.getPackageManager().getApplicationIcon(packageArchiveInfo.packageName)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).addListener(new RequestListener<Drawable>() {
                                                public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                                                    return false;
                                                }

                                                public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                                                    viewHolder2.ivFolder.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_apk_file));
                                                    viewHolder2.ivFolder.setVisibility(0);
                                                    viewHolder2.cardView.setVisibility(8);
                                                    return false;
                                                }
                                            }).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).placeholder((int) R.drawable.ic_apk_file)).into((ImageView) viewHolder2.iv_image);
                                            ((RequestBuilder) Glide.with(this.context).load(packageArchiveInfo.applicationInfo.loadIcon(packageManager)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).addListener(new RequestListener<Drawable>() {
                                                public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                                                    return false;
                                                }

                                                public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                                                    viewHolder2.ivFolder.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_apk_file));
                                                    viewHolder2.ivFolder.setVisibility(0);
                                                    viewHolder2.cardView.setVisibility(8);
                                                    return false;
                                                }
                                            }).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).placeholder((int) R.drawable.ic_apk_file)).into((ImageView) viewHolder2.iv_image);
                                        } else {
                                            str = filenameExtension;
                                            if (str.equalsIgnoreCase("pdf")) {
                                                viewHolder2.txtMimeType.setText(str);
                                                viewHolder2.ivFolder.setVisibility(0);
                                                viewHolder2.cardView.setVisibility(8);
                                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_pdf_file));
                                            } else {
                                                if (!str.equalsIgnoreCase("doc")) {
                                                    if (!str.equalsIgnoreCase("docx")) {
                                                        if (!str.equalsIgnoreCase("xlsx") && !str.equalsIgnoreCase("xls") && !str.equalsIgnoreCase("xlc")) {
                                                            if (!str.equalsIgnoreCase("xld")) {
                                                                if (!str.equalsIgnoreCase("ppt") && !str.equalsIgnoreCase("pptx") && !str.equalsIgnoreCase("ppsx")) {
                                                                    String str7 = str3;
                                                                    if (!str.equalsIgnoreCase(str7)) {
                                                                        if (!str.equalsIgnoreCase("txt") && !str.equalsIgnoreCase("tex") && !str.equalsIgnoreCase("text")) {
                                                                            if (!str.equalsIgnoreCase(str7)) {
                                                                                if (str.equalsIgnoreCase("xml")) {
                                                                                    viewHolder2.ivFolder.setVisibility(0);
                                                                                    viewHolder2.cardView.setVisibility(8);
                                                                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xml_file));
                                                                                } else {
                                                                                    if (!str.equalsIgnoreCase("html")) {
                                                                                        if (!str.equalsIgnoreCase("htm")) {
                                                                                            if (str.equalsIgnoreCase("java")) {
                                                                                                viewHolder2.ivFolder.setVisibility(0);
                                                                                                viewHolder2.cardView.setVisibility(8);
                                                                                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                                            } else if (str.equalsIgnoreCase("php")) {
                                                                                                viewHolder2.ivFolder.setVisibility(0);
                                                                                                viewHolder2.cardView.setVisibility(8);
                                                                                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                                            } else {
                                                                                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                                                viewHolder2.ivFolder.setVisibility(0);
                                                                                                viewHolder2.cardView.setVisibility(8);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    viewHolder2.ivFolder.setVisibility(0);
                                                                                    viewHolder2.cardView.setVisibility(8);
                                                                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_html_file));
                                                                                }
                                                                            }
                                                                        }
                                                                        viewHolder2.ivFolder.setVisibility(0);
                                                                        viewHolder2.cardView.setVisibility(8);
                                                                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_text_file));
                                                                    }
                                                                }
                                                                viewHolder2.ivFolder.setVisibility(0);
                                                                viewHolder2.cardView.setVisibility(8);
                                                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_ppt_file));
                                                            }
                                                        }
                                                        viewHolder2.ivFolder.setVisibility(0);
                                                        viewHolder2.cardView.setVisibility(8);
                                                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xls_file));
                                                    }
                                                }
                                                viewHolder2.ivFolder.setVisibility(0);
                                                viewHolder2.cardView.setVisibility(8);
                                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_doc_file));
                                            }
                                            viewHolder2.txtMimeType.setText(str);
                                            viewHolder2.ivFolder.setVisibility(0);
                                            viewHolder2.cardView.setVisibility(8);
                                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_audio_file));
                                        }
                                    }
                                    str = filenameExtension;
                                    viewHolder2.txtMimeType.setText(str);
                                    viewHolder2.ivFolder.setVisibility(0);
                                    viewHolder2.cardView.setVisibility(8);
                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_audio_file));
                                }
                                ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(file.getPath()).placeholder((int) R.drawable.ic_video_placeholder)).error((int) R.drawable.ic_video_placeholder)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((ImageView) viewHolder2.iv_image);
                                viewHolder2.iv_image.setVisibility(0);
                                viewHolder2.ivFolder.setVisibility(8);
                                viewHolder2.cardView.setVisibility(8);
                            }
                            ((RequestBuilder) Glide.with(this.context).load(Uri.fromFile(file)).placeholder((int) R.drawable.ic_image_placeholder)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((ImageView) viewHolder2.iv_image);
                            viewHolder2.iv_image.setVisibility(0);
                            viewHolder2.ivFolder.setVisibility(8);
                            viewHolder2.cardView.setVisibility(8);
                        } else {
                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                            viewHolder2.ivFolder.setVisibility(0);
                            viewHolder2.cardView.setVisibility(8);
                        }
                        viewHolder2.txtFolderItem.setText(com.io.filemanager.freefileexplorer.easily.utils.Utils.formateSize(file.length()));
                    }
                    if (!internalStorageFilesModel.isFavorite()) {
                        viewHolder2.iv_fav_image.setVisibility(8);
                        viewHolder2.iv_fav_file.setVisibility(8);
                        viewHolder2.iv_fav_other_file.setVisibility(8);
                    } else if (viewHolder2.ivFolder.getVisibility() != 0) {
                        viewHolder2.iv_fav_image.setVisibility(0);
                        viewHolder2.iv_fav_file.setVisibility(8);
                        viewHolder2.iv_fav_other_file.setVisibility(8);
                    } else if (internalStorageFilesModel.isDir()) {
                        viewHolder2.iv_fav_file.setVisibility(0);
                        viewHolder2.iv_fav_image.setVisibility(8);
                        viewHolder2.iv_fav_other_file.setVisibility(8);
                    } else {
                        viewHolder2.iv_fav_file.setVisibility(8);
                        viewHolder2.iv_fav_image.setVisibility(8);
                        viewHolder2.iv_fav_other_file.setVisibility(0);
                    }
                    viewHolder2.txtDateTime.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm").format(Long.valueOf(file.lastModified())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String str8 = str3;
                String str9 = "application/zip";
                String str10 = "video/3gpp";
                if (itemViewType == 1) {
                    try {
                        GridHolder gridHolder = (GridHolder) viewHolder;
                        InternalStorageFilesModel internalStorageFilesModel2 = this.internalStorageList.get(i3);
                        String str11 = "application/rar";
                        String str12 = str9;
                        gridHolder.txtFolderName.setText(internalStorageFilesModel2.getFileName());
                        File file2 = new File(internalStorageFilesModel2.getFilePath());
                        String str13 = str10;
                        String str14 = "audio/ogg";
                        gridHolder.txtDate.setText(new SimpleDateFormat("dd MMM yyyy").format(Long.valueOf(file2.lastModified())));
                        gridHolder.iv_folder_image.setVisibility(8);
                        String filenameExtension2 = com.io.filemanager.freefileexplorer.easily.utils.Utils.getFilenameExtension(file2.getName());
                        if (this.internalStorageList.get(i3).isCheckboxVisible()) {
                            gridHolder.ll_check_grid.setVisibility(0);
                            if (this.internalStorageList.get(i3).isSelected()) {
                                gridHolder.ivCheck.setVisibility(0);
                                gridHolder.ivUncheck.setVisibility(8);
                            } else {
                                gridHolder.ivCheck.setVisibility(8);
                                gridHolder.ivUncheck.setVisibility(0);
                            }
                        } else {
                            gridHolder.ll_check_grid.setVisibility(8);
                        }
                        if (file2.isDirectory()) {
                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_folder));
                            gridHolder.ivFolder.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                        } else {
                            String mineType2 = internalStorageFilesModel2.getMineType();
                            if (mineType2 != null) {
                                if (!mineType2.equalsIgnoreCase("image/jpeg") && !mineType2.equalsIgnoreCase("image/png") && !mineType2.equalsIgnoreCase("image/webp")) {
                                    if (!mineType2.equalsIgnoreCase("video/mp4") && !mineType2.equalsIgnoreCase("video/x-matroska")) {
                                        if (!mineType2.equalsIgnoreCase("audio/mpeg") && !mineType2.equalsIgnoreCase("audio/aac") && !mineType2.equalsIgnoreCase(str14) && !mineType2.equalsIgnoreCase(str13)) {
                                            if (mineType2.equalsIgnoreCase(str12)) {
                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_zip_file));
                                                gridHolder.ivFolder.setVisibility(0);
                                                gridHolder.ivImage.setVisibility(8);
                                                gridHolder.cardViewImage.setVisibility(8);
                                                gridHolder.cardView.setVisibility(8);
                                            } else if (mineType2.equalsIgnoreCase(str11)) {
                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_rar_file));
                                                gridHolder.ivFolder.setVisibility(0);
                                                gridHolder.ivImage.setVisibility(8);
                                                gridHolder.cardViewImage.setVisibility(8);
                                                gridHolder.cardView.setVisibility(8);
                                            } else if (mineType2.equalsIgnoreCase(str2)) {
                                                gridHolder.ivFolder.setVisibility(8);
                                                gridHolder.ivImage.setVisibility(8);
                                                gridHolder.iv_folder_image.setVisibility(0);
                                                gridHolder.cardViewImage.setVisibility(8);
                                                gridHolder.cardView.setVisibility(8);
                                                PackageManager packageManager2 = this.context.getPackageManager();
                                                PackageInfo packageArchiveInfo2 = packageManager2.getPackageArchiveInfo(internalStorageFilesModel2.getFilePath(), 0);
                                                gridHolder.iv_folder_image.setImageDrawable(this.context.getPackageManager().getApplicationIcon(packageArchiveInfo2.packageName));
                                                gridHolder.iv_folder_image.setImageDrawable(packageArchiveInfo2.applicationInfo.loadIcon(packageManager2));
                                            } else if (filenameExtension2.equalsIgnoreCase("pdf")) {
                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_pdf_file));
                                                gridHolder.ivFolder.setVisibility(0);
                                                gridHolder.ivImage.setVisibility(8);
                                                gridHolder.cardViewImage.setVisibility(8);
                                                gridHolder.cardView.setVisibility(8);
                                            } else {
                                                if (!filenameExtension2.equalsIgnoreCase("doc")) {
                                                    if (!filenameExtension2.equalsIgnoreCase("docx")) {
                                                        if (!filenameExtension2.equalsIgnoreCase("xlsx") && !filenameExtension2.equalsIgnoreCase("xls") && !filenameExtension2.equalsIgnoreCase("xlc")) {
                                                            if (!filenameExtension2.equalsIgnoreCase("xld")) {
                                                                if (!filenameExtension2.equalsIgnoreCase("ppt") && !filenameExtension2.equalsIgnoreCase("pptx") && !filenameExtension2.equalsIgnoreCase("ppsx")) {
                                                                    String str15 = str8;
                                                                    if (!filenameExtension2.equalsIgnoreCase(str15)) {
                                                                        if (!filenameExtension2.equalsIgnoreCase("txt") && !filenameExtension2.equalsIgnoreCase("tex") && !filenameExtension2.equalsIgnoreCase("text")) {
                                                                            if (!filenameExtension2.equalsIgnoreCase(str15)) {
                                                                                if (filenameExtension2.equalsIgnoreCase("xml")) {
                                                                                    gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xml_file));
                                                                                    gridHolder.ivFolder.setVisibility(0);
                                                                                    gridHolder.ivImage.setVisibility(8);
                                                                                    gridHolder.cardViewImage.setVisibility(8);
                                                                                    gridHolder.cardView.setVisibility(8);
                                                                                } else {
                                                                                    if (!filenameExtension2.equalsIgnoreCase("html")) {
                                                                                        if (!filenameExtension2.equalsIgnoreCase("htm")) {
                                                                                            if (filenameExtension2.equalsIgnoreCase("java")) {
                                                                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                                                gridHolder.ivFolder.setVisibility(0);
                                                                                                gridHolder.ivImage.setVisibility(8);
                                                                                                gridHolder.cardViewImage.setVisibility(8);
                                                                                                gridHolder.cardView.setVisibility(8);
                                                                                            } else if (filenameExtension2.equalsIgnoreCase("php")) {
                                                                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                                                gridHolder.ivFolder.setVisibility(0);
                                                                                                gridHolder.ivImage.setVisibility(8);
                                                                                                gridHolder.cardViewImage.setVisibility(8);
                                                                                                gridHolder.cardView.setVisibility(8);
                                                                                            } else {
                                                                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                                                gridHolder.ivFolder.setVisibility(0);
                                                                                                gridHolder.ivImage.setVisibility(8);
                                                                                                gridHolder.cardViewImage.setVisibility(8);
                                                                                                gridHolder.cardView.setVisibility(8);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_html_file));
                                                                                    gridHolder.ivFolder.setVisibility(0);
                                                                                    gridHolder.ivImage.setVisibility(8);
                                                                                    gridHolder.cardViewImage.setVisibility(8);
                                                                                    gridHolder.cardView.setVisibility(8);
                                                                                }
                                                                            }
                                                                        }
                                                                        gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_text_file));
                                                                        gridHolder.ivFolder.setVisibility(0);
                                                                        gridHolder.ivImage.setVisibility(8);
                                                                        gridHolder.cardViewImage.setVisibility(8);
                                                                        gridHolder.cardView.setVisibility(8);
                                                                    }
                                                                }
                                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_ppt_file));
                                                                gridHolder.ivFolder.setVisibility(0);
                                                                gridHolder.ivImage.setVisibility(8);
                                                                gridHolder.cardViewImage.setVisibility(8);
                                                                gridHolder.cardView.setVisibility(8);
                                                            }
                                                        }
                                                        gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xls_file));
                                                        gridHolder.ivFolder.setVisibility(0);
                                                        gridHolder.ivImage.setVisibility(8);
                                                        gridHolder.cardViewImage.setVisibility(8);
                                                        gridHolder.cardView.setVisibility(8);
                                                    }
                                                }
                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_doc_file));
                                                gridHolder.ivFolder.setVisibility(0);
                                                gridHolder.ivImage.setVisibility(8);
                                                gridHolder.cardViewImage.setVisibility(8);
                                                gridHolder.cardView.setVisibility(8);
                                            }
                                        }
                                        gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_audio_file));
                                        gridHolder.ivFolder.setVisibility(0);
                                        gridHolder.ivImage.setVisibility(8);
                                        gridHolder.cardViewImage.setVisibility(8);
                                        gridHolder.cardView.setVisibility(8);
                                    }
                                    ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(file2.getPath()).placeholder((int) R.drawable.ic_video_placeholder)).error((int) R.drawable.ic_video_placeholder)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((ImageView) gridHolder.iv_folder_image);
                                    gridHolder.ivFolder.setVisibility(8);
                                    gridHolder.iv_folder_image.setVisibility(0);
                                    gridHolder.ivImage.setVisibility(8);
                                    gridHolder.cardViewImage.setVisibility(8);
                                    gridHolder.cardView.setVisibility(8);
                                }
                                ((RequestBuilder) Glide.with(this.context).load(Uri.fromFile(file2)).placeholder((int) R.drawable.ic_image_placeholder)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((ImageView) gridHolder.iv_folder_image);
                                gridHolder.ivFolder.setVisibility(8);
                                gridHolder.ivImage.setVisibility(8);
                                gridHolder.iv_folder_image.setVisibility(0);
                                gridHolder.cardViewImage.setVisibility(8);
                                gridHolder.cardView.setVisibility(8);
                            } else {
                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                gridHolder.ivFolder.setVisibility(0);
                                i2 = 8;
                                gridHolder.ivImage.setVisibility(8);
                                gridHolder.cardViewImage.setVisibility(8);
                                gridHolder.cardView.setVisibility(8);
                                if (internalStorageFilesModel2.isFavorite()) {
                                    gridHolder.iv_fav_image.setVisibility(i2);
                                    gridHolder.iv_fav_image_file.setVisibility(i2);
                                    return;
                                } else if (gridHolder.ivFolder.getVisibility() != 0) {
                                    gridHolder.iv_fav_image_file.setVisibility(8);
                                    gridHolder.iv_fav_image.setVisibility(0);
                                    return;
                                } else if (internalStorageFilesModel2.isDir()) {
                                    gridHolder.iv_fav_image_file.setVisibility(0);
                                    gridHolder.iv_fav_image.setVisibility(8);
                                    return;
                                } else {
                                    gridHolder.iv_fav_image_file.setVisibility(8);
                                    gridHolder.iv_fav_image.setVisibility(0);
                                    return;
                                }
                            }
                        }
                        i2 = 8;
                        if (internalStorageFilesModel2.isFavorite()) {
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (itemViewType == 2) {
                    BannerViewHolder bannerViewHolder = (BannerViewHolder) viewHolder;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    private int getFilesList(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            return listFiles.length;
        }
        return 0;
    }

    public String size(int i) {
        double d = (double) i;
        Double.isNaN(d);
        Double.isNaN(d);
        double d2 = d / 1024.0d;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" MB");
        }
        return decimalFormat.format((long) i).concat(" KB");
    }

    public int getItemCount() {
        return this.internalStorageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //@BindView(R.id.card_view)
        CardView cardView;
        //@BindView(R.id.iv_check)
        ImageView ivCheck;
        //@BindView(R.id.iv_folder)
        AppCompatImageView ivFolder;
        //@BindView(R.id.iv_uncheck)
        ImageView ivUncheck;
        //@BindView(R.id.iv_fav_file)
        ImageView iv_fav_file;
        //@BindView(R.id.iv_fav_image)
        ImageView iv_fav_image;
        //@BindView(R.id.iv_fav_other_file)
        ImageView iv_fav_other_file;
        //@BindView(R.id.iv_image)
        AppCompatImageView iv_image;
        //@BindView(R.id.ll_check)
        RelativeLayout ll_check;
        //@BindView(R.id.txt_date_time)
        AppCompatTextView txtDateTime;
        //@BindView(R.id.txt_folder_item)
        AppCompatTextView txtFolderItem;
        //@BindView(R.id.txt_folder_name)
        AppCompatTextView txtFolderName;
        //@BindView(R.id.txt_mime_type)
        AppCompatTextView txtMimeType;


        public ViewHolder(View view) {
            super(view);
            //ButterKnife.bind((Object) this, view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            ivCheck = (ImageView) view.findViewById(R.id.iv_check);
            ivFolder = (AppCompatImageView) view.findViewById(R.id.iv_folder);
            ivUncheck = (ImageView) view.findViewById(R.id.iv_uncheck);
            iv_fav_file = (ImageView) view.findViewById(R.id.iv_fav_file);
            iv_fav_image = (ImageView) view.findViewById(R.id.iv_fav_image);
            iv_fav_other_file = (ImageView) view.findViewById(R.id.iv_fav_other_file);
            iv_image = (AppCompatImageView) view.findViewById(R.id.iv_image);
            ll_check = (RelativeLayout) view.findViewById(R.id.ll_check);
            txtDateTime = (AppCompatTextView) view.findViewById(R.id.txt_date_time);
            txtFolderItem = (AppCompatTextView) view.findViewById(R.id.txt_folder_item);
            txtFolderName = (AppCompatTextView) view.findViewById(R.id.txt_folder_name);
            txtMimeType = (AppCompatTextView) view.findViewById(R.id.txt_mime_type);

            this.ivUncheck.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
            this.ivCheck.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
            this.iv_fav_image.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_fav_fill));
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            StorageAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            StorageAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class GridHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //@BindView(R.id.card_view)
        CardView cardView;
        //@BindView(R.id.iv_check)
        AppCompatImageView ivCheck;
        //@BindView(R.id.iv_folder)
        AppCompatImageView ivFolder;
        //@BindView(R.id.iv_uncheck)
        ImageView ivUncheck;
        //@BindView(R.id.iv_fav_image)
        ImageView iv_fav_image;
        //@BindView(R.id.txt_folder_name)
        AppCompatTextView txtFolderName;
        //@BindView(R.id.txt_mime_type)
        AppCompatTextView txtMimeType;
        //@BindView(R.id.card_view_image)
        CardView cardViewImage;
        //@BindView(R.id.iv_image)
        AppCompatImageView ivImage;
        //@BindView(R.id.iv_fav_image_file)
        ImageView iv_fav_image_file;
        //@BindView(R.id.iv_folder_image)
        AppCompatImageView iv_folder_image;
        //@BindView(R.id.ll_details)
        LinearLayout llDetails;
        //@BindView(R.id.ll_check_grid)
        RelativeLayout ll_check_grid;
        //@BindView(R.id.txt_date_grid)
        AppCompatTextView txtDate;

        public GridHolder(View view) {
            super(view);
            //ButterKnife.bind((Object) this, view);


            cardView = (CardView) view.findViewById(R.id.card_view);
            ivCheck = (AppCompatImageView) view.findViewById(R.id.iv_check_grid);
            ivFolder = (AppCompatImageView) view.findViewById(R.id.iv_folder);
            ivUncheck = (ImageView) view.findViewById(R.id.iv_uncheck);
            iv_fav_image = (ImageView) view.findViewById(R.id.iv_fav_image);
            txtFolderName = (AppCompatTextView) view.findViewById(R.id.txt_folder_name);
            txtMimeType = (AppCompatTextView) view.findViewById(R.id.txt_mime_type);
            cardViewImage = (CardView) view.findViewById(R.id.card_view_image);
            ivImage = (AppCompatImageView) view.findViewById(R.id.iv_image);
            iv_fav_image_file = (ImageView) view.findViewById(R.id.iv_fav_image_file);
            iv_folder_image = (AppCompatImageView) view.findViewById(R.id.iv_folder_image);

            llDetails = (LinearLayout) view.findViewById(R.id.ll_details);
            ll_check_grid = (RelativeLayout) view.findViewById(R.id.ll_check_grid);
            txtDate = (AppCompatTextView) view.findViewById(R.id.txt_date_grid);

            this.ivUncheck.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
            this.ivCheck.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
            this.iv_fav_image.setImageDrawable(StorageAdapter.this.context.getResources().getDrawable(R.drawable.ic_fav_fill));
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            StorageAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            StorageAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(View view) {
            super(view);
        }
    }
}
