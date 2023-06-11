package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.text.format.Formatter;
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
import com.io.filemanager.freefileexplorer.easily.model.InternalStorageFilesModel;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ZipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_AD = 1;
    public static final int TYPE_ITEM = 0;
    
    public static ClickListener listener;
    
    public static LongClickListener longClickListener;
    Context context;
    ArrayList<InternalStorageFilesModel> zipList = new ArrayList<>();

    public interface ClickListener {
        void onItemClick(int i, View view);
    }

    public interface LongClickListener {
        void onItemLongClick(int i, View view);
    }

    public ZipAdapter(Context context2, ArrayList<InternalStorageFilesModel> arrayList) {
        this.context = context2;
        this.zipList = arrayList;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        listener = clickListener;
    }

    public void setOnLongClickListener(LongClickListener longClickListener2) {
        longClickListener = longClickListener2;
    }

    public int getItemViewType(int i) {
        return this.zipList.get(i) == null ? 1 : 0;
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
                    ViewHolder viewHolder2 = (ViewHolder) viewHolder;
                    File file = new File(this.zipList.get(i).getFilePath());
                    if (this.zipList.get(i).isCheckboxVisible()) {
                        viewHolder2.ll_check.setVisibility(0);
                        if (this.zipList.get(i).isSelected()) {
                            viewHolder2.ivCheck.setVisibility(0);
                        } else {
                            viewHolder2.ivCheck.setVisibility(8);
                        }
                    } else {
                        viewHolder2.ll_check.setVisibility(8);
                    }
                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_zip_file));
                    viewHolder2.ivFolder.setVisibility(0);
                    viewHolder2.cardView.setVisibility(8);
                    viewHolder2.txtFolderName.setText(this.zipList.get(i).getFileName());
                    viewHolder2.txtDateTime.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm").format(Long.valueOf(file.lastModified())));
                    viewHolder2.txtFolderItem.setText(Formatter.formatShortFileSize(this.context, file.length()));
                    if (!this.zipList.get(i).isFavorite()) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
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
        return this.zipList.size();
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
            this.ivUncheck.setImageDrawable(ZipAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
            this.ivCheck.setImageDrawable(ZipAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
            this.iv_fav_image.setImageDrawable(ZipAdapter.this.context.getResources().getDrawable(R.drawable.ic_fav_fill));
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            ZipAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            ZipAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(View view) {
            super(view);
        }
    }
}
