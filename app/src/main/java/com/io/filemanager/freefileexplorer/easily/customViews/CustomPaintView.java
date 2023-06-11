package com.io.filemanager.freefileexplorer.easily.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.internal.view.SupportMenu;

public class CustomPaintView extends View {
    private boolean isEraser;
    private float last_x;
    private float last_y;
    private int mColor;
    private Bitmap mDrawBit;
    private Paint mEraserPaint;
    private Paint mPaint;
    private Canvas mPaintCanvas = null;

    public CustomPaintView(Context context) {
        super(context);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mDrawBit == null) {
            generatorBit();
        }
    }

    private void generatorBit() {
        this.mDrawBit = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        this.mPaintCanvas = new Canvas(this.mDrawBit);
    }

    private void init(Context context) {
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        Paint paint2 = new Paint();
        this.mEraserPaint = paint2;
        paint2.setAlpha(0);
        this.mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.mEraserPaint.setAntiAlias(true);
        this.mEraserPaint.setDither(true);
        this.mEraserPaint.setStyle(Paint.Style.STROKE);
        this.mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mEraserPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mEraserPaint.setStrokeWidth(40.0f);
    }

    public void setColor(int i) {
        this.mColor = i;
        this.mPaint.setColor(i);
    }

    public void setWidth(float f) {
        this.mPaint.setStrokeWidth(f);
    }

    public void setStrokeAlpha(float f) {
        this.mPaint.setAlpha((int) f);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = this.mDrawBit;
        if (bitmap != null) {
            Paint paint = null;
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.last_x = x;
            this.last_y = y;
            return true;
        } else if (action == 1) {
            return false;
        } else {
            if (action == 2) {
                this.mPaintCanvas.drawLine(this.last_x, this.last_y, x, y, this.isEraser ? this.mEraserPaint : this.mPaint);
                this.last_x = x;
                this.last_y = y;
                postInvalidate();
                return false;
            } else if (action != 3) {
                return onTouchEvent;
            } else {
                return false;
            }
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Bitmap bitmap = this.mDrawBit;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mDrawBit.recycle();
        }
    }

    public void setEraser(boolean z) {
        this.isEraser = z;
        this.mPaint.setColor(z ? 0 : this.mColor);
    }

    public void setEraserStrokeWidth(float f) {
        this.mEraserPaint.setStrokeWidth(f);
    }

    public Bitmap getPaintBit() {
        return this.mDrawBit;
    }

    public void reset() {
        Bitmap bitmap = this.mDrawBit;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mDrawBit.recycle();
        }
        generatorBit();
    }
}
