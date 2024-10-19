package com.example.androidstudy.utils;

import android.content.Context;

public class Utils {
    public static int dpToPx(Context context, float dp) {
        // 获取屏幕的像素密度（每英寸的像素数）
        float density = context.getResources().getDisplayMetrics().density;
        // 将 dp 转换为 px，公式是 dp * density
        return Math.round(dp * density);
    }

}
