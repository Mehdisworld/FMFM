package com.io.filemanager.freefileexplorer.easily.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import androidx.core.os.ParcelableCompat;
import androidx.core.os.ParcelableCompatCreatorCallbacks;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.widget.EdgeEffectCompat;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomViewPager extends ViewGroup {
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
            return itemInfo.position - itemInfo2.position;
        }
    };
    private static final boolean DEBUG = false;
    private static final int DEFAULT_OFFSCREEN_PAGES = 0;
    private static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * f2 * f2) + 1.0f;
        }
    };
    private int mActivePointerId = -1;
    private PagerAdapter mAdapter;
    private float mBaseLineFlingVelocity;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCurItem;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFlingVelocityInfluence;
    private boolean mInLayout;
    private float mInitialMotionX;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList<>();
    private float mLastMotionX;
    private float mLastMotionY;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mOffscreenPageLimit = 0;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrolling;
    private boolean mScrollingCacheEnabled;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
        }
    }

    public static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;

        ItemInfo() {
        }
    }

    public CustomViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public CustomViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViewPager();
    }

    public void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        this.mBaseLineFlingVelocity = context.getResources().getDisplayMetrics().density * 2500.0f;
        this.mFlingVelocityInfluence = 0.4f;
    }

    private void setScrollState(int i) {
        if (this.mScrollState != i) {
            this.mScrollState = i;
            OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(i);
            }
        }
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        PagerAdapter pagerAdapter2 = this.mAdapter;
        if (pagerAdapter2 != null) {
            pagerAdapter2.startUpdate((ViewGroup) this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo itemInfo = this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeAllViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        this.mAdapter = pagerAdapter;
        if (pagerAdapter != null) {
            this.mPopulatePending = false;
            if (this.mRestoredCurItem >= 0) {
                pagerAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
                return;
            }
            populate();
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setCurrentItem(int i) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int i, boolean z) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, z, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public void setCurrentItemInternal(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2, 0);
    }

    public void setCurrentItemInternal(int i, boolean z, boolean z2, int i2) {
        OnPageChangeListener onPageChangeListener;
        OnPageChangeListener onPageChangeListener2;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (z2 || this.mCurItem != i || this.mItems.size() == 0) {
            boolean z3 = true;
            if (i < 0) {
                i = 0;
            } else if (i >= this.mAdapter.getCount()) {
                i = this.mAdapter.getCount() - 1;
            }
            int i3 = this.mOffscreenPageLimit;
            int i4 = this.mCurItem;
            if (i > i4 + i3 || i < i4 - i3) {
                for (int i5 = 0; i5 < this.mItems.size(); i5++) {
                    this.mItems.get(i5).scrolling = true;
                }
            }
            if (this.mCurItem == i) {
                z3 = false;
            }
            this.mCurItem = i;
            populate();
            int width = (getWidth() + this.mPageMargin) * i;
            if (z) {
                smoothScrollTo(width, 0, i2);
                if (z3 && (onPageChangeListener2 = this.mOnPageChangeListener) != null) {
                    onPageChangeListener2.onPageSelected(i);
                    return;
                }
                return;
            }
            if (z3 && (onPageChangeListener = this.mOnPageChangeListener) != null) {
                onPageChangeListener.onPageSelected(i);
            }
            completeScroll();
            scrollTo(width, 0);
        } else {
            setScrollingCacheEnabled(false);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int i) {
        if (i < 0) {
            Log.w(TAG, "Requested offscreen page limit " + i + " too small; defaulting to " + 0);
            i = 0;
        }
        if (i != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = i;
            populate();
        }
    }

    public void setPageMargin(int i) {
        int i2 = this.mPageMargin;
        this.mPageMargin = i;
        int width = getWidth();
        recomputeScrollPosition(width, width, i, i2);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable drawable) {
        this.mMarginDrawable = drawable;
        if (drawable != null) {
            refreshDrawableState();
        }
        setWillNotDraw(drawable == null);
        invalidate();
    }

    public void setPageMarginDrawable(int i) {
        setPageMarginDrawable(getContext().getResources().getDrawable(i));
    }

    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }

    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mMarginDrawable;
        if (drawable != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
    }

    public float distanceInfluenceForSnapDuration(float f) {
        double d = (double) (f - 0.5f);
        Double.isNaN(d);
        Double.isNaN(d);
        return (float) Math.sin((double) ((float) (d * 0.4712389167638204d)));
    }

    public void smoothScrollTo(int i, int i2) {
        smoothScrollTo(i, i2, 0);
    }

    public void smoothScrollTo(int i, int i2, int i3) {
        int i4;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int i5 = i - scrollX;
        int i6 = i2 - scrollY;
        if (i5 == 0 && i6 == 0) {
            completeScroll();
            setScrollState(0);
            return;
        }
        setScrollingCacheEnabled(true);
        this.mScrolling = true;
        setScrollState(2);
        int abs = (int) ((((float) Math.abs(i5)) / ((float) (getWidth() + this.mPageMargin))) * 100.0f);
        int abs2 = Math.abs(i3);
        if (abs2 > 0) {
            float f = (float) abs;
            i4 = (int) (f + ((f / (((float) abs2) / this.mBaseLineFlingVelocity)) * this.mFlingVelocityInfluence));
        } else {
            i4 = abs + 100;
        }
        this.mScroller.startScroll(scrollX, scrollY, i5, i6, Math.min(i4, 600));
        invalidate();
    }

    public void addNewItem(int i, int i2) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = i;
        itemInfo.object = this.mAdapter.instantiateItem((ViewGroup) this, i);
        if (i2 < 0) {
            this.mItems.add(itemInfo);
        } else {
            this.mItems.add(i2, itemInfo);
        }
    }

    public void dataSetChanged() {
        boolean z = true;
        boolean z2 = this.mItems.size() < 3 && this.mItems.size() < this.mAdapter.getCount();
        int i = 0;
        int i2 = -1;
        while (i < this.mItems.size()) {
            ItemInfo itemInfo = this.mItems.get(i);
            int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            if (itemPosition != -1) {
                if (itemPosition == -2) {
                    this.mItems.remove(i);
                    i--;
                    this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
                    if (this.mCurItem == itemInfo.position) {
                        i2 = Math.max(0, Math.min(this.mCurItem, this.mAdapter.getCount() - 1));
                    }
                } else if (itemInfo.position != itemPosition) {
                    if (itemInfo.position == this.mCurItem) {
                        i2 = itemPosition;
                    }
                    itemInfo.position = itemPosition;
                }
                z2 = true;
            }
            i++;
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (i2 >= 0) {
            setCurrentItemInternal(i2, false, true);
        } else {
            z = z2;
        }
        if (z) {
            populate();
            requestLayout();
        }
    }

    public void populate() {
        int i;
        ItemInfo itemInfo;
        ItemInfo itemInfo2;
        if (this.mAdapter != null && !this.mPopulatePending && getWindowToken() != null) {
            this.mAdapter.startUpdate((ViewGroup) this);
            int i2 = this.mOffscreenPageLimit;
            int i3 = 0;
            int max = Math.max(0, this.mCurItem - i2);
            int min = Math.min(this.mAdapter.getCount() - 1, this.mCurItem + i2);
            int i4 = 0;
            int i5 = -1;
            while (i4 < this.mItems.size()) {
                ItemInfo itemInfo3 = this.mItems.get(i4);
                if ((itemInfo3.position < max || itemInfo3.position > min) && !itemInfo3.scrolling) {
                    this.mItems.remove(i4);
                    i4--;
                    this.mAdapter.destroyItem((ViewGroup) this, itemInfo3.position, itemInfo3.object);
                } else if (i5 < min && itemInfo3.position > max) {
                    int i6 = i5 + 1;
                    if (i6 < max) {
                        i6 = max;
                    }
                    while (i6 <= min && i6 < itemInfo3.position) {
                        addNewItem(i6, i4);
                        i6++;
                        i4++;
                    }
                }
                i5 = itemInfo3.position;
                i4++;
            }
            if (this.mItems.size() > 0) {
                ArrayList<ItemInfo> arrayList = this.mItems;
                i = arrayList.get(arrayList.size() - 1).position;
            } else {
                i = -1;
            }
            if (i < min) {
                int i7 = i + 1;
                if (i7 > max) {
                    max = i7;
                }
                while (max <= min) {
                    addNewItem(max, -1);
                    max++;
                }
            }
            int i8 = 0;
            while (true) {
                itemInfo = null;
                if (i8 >= this.mItems.size()) {
                    itemInfo2 = null;
                    break;
                } else if (this.mItems.get(i8).position == this.mCurItem) {
                    itemInfo2 = this.mItems.get(i8);
                    break;
                } else {
                    i8++;
                }
            }
            this.mAdapter.setPrimaryItem((ViewGroup) this, this.mCurItem, itemInfo2 != null ? itemInfo2.object : null);
            this.mAdapter.finishUpdate((ViewGroup) this);
            if (hasFocus()) {
                View findFocus = findFocus();
                if (findFocus != null) {
                    itemInfo = infoForAnyChild(findFocus);
                }
                if (itemInfo == null || itemInfo.position != this.mCurItem) {
                    while (i3 < getChildCount()) {
                        View childAt = getChildAt(i3);
                        ItemInfo infoForChild = infoForChild(childAt);
                        if (infoForChild == null || infoForChild.position != this.mCurItem || !childAt.requestFocus(2)) {
                            i3++;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        });
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, i);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel);
            classLoader = classLoader == null ? getClass().getClassLoader() : classLoader;
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader);
            this.loader = classLoader;
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            savedState.adapterState = pagerAdapter.saveState();
        }
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.restoreState(savedState.adapterState, savedState.loader);
            setCurrentItemInternal(savedState.position, false, true);
            return;
        }
        this.mRestoredCurItem = savedState.position;
        this.mRestoredAdapterState = savedState.adapterState;
        this.mRestoredClassLoader = savedState.loader;
    }

    public void addView(View view, int i, LayoutParams layoutParams) {
        if (this.mInLayout) {
            addViewInLayout(view, i, layoutParams);
            view.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
            return;
        }
        super.addView(view, i, layoutParams);
    }

    public ItemInfo infoForChild(View view) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(view, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }

    public ItemInfo infoForAnyChild(View view) {
        while (true) {
            ViewParent parent = view.getParent();
            if (parent == this) {
                return infoForChild(view);
            }
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            view = (View) parent;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    public void onMeasure(int i, int i2) {
        setMeasuredDimension(getDefaultSize(0, i), getDefaultSize(0, i2));
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), 0);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), 0);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                childAt.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
            }
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3) {
            int i5 = this.mPageMargin;
            recomputeScrollPosition(i, i3, i5, i5);
        }
    }

    private void recomputeScrollPosition(int i, int i2, int i3, int i4) {
        int i5 = i + i3;
        if (i2 > 0) {
            int scrollX = getScrollX();
            int i6 = i2 + i4;
            int i7 = (int) ((((float) (scrollX / i6)) + (((float) (scrollX % i6)) / ((float) i6))) * ((float) i5));
            scrollTo(i7, getScrollY());
            if (!this.mScroller.isFinished()) {
                Scroller scroller = this.mScroller;
                scroller.startScroll(i7, 0, this.mCurItem * i5, 0, scroller.getDuration() - this.mScroller.timePassed());
                return;
            }
            return;
        }
        int i8 = this.mCurItem * i5;
        if (i8 != getScrollX()) {
            completeScroll();
            scrollTo(i8, getScrollY());
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        ItemInfo infoForChild;
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int childCount = getChildCount();
        int i5 = i3 - i;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (!(childAt.getVisibility() == 8 || (infoForChild = infoForChild(childAt)) == null)) {
                int paddingLeft = getPaddingLeft() + ((this.mPageMargin + i5) * infoForChild.position);
                int paddingTop = getPaddingTop();
                childAt.layout(paddingLeft, paddingTop, childAt.getMeasuredWidth() + paddingLeft, childAt.getMeasuredHeight() + paddingTop);
            }
        }
        this.mFirstLayout = false;
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll();
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int currX = this.mScroller.getCurrX();
        int currY = this.mScroller.getCurrY();
        if (!(scrollX == currX && scrollY == currY)) {
            scrollTo(currX, currY);
        }
        if (this.mOnPageChangeListener != null) {
            int width = getWidth() + this.mPageMargin;
            int i = currX / width;
            int i2 = currX % width;
            this.mOnPageChangeListener.onPageScrolled(i, ((float) i2) / ((float) width), i2);
        }
        invalidate();
    }

    private void completeScroll() {
        boolean z = this.mScrolling;
        if (z) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (!(scrollX == currX && scrollY == currY)) {
                scrollTo(currX, currY);
            }
            setScrollState(0);
        }
        this.mPopulatePending = false;
        this.mScrolling = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.scrolling) {
                itemInfo.scrolling = false;
                z = true;
            }
        }
        if (z) {
            populate();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        PagerAdapter pagerAdapter;
        int action = motionEvent.getAction() & 255;
        if (action == 3 || action == 1) {
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return false;
            }
        }
        if (action == 0) {
            float x = motionEvent.getX();
            this.mInitialMotionX = x;
            this.mLastMotionX = x;
            this.mLastMotionY = motionEvent.getY();
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
            if (this.mScrollState == 2) {
                this.mIsBeingDragged = true;
                this.mIsUnableToDrag = false;
                setScrollState(1);
            } else {
                completeScroll();
                this.mIsBeingDragged = false;
                this.mIsUnableToDrag = false;
            }
        } else if (action == 2) {
            int i = this.mActivePointerId;
            if (i != -1) {
                int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, i);
                float x2 = MotionEventCompat.getX(motionEvent, findPointerIndex);
                float f = x2 - this.mLastMotionX;
                float abs = Math.abs(f);
                float y = MotionEventCompat.getY(motionEvent, findPointerIndex);
                float abs2 = Math.abs(y - this.mLastMotionY);
                int scrollX = getScrollX();
                if ((f <= 0.0f || scrollX != 0) && f < 0.0f && (pagerAdapter = this.mAdapter) != null) {
                    pagerAdapter.getCount();
                    getWidth();
                }
                if (canScroll(this, false, (int) f, (int) x2, (int) y)) {
                    this.mLastMotionX = x2;
                    this.mInitialMotionX = x2;
                    this.mLastMotionY = y;
                    return false;
                }
                float f2 = (float) this.mTouchSlop;
                if (abs > f2 && abs > abs2) {
                    this.mIsBeingDragged = true;
                    setScrollState(1);
                    this.mLastMotionX = x2;
                    setScrollingCacheEnabled(true);
                } else if (abs2 > f2) {
                    this.mIsUnableToDrag = true;
                }
            }
        } else if (action == 6) {
            onSecondaryPointerUp(motionEvent);
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        PagerAdapter pagerAdapter;
        boolean z;
        if (this.mFakeDragging) {
            return true;
        }
        boolean z2 = false;
        if ((motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) || (pagerAdapter = this.mAdapter) == null || pagerAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (!this.mIsBeingDragged) {
                        int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                        float x = MotionEventCompat.getX(motionEvent, findPointerIndex);
                        float abs = Math.abs(x - this.mLastMotionX);
                        float abs2 = Math.abs(MotionEventCompat.getY(motionEvent, findPointerIndex) - this.mLastMotionY);
                        if (abs > ((float) this.mTouchSlop) && abs > abs2) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionX = x;
                            setScrollState(1);
                            setScrollingCacheEnabled(true);
                        }
                    }
                    if (this.mIsBeingDragged) {
                        float x2 = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                        float f = this.mLastMotionX - x2;
                        this.mLastMotionX = x2;
                        float scrollX = ((float) getScrollX()) + f;
                        int width = getWidth();
                        int i = this.mPageMargin + width;
                        int count = this.mAdapter.getCount() - 1;
                        float max = (float) Math.max(0, (this.mCurItem - 1) * i);
                        float min = (float) (Math.min(this.mCurItem + 1, count) * i);
                        if (scrollX < max) {
                            if (max == 0.0f) {
                                this.mLeftEdge.onPull((-scrollX) / ((float) width));
                            }
                            scrollX = max;
                        } else if (scrollX > min) {
                            if (min == ((float) (count * i))) {
                                this.mRightEdge.onPull((scrollX - min) / ((float) width));
                            }
                            scrollX = min;
                        }
                        int i2 = (int) scrollX;
                        this.mLastMotionX += scrollX - ((float) i2);
                        scrollTo(i2, getScrollY());
                        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
                        if (onPageChangeListener != null) {
                            int i3 = i2 / i;
                            int i4 = i2 % i;
                            onPageChangeListener.onPageScrolled(i3, ((float) i4) / ((float) i), i4);
                        }
                    }
                } else if (action != 3) {
                    if (action == 5) {
                        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                        this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
                        this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                    } else if (action == 6) {
                        onSecondaryPointerUp(motionEvent);
                        this.mLastMotionX = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                    }
                } else if (this.mIsBeingDragged) {
                    setCurrentItemInternal(this.mCurItem, true, true);
                    this.mActivePointerId = -1;
                    endDrag();
                    z2 = this.mLeftEdge.onRelease();
                    z = this.mRightEdge.onRelease();
                    z2 |= z;
                }
            } else if (this.mIsBeingDragged) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int xVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                this.mPopulatePending = true;
                int scrollX2 = getScrollX() / (getWidth() + this.mPageMargin);
                if (xVelocity <= 0) {
                    scrollX2++;
                }
                setCurrentItemInternal(scrollX2, true, true, xVelocity);
                this.mActivePointerId = -1;
                endDrag();
                z2 = this.mLeftEdge.onRelease();
                z = this.mRightEdge.onRelease();
                z2 |= z;
            }
            z = false;
            z2 |= z;
        } else {
            completeScroll();
            float x3 = motionEvent.getX();
            this.mInitialMotionX = x3;
            this.mLastMotionX = x3;
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
        }
        if (z2) {
            invalidate();
        }
        return true;
    }

    public void draw(Canvas canvas) {
        PagerAdapter pagerAdapter;
        super.draw(canvas);
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        boolean z = false;
        int i = 1;
        if (overScrollMode == 0 || (overScrollMode == 1 && (pagerAdapter = this.mAdapter) != null && pagerAdapter.getCount() > 1)) {
            if (!this.mLeftEdge.isFinished()) {
                int save = canvas.save();
                int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(270.0f);
                canvas.translate((float) ((-height) + getPaddingTop()), 0.0f);
                this.mLeftEdge.setSize(height, getWidth());
                z = false | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(save);
            }
            if (!this.mRightEdge.isFinished()) {
                int save2 = canvas.save();
                int width = getWidth();
                int height2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
                PagerAdapter pagerAdapter2 = this.mAdapter;
                if (pagerAdapter2 != null) {
                    i = pagerAdapter2.getCount();
                }
                canvas.rotate(90.0f);
                int i2 = this.mPageMargin;
                canvas.translate((float) (-getPaddingTop()), (float) (((-i) * (width + i2)) + i2));
                this.mRightEdge.setSize(height2, width);
                z |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(save2);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (z) {
            invalidate();
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            int i = this.mPageMargin;
            int i2 = scrollX % (width + i);
            if (i2 != 0) {
                int i3 = (scrollX - i2) + width;
                this.mMarginDrawable.setBounds(i3, 0, i + i3, getHeight());
                this.mMarginDrawable.draw(canvas);
            }
        }
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
        this.mFakeDragBeginTime = uptimeMillis;
        return true;
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            this.mPopulatePending = true;
            if (Math.abs((int) VelocityTrackerCompat.getYVelocity(velocityTracker, this.mActivePointerId)) <= this.mMinimumVelocity && Math.abs(this.mInitialMotionX - this.mLastMotionX) < ((float) (getWidth() / 3))) {
                setCurrentItemInternal(this.mCurItem, true, true);
            } else if (this.mLastMotionX > this.mInitialMotionX) {
                setCurrentItemInternal(this.mCurItem - 1, true, true);
            } else {
                setCurrentItemInternal(this.mCurItem + 1, true, true);
            }
            endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public void fakeDragBy(float f) {
        if (this.mFakeDragging) {
            this.mLastMotionX += f;
            float scrollX = ((float) getScrollX()) - f;
            int width = getWidth() + this.mPageMargin;
            float max = (float) Math.max(0, (this.mCurItem - 1) * width);
            float min = (float) (Math.min(this.mCurItem + 1, this.mAdapter.getCount() - 1) * width);
            if (scrollX < max) {
                scrollX = max;
            } else if (scrollX > min) {
                scrollX = min;
            }
            int i = (int) scrollX;
            this.mLastMotionX += scrollX - ((float) i);
            scrollTo(i, getScrollY());
            OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
            if (onPageChangeListener != null) {
                int i2 = i / width;
                int i3 = i % width;
                onPageChangeListener.onPageScrolled(i2, ((float) i3) / ((float) width), i3);
            }
            MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
            this.mVelocityTracker.addMovement(obtain);
            obtain.recycle();
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, actionIndex) == this.mActivePointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(motionEvent, i);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean z) {
        if (this.mScrollingCacheEnabled != z) {
            this.mScrollingCacheEnabled = z;
        }
    }

    public boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        int i4;
        View view2 = view;
        if (view2 instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view2;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                int i5 = i2 + scrollX;
                if (i5 >= childAt.getLeft() && i5 < childAt.getRight() && (i4 = i3 + scrollY) >= childAt.getTop() && i4 < childAt.getBottom()) {
                    if (canScroll(childAt, true, i, i5 - childAt.getLeft(), i4 - childAt.getTop())) {
                        return true;
                    }
                }
            }
        }
        if (!z || !ViewCompat.canScrollHorizontally(view, -i)) {
            return false;
        }
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 21) {
            return arrowScroll(17);
        }
        if (keyCode == 22) {
            return arrowScroll(66);
        }
        if (keyCode != 61) {
            return false;
        }
        if (keyEvent.hasNoModifiers()) {
            return arrowScroll(2);
        }
        if (keyEvent.hasModifiers(1)) {
            return arrowScroll(1);
        }
        return false;
    }

    public boolean arrowScroll(int i) {
        boolean requestFocus = false;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        boolean z = false;
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i);
        if (findNextFocus != null && findNextFocus != findFocus) {
            if (i == 17) {
                requestFocus = (findFocus == null || findNextFocus.getLeft() < findFocus.getLeft()) ? findNextFocus.requestFocus() : pageLeft();
            } else if (i == 66) {
                requestFocus = (findFocus == null || findNextFocus.getLeft() > findFocus.getLeft()) ? findNextFocus.requestFocus() : pageRight();
            }
            z = requestFocus;
        } else if (i == 17 || i == 1) {
            z = pageLeft();
        } else if (i == 66 || i == 2) {
            z = pageRight();
        }
        if (z) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
        }
        return z;
    }

    public boolean pageLeft() {
        int i = this.mCurItem;
        if (i <= 0) {
            return false;
        }
        setCurrentItem(i - 1, true);
        return true;
    }

    public boolean pageRight() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || this.mCurItem >= pagerAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        ItemInfo infoForChild;
        int size = arrayList.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem) {
                    childAt.addFocusables(arrayList, i, i2);
                }
            }
        }
        if ((descendantFocusability == 262144 && size != arrayList.size()) || !isFocusable()) {
            return;
        }
        if (((i2 & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && arrayList != null) {
            arrayList.add(this);
        }
    }

    public void addTouchables(ArrayList<View> arrayList) {
        ItemInfo infoForChild;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem) {
                childAt.addTouchables(arrayList);
            }
        }
    }

    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        int i2;
        int i3;
        ItemInfo infoForChild;
        int childCount = getChildCount();
        int i4 = -1;
        if ((i & 2) != 0) {
            i4 = childCount;
            i3 = 0;
            i2 = 1;
        } else {
            i3 = childCount - 1;
            i2 = -1;
        }
        while (i3 != i4) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem && childAt.requestFocus(i, rect)) {
                return true;
            }
            i3 += i2;
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        ItemInfo infoForChild;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem && childAt.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                return true;
            }
        }
        return false;
    }
}
