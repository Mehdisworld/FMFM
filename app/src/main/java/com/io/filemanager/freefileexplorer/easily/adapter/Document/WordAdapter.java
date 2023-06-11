package com.io.filemanager.freefileexplorer.easily.adapter.Document;

import android.content.Context;
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
import com.io.filemanager.freefileexplorer.easily.model.DocumentModel;
//import com.onesignal.OSInAppMessageContentKt;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_AD = 2;
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_ITEM_GRID = 1;
    
    public static ClickListener listener;
    
    public static LongClickListener longClickListener;
    Context context;
    ArrayList<DocumentModel> documentList = new ArrayList<>();
    boolean isGrid = false;

    public interface ClickListener {
        void onItemClick(int i, View view);
    }

    public interface LongClickListener {
        void onItemLongClick(int i, View view);
    }

    public WordAdapter(Context context2, ArrayList<DocumentModel> arrayList) {
        this.context = context2;
        this.documentList = arrayList;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        listener = clickListener;
    }

    public void setOnLongClickListener(LongClickListener longClickListener2) {
        longClickListener = longClickListener2;
    }

    public int getItemViewType(int i) {
        Log.e("ItemViewType", "isGrid: " + this.isGrid);
        if (this.documentList.get(i) == null) {
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
        try {
            int itemViewType = getItemViewType(i);
            if (itemViewType == 0) {
                try {
                    ViewHolder viewHolder2 = (ViewHolder) viewHolder;
                    DocumentModel documentModel = this.documentList.get(i);
                    File file = new File(documentModel.getPath());
                    documentModel.setName(file.getName());
                    viewHolder2.txtFolderName.setText(documentModel.getName());
                    String filenameExtension = com.io.filemanager.freefileexplorer.easily.utils.Utils.getFilenameExtension(file.getName());
                    if (this.documentList.get(i).isCheckboxVisible()) {
                        viewHolder2.ll_check.setVisibility(0);
                        if (this.documentList.get(i).isSelected()) {
                            viewHolder2.ivCheck.setVisibility(0);
                        } else {
                            viewHolder2.ivCheck.setVisibility(8);
                        }
                    } else {
                        viewHolder2.ll_check.setVisibility(8);
                    }
                    viewHolder2.ivFolder.setVisibility(0);
                    viewHolder2.cardView.setVisibility(8);
                    viewHolder2.iv_image.setVisibility(8);
                    if (documentModel.getAppType() == null) {
                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                    } else if (filenameExtension.equalsIgnoreCase("pdf")) {
                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_pdf_file));
                    } else {
                        if (!filenameExtension.equalsIgnoreCase("doc") && !filenameExtension.equalsIgnoreCase("docx")) {
                            if (!filenameExtension.equalsIgnoreCase("xlsx") && !filenameExtension.equalsIgnoreCase("xls") && !filenameExtension.equalsIgnoreCase("xlc") && !filenameExtension.equalsIgnoreCase("xld")) {
                                if (!filenameExtension.equalsIgnoreCase("ppt") && !filenameExtension.equalsIgnoreCase("pptx") && !filenameExtension.equalsIgnoreCase("ppsx") && !filenameExtension.equalsIgnoreCase("pptm")) {
                                    if (!filenameExtension.equalsIgnoreCase("txt") && !filenameExtension.equalsIgnoreCase("tex") && !filenameExtension.equalsIgnoreCase("text") && !filenameExtension.equalsIgnoreCase("pptm")) {
                                        if (filenameExtension.equalsIgnoreCase("xml")) {
                                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xml_file));
                                        } else {
                                            if (!filenameExtension.equalsIgnoreCase("html") && !filenameExtension.equalsIgnoreCase("htm")) {
                                                if (filenameExtension.equalsIgnoreCase("java")) {
                                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                } else if (filenameExtension.equalsIgnoreCase("php")) {
                                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                } else {
                                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_all_type_document));
                                                }
                                            }
                                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_html_file));
                                        }
                                    }
                                    viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_text_file));
                                }
                                viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_ppt_file));
                            }
                            viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_xls_file));
                        }
                        viewHolder2.ivFolder.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_doc_file));
                    }
                    viewHolder2.txtFolderItem.setText(Formatter.formatShortFileSize(this.context, file.length()));
                    viewHolder2.txtDateTime.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm").format(Long.valueOf(file.lastModified())));
                    if (!this.documentList.get(i).isFavorite()) {
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
            } else if (itemViewType == 2) {
                BannerViewHolder bannerViewHolder = (BannerViewHolder) viewHolder;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
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
        return this.documentList.size();
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
            this.ivUncheck.setImageDrawable(WordAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_unseleted));
            this.ivCheck.setImageDrawable(WordAdapter.this.context.getResources().getDrawable(R.drawable.ic_radio_btn_selected));
            this.iv_fav_image.setImageDrawable(WordAdapter.this.context.getResources().getDrawable(R.drawable.ic_fav_fill));
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            this.ll_check.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                }
            });
        }

        public void onClick(View view) {
            WordAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            WordAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class GridHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public GridHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }

        public void onClick(View view) {
            WordAdapter.listener.onItemClick(getAdapterPosition(), view);
        }

        public boolean onLongClick(View view) {
            WordAdapter.longClickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(View view) {
            super(view);
        }
    }
}
