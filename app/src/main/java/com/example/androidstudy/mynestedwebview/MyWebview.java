package com.example.androidstudy.mynestedwebview;

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
    private float lastY;
    private boolean isDownClick;
    private OverScroller scroller;
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

    private void init() {
        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        nestedScrollingChildHelper.setNestedScrollingEnabled(true);
        scroller = new OverScroller(getContext());
        velocityTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nestedScrollingChildHelper.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                lastY = event.getY();
                isDownClick = true;
                hasFling = false;

                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (lastY - event.getY());
                lastY = event.getY();
                velocityTracker.addMovement(event);

                nestedScrollingChildHelper.dispatchNestedPreScroll(0, dy, consumed, null);
                dy -= consumed[1];
                Log.d(ConstModel.TAG, "onTouchEvent: Webview scrollBy" + -dy + "scroll dy=" + getScrollY());

                scrollBy(0, -dy);

                if (Math.abs(dy) > 5) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }

                break;
            case MotionEvent.ACTION_UP:
                isDownClick = false;

                velocityTracker.computeCurrentVelocity(1000, Integer.MAX_VALUE);
                int velocityY = (int) -velocityTracker.getYVelocity();
                scroller.fling(0, getScrollY(), 0, velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                scroller.computeScrollOffset();
                Log.d(ConstModel.TAG, "onTouchEvent: ACTION_UP" + velocityY + " scrollY=" + getScrollY());
                invalidate();
                break;

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int dy = scroller.getCurrY() - getScrollY();
            int[] consumed = new int[2];

            scrollTo(0, scroller.getCurrY());
            invalidate();

            Log.d(ConstModel.TAG, "isScrollToBottom()" + isScrollToBottom() + "hasFling   " + hasFling);

            if (!hasFling && isScrollToBottom()) {
//                nestedScrollingChildHelper.dispatchNestedPreScroll(0, dy, consumed, null);

                Log.d(ConstModel.TAG, "computeScroll: scroller.getCurrVelocity()" + scroller.getCurrVelocity());

                nestedScrollingChildHelper.dispatchNestedPreFling(0, scroller.getCurrVelocity());
                hasFling = true;
            }
        }
    }

    public boolean isDownClick() {
        return isDownClick;
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return false;
    }

    @Override
    public void stopNestedScroll(int type) {

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

    public boolean isScrollToBottom() {
        float contentHeight = getContentHeight() * getScale();
        return getScrollY() >= contentHeight - Utils.dpToPx(getContext(), 400) - 5;
    }

    @Override
    public void scrollBy(int x, int y) {
        int newY = getScrollY() + y;
        if (newY < 0) {
            newY = 0;
        }

        scrollTo(x, newY);
    }
}
