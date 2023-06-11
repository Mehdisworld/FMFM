package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.model.ApkModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ApkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_AD = 1;
    public static final int TYPE_ITEM = 0;
    
    public static ClickListener listener;
    
    public static LongClickListener longClickListener;
    ArrayList<ApkModel> apkList = new ArrayList<>();
    Context context;

    public interface ClickListener {
        void onItemClick(int i, View view);
    }

    public interface LongClickListener {
        void onItemLongClick(int i, View view);
    }

    public ApkAdapter(Context context2, ArrayList<ApkModel> arrayList) {
        this.context = context2;
        this.apkList = arrayList;
        Log.i("TAG", "getItemCount: apkList:" + this.apkList.size());
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        listener = clickListener;
    }

    public void setOnLongClickListener(LongClickListener longClickListener2) {
        longClickListener = longClickListener2;
    }

    public int getItemViewType(int i) {
        return this.apkList.get(i) == null ? 1 : 0;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_storage_list, viewGroup, false));
        }
        throw new RuntimeException("there is no type that matches the type " + i + " + make sure your using types correctly");
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        try {
            if (getItemViewType(i) == 0) {
                try {
                    final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
                    File file = new File(this.apkList.get(i).getPath());
                    Uri.fromFile(file);
                    viewHolder2.txtFolderName.setText(this.apkList.get(i).getApkName());
                    viewHolder2.txtDateTime.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm").format(Long.valueOf(file.lastModified())));
                    viewHolder2.txtFolderItem.setText(Formatter.formatShortFileSize(this.context, file.length()));
                    if (this.apkList.get(i).isCheckboxVisible()) {
                        viewHolder2.ll_check.setVisibility(0);
                        if (this.apkList.get(i).isSelected()) {
                            viewHolder2.ivCheck.setVisibility(0);
                        } else {
                            viewHolder2.ivCheck.setVisibility(8);
                        }
                    } else {
                        viewHolder2.ll_check.setVisibility(8);
                    }
                    Drawable appIcon = this.apkList.get(i).getAppIcon();
                    Bitmap appIconBitmap = this.apkList.get(i).getAppIconBitmap();
                    if (appIconBitmap == null) {
                        if (appIcon != null) {
                            if (appIcon.getIntrinsicWidth() > 0 && appIcon.getIntrinsicHeight() > 0) {
                                Bitmap createBitmap = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                                this.apkList.get(i).setAppIconBitmap(createBitmap);
                                Canvas canvas = new Canvas(createBitmap);
                                appIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                                appIcon.draw(canvas);
                            }
                            appIconBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                            this.apkList.get(i).setAppIconBitmap(appIconBitmap);
                            Canvas canvas2 = new Canvas(appIconBitmap);
                            appIcon.setBounds(0, 0, canvas2.getWidth(), canvas2.getHeight());
                            appIcon.draw(canvas2);
                        } else {
                            PackageManager packageManager = this.context.getPackageManager();
                            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(this.apkList.get(i).getPath(), 0);
                            if (packageArchiveInfo != null) {
                                try {
                                    this.apkList.get(i).setPackageName(packageArchiveInfo.packageName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    this.apkList.get(i).setAppIcon(this.context.getPackageManager().getApplicationIcon(packageArchiveInfo.packageName));
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                    try {
                                        this.apkList.get(i).setAppIcon(packageArchiveInfo.applicationInfo.loadIcon(packageManager));
                                    } catch (Exception e3) {
                                        e3.printStackTrace();
                                    }
                                }
                            }
                            Drawable appIcon2 = this.apkList.get(i).getAppIcon();
                            if (appIcon2 != null) {
                                if (appIcon2.getIntrinsicWidth() > 0) {
                                    if (appIcon2.getIntrinsicHeight() > 0) {
                                        appIconBitmap = Bitmap.createBitmap(appIcon2.getIntrinsicWidth(), appIcon2.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                                        this.apkList.get(i).setAppIconBitmap(appIconBitmap);
                                        Canvas canvas3 = new Canvas(appIconBitmap);
                                        appIcon2.setBounds(0, 0, canvas3.getWidth(), canvas3.getHeight());
                                        appIcon2.draw(canvas3);
                                    }
                                }
                                appIconBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                                this.apkList.get(i).setAppIconBitmap(appIconBitmap);
                                Canvas canvas32 = new Canvas(appIconBitmap);
                                appIcon2.setBounds(0, 0, canvas32.getWidth(), canvas32.getHeight());
                                appIcon2.draw(canvas32);
                            }
                        }
                    }
                    if (appIconBitmap != null) {
                        viewHolder2.iv_image.setVisibility(0);
                        viewHolder2.ivFolder.setVisibility(8);
                        viewHolder2.cardView.setVisibility(8);
                        ((RequestBuilder) Glide.with(this.context).load(appIconBitmap).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).addListener(new RequestListener<Drawable>() {
                            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                                return false;
                            }

                            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                                viewHolder2.ivFolder.setImageDrawable(ApkAdapter.this.context.getResources().getDrawable(R.drawable.ic_apk_file));
                                viewHolder2.ivFolder.setVisibility(0);
                                viewHolder2.cardView.setVisibility(8);
                                return false;
                            }
                        }).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).placeholder((int) R.drawable.ic_image_placeholder)).into((ImageView) viewHolder2.iv_image);
                    } else {
                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_apk_file));
                        viewHolder2.ivFolder.setVisibility(0);
                        viewHolder2.cardView.setVisibility(8);
                    }
                    if (!this.apkList.get(i).isFavorite()) {
                        viewHolder2.iv_fav_image.setVisibility(8);
                        viewHolder2.iv_fav_file.setVisibility(8);
                        viewHolder2.iv_fav_other_file.setVisibility(8);
                    } else if (viewHolder2.ivFolder.getVisibility() == 0) {
                        viewHolder2.iv_fav_file.setVisibility(8);
                        viewHolder2.iv_fav_image.setVisibility(8);
                        viewHolder2.iv_fav_other_file.setVisibility(0);
                    } else {
                        viewHolder2.iv_fav_image.setVisibility(0);
                        viewHolder2.iv_fav_file.setVisibility(8);
                        viewHolder2.iv_fav_other_file.setVisibility(8);
                    }
                } catch (Exception e4) {
                    e4.printStackTrace();
                    Log.e("apkAdapter", "msg: " + e4.getMessage());
                }
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
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
        return this.apkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_check)
        AppCompatImageView ivCheck;
        @BindView(R.id.iv_folder)
        AppCompatImageView ivFolder;
        @BindView(R.id.iv_uncheck)
        ImageView ivUncheck;
        @BindView(R.id.iv_fav_file)
        ImageView iv_fav_file;
        @BindView(R.id.iv_fav_image)
        ImageView iv_fav_image;
        @BindView(R.id.iv_fav_other_file)
        ImageView iv_fav_other_file;
        @BindView(R.id.iv_image)
        AppCompatImageView iv_image;
        @BindView(R.id.ll_check)
        RelativeLayout ll_check;
        @BindView(R.id.txt_date_time)
        AppCompatTextView txtDateTime;
        @BindView(R.id.txt_folder_item)
        AppCompatTextView txtFolderItem;
        @BindView(R.id.txt_folder_name)
        AppCompatTextView txtFolderName;
        @BindView(R.id.txt_mime_type)
        AppCompatTextView txtMimeType;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
            this.ivUncheck.setImageDrawable(ApkAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
            this.ivCheck.setImageDrawable(ApkAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
            this.iv_fav_image.setImageDrawable(ApkAdapter.this.context.getResources().getDrawable(R.drawable.ic_fav_fill));
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            ApkAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            ApkAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(View view) {
            super(view);
        }
    }
}
