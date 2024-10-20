package com.example.androidstudy.mynestedwebview;

import static com.example.androidstudy.utils.ConstModel.TAG;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;

public class NestedViewGroup extends LinearLayout implements NestedScrollingParent2 {

    public NestedViewGroup(Context context) {
        this(context, null);
    }

    public NestedViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //================================================================================================
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        if (target instanceof FirstRecyclerView) {
            if (getFloorRecyclerView().isScrollToBottom()) {
                if (dy > 0) {
                    if (getScrollY() < getFloorRecyclerView().getHeight()) {
                        int newDY = dy;
                        if (getScrollY() + dy > getFloorRecyclerView().getHeight()) {
                            newDY = getFloorRecyclerView().getHeight() - getScrollY();
                        }
                        scrollBy(0, newDY);
                        consumed[1] = newDY;
                    } else if (getScrollY() < getMaxScrollHeight()){
                        if (!getWebview().isScrollToBottom()) {
                            getWebview().scrollBy(0, dy);
                        } else {
                            scrollBy(0, dy);
                        }
                        consumed[1] = dy;
                    } else {
                        getRecommendRecyclerView().scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                } else if (dy < 0) {
                    if (getScrollY() > 0) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                }
            }

        }

        if (target instanceof MyWebview) {
            // Webview上滑有三种情况 影响因素【scrollY】【Webview是否滑动到边界】
            // 1.滑动Parent》[内容滑动到底部] or [scrollY < FloorHeight]
            // 2.滑动自身》scrollY == FloorHeight
            // 3.滑动Recommend（Fling）》scrollY == FloorHeight + WebviewHeight

            if (dy > 0) {
                // 滑动Parent
                Log.d(TAG, "(((MyWebview) target).isScrollToBottom(): " + (((MyWebview) target).isScrollToBottom()));
                if (((MyWebview) target).isScrollToBottom() || getScrollY() < getFloorRecyclerView().getHeight()) {
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == getFloorRecyclerView().getHeight()) {
                    // 滑动自身
                } else if (getScrollY() == (getFloorRecyclerView().getHeight() + getWebview().getHeight())) {
                    // 滑动Recommend
                    getRecommendRecyclerView().scrollBy(0, dy);
                    consumed[1] = dy;
                }
            } else if (dy < 0) {
                // Webview下滑有三种情况 影响因素【scrollY】【Webview是否滑动到边界】
                // 1.滑动Parent》[内容滑动到Top] or [FloorHeight < scrollY < FloorHeight + WebH]
                // 2.滑动自身》[scrollY == FloorH and 内容未滑动到Top]
                // 3.滑动Floor》scrollY == 0

                if (getScrollY() == 0) {
                    // 滑动Floor
                    getFloorRecyclerView().scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getWebview().isScrollToTop() ||
                        (getScrollY() > getFloorRecyclerView().getHeight() && getScrollY() < (getFloorRecyclerView().getHeight() + getWebview().getHeight()))) {
                    // 滑动Parent
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == getFloorRecyclerView().getHeight() && !getWebview().isScrollToTop()) {
                    // 滑动自身
                }
            }
        }

        if (target instanceof MyRecyclerView) {
            if (dy < 0) {
                if (getRecommendRecyclerView().isScrollToTop()) {
                    if (getScrollY() > 0) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    } else {
                        getWebview().scrollBy(0, dy);
                        consumed[1] = dy;
                    }

                }
            } else if (dy > 0) {
                if (getScrollY() < getWebview().getHeight()) {
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
            getRecommendRecyclerView().stopScroll();
            getWebview().stopScroll();
            getFloorRecyclerView().stopScroll();
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void scrollBy(int x, int y) {
        int newY = getScrollY() + y;

        if (newY < 0) {
            newY = 0;
        } else if (newY > getMaxScrollHeight()) {
            newY = getMaxScrollHeight();
        }

        Log.d(TAG, "newY > getMaxScrollHeight(): " + (newY > getMaxScrollHeight()));

        scrollTo(x, newY);
    }

    private int getMaxScrollHeight() {
        return getWebview().getHeight() + getFloorRecyclerView().getHeight();
    }

    private FirstRecyclerView getFloorRecyclerView() {
        return (FirstRecyclerView) getChildAt(0);
    }

    private MyWebview getWebview() {
        return (MyWebview) getChildAt(1);
    }

    private MyRecyclerView getRecommendRecyclerView() {
        return (MyRecyclerView) getChildAt(2);
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

}
