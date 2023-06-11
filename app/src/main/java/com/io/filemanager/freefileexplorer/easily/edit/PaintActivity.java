package com.io.filemanager.freefileexplorer.easily.edit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.SplashLaunchActivity;
import com.io.filemanager.freefileexplorer.easily.customViews.CustomPaintView;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.ImageViewTouch;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.ImageViewTouchBase;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.paint.BrushConfigDialog;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.paint.EraserConfigDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

public class PaintActivity extends AppCompatActivity implements BrushConfigDialog.Properties, EraserConfigDialog.Properties {
    private static final float INITIAL_WIDTH = 50.0f;
    private static final float MAX_ALPHA = 255.0f;
    private static final float MAX_PERCENT = 100.0f;
    
    public String TAG = "PaintActiviy";
    private float brushAlpha = MAX_ALPHA;
    private int brushColor = -1;
    
    public BrushConfigDialog brushConfigDialog;
    private float brushSize = 50.0f;
    private LinearLayout brushView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CustomPaintView custom_paint_view;
    
    public EraserConfigDialog eraserConfigDialog;
    private float eraserSize = 50.0f;
    private LinearLayout eraserView;
    private String imagePath;
    private ImageView img_close;
    private ImageView img_save;
    
    public ImageViewTouch img_set_sticker;
    
    public boolean isEraser = false;
    private ProgressDialog loadingDialog;
    
    public Bitmap originalBitmap;
    private String outputPath;
    private ImageView settings;

