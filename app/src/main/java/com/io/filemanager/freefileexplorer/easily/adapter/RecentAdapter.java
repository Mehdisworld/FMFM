package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;*/
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.model.InternalStorageFilesModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
//import com.onesignal.OSInAppMessageContentKt;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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


    public RecentAdapter(Context context2, ArrayList<InternalStorageFilesModel> arrayList) {
        this.context = context2;
        this.internalStorageList = arrayList;
        this.isGrid = true;
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
            return new GridHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recent_grid, viewGroup, false));
        }
        throw new RuntimeException("there is no type that matches the type " + i + " + make sure your using types correctly");
    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        String str;
        int i2 = i;
        try {
            int itemViewType = getItemViewType(i2);
            String str2 = "pdf";
            String str3 = "application/vnd.android.package-archive";
            String str4 = "pptm";
            if (itemViewType == 0) {
                try {
                    ViewHolder viewHolder2 = (ViewHolder) viewHolder;
                    InternalStorageFilesModel internalStorageFilesModel = this.internalStorageList.get(i2);
                    String str5 = "application/rar";
                    String str6 = "application/zip";
                    viewHolder2.txtFolderName.setText(internalStorageFilesModel.getFileName());
                    File file = new File(internalStorageFilesModel.getFilePath());
                    String filenameExtension = com.io.filemanager.freefileexplorer.easily.utils.Utils.getFilenameExtension(file.getName());
                    String str7 = "video/3gpp";
                    viewHolder2.iv_image.setVisibility(8);
                    if (!this.internalStorageList.get(i2).isCheckboxVisible()) {
                        viewHolder2.ivCheck.setVisibility(8);
                    }
                    if (file.isDirectory()) {
                        int filesList = getFilesList(file.getPath());
                        AppCompatTextView appCompatTextView = viewHolder2.txtFolderItem;
                        appCompatTextView.setText(filesList + " item");
                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_folder));
                        viewHolder2.ivFolder.setVisibility(0);
                        viewHolder2.cardView.setVisibility(8);
                    } else {
                        if (!this.internalStorageList.get(i2).isCheckboxVisible()) {
                            viewHolder2.ivCheck.setVisibility(8);
                        }
                        String mineType = internalStorageFilesModel.getMineType();
                        if (mineType != null) {
                            StringBuilder sb = new StringBuilder();
                            InternalStorageFilesModel internalStorageFilesModel2 = internalStorageFilesModel;
                            sb.append("type: ");
                            sb.append(mineType);
                            sb.append("  name: ");
                            sb.append(file.getName());
                            Log.e("storage", sb.toString());
                            if (!mineType.equalsIgnoreCase("image/jpeg") && !mineType.equalsIgnoreCase("image/png") && !mineType.equalsIgnoreCase("image/webp")) {
                                if (!mineType.equalsIgnoreCase("video/mp4") && !mineType.equalsIgnoreCase("video/x-matroska")) {
                                    if (!mineType.equalsIgnoreCase("audio/mpeg") && !mineType.equalsIgnoreCase("audio/aac") && !mineType.equalsIgnoreCase("audio/ogg") && !mineType.equalsIgnoreCase(str7)) {
                                        if (mineType.equalsIgnoreCase(str6)) {
                                            viewHolder2.ivFolder.setVisibility(0);
                                            viewHolder2.cardView.setVisibility(8);
                                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_zip_file));
                                        } else if (mineType.equalsIgnoreCase(str5)) {
                                            viewHolder2.ivFolder.setVisibility(0);
                                            viewHolder2.cardView.setVisibility(8);
                                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_rar_file));
                                        } else if (mineType.equalsIgnoreCase(str3)) {
                                            viewHolder2.iv_image.setVisibility(0);
                                            viewHolder2.ivFolder.setVisibility(8);
                                            viewHolder2.cardView.setVisibility(8);
                                            PackageInfo packageArchiveInfo = this.context.getPackageManager().getPackageArchiveInfo(internalStorageFilesModel2.getFilePath(), 0);
                                            if (packageArchiveInfo != null) {
                                                Drawable applicationIcon = this.context.getPackageManager().getApplicationIcon(packageArchiveInfo.packageName);
                                                if (applicationIcon != null) {
                                                    viewHolder2.iv_image.setImageDrawable(applicationIcon);
                                                } else {
                                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_apk_file));
                                                    viewHolder2.ivFolder.setVisibility(0);
                                                    viewHolder2.cardView.setVisibility(8);
                                                }
                                            } else {
                                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_apk_file));
                                                viewHolder2.ivFolder.setVisibility(0);
                                                viewHolder2.cardView.setVisibility(8);
                                            }
                                        } else {
                                            str = filenameExtension;
                                            if (str.equalsIgnoreCase(str2)) {
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
                                                                    String str8 = str4;
                                                                    if (!str.equalsIgnoreCase(str8)) {
                                                                        if (!str.equalsIgnoreCase("txt") && !str.equalsIgnoreCase("tex") && !str.equalsIgnoreCase("text")) {
                                                                            if (!str.equalsIgnoreCase(str8)) {
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
                    viewHolder2.txtDateTime.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm").format(Long.valueOf(file.lastModified())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String str9 = "video/3gpp";
                String str10 = str4;
                String str11 = "application/rar";
                if (itemViewType == 1) {
                    try {
                        GridHolder gridHolder = (GridHolder) viewHolder;
                        InternalStorageFilesModel internalStorageFilesModel3 = this.internalStorageList.get(i2);
                        String str12 = str11;
                        String str13 = "application/zip";
                        String str14 = str9;
                        int dimensionPixelSize = (Resources.getSystem().getDisplayMetrics().widthPixels - this.context.getResources().getDimensionPixelSize(R.dimen._4sdp)) / 4;
                        gridHolder.lout_main.getLayoutParams().height = dimensionPixelSize;
                        gridHolder.lout_main.getLayoutParams().width = dimensionPixelSize;
                        gridHolder.lout_main.requestLayout();
                        gridHolder.txtFolderName.setText(internalStorageFilesModel3.getFileName());
                        File file2 = new File(internalStorageFilesModel3.getFilePath());
                        String str15 = "audio/ogg";
                        gridHolder.txtDate.setText(new SimpleDateFormat("dd MMM yyyy").format(Long.valueOf(file2.lastModified())));
                        gridHolder.iv_folder_image.setVisibility(8);
                        String filenameExtension2 = com.io.filemanager.freefileexplorer.easily.utils.Utils.getFilenameExtension(file2.getName());
                        if (this.internalStorageList.get(i2).isCheckboxVisible()) {
                            gridHolder.ivCheck.setVisibility(0);
                        } else {
                            gridHolder.ivCheck.setVisibility(8);
                        }
                        if (file2.isDirectory()) {
                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_folder));
                            gridHolder.ivFolder.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                            return;
                        }
                        if (!this.internalStorageList.get(i2).isCheckboxVisible()) {
                            gridHolder.ivCheck.setVisibility(8);
                        }
                        String mineType2 = internalStorageFilesModel3.getMineType();
                        if (mineType2 == null) {
                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                            gridHolder.ivFolder.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                        } else if (mineType2.equalsIgnoreCase("image/jpeg") || mineType2.equalsIgnoreCase("image/png") || mineType2.equalsIgnoreCase("image/webp")) {
                            ((RequestBuilder) Glide.with(this.context).load(Uri.fromFile(file2)).placeholder((int) R.drawable.ic_image_placeholder)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((ImageView) gridHolder.iv_folder_image);
                            gridHolder.ivFolder.setVisibility(8);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.iv_folder_image.setVisibility(0);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                        } else if (mineType2.equalsIgnoreCase("video/mp4") || mineType2.equalsIgnoreCase("video/x-matroska")) {
                            ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(file2.getPath()).placeholder((int) R.drawable.ic_video_placeholder)).error((int) R.drawable.ic_video_placeholder)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into((ImageView) gridHolder.iv_folder_image);
                            gridHolder.ivFolder.setVisibility(8);
                            gridHolder.iv_folder_image.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                        } else if (mineType2.equalsIgnoreCase("audio/mpeg") || mineType2.equalsIgnoreCase("audio/aac") || mineType2.equalsIgnoreCase(str15) || mineType2.equalsIgnoreCase(str14)) {
                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_audio_file));
                            gridHolder.ivFolder.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                        } else if (mineType2.equalsIgnoreCase(str13)) {
                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_zip_file));
                            gridHolder.ivFolder.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                        } else if (mineType2.equalsIgnoreCase(str12)) {
                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_rar_file));
                            gridHolder.ivFolder.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                        } else if (mineType2.equalsIgnoreCase(str3)) {
                            gridHolder.ivFolder.setVisibility(8);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.iv_folder_image.setVisibility(0);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
                            PackageManager packageManager = this.context.getPackageManager();
                            PackageInfo packageArchiveInfo2 = packageManager.getPackageArchiveInfo(internalStorageFilesModel3.getFilePath(), 0);
                            gridHolder.iv_folder_image.setImageDrawable(this.context.getPackageManager().getApplicationIcon(packageArchiveInfo2.packageName));
                        } else if (filenameExtension2.equalsIgnoreCase(str2)) {
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
                                                String str16 = str10;
                                                if (!filenameExtension2.equalsIgnoreCase(str16)) {
                                                    if (!filenameExtension2.equalsIgnoreCase("txt") && !filenameExtension2.equalsIgnoreCase("tex") && !filenameExtension2.equalsIgnoreCase("text")) {
                                                        if (!filenameExtension2.equalsIgnoreCase(str16)) {
                                                            if (filenameExtension2.equalsIgnoreCase("xml")) {
                                                                gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xml_file));
                                                                gridHolder.ivFolder.setVisibility(0);
                                                                gridHolder.ivImage.setVisibility(8);
                                                                gridHolder.cardViewImage.setVisibility(8);
                                                                gridHolder.cardView.setVisibility(8);
                                                                return;
                                                            }
                                                            if (!filenameExtension2.equalsIgnoreCase("html")) {
                                                                if (!filenameExtension2.equalsIgnoreCase("htm")) {
                                                                    if (filenameExtension2.equalsIgnoreCase("java")) {
                                                                        gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                        gridHolder.ivFolder.setVisibility(0);
                                                                        gridHolder.ivImage.setVisibility(8);
                                                                        gridHolder.cardViewImage.setVisibility(8);
                                                                        gridHolder.cardView.setVisibility(8);
                                                                        return;
                                                                    } else if (filenameExtension2.equalsIgnoreCase("php")) {
                                                                        gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                        gridHolder.ivFolder.setVisibility(0);
                                                                        gridHolder.ivImage.setVisibility(8);
                                                                        gridHolder.cardViewImage.setVisibility(8);
                                                                        gridHolder.cardView.setVisibility(8);
                                                                        return;
                                                                    } else {
                                                                        gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                                        gridHolder.ivFolder.setVisibility(0);
                                                                        gridHolder.ivImage.setVisibility(8);
                                                                        gridHolder.cardViewImage.setVisibility(8);
                                                                        gridHolder.cardView.setVisibility(8);
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_html_file));
                                                            gridHolder.ivFolder.setVisibility(0);
                                                            gridHolder.ivImage.setVisibility(8);
                                                            gridHolder.cardViewImage.setVisibility(8);
                                                            gridHolder.cardView.setVisibility(8);
                                                            return;
                                                        }
                                                    }
                                                    gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_text_file));
                                                    gridHolder.ivFolder.setVisibility(0);
                                                    gridHolder.ivImage.setVisibility(8);
                                                    gridHolder.cardViewImage.setVisibility(8);
                                                    gridHolder.cardView.setVisibility(8);
                                                    return;
                                                }
                                            }
                                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_ppt_file));
                                            gridHolder.ivFolder.setVisibility(0);
                                            gridHolder.ivImage.setVisibility(8);
                                            gridHolder.cardViewImage.setVisibility(8);
                                            gridHolder.cardView.setVisibility(8);
                                            return;
                                        }
                                    }
                                    gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xls_file));
                                    gridHolder.ivFolder.setVisibility(0);
                                    gridHolder.ivImage.setVisibility(8);
                                    gridHolder.cardViewImage.setVisibility(8);
                                    gridHolder.cardView.setVisibility(8);
                                    return;
                                }
                            }
                            gridHolder.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_doc_file));
                            gridHolder.ivFolder.setVisibility(0);
                            gridHolder.ivImage.setVisibility(8);
                            gridHolder.cardViewImage.setVisibility(8);
                            gridHolder.cardView.setVisibility(8);
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
        //@BindView(R.id.iv_image)
        AppCompatImageView iv_image;
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
            //ButterKnife.bind(this, view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            ivCheck = (ImageView) view.findViewById(R.id.iv_check);
            ivFolder = (AppCompatImageView) view.findViewById(R.id.iv_folder);
            iv_image = (AppCompatImageView) view.findViewById(R.id.iv_image);
            txtDateTime = (AppCompatTextView) view.findViewById(R.id.txt_date_time);
            txtFolderItem = (AppCompatTextView) view.findViewById(R.id.txt_folder_item);
            txtFolderName = (AppCompatTextView) view.findViewById(R.id.txt_folder_name);
            txtMimeType = (AppCompatTextView) view.findViewById(R.id.txt_mime_type);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            RecentAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            RecentAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class GridHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //@BindView(R.id.card_view)
        CardView cardView;
        //@BindView(R.id.iv_check)
        ImageView ivCheck;
        //@BindView(R.id.iv_folder)
        AppCompatImageView ivFolder;
        //@BindView(R.id.txt_folder_name)
        AppCompatTextView txtFolderName;
        //@BindView(R.id.txt_mime_type)
        AppCompatTextView txtMimeType;
        //@BindView(R.id.card_view_image)
        CardView cardViewImage;
        //@BindView( R.id.iv_image)
        AppCompatImageView ivImage;
        //@BindView(R.id.iv_folder_image)
        AppCompatImageView iv_folder_image;
        //@BindView(R.id.ll_details)
        LinearLayout llDetails;
        //@BindView(R.id.lout_main)
        RelativeLayout lout_main;
        //@BindView(R.id.txt_date_grid)
        AppCompatTextView txtDate;

        public GridHolder(View view) {
            super(view);
            //ButterKnife.bind(this, view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            ivCheck = (ImageView) view.findViewById(R.id.iv_check);
            ivFolder = (AppCompatImageView) view.findViewById(R.id.iv_folder);
            txtFolderName = (AppCompatTextView) view.findViewById(R.id.txt_folder_name);
            txtMimeType = (AppCompatTextView) view.findViewById(R.id.txt_mime_type);
            cardViewImage = (CardView) view.findViewById(R.id.card_view);
            ivImage = (AppCompatImageView) view.findViewById(R.id.iv_image);
            iv_folder_image = (AppCompatImageView) view.findViewById(R.id.iv_folder_image);
            llDetails = (LinearLayout) view.findViewById(R.id.ll_details);
            lout_main = (RelativeLayout) view.findViewById(R.id.lout_main);
            txtDate = (AppCompatTextView) view.findViewById(R.id.txt_date_grid);


            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            RecentAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            RecentAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(View view) {
            super(view);
        }
    }

}
