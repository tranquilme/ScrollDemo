package com.example.androidstudy.mynestedwebview;

// ColorAdapter.java
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.FrameLayout;

import com.example.androidstudy.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private List<Integer> colorList;

    public ColorAdapter(List<Integer> colorList) {
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        // 设置背景颜色
        GradientDrawable drawable = (GradientDrawable) holder.colorBlock.getBackground();
        drawable.setColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    // ViewHolder 类
    public static class ColorViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout colorBlock;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorBlock = itemView.findViewById(R.id.colorBlock);
        }
    }
}

