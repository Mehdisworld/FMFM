package com.io.filemanager.freefileexplorer.easily.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.edit.imagezoom.utils.RectUtil;

public class StickerItem {
    private static final int BORDER_STROKE_WIDTH = 8;
    private static final int BUTTON_WIDTH = 30;
    private static final int HELP_BOX_PAD = 25;
    private static final float MIN_SCALE = 0.15f;
    public static Bitmap deleteBit;
    public static Bitmap rotateBit;
    public Bitmap bitmap;
    public RectF deleteRect;
    public RectF detectDeleteRect;
    public RectF detectRotateRect;
    public RectF dstRect;
    private RectF helpBox;
    public Paint helpBoxPaint;
    public Rect helpToolsRect;
    private float initWidth;
    public boolean isDrawHelpTool = false;
    public Matrix matrix;
    public Paint paint = new Paint();
    public float roatetAngle = 0.0f;
    public RectF rotateRect;

    public StickerItem(Context context) {
        Paint paint2 = new Paint();
        this.helpBoxPaint = paint2;
        paint2.setColor(-1);
        this.helpBoxPaint.setStyle(Paint.Style.STROKE);
        this.helpBoxPaint.setAntiAlias(true);
        this.helpBoxPaint.setStrokeWidth(8.0f);
        Paint paint3 = new Paint();
        paint3.setColor(SupportMenu.CATEGORY_MASK);
        paint3.setAlpha(120);
        Paint paint4 = new Paint();
        paint4.setColor(-16711936);
        paint4.setAlpha(120);
        if (deleteBit == null) {
            deleteBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_close_edit);
        }
        if (rotateBit == null) {
            rotateBit = BitmapFactory.decodeResource(context.getResources(), 2131231008);
        }
    }

    public void init(Bitmap bitmap2, View view) {
        this.bitmap = bitmap2;
        int min = Math.min(bitmap2.getWidth(), view.getWidth() >> 1);
        int height = (bitmap2.getHeight() * min) / bitmap2.getWidth();
        int width = (view.getWidth() >> 1) - (min >> 1);
        int height2 = (view.getHeight() >> 1) - (height >> 1);
        this.dstRect = new RectF((float) width, (float) height2, (float) (width + min), (float) (height2 + height));
        Matrix matrix2 = new Matrix();
        this.matrix = matrix2;
        matrix2.postTranslate(this.dstRect.left, this.dstRect.top);
        this.matrix.postScale(((float) min) / ((float) bitmap2.getWidth()), ((float) height) / ((float) bitmap2.getHeight()), this.dstRect.left, this.dstRect.top);
        this.initWidth = this.dstRect.width();
        this.isDrawHelpTool = true;
        this.helpBox = new RectF(this.dstRect);
        updateHelpBoxRect();
        this.helpToolsRect = new Rect(0, 0, deleteBit.getWidth(), deleteBit.getHeight());
        this.deleteRect = new RectF(this.helpBox.left - 30.0f, this.helpBox.top - 30.0f, this.helpBox.left + 30.0f, this.helpBox.top + 30.0f);
        this.rotateRect = new RectF(this.helpBox.right - 30.0f, this.helpBox.bottom - 30.0f, this.helpBox.right + 30.0f, this.helpBox.bottom + 30.0f);
        this.detectRotateRect = new RectF(this.rotateRect);
        this.detectDeleteRect = new RectF(this.deleteRect);
    }

    public void updateHelpBoxRect() {
        this.helpBox.left -= 25.0f;
        this.helpBox.right += 25.0f;
        this.helpBox.top -= 25.0f;
        this.helpBox.bottom += 25.0f;
    }

    public void updatePos(float f, float f2) {
        this.matrix.postTranslate(f, f2);
        this.dstRect.offset(f, f2);
        this.helpBox.offset(f, f2);
        this.deleteRect.offset(f, f2);
        this.rotateRect.offset(f, f2);
        this.detectRotateRect.offset(f, f2);
        this.detectDeleteRect.offset(f, f2);
    }

    public void updateRotateAndScale(float f, float f2, float f3, float f4) {
        float centerX = this.dstRect.centerX();
        float centerY = this.dstRect.centerY();
        float centerX2 = this.detectRotateRect.centerX();
        float centerY2 = this.detectRotateRect.centerY();
        float f5 = f3 + centerX2;
        float f6 = f4 + centerY2;
        float f7 = centerX2 - centerX;
        float f8 = centerY2 - centerY;
        float f9 = f5 - centerX;
        float f10 = f6 - centerY;
        float sqrt = (float) Math.sqrt((double) ((f7 * f7) + (f8 * f8)));
        float sqrt2 = (float) Math.sqrt((double) ((f9 * f9) + (f10 * f10)));
        float f11 = sqrt2 / sqrt;
        if ((this.dstRect.width() * f11) / this.initWidth >= 0.15f) {
            this.matrix.postScale(f11, f11, this.dstRect.centerX(), this.dstRect.centerY());
            RectUtil.scaleRect(this.dstRect, f11);
            this.helpBox.set(this.dstRect);
            updateHelpBoxRect();
            this.rotateRect.offsetTo(this.helpBox.right - 30.0f, this.helpBox.bottom - 30.0f);
            this.deleteRect.offsetTo(this.helpBox.left - 30.0f, this.helpBox.top - 30.0f);
            this.detectRotateRect.offsetTo(this.helpBox.right - 30.0f, this.helpBox.bottom - 30.0f);
            this.detectDeleteRect.offsetTo(this.helpBox.left - 30.0f, this.helpBox.top - 30.0f);
            double d = (double) (((f7 * f9) + (f8 * f10)) / (sqrt * sqrt2));
            if (d <= 1.0d && d >= -1.0d) {
                float degrees = ((float) ((f7 * f10) - (f9 * f8) > 0.0f ? 1 : -1)) * ((float) Math.toDegrees(Math.acos(d)));
                this.roatetAngle += degrees;
                this.matrix.postRotate(degrees, this.dstRect.centerX(), this.dstRect.centerY());
                RectUtil.rotateRect(this.detectRotateRect, this.dstRect.centerX(), this.dstRect.centerY(), this.roatetAngle);
                RectUtil.rotateRect(this.detectDeleteRect, this.dstRect.centerX(), this.dstRect.centerY(), this.roatetAngle);
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, this.matrix, (Paint) null);
        if (this.isDrawHelpTool) {
            canvas.save();
            canvas.rotate(this.roatetAngle, this.helpBox.centerX(), this.helpBox.centerY());
            canvas.drawRoundRect(this.helpBox, 10.0f, 10.0f, this.helpBoxPaint);
            Paint paint2 = null;
            canvas.drawBitmap(deleteBit, this.helpToolsRect, this.deleteRect, (Paint) null);
            canvas.drawBitmap(rotateBit, this.helpToolsRect, this.rotateRect, (Paint) null);
            canvas.restore();
        }
    }
}
