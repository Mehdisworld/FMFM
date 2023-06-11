package com.io.filemanager.freefileexplorer.easily.edit;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.event.CopyMoveEvent;
import com.io.filemanager.freefileexplorer.easily.event.ImageDeleteEvent;
import com.io.filemanager.freefileexplorer.easily.utils.RxBus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
//import com.safedk.android.utils.Logger;
import java.io.File;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    @BindView(R.id.ad_view_container)
    FrameLayout adViewContainer;
    
    public String imagePath;
    private ImageView img_back;
    private ImageView img_edit_path;
    boolean isAdLoad = false;
    boolean isEditImage = false;
    private LinearLayout layout_beauty;
    private LinearLayout layout_crop;
    private LinearLayout layout_filter;
    private LinearLayout layout_paint;
    private LinearLayout layout_sticker;
    
    public String outputPath;
    private TextView txt_save;

    private void loadBanner() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_edit);
        ButterKnife.bind((Activity) this);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        System.loadLibrary("photoprocessing");
        init();
        click();
    }

    public void onResume() {
        super.onResume();
        if (!this.isAdLoad) {
            loadBanner();
        }
    }

    private void init() {
        this.imagePath = getIntent().getStringExtra("imagePath");
        this.outputPath = getIntent().getStringExtra("outputPath");
        this.img_edit_path = (ImageView) findViewById(R.id.img_edit_path);
        Glide.with((FragmentActivity) this).load(this.imagePath).apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true)).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(this.img_edit_path);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.txt_save = (TextView) findViewById(R.id.txt_save);
        this.layout_crop = (LinearLayout) findViewById(R.id.layout_crop);
        this.layout_sticker = (LinearLayout) findViewById(R.id.layout_sticker);
        this.layout_filter = (LinearLayout) findViewById(R.id.layout_filter);
        this.layout_paint = (LinearLayout) findViewById(R.id.layout_paint);
        this.layout_beauty = (LinearLayout) findViewById(R.id.layout_beauty);
    }

    private void click() {
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditActivity.this.onBackPressed();
            }
        });
        this.txt_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("outputPath", EditActivity.this.outputPath);
                ArrayList arrayList = new ArrayList();
                File file = new File(EditActivity.this.outputPath);
                if (file.exists()) {
                    arrayList.add(file);
                    RxBus.getInstance().post(new CopyMoveEvent(arrayList, 2, new ArrayList()));
                }
                EditActivity.this.setResult(-1, intent);
                EditActivity.this.finish();
            }
        });
        this.layout_crop.setOnClickListener(new View.OnClickListener() {
            public void safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity p0, Intent p1, int p2) {
                if (p1 != null) {
                    p0.startActivityForResult(p1, p2);
                }
            }

            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, CropActivity.class);
                intent.putExtra("imagePath", EditActivity.this.imagePath);
                intent.putExtra("outputPath", EditActivity.this.outputPath);
                safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity.this, intent, 100);
            }
        });
        this.layout_filter.setOnClickListener(new View.OnClickListener() {
            public void safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity p0, Intent p1, int p2) {
                if (p1 != null) {
                    p0.startActivityForResult(p1, p2);
                }
            }

            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, FilterActivity.class);
                intent.putExtra("imagePath", EditActivity.this.imagePath);
                intent.putExtra("outputPath", EditActivity.this.outputPath);
                safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity.this, intent, 200);
            }
        });
        this.layout_sticker.setOnClickListener(new View.OnClickListener() {
            public void safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity p0, Intent p1, int p2) {
                if (p1 != null) {
                    p0.startActivityForResult(p1, p2);
                }
            }

            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, StickerActivity.class);
                intent.putExtra("imagePath", EditActivity.this.imagePath);
                intent.putExtra("outputPath", EditActivity.this.outputPath);
                safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity.this, intent, 300);
            }
        });
        this.layout_paint.setOnClickListener(new View.OnClickListener() {
            public void safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity p0, Intent p1, int p2) {
                if (p1 != null) {
                    p0.startActivityForResult(p1, p2);
                }
            }

            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, PaintActivity.class);
                intent.putExtra("imagePath", EditActivity.this.imagePath);
                intent.putExtra("outputPath", EditActivity.this.outputPath);
                safedk_EditActivity_startActivityForResult_958dae75678e23de230f18c5f13de807(EditActivity.this, intent, 400);
            }
        });
        this.layout_beauty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, BeautyActivity.class);
                intent.putExtra("imagePath", EditActivity.this.imagePath);
                intent.putExtra("outputPath", EditActivity.this.outputPath);
                //startActivity(intent);
                SplashLaunchActivity.InterstitialAdsCall(EditActivity.this, intent);
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            String stringExtra = intent.getStringExtra("cropPath");
            Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File file = new File(stringExtra);
            intent2.setData(Uri.fromFile(file));
            sendBroadcast(intent2);
            this.isEditImage = true;
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
            this.imagePath = stringExtra;
            Glide.with((FragmentActivity) this).load(stringExtra).apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true)).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(this.img_edit_path);
        }
    }

    public void onBackPressed() {
        RxBus.getInstance().post(new ImageDeleteEvent(this.outputPath));
        setResult(0);
        finish();
    }
}
