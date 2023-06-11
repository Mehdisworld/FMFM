package com.io.filemanager.freefileexplorer.easily.edit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.ImageViewTouch;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.ImageViewTouchBase;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.utils.StickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import net.lingala.zip4j.util.InternalZipConstants;

public class StickerActivity extends AppCompatActivity {
    
    public CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String imagePath;
    private ImageView img_close;
    private ImageView img_save;
    
    public ImageViewTouch img_set_sticker;
    
    public ProgressDialog loadingDialog;
    
    public Bitmap mainBitmap;
    
    public String outputPath;
    private StickerAdapter stickerAdapter;
    
    public StickerView stickerView;
    
    public RecyclerView stickers_list;
    
    public RecyclerView stickers_type_list;
    
    public TextView txt_theme_name;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_sticker);


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
        this.txt_theme_name = (TextView) findViewById(R.id.txt_theme_name);
        this.img_close = (ImageView) findViewById(R.id.img_close);
        this.img_save = (ImageView) findViewById(R.id.img_save);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.setCancelable(false);
        this.loadingDialog.setMessage("Saving");
        ImageViewTouch imageViewTouch = (ImageViewTouch) findViewById(R.id.img_set_sticker);
        this.img_set_sticker = imageViewTouch;
        imageViewTouch.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        this.img_set_sticker.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        Glide.with((FragmentActivity) this).asBitmap().load(this.imagePath).into(new CustomTarget<Bitmap>() {
            public void onLoadCleared(Drawable drawable) {
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                Bitmap unused = StickerActivity.this.mainBitmap = bitmap;
                StickerActivity.this.img_set_sticker.setImageBitmap(bitmap);
            }
        });
        this.stickerView = (StickerView) findViewById(R.id.sticker_panel);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stickers_list);
        this.stickers_list = recyclerView;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(0);
        this.stickers_list.setLayoutManager(linearLayoutManager);
        this.stickers_list.setAdapter(new StickerTypeAdapter(this));
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.stickers_type_list);
        this.stickers_type_list = recyclerView2;
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(0);
        this.stickers_type_list.setLayoutManager(linearLayoutManager2);
        StickerAdapter stickerAdapter2 = new StickerAdapter(this);
        this.stickerAdapter = stickerAdapter2;
        this.stickers_type_list.setAdapter(stickerAdapter2);
    }

    private void click() {
        this.img_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerActivity.this.stickers_type_list.getVisibility() == 0) {
                    StickerActivity.this.stickers_list.setVisibility(0);
                    StickerActivity.this.stickers_type_list.setVisibility(8);
                    StickerActivity.this.txt_theme_name.setText("Sticker");
                    return;
                }
                StickerActivity.this.onBackPressed();
            }
        });
        this.img_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerActivity stickerActivity = StickerActivity.this;
                stickerActivity.compositeDisposable.add(stickerActivity.applyStickerToImage(stickerActivity.mainBitmap).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer() {
                    public final void accept(Object obj) throws Exception {
                        lambda$onClick$0$StickerActivity$4((Disposable) obj);
                    }
                }).doFinally(new Action() {
                    public final void run() throws Exception {
                        lambda$onClick$1$StickerActivity$4();
                    }
                }).subscribe(new Consumer() {
                    public final void accept(Object obj) throws Exception {
                        lambda$onClick$2$StickerActivity$4((Bitmap) obj);
                    }
                }, new Consumer() {
                    public final void accept(Object obj) throws Exception {
                        lambda$onClick$3$StickerActivity$4((Throwable) obj);
                    }
                }));
            }

            public void lambda$onClick$0$StickerActivity$4(Disposable disposable) throws Exception {
                StickerActivity.this.loadingDialog.show();
            }

            public void lambda$onClick$1$StickerActivity$4() throws Exception {
                StickerActivity.this.loadingDialog.dismiss();
            }

            public void lambda$onClick$2$StickerActivity$4(Bitmap bitmap) throws Exception {
                if (bitmap != null) {
                    StickerActivity.this.stickerView.clear();
                    try {
                        Environment.getExternalStorageDirectory().toString();
                        File file = new File(StickerActivity.this.outputPath);
                        if (file.exists()) {
                            file.delete();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        Intent intent = new Intent();
                        intent.putExtra("cropPath", file.getAbsolutePath());
                        StickerActivity.this.setResult(-1, intent);
                        StickerActivity.this.finish();
                    } catch (Exception unused) {
                    }
                }
            }

            public void lambda$onClick$3$StickerActivity$4(Throwable th) throws Exception {
                Toast.makeText(StickerActivity.this.getApplicationContext(), "save eror", 0).show();
            }
        });
    }

    public void onPause() {
        this.compositeDisposable.clear();
        super.onPause();
    }

    public void onDestroy() {
        this.compositeDisposable.dispose();
        super.onDestroy();
    }

    private void tryRecycleFilterBitmap() {
        Bitmap bitmap = this.mainBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mainBitmap.recycle();
        }
    }

    
    public Single<Bitmap> applyStickerToImage(final Bitmap bitmap) {
        return Single.fromCallable(new Callable() {
            public final Object call() throws Exception {
                return StickerActivity.this.lambda$applyStickerToImage$0$StickerActivity(bitmap);
            }
        });
    }

    public Bitmap lambda$applyStickerToImage$0$StickerActivity(Bitmap bitmap) throws Exception {
        Matrix imageViewMatrix = this.img_set_sticker.getImageViewMatrix();
        Bitmap copy = Bitmap.createBitmap(bitmap).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(copy);
        float[] fArr = new float[9];
        imageViewMatrix.getValues(fArr);
        Matrix3 inverseMatrix = new Matrix3(fArr).inverseMatrix();
        Matrix matrix = new Matrix();
        matrix.setValues(inverseMatrix.getValues());
        handleImage(canvas, matrix);
        return copy;
    }

    private void handleImage(Canvas canvas, Matrix matrix) {
        LinkedHashMap<Integer, StickerItem> bank = this.stickerView.getBank();
        for (Integer num : bank.keySet()) {
            StickerItem stickerItem = bank.get(num);
            stickerItem.matrix.postConcat(matrix);
            canvas.drawBitmap(stickerItem.bitmap, stickerItem.matrix, (Paint) null);
        }
    }

    public void swipToStickerDetails(String str, int i) {
        try {
            String[] list = getAssets().list(str);
            if (list.length > 0) {
                i = list.length;
            }
        } catch (Exception unused) {
        }
        this.stickers_list.setVisibility(8);
        this.stickers_type_list.setVisibility(0);
        TextView textView = this.txt_theme_name;
        textView.setText(String.valueOf(str.split(InternalZipConstants.ZIP_FILE_SEPARATOR)[1].charAt(0)).toUpperCase() + str.split(InternalZipConstants.ZIP_FILE_SEPARATOR)[1].subSequence(1, str.split(InternalZipConstants.ZIP_FILE_SEPARATOR)[1].length()));
        this.stickerAdapter.addStickerImages(str, i);
    }

    public void selectedStickerItem(String str) {
        try {
            Log.d("TAG", "onBindViewHolder: " + str);
            this.stickerView.addBitImage(((BitmapDrawable) Drawable.createFromStream(getAssets().open(str), (String) null)).getBitmap());
        } catch (Exception unused) {
        }
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
