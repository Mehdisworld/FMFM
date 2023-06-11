package com.io.filemanager.freefileexplorer.easily.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phone.myapplication.sneha.utils.AspectRatio;
import com.phone.myapplication.sneha.utils.RatioText;
import com.theartofdev.edmodo.cropper.CropImageView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

public class CropActivity extends AppCompatActivity {
    private static int SELECTED_COLOR = 2131099701;
    private static final String TAG = "CropActivity";
    private static int UNSELECTED_COLOR = 2131099921;
    
    public CompositeDisposable disposables = new CompositeDisposable();
    private String imagePath;
    private ImageView img_close;
    
    public CropImageView img_crop_path;
    private ImageView img_save;
    private RelativeLayout layout_crop;
    
    public HorizontalScrollView layout_crop_ratio;
    private RelativeLayout layout_horizontal_rotate;
    
    public LinearLayout layout_option;
    private RelativeLayout layout_rotate;
    private RelativeLayout layout_vertical_rotate;
    private int[] non_selected = {R.drawable.ic_icon_custom, R.drawable.ratio_4_5, R.drawable.ratio_insstory, R.drawable.ratio_5_4, R.drawable.ratio_3_4, R.drawable.ratio_4_3, R.drawable.ratio_fbpost, R.drawable.ratio_fbcover, R.drawable.ratio_pinpost, R.drawable.ratio_3_2, R.drawable.ratio_9_16, R.drawable.ratio_16_9, R.drawable.ratio_1_2, R.drawable.ratio_youtubecover, R.drawable.ratio_twitterpost, R.drawable.ratio_twitterheader};
    
    public Bitmap originalBitmap;
    
    public String outputPath;
    
    public int position = 0;
    private LinearLayout ratio_list_group;
    private int[] selected = {R.drawable.ic_icon_custom_selected, R.drawable.ratio_4_5_click, R.drawable.ratio_insstory_click, R.drawable.ratio_5_4_click, R.drawable.ratio_3_4_click, R.drawable.ratio_4_3_click, R.drawable.ratio_fbpost_click, R.drawable.ratio_fbcover_click, R.drawable.ratio_pinpost_click, R.drawable.ratio_3_2_click, R.drawable.ratio_9_16_click, R.drawable.ratio_16_9_click, R.drawable.ratio_1_2_click, R.drawable.ratio_youtubecover_click, R.drawable.ratio_twitterpost_click, R.drawable.ratio_twitterheader_click};
    
    public ImageView selectedImageView;
    
