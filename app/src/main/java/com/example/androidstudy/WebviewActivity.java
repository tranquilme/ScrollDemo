package com.example.androidstudy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidstudy.R;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // 初始化 WebView
        webView = findViewById(R.id.webView);

        // 启用 JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // 设置 WebViewClient 以在当前应用中打开网页
        webView.setWebViewClient(new WebViewClient());

        // 加载网页
        webView.loadUrl("https://m.chinese.alibaba.com/p-detail/%25E7%2583%25AD%25E5%258D%25962024%25E6%2596%25B0%25E8%25AE%25BE%25E8%25AE%25A1LC-40SF-40L%25E4%25BE%25BF%25E6%2590%25BA%25E5%25BC%258F%25E5%25AE%25B6%25E7%2594%25A8%25E8%2592%25B8%25E6%25B1%25BD%25E5%2590%25B8%25E5%25B0%2598%25E5%2599%25A8%25E6%25B1%25BD%25E8%25BD%25A6%25E5%25B9%25B2%25E6%25B9%25BF%25E4%25B8%25A4%25E7%2594%25A8%25E5%2590%25B8%25E5%25B0%2598%25E5%2599%25A8%25E5%259C%25B0%25E6%25AF%25AF%25E6%25B8%2585%25E6%25B4%2597%25E6%259C%25BA-1600180538584/specifications.html?1=0&isApp=true&screenWidth=360&deviceId=YQkgw5pgebADAJ0whfc5M1%2Bm");

        // 创建一个 WebView 并加载网页

        // 等待 WebView 加载完成后，获取其内容生成 Bitmap
//        webView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 捕获 WebView 的内容为 Picture 对象
//                Picture picture = webView.capturePicture();
//
//                // 创建 Bitmap 来存储 WebView 的内容
//                Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
//
//                // 创建 Canvas 并将其关联到 Bitmap 上
//                Canvas canvas = new Canvas(bitmap);
//
//                // 将 Picture 绘制到 Canvas 上，最终生成 Bitmap
//                picture.draw(canvas);
//
//                // 获取 Bitmap 的宽高
//                int bitmapWidth = bitmap.getWidth();
//                int bitmapHeight = bitmap.getHeight();
//                int bitmapSizeInBytes = bitmap.getByteCount(); // 获取 Bitmap 占用的字节数
//
//                // 输出 Bitmap 的信息
//                Log.d("Bitmap Info", "Width: " + bitmapWidth + ", Height: " + bitmapHeight);
//                Log.d("Bitmap Info", "Size in bytes: " + bitmapSizeInBytes);
//            }
//        }, 1000); // 延迟一段时间，确保网页加载完成

    }



    @Override
    public void onBackPressed() {
        // 如果 WebView 可以返回，返回上一个页面
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
