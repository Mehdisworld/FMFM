package com.io.filemanager.freefileexplorer.easily.edit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.io.filemanager.freefileexplorer.easily.R;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import net.lingala.zip4j.util.InternalZipConstants;

public class StickerTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int TAG_STICKERS_COUNT = 222;
    public static int TAG_STICKERS_PATH = 111;
    private ImageClick imageClick = new ImageClick();
    
    public StickerActivity stickerActivity;
    private int[] stickerCount;
    private ArrayList<String> stickerPath = new ArrayList<>();

    public int getItemViewType(int i) {
        return 1;
    }

    public StickerTypeAdapter(StickerActivity stickerActivity2) {
        this.stickerActivity = stickerActivity2;
        listAssetFiles("sticker");
    }

    private boolean listAssetFiles(String str) {
        try {
            String[] list = this.stickerActivity.getAssets().list(str);
            if (list.length > 0) {
                if (this.stickerCount == null) {
                    this.stickerCount = new int[(list.length + 1)];
                }
                for (int i = 0; i < list.length; i++) {
                    String str2 = list[i];
                    if (!listAssetFiles(str + InternalZipConstants.ZIP_FILE_SEPARATOR + str2)) {
                        return false;
                    }
                    if (!str2.contains(".")) {
                        ArrayList<String> arrayList = this.stickerPath;
                        arrayList.add(str + InternalZipConstants.ZIP_FILE_SEPARATOR + str2);
                        StringBuilder sb = new StringBuilder();
                        sb.append("listAssetFiles: ");
                        sb.append(i);
                        Log.d("TAG", sb.toString());
                        this.stickerCount[i] = list.length;
                        PrintStream printStream = System.out;
                        printStream.println("This is a folder = " + str + InternalZipConstants.ZIP_FILE_SEPARATOR + str2);
                    }
                }
            }
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        LinearLayout lout_item;
        public TextView text;

        ImageHolder(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.text = (TextView) view.findViewById(R.id.text);
            this.lout_item = (LinearLayout) view.findViewById(R.id.lout_item);
        }
    }

    public int getItemCount() {
        return this.stickerPath.size();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ImageHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_sticker_type_item, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ImageHolder imageHolder = (ImageHolder) viewHolder;
        String str = this.stickerPath.get(i);
        Log.d("TAG", "onBindViewHolder: " + str);
        String str2 = String.valueOf(str.split(InternalZipConstants.ZIP_FILE_SEPARATOR)[1].charAt(0)).toUpperCase() + str.split(InternalZipConstants.ZIP_FILE_SEPARATOR)[1].subSequence(1, str.split(InternalZipConstants.ZIP_FILE_SEPARATOR)[1].length());
        if (str2.equalsIgnoreCase("animal")) {
            imageHolder.icon.setImageDrawable(this.stickerActivity.getResources().getDrawable(R.drawable.ic_animal_unselect));
        } else if (str2.equalsIgnoreCase("emoji")) {
            imageHolder.icon.setImageDrawable(this.stickerActivity.getResources().getDrawable(R.drawable.ic_emoji_unselect));
        } else if (str2.equalsIgnoreCase("Face")) {
            imageHolder.icon.setImageDrawable(this.stickerActivity.getResources().getDrawable(R.drawable.ic_face_unselect));
        } else if (str2.equalsIgnoreCase("Gesture")) {
            imageHolder.icon.setImageDrawable(this.stickerActivity.getResources().getDrawable(R.drawable.ic_gesture_unselect));
        } else if (str2.equalsIgnoreCase("Group")) {
            imageHolder.icon.setImageDrawable(this.stickerActivity.getResources().getDrawable(R.drawable.ic_group_unselect));
        } else if (str2.equalsIgnoreCase("Heart")) {
            imageHolder.icon.setImageDrawable(this.stickerActivity.getResources().getDrawable(R.drawable.ic_heart_unselect));
        } else if (str2.equalsIgnoreCase("Symbol")) {
            imageHolder.icon.setImageDrawable(this.stickerActivity.getResources().getDrawable(R.drawable.ic_symbol_unselect));
        }
        imageHolder.text.setText(str2);
        imageHolder.lout_item.setTag(TAG_STICKERS_PATH, this.stickerPath.get(i));
        imageHolder.lout_item.setTag(TAG_STICKERS_COUNT, Integer.valueOf(this.stickerCount[i]));
        imageHolder.lout_item.setOnClickListener(this.imageClick);
    }

    private final class ImageClick implements View.OnClickListener {
        private ImageClick() {
        }

        public void onClick(View view) {
            StickerTypeAdapter.this.stickerActivity.swipToStickerDetails((String) view.getTag(StickerTypeAdapter.TAG_STICKERS_PATH), ((Integer) view.getTag(StickerTypeAdapter.TAG_STICKERS_COUNT)).intValue());
        }
    }
}
