package com.example.androidstudy.mynestedwebview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FirstRecyclerView extends RecyclerView {
    public FirstRecyclerView(@NonNull Context context) {
        super(context);
    }

    public FirstRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 不能继续上滑
    public boolean isScrollToBottom() {
        return !canScrollVertically(1);
    }

    public boolean canScrollUp() {
        return canScrollVertically(1);
    }

    public boolean canScrollDown() {
        return canScrollVertically(-1);
    }

}