    public TextView selectedTextView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_crop);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        init();
        click();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black, getTheme()));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        } else if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
        this.imagePath = getIntent().getStringExtra("imagePath");
        this.outputPath = getIntent().getStringExtra("outputPath");
        Log.d(TAG, "init: " + this.imagePath);
        CropImageView cropImageView = (CropImageView) findViewById(R.id.img_crop_path);
        this.img_crop_path = cropImageView;
        cropImageView.setShowCropOverlay(false);
        this.img_crop_path.setRotatedDegrees(0);
        Glide.with((FragmentActivity) this).asBitmap().load(this.imagePath).apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true)).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(new CustomTarget<Bitmap>() {
            public void onLoadCleared(Drawable drawable) {
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                CropActivity.this.img_crop_path.setImageBitmap(bitmap);
                Bitmap unused = CropActivity.this.originalBitmap = bitmap;
            }
        });
        this.ratio_list_group = (LinearLayout) findViewById(R.id.ratio_list_group);
        this.layout_option = (LinearLayout) findViewById(R.id.layout_option);
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.layout_crop_ratio);
        this.layout_crop_ratio = horizontalScrollView;
        horizontalScrollView.setVisibility(8);
        Log.d(TAG, "onClick111: " + this.img_crop_path.getAspectRatio());
        this.img_close = (ImageView) findViewById(R.id.img_close);
        this.img_save = (ImageView) findViewById(R.id.img_save);
        this.layout_crop = (RelativeLayout) findViewById(R.id.layout_crop);
        this.layout_rotate = (RelativeLayout) findViewById(R.id.layout_rotate);
        this.layout_vertical_rotate = (RelativeLayout) findViewById(R.id.layout_vertical_rotate);
        this.layout_horizontal_rotate = (RelativeLayout) findViewById(R.id.layout_horizontal_rotate);
        setUpRatioList();
    }

    private void click() {
        this.img_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CropActivity.this.layout_crop_ratio.getVisibility() == 0) {
                    CropActivity.this.layout_crop_ratio.setVisibility(8);
                    CropActivity.this.layout_option.setVisibility(0);
                    CropActivity.this.img_crop_path.setShowCropOverlay(false);
                    return;
                }
                CropActivity.this.onBackPressed();
            }
        });
        this.img_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CropActivity.this.layout_crop_ratio.getVisibility() == 0) {
                    CropActivity.this.disposables.add(CropActivity.this.getCroppedBitmap().subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
                        public final void accept(Object obj) throws Exception {
                            lambda$onClick$0$CropActivity$3((Bitmap) obj);
                        }
                    }, new Consumer() {
                        public final void accept(Object obj) throws Exception {
                            lambda$onClick$1$CropActivity$3((Throwable) obj);
                        }
                    }));
                    return;
                }
                try {
                    File file = new File(CropActivity.this.outputPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    CropActivity.this.originalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Intent intent = new Intent();
                    intent.putExtra("cropPath", file.getAbsolutePath());
                    CropActivity.this.setResult(-1, intent);
                    CropActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void lambda$onClick$0$CropActivity$3(Bitmap bitmap) throws Exception {
                Log.d(CropActivity.TAG, "onClick:subscribe ");
                Bitmap unused = CropActivity.this.originalBitmap = bitmap;
                CropActivity.this.img_crop_path.setImageBitmap(CropActivity.this.originalBitmap);
                CropActivity.this.layout_crop_ratio.setVisibility(8);
                CropActivity.this.layout_option.setVisibility(0);
                CropActivity.this.img_crop_path.setShowCropOverlay(false);
            }

            public void lambda$onClick$1$CropActivity$3(Throwable th) throws Exception {
                Log.d(CropActivity.TAG, "onClick:erro ");
                th.printStackTrace();
                CropActivity.this.layout_crop_ratio.setVisibility(8);
                CropActivity.this.layout_option.setVisibility(0);
                CropActivity.this.img_crop_path.setShowCropOverlay(false);
                Toast.makeText(CropActivity.this.getApplicationContext(), "Error while saving image", 0).show();
            }
        });
        this.layout_crop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CropActivity.this.layout_crop_ratio.setVisibility(0);
                CropActivity.this.layout_option.setVisibility(8);
                CropActivity.this.img_crop_path.setShowCropOverlay(true);
            }
        });
        this.layout_rotate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(CropActivity.TAG, "onClick: " + CropActivity.this.img_crop_path.getRotatedDegrees());
                Matrix matrix = new Matrix();
                if (CropActivity.this.img_crop_path.getRotatedDegrees() == 90) {
                    matrix.postRotate(180.0f);
                    try {
                        CropActivity cropActivity = CropActivity.this;
                        Bitmap unused = cropActivity.originalBitmap = Bitmap.createBitmap(cropActivity.originalBitmap, 0, 0, CropActivity.this.originalBitmap.getWidth(), CropActivity.this.originalBitmap.getHeight(), matrix, true);
                        CropActivity.this.img_crop_path.setRotatedDegrees(180);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CropActivity.this.img_crop_path.getRotatedDegrees() == 180) {
                    try {
                        matrix.postRotate(270.0f);
                        CropActivity cropActivity2 = CropActivity.this;
                        Bitmap unused2 = cropActivity2.originalBitmap = Bitmap.createBitmap(cropActivity2.originalBitmap, 0, 0, CropActivity.this.originalBitmap.getWidth(), CropActivity.this.originalBitmap.getHeight(), matrix, true);
                        CropActivity.this.img_crop_path.setRotatedDegrees(270);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (CropActivity.this.img_crop_path.getRotatedDegrees() == 270) {
                    try {
                        matrix.postRotate(0.0f);
                        CropActivity cropActivity3 = CropActivity.this;
                        Bitmap unused3 = cropActivity3.originalBitmap = Bitmap.createBitmap(cropActivity3.originalBitmap, 0, 0, CropActivity.this.originalBitmap.getWidth(), CropActivity.this.originalBitmap.getHeight(), matrix, true);
                        CropActivity.this.img_crop_path.setRotatedDegrees(0);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                } else if (CropActivity.this.img_crop_path.getRotatedDegrees() == 0) {
                    try {
                        matrix.postRotate(90.0f);
                        CropActivity cropActivity4 = CropActivity.this;
                        Bitmap unused4 = cropActivity4.originalBitmap = Bitmap.createBitmap(cropActivity4.originalBitmap, 0, 0, CropActivity.this.originalBitmap.getWidth(), CropActivity.this.originalBitmap.getHeight(), matrix, true);
                        CropActivity.this.img_crop_path.setRotatedDegrees(90);
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
            }
        });
        this.layout_vertical_rotate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Matrix matrix = new Matrix();
                    matrix.preScale(1.0f, -1.0f);
                    Bitmap createBitmap = Bitmap.createBitmap(CropActivity.this.originalBitmap, 0, 0, CropActivity.this.originalBitmap.getWidth(), CropActivity.this.originalBitmap.getHeight(), matrix, true);
                    Bitmap unused = CropActivity.this.originalBitmap = createBitmap;
                    CropActivity.this.img_crop_path.setImageBitmap(createBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.layout_horizontal_rotate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Matrix matrix = new Matrix();
                    matrix.preScale(-1.0f, 1.0f);
                    Bitmap createBitmap = Bitmap.createBitmap(CropActivity.this.originalBitmap, 0, 0, CropActivity.this.originalBitmap.getWidth(), CropActivity.this.originalBitmap.getHeight(), matrix, true);
                    CropActivity.this.img_crop_path.setImageBitmap(createBitmap);
                    Bitmap unused = CropActivity.this.originalBitmap = createBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    public Single<Bitmap> getCroppedBitmap() {
        return Single.fromCallable(new Callable() {
            public final Object call() throws Exception {
                return CropActivity.this.lambda$getCroppedBitmap$0$CropActivity();
            }
        });
    }

    public Bitmap lambda$getCroppedBitmap$0$CropActivity() throws Exception {
        return this.img_crop_path.getCroppedImage();
    }

    private void setUpRatioList() {
        this.ratio_list_group.removeAllViews();
        RatioText[] values = RatioText.values();
        Log.d(TAG, "setUpRatioList: " + this.selected.length + "   " + this.non_selected.length + "   " + values.length);
        for (int i = 0; i < values.length; i++) {
            ViewGroup viewGroup = null;
            View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_crop, (ViewGroup) null);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.img_ratio);
            TextView textView = (TextView) inflate.findViewById(R.id.txt_ratio);
            imageView.setImageResource(this.non_selected[i]);
            textView.setText(getResources().getText(values[i].getRatioTextId()));
            this.ratio_list_group.addView(inflate);
            if (i == 0) {
                this.selectedTextView = textView;
                this.selectedImageView = imageView;
                this.position = 0;
            }
            imageView.setTag(Integer.valueOf(i));
            textView.setTag(values[i]);
            inflate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CropActivity cropActivity = CropActivity.this;
                    cropActivity.toggleButtonStatus(cropActivity.selectedImageView, CropActivity.this.selectedTextView, ((Integer) CropActivity.this.selectedImageView.getTag()).intValue(), false);
                    LinearLayout linearLayout = (LinearLayout) view;
                    TextView textView = (TextView) linearLayout.getChildAt(1);
                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                    CropActivity.this.toggleButtonStatus(imageView, textView, ((Integer) imageView.getTag()).intValue(), true);
                    ImageView unused = CropActivity.this.selectedImageView = imageView;
                    CropActivity cropActivity2 = CropActivity.this;
                    int unused2 = cropActivity2.position = ((Integer) cropActivity2.selectedImageView.getTag()).intValue();
                    TextView unused3 = CropActivity.this.selectedTextView = textView;
                    RatioText ratioText = (RatioText) textView.getTag();
                    if (ratioText == RatioText.FREE) {
                        CropActivity.this.img_crop_path.setFixedAspectRatio(false);
                        return;
                    }
                    AspectRatio aspectRatio = ratioText.getAspectRatio();
                    CropActivity.this.img_crop_path.setAspectRatio((int) aspectRatio.getAspectX(), (int) aspectRatio.getAspectY());
                }
            });
        }
        ImageView imageView2 = this.selectedImageView;
        toggleButtonStatus(imageView2, this.selectedTextView, ((Integer) imageView2.getTag()).intValue(), true);
    }

    
    public void toggleButtonStatus(ImageView imageView, TextView textView, int i, boolean z) {
        if (z) {
            imageView.setImageResource(this.selected[i]);
            textView.setTextColor(getColorFromRes(SELECTED_COLOR));
            return;
        }
        imageView.setImageResource(this.non_selected[i]);
        textView.setTextColor(getColorFromRes(UNSELECTED_COLOR));
    }

    private int getColorFromRes(int i) {
        return ContextCompat.getColor(getApplicationContext(), i);
    }

    public void onStop() {
        this.disposables.clear();
        super.onStop();
    }

    public void onDestroy() {
        this.disposables.dispose();
        super.onDestroy();
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
