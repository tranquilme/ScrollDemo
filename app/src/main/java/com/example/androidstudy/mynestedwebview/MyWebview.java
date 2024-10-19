package com.example.androidstudy.mynestedwebview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nestedScrollingChildHelper.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                lastY = event.getY();
                isDownClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getY() - lastY);
                lastY = event.getY();

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
                break;

        }

        return super.onTouchEvent(event);
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
