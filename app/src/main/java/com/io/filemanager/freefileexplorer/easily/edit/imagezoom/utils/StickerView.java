package com.io.filemanager.freefileexplorer.easily.edit.imagezoom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import com.io.filemanager.freefileexplorer.easily.edit.StickerItem;
import java.util.LinkedHashMap;

public class StickerView extends View {
    private static int STATUS_DELETE = 2;
    private static int STATUS_IDLE = 0;
    private static int STATUS_MOVE = 1;
    private static int STATUS_ROTATE = 3;
    private LinkedHashMap<Integer, StickerItem> bank = new LinkedHashMap<>();
    private Paint boxPaint = new Paint();
    public StickerItem currentItem;
    private int currentStatus;
    private int imageCount;
    private Context mContext;
    private float oldx;
    private float oldy;
    private Paint rectPaint = new Paint();

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.currentStatus = STATUS_IDLE;
        this.rectPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.rectPaint.setAlpha(100);
    }

    public void addBitImage(Bitmap bitmap) {
        StickerItem stickerItem = new StickerItem(getContext());
        stickerItem.init(bitmap, this);
        StickerItem stickerItem2 = this.currentItem;
        if (stickerItem2 != null) {
            stickerItem2.isDrawHelpTool = false;
        }
        LinkedHashMap<Integer, StickerItem> linkedHashMap = this.bank;
        int i = this.imageCount + 1;
        this.imageCount = i;
        linkedHashMap.put(Integer.valueOf(i), stickerItem);
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Integer num : this.bank.keySet()) {
            this.bank.get(num).draw(canvas);
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        StickerItem stickerItem;
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int i = action & 255;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    int i2 = this.currentStatus;
                    if (i2 == STATUS_MOVE) {
                        float f = x - this.oldx;
                        float f2 = y - this.oldy;
                        StickerItem stickerItem2 = this.currentItem;
                        if (stickerItem2 != null) {
                            stickerItem2.updatePos(f, f2);
                            invalidate();
                        }
                        this.oldx = x;
                        this.oldy = y;
                    } else if (i2 == STATUS_ROTATE) {
                        float f3 = this.oldx;
                        float f4 = x - f3;
                        float f5 = this.oldy;
                        float f6 = y - f5;
                        StickerItem stickerItem3 = this.currentItem;
                        if (stickerItem3 != null) {
                            stickerItem3.updateRotateAndScale(f3, f5, f4, f6);
                            invalidate();
                        }
                        this.oldx = x;
                        this.oldy = y;
                    }
                    return true;
                } else if (i != 3) {
                    return onTouchEvent;
                }
            }
            this.currentStatus = STATUS_IDLE;
            return false;
        }
        int i3 = -1;
        for (Integer next : this.bank.keySet()) {
            StickerItem stickerItem4 = this.bank.get(next);
            if (stickerItem4.detectDeleteRect.contains(x, y)) {
                i3 = next.intValue();
                this.currentStatus = STATUS_DELETE;
            } else {
                if (stickerItem4.detectRotateRect.contains(x, y)) {
                    StickerItem stickerItem5 = this.currentItem;
                    if (stickerItem5 != null) {
                        stickerItem5.isDrawHelpTool = false;
                    }
                    this.currentItem = stickerItem4;
                    stickerItem4.isDrawHelpTool = true;
                    this.currentStatus = STATUS_ROTATE;
                    this.oldx = x;
                    this.oldy = y;
                } else if (stickerItem4.dstRect.contains(x, y)) {
                    StickerItem stickerItem6 = this.currentItem;
                    if (stickerItem6 != null) {
                        stickerItem6.isDrawHelpTool = false;
                    }
                    this.currentItem = stickerItem4;
                    stickerItem4.isDrawHelpTool = true;
                    this.currentStatus = STATUS_MOVE;
                    this.oldx = x;
                    this.oldy = y;
                }
                onTouchEvent = true;
            }
        }
        if (!onTouchEvent && (stickerItem = this.currentItem) != null && this.currentStatus == STATUS_IDLE) {
            stickerItem.isDrawHelpTool = false;
            this.currentItem = null;
            invalidate();
        }
        if (i3 > 0 && this.currentStatus == STATUS_DELETE) {
            this.bank.remove(Integer.valueOf(i3));
            this.currentStatus = STATUS_IDLE;
            invalidate();
        }
        return onTouchEvent;
    }

    public LinkedHashMap<Integer, StickerItem> getBank() {
        return this.bank;
    }

    public void clear() {
        this.bank.clear();
        invalidate();
    }
}
