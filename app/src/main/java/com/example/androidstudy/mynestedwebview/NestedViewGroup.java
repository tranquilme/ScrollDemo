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
            // 在 Floor 上滑有三种情况
            // 1.滑动自身 =》canScrollUp
            // 2.滑动Parent
            // ----Floor 与 Webview 边界滑动 Parent =》[scrollY < FloorHeight && !Floor.canScrollUp]
            // ----WebView 与 Recommend 边界滑动 Parent =》[scrollY < maxScrollY && !WebView.canScrollUp]
            // 3.滑动同级View
            // ----滑动 WebView =》[scrollY == FloorHeight && WebView.canScrollUp]
            // ----滑动 Recommend =》[scrollY == maxScrollY && Recommend.canScrollUp]
            if (dy > 0) {
                if (getFloorRecyclerView().canScrollUp()) {
                    // 滑动自身
                } else if (!getFloorRecyclerView().canScrollUp() && getScrollY() < getFloorRecyclerView().getHeight()) {
                    // 滑动Parent【Floor 与 Webview 边界】

                    if (getScrollY() + dy > getFloorRecyclerView().getHeight()) {
                        dy = getFloorRecyclerView().getHeight() - getScrollY();
                    }

                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == getFloorRecyclerView().getHeight() && getWebview().canScrollUp()) {
                    // 滑动 WebView
                    getWebview().scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (!getWebview().canScrollUp() && getScrollY() < getMaxScrollHeight()) {
                    // 滑动 Parent

                    if (getScrollY() + dy > getMaxScrollHeight()) {
                        dy = getMaxScrollHeight() - getScrollY();
                    }

                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == getMaxScrollHeight() && getRecommendRecyclerView().canScrollUp()) {
                    getRecommendRecyclerView().scrollBy(0, dy);
                    consumed[1] = dy;
                }
            } else if (dy < 0) {
                // 下滑 Floor 有两种情况
                // 1.滑动自身 =》scrollY == 0
                // 2.滑动Parent =》scrollY > 0
                if (getScrollY() == 0) {
                    // 滑动自身
                } else if (getScrollY() > 0) {
                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
            }

        }

        if (target instanceof MyWebview) {
            // Webview上滑有三种情况 影响因素【scrollY】【Webview是否滑动到边界】
            // 1.滑动Parent》
            //   --在 Floor 与 WebView 之间滑动Parent=》scrollY < FloorHeight
            //   --在 WebView 与 Recommend 之前滑动Parent=》floorHeight <= scrollY < maxScrollHeight && !getWebview().canScrollUp()
            // 2.滑动自身》scrollY == FloorHeight && canScrollUp
            // 3.滑动Recommend（Fling）》scrollY == FloorHeight + WebviewHeight

            if (dy > 0) {
                // 滑动Parent[Floor 与 WebView边界]
                if (getScrollY() < getFloorRecyclerView().getHeight()) {

                    if (getScrollY() + dy > getFloorRecyclerView().getHeight()) {
                        dy = getFloorRecyclerView().getHeight() - getScrollY();
                    }

                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == getFloorRecyclerView().getHeight() && getWebview().canScrollUp()) {
                    // 滑动自身
                } else if ((getScrollY() >= getFloorRecyclerView().getHeight() && getScrollY() < getMaxScrollHeight()) && !getWebview().canScrollUp()) {
                    // 滑动Parent[Webview 与 Recommend边界]
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == (getFloorRecyclerView().getHeight() + getWebview().getHeight())) {
                    // 滑动Recommend
                    getRecommendRecyclerView().scrollBy(0, dy);
                    consumed[1] = dy;
                }
            } else if (dy < 0) {
                // Webview下滑有三种情况 影响因素【scrollY】【Webview是否滑动到边界】
                // 1.滑动Parent-》
                // ----Floor 与 WebView 边界 -》!webView.canScrollDown
                // ----WebView 与 Recommend 边界 -》[FloorHeight < scrollY]
                // 2.滑动自身-》[scrollY == FloorH && webView.canScrollDown]
                // 3.滑动Floor-》scrollY == 0
                if (getScrollY() == 0) {
                    // 滑动Floor
                    getFloorRecyclerView().scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (!getWebview().canScrollDown()) {
                    // 滑动Parent[Floor 与 WebView 边界]
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == getFloorRecyclerView().getHeight() && getWebview().canScrollDown()) {
                    // 滑动自身
                } else if (getScrollY() > getFloorRecyclerView().getHeight()) {
                    // 滑动Parent[WebView 与 Recommend 边界]
                    if (getScrollY() + dy < getFloorRecyclerView().getHeight()) {
                        dy = getFloorRecyclerView().getHeight() - getScrollY();
                    }

                    scrollBy(0, dy);
                    consumed[1] = dy;
                }
            }
        }

        if (target instanceof MyRecyclerView) {
            // 推荐容器下滑有四种情况
            // 1.滑动自身=》[recommend.canScrollDown]
            // 2.滑动Parent=》
            // ----Recommend 与 WebView 边界滑动 =》[!recommend.canScrollDown && scrollY > FloorHeight]
            // ----WebView 与 Floor 边界滑动 =》[!webView.canScrollDown && 0 < scrollY <= FloorHeight]
            // 3.滑动同级View
            // ----滑动 WebView =》[scrollY == floorHeight && webView.canScrollDown]
            // ----滑动 Floor =》[scrollY == 0 && floor.canScrollDown]
            if (dy < 0) {
                if (getRecommendRecyclerView().canScrollDown()) {
                    // 滑动自身
                } else if (!getRecommendRecyclerView().canScrollDown() && getScrollY() > getFloorRecyclerView().getHeight()) {
                    // 滑动Parent
                    if (getScrollY() + dy < getFloorRecyclerView().getHeight()) {
                        dy = getFloorRecyclerView().getHeight() - getScrollY();
                    }

                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == getFloorRecyclerView().getHeight() && getWebview().canScrollDown()) {
                    // 滑动WebView
                    getWebview().scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (!getWebview().canScrollDown() && (getScrollY() > 0 && getScrollY() <= getFloorRecyclerView().getHeight())) {
                    // 滑动 Parent
                    if (getScrollY() + dy < 0) {
                        dy = -getScrollY();
                    }

                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else if (getScrollY() == 0 && getFloorRecyclerView().canScrollDown()) {
                    getFloorRecyclerView().scrollBy(0, dy);
                    consumed[1] = dy;
                }
            } else if (dy > 0) {
                // 推荐容器上滑有两种情况
                // 1.滑动自身=》scrollY == maxScrollY
                // 2.滑动parent=》scrollY < maxScrollY
                if (getScrollY() == getMaxScrollHeight()) {
                    // 滑动自身
                } else if (getScrollY() < getMaxScrollHeight()) {
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
