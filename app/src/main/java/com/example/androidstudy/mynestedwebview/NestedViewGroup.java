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
    private Scroller scroller;

    private static final int FLING_FLAG_RECYCLERVIEW_DOWN = 1;
    private static final int FLING_FLAG_WEBVIEW_UP = 2;
    private int flingFlag;
    private boolean hasFling;
    private VelocityTracker velocityTracker;

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

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof MyWebview) {
            if (((MyWebview) target).isScrollToBottom()) {

                if (dy > 0) {
                    if (getScrollY() < getWebviewHeight()) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    } else {
                        getRecyclerView().scrollBy(0, dy);

                        Log.d(TAG, "onNestedPreScroll: ");
                    }
                } else if (dy < 0) {
                    if (getScrollY() > 0) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                }
            }
        }

        if (target instanceof MyRecyclerView) {
            if (dy < 0) {
                if (getScrollY() > 0 && getRecyclerView().isScrollToTop()) {
                    Log.d(TAG, "onNestedPreScroll: isScrollToTop" + dy);

                    scrollBy(0, dy);
                    consumed[1] = dy;
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

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (target instanceof MyWebview) {
            if (velocityY > 0) {
                flingFlag = FLING_FLAG_WEBVIEW_UP;
                hasFling = false;
                parentFling((int) velocityY);
            }
        }

        if (target instanceof MyRecyclerView) {
            if (velocityY < 0) {
                Log.d(TAG, "FLING_FLAG_WEBVIEW_UP:velocityY " + velocityY);
                flingFlag = FLING_FLAG_RECYCLERVIEW_DOWN;
                hasFling = false;
                parentFling((int) velocityY);
            }
        }

//        return true;
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    public void parentFling(int vy) {
        scroller.fling(0, getScrollY(), 0,vy, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        scroller.computeScrollOffset();
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (scroller.computeScrollOffset()) {

            int curY = scroller.getCurrY();
            switch (flingFlag) {
                case FLING_FLAG_RECYCLERVIEW_DOWN:
                    if (getScrollY() > 0) {
                        invalidate();
                    } else if (!hasFling) {
                        hasFling = true;
                        getWebview().flingScroll(0, (int) -scroller.getCurrVelocity());
                    }
                    break;
                case FLING_FLAG_WEBVIEW_UP:
                    if (curY < getWebviewHeight()) {
                        scrollTo(0, scroller.getCurrY());
                        invalidate();
                    } else if (!hasFling) {
                        hasFling = true;
                        getRecyclerView().fling(0, (int) scroller.getCurrVelocity());
                    }
                    break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // RecyclerView Scroll马达即使在传递了Fling之后仍然会一直启动，需要手动关闭
            getRecyclerView().stopScroll();
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
        scroller = new Scroller(getContext());
    }

}
