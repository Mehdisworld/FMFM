package com.io.filemanager.freefileexplorer.easily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.model.PhotoData;
import com.io.filemanager.freefileexplorer.easily.oncliclk.ImageToolbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;

public class DisplayImageAdapter extends PagerAdapter {
    Context context;
    List<PhotoData> imageList = new ArrayList();
    ImageToolbar imageToolbar;
    boolean isFirst = false;
    LayoutInflater layoutInflater;

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public DisplayImageAdapter(Context context2, List<PhotoData> list, ImageToolbar imageToolbar2) {
        this.context = context2;
        this.imageList = list;
        this.imageToolbar = imageToolbar2;
        this.layoutInflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.imageList.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View inflate = this.layoutInflater.inflate(R.layout.item_full_screen_image, viewGroup, false);
        GestureImageView gestureImageView = (GestureImageView) inflate.findViewById(R.id.iv_display);
        gestureImageView.getController().getSettings().setMaxZoom(6.0f).setMinZoom(0.0f).setDoubleTapZoom(3.0f);
        ((RequestBuilder) Glide.with(this.context).setDefaultRequestOptions((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).override(900, 900)).dontTransform()).load(this.imageList.get(i).getFilePath()).placeholder(ContextCompat.getDrawable(this.context, R.drawable.ic_image_placeholder))).into((ImageView) gestureImageView);
        gestureImageView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DisplayImageAdapter.this.lambda$instantiateItem$0$DisplayImageAdapter(view);
            }
        });
        viewGroup.addView(inflate);
        return inflate;
    }

    public void lambda$instantiateItem$0$DisplayImageAdapter(View view) {
        this.imageToolbar.OnImageToolbar(view);
    }

    public Bitmap rotateImage(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((RelativeLayout) obj);
    }
}
