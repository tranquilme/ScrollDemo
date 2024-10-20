package com.example.androidstudy.mynestedwebview;

import static com.example.androidstudy.utils.ConstModel.TAG;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.webkit.WebView;
import android.widget.OverScroller;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.example.androidstudy.utils.ConstModel;
import com.example.androidstudy.utils.Utils;

public class MyWebview extends WebView implements NestedScrollingChild2 {

    private NestedScrollingChildHelper nestedScrollingChildHelper;
    private final int[] consumed = new int[2];
    private final int[] mNestedOffsets = new int[2];
    private final int[] mScrollOffset = new int[2];
    private float lastY;
    private Scroller scroller;
    private VelocityTracker velocityTracker;
    private int mLastFlingY;

    public MyWebview(@NonNull Context context) {
        this(context, null);
    }

    public MyWebview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWebview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    // ==============================================================================================
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MotionEvent recordEvent = MotionEvent.obtain(event);
        recordEvent.offsetLocation(mNestedOffsets[0], mNestedOffsets[1]);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nestedScrollingChildHelper.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                lastY = event.getRawY();

                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }

                initOrResetVelocityTracker();

                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (lastY - event.getRawY());
                lastY = event.getRawY();
                velocityTracker.addMovement(recordEvent);
                if (!nestedScrollingChildHelper.dispatchNestedPreScroll(0, dy, consumed, mScrollOffset)) {
                    scrollBy(0, dy);
                } else {
                    mNestedOffsets[0] += mScrollOffset[0];
                    mNestedOffsets[1] += mScrollOffset[1];
                }

                if (Math.abs(dy) > 5) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }

                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, Integer.MAX_VALUE);
                int velocityY = (int) -velocityTracker.getYVelocity();
                Log.d(TAG, "velocityTrackervelocityY: " + velocityY);
                recycleVelocityTracker();

                flingScroll(0, velocityY);
                invalidate();
                break;

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void flingScroll(int vx, int vy) {
        mLastFlingY = 0;
        scroller.fling(0, 0, 0, vy, 0, Integer.MAX_VALUE, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int dy = scroller.getCurrY() - mLastFlingY;
            mLastFlingY = scroller.getCurrY();
            consumed[0] = 0;
            consumed[1] = 0;
            if (dy != 0 && !nestedScrollingChildHelper.dispatchNestedPreScroll(0, dy, consumed, null)) {
                scrollBy(0, dy);
            }
            // 马达启动
            invalidate();
        }
    }

    // ==============================================================================================
    public boolean isScrollToBottom() {
        float contentHeight = getContentHeight() * getScale();
        return getScrollY() >= contentHeight - getHeight();
    }

    public boolean canScroll() {
        return canScrollVertically(-1) || canScrollVertically(1);
    }

    public boolean isScrollToTop() {
        return getScrollY() == 0;
    }

    public void stopScroll() {
        scroller.abortAnimation();
    }

    @Override
    public void scrollBy(int x, int y) {
        int newY = getScrollY() + y;
        if (newY < 0) {
            newY = 0;
        } else if (newY > getMaxScrollDistance()) {
            newY = getMaxScrollDistance();
        }

        scrollTo(x, newY);
    }

    private int getMaxScrollDistance() {
        return (int) (getContentHeight() * getScale() - getHeight());
    }

    private void init() {
        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        nestedScrollingChildHelper.setNestedScrollingEnabled(true);
        scroller = new Scroller(getContext());
        velocityTracker = VelocityTracker.obtain();
    }

    private void initOrResetVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return false;
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return false;
    }

    @Override
    public void stopNestedScroll(int type) {

    }
}
