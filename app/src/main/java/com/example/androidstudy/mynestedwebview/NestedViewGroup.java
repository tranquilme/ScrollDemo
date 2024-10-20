package com.example.androidstudy.mynestedwebview;

import static com.example.androidstudy.utils.ConstModel.TAG;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudy.utils.ConstModel;

import java.lang.reflect.Method;

public class NestedViewGroup extends LinearLayout implements NestedScrollingParent2 {

    public NestedViewGroup(Context context) {
        this(context, null);
    }

    public NestedViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    //================================================================================================
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof MyWebview) {
            if (((MyWebview) target).isScrollToBottom()) {
                Log.d(TAG, "((MyWebview) target).isScrollToBottom(): " + dy);

                if (dy > 0) {
                    Log.d(TAG, "getScrollY() < getWebviewHeight() getY: " + getScrollY() + " web=" + getWebviewHeight());

                    if (getScrollY() < getWebviewHeight()) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    } else {
                        Log.d(TAG, "onNestedPreScroll: getRecyclerView().scrollBy = " + dy);

                        getRecyclerView().scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                } else if (dy < 0) {
                    Log.d(TAG, "onNestedPreScroll: if (dy < 0)" + getScrollY());
                    if (getScrollY() > 0) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                }
            }
        }

        if (target instanceof MyRecyclerView) {
            if (dy < 0) {
                if (getRecyclerView().isScrollToTop()) {
                    if (getScrollY() > 0) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    } else {
                        getWebview().scrollBy(0, dy);
                        consumed[1] = dy;
                    }

                }
            } else if (dy > 0) {
                if (getScrollY() < getWebviewHeight()) {
                    Log.d(TAG, "onNestedPreScroll: scrollBy" + dy);

                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
            }
        }
    }
    //================================================================================================

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 关闭View内部Scroll马达
            getRecyclerView().stopScroll();
            getWebview().stopScroll();
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void scrollBy(int x, int y) {
        int newY = getScrollY() + y;

        if (newY < 0) {
            newY = 0;
        } else if (newY > getWebviewHeight()) {
            newY = getWebviewHeight();
        }

        Log.d(TAG, "scrollBy: scrollBy" + newY);

        scrollTo(x, newY);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y > getWebviewHeight()) {
            y = getWebviewHeight();
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    private int getWebviewHeight() {
        int height = 0;
        if (getChildAt(0) instanceof MyWebview) {
            height = getChildAt(0).getHeight();
        }

        return height;
    }

    private MyWebview getWebview() {
        return (MyWebview) getChildAt(0);
    }

    private MyRecyclerView getRecyclerView() {
        return (MyRecyclerView) getChildAt(1);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    private void init() {
    }

}