    static void lambda$savePaintImage$4(Throwable th) throws Exception {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(1);
        }
        setContentView((int) R.layout.activity_paint);


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
        BrushConfigDialog brushConfigDialog2 = new BrushConfigDialog();
        this.brushConfigDialog = brushConfigDialog2;
        brushConfigDialog2.setPropertiesChangeListener(this);
        EraserConfigDialog eraserConfigDialog2 = new EraserConfigDialog();
        this.eraserConfigDialog = eraserConfigDialog2;
        eraserConfigDialog2.setPropertiesChangeListener(this);
        this.img_close = (ImageView) findViewById(R.id.img_close);
        this.img_save = (ImageView) findViewById(R.id.img_save);
        this.settings = (ImageView) findViewById(R.id.settings);
        this.img_set_sticker = (ImageViewTouch) findViewById(R.id.img_set_sticker);
        this.custom_paint_view = (CustomPaintView) findViewById(R.id.custom_paint_view);
        this.img_set_sticker.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        Glide.with((FragmentActivity) this).asBitmap().load(this.imagePath).apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true)).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(new CustomTarget<Bitmap>() {
            public void onLoadCleared(Drawable drawable) {
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                PaintActivity.this.img_set_sticker.setImageBitmap(bitmap);
                Bitmap unused = PaintActivity.this.originalBitmap = bitmap;
            }
        });
        this.eraserView = (LinearLayout) findViewById(R.id.eraser_btn);
        this.brushView = (LinearLayout) findViewById(R.id.brush_btn);
        this.custom_paint_view.setWidth(50.0f);
        int i = -1;
        this.custom_paint_view.setColor(-1);
        this.custom_paint_view.setStrokeAlpha(MAX_ALPHA);
        this.custom_paint_view.setEraserStrokeWidth(50.0f);
        ((TextView) this.brushView.findViewById(R.id.txt_brush)).setTextColor(-1);
        ((ImageView) this.eraserView.findViewById(R.id.eraser_icon)).setImageResource(this.isEraser ? R.drawable.ic_eraser_enabled : R.drawable.ic_eraser_disabled);
        ((ImageView) this.brushView.findViewById(R.id.brush_icon)).setImageResource(this.isEraser ? R.drawable.ic_brush_white_24dp : R.drawable.ic_brush_grey_24dp);
        ((TextView) this.eraserView.findViewById(R.id.txt_eraser)).setTextColor(this.isEraser ? ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) : -1);
        TextView textView = (TextView) this.brushView.findViewById(R.id.txt_brush);
        if (!this.isEraser) {
            i = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        }
        textView.setTextColor(i);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.setCancelable(false);
        this.loadingDialog.setMessage("Saving");
    }

    private void click() {
        this.img_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PaintActivity.this.onBackPressed();
            }
        });
        this.img_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(PaintActivity.this.TAG, "onClick: save");
                PaintActivity.this.savePaintImage();
            }
        });
        this.settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PaintActivity paintActivity = PaintActivity.this;
                paintActivity.showDialog(paintActivity.isEraser ? PaintActivity.this.eraserConfigDialog : PaintActivity.this.brushConfigDialog);
            }
        });
        this.brushView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (PaintActivity.this.isEraser) {
                    PaintActivity.this.toggleButtons();
                }
            }
        });
        this.eraserView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!PaintActivity.this.isEraser) {
                    PaintActivity.this.toggleButtons();
                }
            }
        });
    }

    
    public void toggleButtons() {
        this.isEraser = !this.isEraser;
        String str = this.TAG;
        Log.d(str, "toggleButtons: " + this.isEraser);
        this.custom_paint_view.setEraser(this.isEraser);
        ((ImageView) this.eraserView.findViewById(R.id.eraser_icon)).setImageResource(this.isEraser ? R.drawable.ic_eraser_enabled : R.drawable.ic_eraser_disabled);
        ((ImageView) this.brushView.findViewById(R.id.brush_icon)).setImageResource(this.isEraser ? R.drawable.ic_brush_white_24dp : R.drawable.ic_brush_grey_24dp);
        int i = -1;
        ((TextView) this.eraserView.findViewById(R.id.txt_eraser)).setTextColor(this.isEraser ? ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) : -1);
        TextView textView = (TextView) this.brushView.findViewById(R.id.txt_brush);
        if (!this.isEraser) {
            i = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        }
        textView.setTextColor(i);
    }

    
    public void showDialog(BottomSheetDialogFragment bottomSheetDialogFragment) {
        String tag = bottomSheetDialogFragment.getTag();
        if (!bottomSheetDialogFragment.isAdded()) {
            bottomSheetDialogFragment.show(getSupportFragmentManager(), tag);
            if (this.isEraser) {
                updateEraserSize();
            } else {
                updateBrushParams();
            }
        }
    }

    private void updateBrushParams() {
        this.custom_paint_view.setColor(this.brushColor);
        this.custom_paint_view.setWidth(this.brushSize);
        this.custom_paint_view.setStrokeAlpha(this.brushAlpha);
    }

    private void updateEraserSize() {
        this.custom_paint_view.setEraserStrokeWidth(this.eraserSize);
    }

    public void onColorChanged(int i) {
        this.brushColor = i;
        updateBrushParams();
    }

    public void onOpacityChanged(int i) {
        this.brushAlpha = (((float) i) / MAX_PERCENT) * MAX_ALPHA;
        updateBrushParams();
    }

    public void onBrushSizeChanged(int i) {
        if (this.isEraser) {
            this.eraserSize = (float) i;
            updateEraserSize();
            return;
        }
        this.brushSize = (float) i;
        updateBrushParams();
    }

    public void savePaintImage() {
        this.compositeDisposable.clear();
        this.compositeDisposable.add(applyPaint(this.originalBitmap).flatMap(C$$Lambda$PaintActivity$FSfqM6nDnQhDZnnPQXqXBd_SsM.INSTANCE).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer() {
            public final void accept(Object obj) throws Exception {
                PaintActivity.this.lambda$savePaintImage$1$PaintActivity((Disposable) obj);
            }
        }).doFinally(new Action() {
            public final void run() throws Exception {
                PaintActivity.this.lambda$savePaintImage$2$PaintActivity();
            }
        }).subscribe(new Consumer() {
            public final void accept(Object obj) throws Exception {
                PaintActivity.this.lambda$savePaintImage$3$PaintActivity((Bitmap) obj);
            }
        }, C$$Lambda$PaintActivity$LLbipzlmIqrnMQI1XmPC9ceFro.INSTANCE));
    }

    static SingleSource lambda$savePaintImage$0(Bitmap bitmap) throws Exception {
        if (bitmap == null) {
            return Single.error(new Throwable("Error occurred while applying paint"));
        }
        return Single.just(bitmap);
    }

    public void lambda$savePaintImage$1$PaintActivity(Disposable disposable) throws Exception {
        this.loadingDialog.show();
    }

    public void lambda$savePaintImage$2$PaintActivity() throws Exception {
        this.loadingDialog.dismiss();
    }

    public void lambda$savePaintImage$3$PaintActivity(Bitmap bitmap) throws Exception {
        this.custom_paint_view.reset();
        try {
            File file = new File(this.outputPath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Intent intent = new Intent();
            intent.putExtra("cropPath", file.getAbsolutePath());
            setResult(-1, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        this.compositeDisposable.dispose();
        super.onDestroy();
    }

    private Single<Bitmap> applyPaint(final Bitmap bitmap) {
        return Single.fromCallable(new Callable() {
            public final Object call() throws Exception {
                return PaintActivity.this.lambda$applyPaint$5$PaintActivity(bitmap);
            }
        });
    }

    public Bitmap lambda$applyPaint$5$PaintActivity(Bitmap bitmap) throws Exception {
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
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        float f = fArr[0];
        float f2 = fArr[4];
        canvas.save();
        canvas.translate((float) ((int) fArr[2]), (float) ((int) fArr[5]));
        canvas.scale(f, f2);
        if (this.custom_paint_view.getPaintBit() != null) {
            Paint paint = null;
            canvas.drawBitmap(this.custom_paint_view.getPaintBit(), 0.0f, 0.0f, (Paint) null);
        }
        canvas.restore();
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
