package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.io.filemanager.freefileexplorer.easily.R;
import java.io.File;
import java.util.ArrayList;

public class ZipHeaderListAdapter extends RecyclerView.Adapter<ZipHeaderListAdapter.ViewHolder> {
    
    public static ClickListener listener;
    Context context;
    ArrayList<String> pathList = new ArrayList<>();

    public interface ClickListener {
        void onItemClick(int i, View view);

        void onItemHeaderClick(int i, View view);
    }

    public ZipHeaderListAdapter(Context context2, ArrayList<String> arrayList) {
        this.context = context2;
        this.pathList = arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (i != 0) {
            viewHolder.txtStoragePath.setText(new File(this.pathList.get(i)).getName());
            viewHolder.txt_app_name.setVisibility(8);
        } else if (this.pathList.get(i).equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            viewHolder.txtStoragePath.setText("Internal storage");
            viewHolder.txt_app_name.setVisibility(0);
            viewHolder.txt_app_name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ZipHeaderListAdapter.listener.onItemHeaderClick(i, view);
                }
            });
        } else {
            if (this.pathList.get(i).equalsIgnoreCase(Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS)) {
                viewHolder.txtStoragePath.setText("Download");
                viewHolder.txt_app_name.setVisibility(0);
                return;
            }
            viewHolder.txtStoragePath.setText("Sd card");
            viewHolder.txt_app_name.setVisibility(0);
            viewHolder.txt_app_name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ZipHeaderListAdapter.listener.onItemHeaderClick(i, view);
                }
            });
        }
    }

    public int getItemCount() {
        return this.pathList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        listener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_right)
        AppCompatImageView iv_right;
        @BindView(R.id.txt_storage_path)
        AppCompatTextView txtStoragePath;
        @BindView(R.id.txt_app_name)
        AppCompatTextView txt_app_name;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            ZipHeaderListAdapter.listener.onItemClick(getAdapterPosition(), view);
        }
    }
}
