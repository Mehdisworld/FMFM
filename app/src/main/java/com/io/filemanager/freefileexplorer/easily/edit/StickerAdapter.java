package com.io.filemanager.freefileexplorer.easily.edit;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.io.filemanager.freefileexplorer.easily.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;

public class StickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    public StickerActivity context;
    private ImageClick imageClick = new ImageClick();
    private List<String> pathList = new ArrayList();

    public int getItemViewType(int i) {
        return 1;
    }

    public StickerAdapter(StickerActivity stickerActivity) {
        this.context = stickerActivity;
        this.pathList = new ArrayList();
    }

    public int getItemCount() {
        return this.pathList.size();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StickerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_sticker_item, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        StickerViewHolder stickerViewHolder = (StickerViewHolder) viewHolder;
        String str = this.pathList.get(i);
        try {
            Log.d("TAG", "onBindViewHolder: " + str);
            stickerViewHolder.img.setImageDrawable(Drawable.createFromStream(this.context.getAssets().open(str), (String) null));
            stickerViewHolder.img.setTag(str);
            stickerViewHolder.img.setOnClickListener(this.imageClick);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean listAssetFiles(String str) {
        try {
            String[] list = this.context.getAssets().list(str);
            if (list.length <= 0) {
                return true;
            }
            for (String str2 : list) {
                if (!listAssetFiles(str + InternalZipConstants.ZIP_FILE_SEPARATOR + str2)) {
                    return false;
                }
                if (str2.contains(".")) {
                    this.pathList.add(str + InternalZipConstants.ZIP_FILE_SEPARATOR + str2);
                    System.out.println("This is a file = " + str + InternalZipConstants.ZIP_FILE_SEPARATOR + str2);
                } else {
                    System.out.println("This is a folder = " + str + InternalZipConstants.ZIP_FILE_SEPARATOR + str2);
                }
            }
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    public void addStickerImages(String str, int i) {
        this.pathList.clear();
        listAssetFiles(str);
        notifyDataSetChanged();
    }

    private final class ImageClick implements View.OnClickListener {
        private ImageClick() {
        }

        public void onClick(View view) {
            StickerAdapter.this.context.selectedStickerItem((String) view.getTag());
        }
    }

    public class StickerViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public StickerViewHolder(View view) {
            super(view);
            this.img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
