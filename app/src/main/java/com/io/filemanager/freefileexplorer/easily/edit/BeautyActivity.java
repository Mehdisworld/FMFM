package com.io.filemanager.freefileexplorer.easily.edit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.ImageViewTouch;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.ImageViewTouchBase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import iamutkarshtiwari.github.io.ananas.editimage.fliter.PhotoProcessing;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

public class BeautyActivity extends AppCompatActivity {
    
    public String TAG = "BeatyActivity";
    private Disposable beautyDisposable;
    private CompositeDisposable disposable = new CompositeDisposable();
    
    public Bitmap finalBmp;
    private String imagePath;
    
    public ImageViewTouch img_beauty_path;
    private ImageView img_close;
    private ImageView img_save;
    private ProgressDialog loadingDialog;
    private String outputPath;
    private int smooth = 0;
    private SeekBar smooth_value_bar;
    private int whiteSkin = 0;
    private SeekBar white_skin_value_bar;

    static void lambda$doBeautyTask$3(Throwable th) throws Exception {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_beauty);


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
        this.img_close = (ImageView) findViewById(R.id.img_close);
        this.img_save = (ImageView) findViewById(R.id.img_save);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.setCancelable(false);
        this.loadingDialog.setMessage("Applying");
        ImageViewTouch imageViewTouch = (ImageViewTouch) findViewById(R.id.img_beauty_path);
        this.img_beauty_path = imageViewTouch;
        imageViewTouch.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        Glide.with((FragmentActivity) this).asBitmap().load(this.imagePath).apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true)).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(new CustomTarget<Bitmap>() {
            public void onLoadCleared(Drawable drawable) {
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                BeautyActivity.this.img_beauty_path.setImageBitmap(bitmap);
                Bitmap unused = BeautyActivity.this.finalBmp = bitmap;
            }
        });
        this.white_skin_value_bar = (SeekBar) findViewById(R.id.white_skin_value_bar);
        this.smooth_value_bar = (SeekBar) findViewById(R.id.smooth_value_bar);
    }

    private void click() {
        this.img_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BeautyActivity.this.onBackPressed();
            }
        });
        this.img_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(BeautyActivity.this.TAG, "onClick: save");
                BeautyActivity.this.applyBeauty();
            }
        });
        this.white_skin_value_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                BeautyActivity.this.doBeautyTask();
            }
        });
        this.smooth_value_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                BeautyActivity.this.doBeautyTask();
            }
        });
    }

    public void doBeautyTask() {
        Disposable disposable2 = this.beautyDisposable;
        if (disposable2 != null && !disposable2.isDisposed()) {
            this.beautyDisposable.dispose();
        }
        this.smooth = this.smooth_value_bar.getProgress();
        int progress = this.white_skin_value_bar.getProgress();
        this.whiteSkin = progress;
        int i = this.smooth;
        if (i == 0 && progress == 0) {
            this.img_beauty_path.setImageBitmap(this.finalBmp);
            return;
        }
        Disposable subscribe = beautify(i, progress).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer() {
            public final void accept(Object obj) throws Exception {
                BeautyActivity.this.lambda$doBeautyTask$0$BeautyActivity((Disposable) obj);
            }
        }).doFinally(new Action() {
            public final void run() throws Exception {
                BeautyActivity.this.lambda$doBeautyTask$1$BeautyActivity();
            }
        }).subscribe(new Consumer() {
            public final void accept(Object obj) throws Exception {
                BeautyActivity.this.lambda$doBeautyTask$2$BeautyActivity((Bitmap) obj);
            }
        }, C$$Lambda$BeautyActivity$cZheYoFdrxvtueK2qw4J2KmLEk.INSTANCE);
        this.beautyDisposable = subscribe;
        this.disposable.add(subscribe);
    }

    public void lambda$doBeautyTask$0$BeautyActivity(Disposable disposable2) throws Exception {
        this.loadingDialog.show();
    }

    public void lambda$doBeautyTask$1$BeautyActivity() throws Exception {
        this.loadingDialog.dismiss();
    }

    public void lambda$doBeautyTask$2$BeautyActivity(Bitmap bitmap) throws Exception {
        if (bitmap != null) {
            Log.d(this.TAG, "doBeautyTask: ");
            this.img_beauty_path.setImageBitmap(bitmap);
            this.finalBmp = bitmap;
        }
    }

    public void applyBeauty() {
        if (this.finalBmp != null) {
            if (this.smooth != 0 || this.whiteSkin != 0) {
                try {
                    Environment.getExternalStorageDirectory().toString();
                    File file = new File(this.outputPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    this.finalBmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Intent intent = new Intent();
                    intent.putExtra("cropPath", file.getAbsolutePath());
                    setResult(-1, intent);
                    finish();
                } catch (Exception unused) {
                }
            }
        }
    }

    private Single<Bitmap> beautify(final int i, final int i2) {
        return Single.fromCallable(new Callable() {
            public final Object call() throws Exception {
                return BeautyActivity.this.lambda$beautify$4$BeautyActivity(i, i2);
            }
        });
    }

    public Bitmap lambda$beautify$4$BeautyActivity(int i, int i2) throws Exception {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(this.finalBmp.copy(Bitmap.Config.ARGB_8888, true));
            PhotoProcessing.handleSmoothAndWhiteSkin(createBitmap, (float) i, (float) i2);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.disposable.dispose();
    }

    public void onPause() {
        super.onPause();
        this.disposable.clear();
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
