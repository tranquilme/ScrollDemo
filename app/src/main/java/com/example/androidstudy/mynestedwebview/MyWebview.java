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
    private int[] consumed = new int[2];
    private int[] mNestedOffsets = new int[2];
    private final int[] mScrollOffset = new int[2];
    private float lastY;
    private Scroller scroller;
    private VelocityTracker velocityTracker;
    private boolean hasFling = false;

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
                hasFling = false;

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
        int startY = (int) (getContentHeight() * getScale() - getHeight());
        if (getScrollY() < startY) {
            startY = getScrollY();
        }
        scroller.fling(0, startY, 0, vy, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
            if (!hasFling && scroller.getStartY() < scroller.getCurrY() && isScrollToBottom()) {
                nestedScrollingChildHelper.dispatchNestedPreFling(0, scroller.getCurrVelocity());
                hasFling = true;
            }
        }
    }

    // ==============================================================================================

    public boolean isScrollToBottom() {
        float contentHeight = getContentHeight() * getScale();
        return getScrollY() >= contentHeight - getHeight() - 5;
    }

    @Override
    public void scrollBy(int x, int y) {
        int newY = getScrollY() + y;
        if (newY < 0) {
            newY = 0;
        }

        scrollTo(x, newY);
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
