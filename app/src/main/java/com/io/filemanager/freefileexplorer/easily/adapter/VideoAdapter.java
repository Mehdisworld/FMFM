package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.model.PhotoHeader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_HEADER_TYPE = 1;
    private static final int ITEM_PHOTOS_TYPE = 2;
    public static final int TYPE_AD = 3;
    
    public static ClickListener listener;
    
    public static LongClickListener longClickListener;
    Context context;
    List<Object> photoList = new ArrayList();

    public interface ClickListener {
        void onItemClick(int i, View view);
    }

    public interface LongClickListener {
        void onItemLongClick(int i, View view);
    }


    public VideoAdapter(Context context2, List<Object> list) {
        this.context = context2;
        this.photoList = list;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        listener = clickListener;
    }

    public void setOnLongClickListener(LongClickListener longClickListener2) {
        longClickListener = longClickListener2;
    }

    public int getItemViewType(int i) {
        if (this.photoList.get(i) == null) {
            return 3;
        }
        return this.photoList.get(i) instanceof PhotoData ? 2 : 1;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater.from(viewGroup.getContext());
        if (i == 1) {
            return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_header, viewGroup, false));
        }
        if (i == 2) {
            return new ImageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_grid, viewGroup, false));
        }
        RecyclerView.ViewHolder viewHolder = null;
        viewHolder.getClass();
        return viewHolder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 1) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            headerViewHolder.txtPhotoCounter.setVisibility(0);
            headerViewHolder.iv_select_all.setVisibility(8);
            PhotoHeader photoHeader = (PhotoHeader) this.photoList.get(i);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            String format = simpleDateFormat.format(Calendar.getInstance().getTime());
            Calendar instance = Calendar.getInstance();
            instance.add(5, -1);
            instance.getTime();
            String format2 = simpleDateFormat.format(instance.getTime());
            instance.add(5, -1);
            String format3 = simpleDateFormat.format(instance.getTime());
            if (format.equalsIgnoreCase(photoHeader.getTitle())) {
                headerViewHolder.txtDate.setText("Today");
            } else if (format2.equalsIgnoreCase(photoHeader.getTitle())) {
                headerViewHolder.txtDate.setText("Yesterday");
            } else if (format3.equalsIgnoreCase(photoHeader.getTitle())) {
                headerViewHolder.txtDate.setText("3 days ago");
            } else {
                headerViewHolder.txtDate.setText(photoHeader.getTitle());
            }
        } else if (itemViewType == 2) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
            PhotoData photoData = (PhotoData) this.photoList.get(i);
            if (photoData.getFilePath() != null && !photoData.getFilePath().equalsIgnoreCase("")) {
                ((RequestBuilder) Glide.with(this.context).load(photoData.getFilePath()).placeholder((int) R.drawable.ic_video_placeholder)).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(imageViewHolder.ivImg);
                imageViewHolder.txt_folder_name_grid.setText(photoData.getDuration());
                if (photoData.isFavorite()) {
                    imageViewHolder.iv_fav_image.setVisibility(0);
                } else {
                    imageViewHolder.iv_fav_image.setVisibility(8);
                }
                if (photoData.isCheckboxVisible()) {
                    imageViewHolder.iv_un_select.setVisibility(0);
                    if (photoData.isSelected()) {
                        imageViewHolder.iv_select.setVisibility(0);
                    } else {
                        imageViewHolder.iv_select.setVisibility(8);
                    }
                } else {
                    imageViewHolder.iv_select.setVisibility(8);
                    imageViewHolder.iv_un_select.setVisibility(8);
                }
            }
        }
    }

    public int getItemCount() {
        return this.photoList.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_select_all)
        AppCompatImageView iv_select_all;
        @BindView(R.id.ll_select)
        LinearLayout ll_select;
        @BindView(R.id.txt_date)
        AppCompatTextView txtDate;
        @BindView(R.id.txt_photo_counter)
        AppCompatTextView txtPhotoCounter;

        public HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.ivimg)
        ImageView ivImg;
        @BindView(R.id.iv_video)
        ImageView ivVideo;
        @BindView(R.id.iv_fav_image)
        ImageView iv_fav_image;
        @BindView(R.id.iv_select)
        AppCompatImageView iv_select;
        @BindView(R.id.iv_un_select)
        AppCompatImageView iv_un_select;
        @BindView(R.id.ll_main)
        RelativeLayout ll_main;
        @BindView(R.id.ll_select)
        RelativeLayout ll_select;
        @BindView(R.id.lout_video)
        RelativeLayout lout_video;
        @BindView(R.id.txt_duration)
        TextView txt_duration;
        @BindView(R.id.txt_folder_name_grid)
        AppCompatTextView txt_folder_name_grid;

        public ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
            this.iv_un_select.setImageDrawable(VideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_unseleted_2));
            this.iv_select.setImageDrawable(VideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
            this.iv_fav_image.setImageDrawable(VideoAdapter.this.context.getResources().getDrawable(R.drawable.ic_fav_fill));
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            VideoAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            VideoAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }
}
