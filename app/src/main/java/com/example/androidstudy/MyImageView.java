package com.example.androidstudy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {


    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取当前的绘制区域
//        Rect clipBounds = canvas.getClipBounds();
//        System.out.println("Drawing area: " + clipBounds.toString());
//
//        // 绘制矩形以可视化绘制区域
//        Paint paint = new Paint();
//        paint.setColor(Color.RED); // 设置矩形颜色
//        paint.setStyle(Paint.Style.STROKE); // 设置为描边模式
//        paint.setStrokeWidth(20);
//        canvas.drawRect(clipBounds, paint); // 绘制矩形
        canvas.drawColor(Color.parseColor("#88880000"));

        System.out.println("clll ondraw");
    }
}
