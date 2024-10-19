package com.example.androidstudy.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.example.androidstudy.utils.Utils;

public class EventScrollView extends NestedScrollView {

    private double mLastY;

    public EventScrollView(Context context) {
        super(context);
    }

    public EventScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(target, dx, dy, consumed, type);

        if (dy > 0 && getScrollY() <= Utils.dpToPx(target.getContext(), 300)) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    private boolean isScrollToBottom() {
        return getHeight() + getScrollY() == getChildAt(0).getHeight();
    }

    private boolean isScrollToTop() {
        return getScrollY() == 0;
    }
}
