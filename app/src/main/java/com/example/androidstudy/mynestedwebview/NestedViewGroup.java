package com.example.androidstudy.mynestedwebview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudy.utils.ConstModel;

import java.lang.reflect.Method;

public class NestedViewGroup extends LinearLayout implements NestedScrollingParent2 {
    private Scroller scroller;

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

    private void init() {
        scroller = new Scroller(getContext());
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }



    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        return super.onNestedFling(target, velocityX, velocityY, consumed);

//        if (target instanceof MyRecyclerView) {
//            if (velocityY < 0 && ((MyRecyclerView) target).isScrollToTop()) {
//                getWebview().flingScroll(0, (int) velocityY);
//            }
//        }
//
//        Log.d(ConstModel.TAG, "onNestedFling: " + velocityY);




        return true;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {


        if (target instanceof MyWebview) {

            Log.d(ConstModel.TAG, "onNestedPreScroll: Webview dy===" + dy);
            if (((MyWebview) target).isScrollToBottom()) {


                if (dy > 0) {

                    if (getScrollY() < getWebviewHeight()) {
                        Log.d(ConstModel.TAG, "getScrollY = " + getScrollY() + "  webviewHeight = " + getWebviewHeight());

                        scrollBy(0, dy);
                        consumed[1] = dy;
                    } else {
                        Log.d(ConstModel.TAG, "onNestedPreScroll: toRecyclerView dy" + dy);
                        getRecyclerView().scrollBy(0, dy);
                    }

                }
            }
        }

        if (target instanceof MyRecyclerView) {
            if (dy < 0 && ((MyRecyclerView) target).isScrollToTop()) {
                if (getScrollY() > 0) {
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else {
                    Log.d(ConstModel.TAG, "onNestedPreScroll: " + dy + "-------target=MyRecyclerview");
                    getWebview().scrollBy(0, dy);

                    if (getWebview().isDownClick()) {
                        try {
                            Method method = RecyclerView.class.getDeclaredMethod("stopScrollersInternal");
                            method.setAccessible(true);
                            method.invoke(getRecyclerView());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            } else if (dy > 0 && getScrollY() < getWebviewHeight()) {
                scrollBy(0, dy);
                consumed[1] = dy;
            }
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (target instanceof MyWebview) {


            if (velocityY > 0 && ((MyWebview) target).isScrollToBottom()) {



                getRecyclerView().fling(0, (int) velocityY);
            }
        }

        if (target instanceof MyRecyclerView) {
            Log.d(ConstModel.TAG, "onNestedFling: " + velocityY + "((MyRecyclerView) target).isScrollToTop()" + ((MyRecyclerView) target).isScrollToTop());
            if (velocityY < 0 && ((MyRecyclerView) target).isScrollToTop()) {



                getWebview().flingScroll(0, (int) velocityY);
            }
        }


//        return true;
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (scroller.computeScrollOffset()) {

        }
    }

    private int getWebviewHeight() {
        int height = 0;
        if (getChildAt(0) instanceof MyWebview) {
            height = getChildAt(0).getHeight();
        }

        return height;
    }

    @Override
    public void scrollBy(int x, int y) {
        int newY = getScrollY() + y;

        if (newY < 0) {
            newY = 0;
        }

        scrollTo(x, newY);
    }

    private MyWebview getWebview() {
        return (MyWebview) getChildAt(0);
    }

    private MyRecyclerView getRecyclerView() {
        return (MyRecyclerView) getChildAt(1);
    }
}
