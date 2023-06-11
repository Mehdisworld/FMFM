package com.io.filemanager.freefileexplorer.easily.edit;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.recyclerview.widget.RecyclerView;
import com.io.filemanager.freefileexplorer.easily.R;
import java.util.ArrayList;
import java.util.List;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {
    
    public List<Integer> colorPickerColors;
    private LayoutInflater inflater;
    
    public OnColorPickerClickListener onColorPickerClickListener;

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int i);
    }

    public ColorPickerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = getKelly22Colors(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.color_picker_item_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.colorPickerView.setBackgroundColor(this.colorPickerColors.get(i).intValue());
    }

    public int getItemCount() {
        return this.colorPickerColors.size();
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener2) {
        this.onColorPickerClickListener = onColorPickerClickListener2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View colorPickerView;

        ViewHolder(View view) {
            super(view);
            this.colorPickerView = view.findViewById(R.id.color_picker_view);
            view.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    ViewHolder.this.lambda$new$0$ColorPickerAdapter$ViewHolder(view);
                }
            });
        }

        public void lambda$new$0$ColorPickerAdapter$ViewHolder(View view) {
            if (ColorPickerAdapter.this.onColorPickerClickListener != null) {
                ColorPickerAdapter.this.onColorPickerClickListener.onColorPickerClickListener(((Integer) ColorPickerAdapter.this.colorPickerColors.get(getAdapterPosition())).intValue());
            }
        }
    }

    private List<Integer> getKelly22Colors(Context context) {
        Resources resources = context.getResources();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i <= 21) {
            StringBuilder sb = new StringBuilder();
            sb.append("kelly_");
            i++;
            sb.append(i);
            arrayList.add(Integer.valueOf(resources.getColor(resources.getIdentifier(sb.toString(), "color", context.getPackageName()))));
        }
        return arrayList;
    }
}
