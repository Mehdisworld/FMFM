package com.io.filemanager.freefileexplorer.easily.edit;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.io.filemanager.freefileexplorer.easily.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    private static final int DEFAULT_TYPE = 1;
    private Context context;
    private int[] filterImages = {R.drawable.filter_1, R.drawable.filter_2, R.drawable.filter_3, R.drawable.filter_4, R.drawable.filter_5, R.drawable.filter_6,
            R.drawable.filter_7, R.drawable.filter_8, R.drawable.filter_9, R.drawable.filter_10, R.drawable.filter_11, R.drawable.filter_12};
    private String[] filters;
    
    public OnItemClickListner onItemClickListner;

    public int getItemViewType(int i) {
        return 1;
    }

    public void setListner(OnItemClickListner onItemClickListner2) {
        this.onItemClickListner = onItemClickListner2;
    }

    public FilterAdapter(Context context2) {
        this.context = context2;
        this.filters = context2.getResources().getStringArray(R.array.iamutkarshtiwari_github_io_ananas_filters);
        //this.filterImages = context2.getResources().getStringArray(R.array.iamutkarshtiwari_github_io_ananas_filter_drawable_list);
    }

    public int getItemCount() {
        return this.filterImages.length;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.filter_name.setText(this.filters[i]);
        ImageView imageView = myViewHolder.filter_image;
        Resources resources = this.context.getResources();
        Resources resources2 = this.context.getResources();
        imageView.setImageResource(filterImages[i]);
        //imageView.setImageDrawable(resources.getDrawable(resources2.getIdentifier("drawable/" + this.filterImages[i], "drawable", this.context.getPackageName())));
        myViewHolder.filter_image.setTag(Integer.valueOf(i));
        myViewHolder.filter_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FilterAdapter.this.onItemClickListner != null) {
                    FilterAdapter.this.onItemClickListner.onItemClickListner(i);
                }
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView filter_image;
        public TextView filter_name;

        public MyViewHolder(View view) {
            super(view);
            this.filter_image = (ImageView) view.findViewById(R.id.filter_image);
            this.filter_name = (TextView) view.findViewById(R.id.filter_name);
        }
    }
}
