package com.example.androidstudy.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class EventViewGroup extends FrameLayout {


    public EventViewGroup(Context context) {
        super(context);
    }

    public EventViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EventViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(EventActivity.TAG, "dispatchTouchEvent: ViewGroup===========" + MotionEvent.actionToString(ev.getAction()));

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(EventActivity.TAG, "onInterceptTouchEvent: ViewGroup===========" + MotionEvent.actionToString(ev.getAction()));

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(EventActivity.TAG, "onTouchEvent: ViewGroup===========" + MotionEvent.actionToString(event.getAction()));

        return super.onTouchEvent(event);
    }
}
