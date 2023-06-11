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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
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

public class FilterActivity extends AppCompatActivity {
    private static final String TAG = "FilterActivity";
    
    public CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    public Bitmap currentBitmap;
    private FilterAdapter filterAdapter;
    private Bitmap filterBitmap;
    private RecyclerView filter_recycler;
    private String imagePath;
    private ImageView img_close;
    
    public ImageView img_filter;
    private ImageView img_save;
    
    public ProgressDialog loadingDialog;
    
    public Bitmap mainBitmap;
    
    public String outputPath;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_filter);


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
        this.img_filter = (ImageView) findViewById(R.id.img_filter);
        this.img_close = (ImageView) findViewById(R.id.img_close);
        this.img_save = (ImageView) findViewById(R.id.img_save);
        this.filter_recycler = (RecyclerView) findViewById(R.id.filter_recycler);
        Glide.with((FragmentActivity) this).asBitmap().load(this.imagePath).apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true)).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(new CustomTarget<Bitmap>() {
            public void onLoadCleared(Drawable drawable) {
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                FilterActivity.this.img_filter.setImageBitmap(bitmap);
                Bitmap unused = FilterActivity.this.mainBitmap = bitmap;
            }
        });
        this.filterAdapter = new FilterAdapter(getApplicationContext());
        this.filter_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        this.filter_recycler.setAdapter(this.filterAdapter);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.setCancelable(false);
        this.loadingDialog.setMessage("Filtering");
    }

    private void click() {
        this.img_filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FilterActivity.this.onBackPressed();
            }
        });
        this.img_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Environment.getExternalStorageDirectory().toString();
                    File file = new File(FilterActivity.this.outputPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    FilterActivity.this.currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Intent intent = new Intent();
                    intent.putExtra("cropPath", file.getAbsolutePath());
                    FilterActivity.this.setResult(-1, intent);
                    FilterActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.filterAdapter.setListner(new OnItemClickListner() {
            public void onItemClickListner(int i) {
                Log.d(FilterActivity.TAG, "onItemClickListner: " + i);
                FilterActivity.this.compositeDisposable.clear();
                FilterActivity.this.compositeDisposable.add(FilterActivity.this.applyFilter(i).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer() {
                    public final void accept(Object obj) throws Exception {
                        lambda$onItemClickListner$0$FilterActivity$4((Disposable) obj);
                    }
                }).doFinally(new Action() {
                    public final void run() throws Exception {
                        lambda$onItemClickListner$1$FilterActivity$4();
                    }
                }).subscribe(new Consumer() {
                    public final void accept(Object obj) throws Exception {
                        lambda$onItemClickListner$2$FilterActivity$4((Bitmap) obj);
                    }
                }, new Consumer() {
                    public final void accept(Object obj) throws Exception {
                        lambda$onItemClickListner$3$FilterActivity$4((Throwable) obj);
                    }
                }));
            }

            public void lambda$onItemClickListner$0$FilterActivity$4(Disposable disposable) throws Exception {
                FilterActivity.this.loadingDialog.show();
            }

            public void lambda$onItemClickListner$1$FilterActivity$4() throws Exception {
                FilterActivity.this.loadingDialog.dismiss();
            }

            public void lambda$onItemClickListner$2$FilterActivity$4(Bitmap bitmap) throws Exception {
                FilterActivity.this.updatePreviewWithFilter(bitmap);
            }

            public void lambda$onItemClickListner$3$FilterActivity$4(Throwable th) throws Exception {
                FilterActivity.this.showSaveErrorToast(th);
            }
        });
        this.img_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FilterActivity.this.onBackPressed();
            }
        });
    }

    
    public Single<Bitmap> applyFilter(final int i) {
        return Single.fromCallable(new Callable() {
            public final Object call() throws Exception {
                return FilterActivity.this.lambda$applyFilter$0$FilterActivity(i);
            }
        });
    }

    public Bitmap lambda$applyFilter$0$FilterActivity(int i) throws Exception {
        return PhotoProcessing.filterPhoto(Bitmap.createBitmap(getMainBit().copy(Bitmap.Config.RGB_565, true)), i);
    }

    
    public void showSaveErrorToast(Throwable th) {
        Log.d(TAG, "showSaveErrorToast: " + th);
        Toast.makeText(getApplicationContext(), "Save error", 0).show();
    }

    
    public void updatePreviewWithFilter(Bitmap bitmap) {
        Log.d(TAG, "updatePreviewWithFilter: ");
        if (bitmap != null) {
            Bitmap bitmap2 = this.filterBitmap;
            if (bitmap2 != null && !bitmap2.isRecycled()) {
                this.filterBitmap.recycle();
            }
            this.filterBitmap = bitmap;
            this.img_filter.setImageBitmap(bitmap);
            this.currentBitmap = this.filterBitmap;
        }
    }

    public Bitmap getMainBit() {
        return this.mainBitmap;
    }

    public void onPause() {
        this.compositeDisposable.clear();
        super.onPause();
    }

    public void onDestroy() {
        tryRecycleFilterBitmap();
        this.compositeDisposable.dispose();
        super.onDestroy();
    }

    private void tryRecycleFilterBitmap() {
        Bitmap bitmap = this.filterBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.filterBitmap.recycle();
        }
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
