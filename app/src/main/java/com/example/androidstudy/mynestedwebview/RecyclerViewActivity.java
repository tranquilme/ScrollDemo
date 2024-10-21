package com.example.androidstudy.mynestedwebview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudy.R;
import com.example.androidstudy.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerViewActivity extends AppCompatActivity {

    private MyWebview webView;
    private String url= "https://m.chinese.alibaba.com/p-detail/%25E7%25BE%258E%25E5%259B%25BD%25E6%25AC%25A7%25E7%259B%259F%25E4%25BB%2593%25E5%25BA%2593%25E5%25BA%2593%25E5%25AD%2598cospheel-T16%25E8%2583%2596%25E8%25BD%25AE%25E8%2583%258E%25E5%2585%25A8%25E6%2582%25AC%25E6%258C%258248V-750W-1000W%25E8%2583%2596%25E5%25A4%258D%25E5%258F%25A4%25E7%2594%25B5%25E5%258A%25A8%25E8%2587%25AA%25E8%25A1%258C%25E8%25BD%25A6%25E5%25A4%258D%25E5%258F%25A4%25E5%25A5%25B3%25E7%2594%25B5%25E5%258A%25A8%25E5%259F%258E%25E5%25B8%2582%25E8%2587%25AA%25E8%25A1%258C%25E8%25BD%25A6-1601212076567/specifications.html?1=0&isApp=true&screenWidth=375&deviceId=ZMxmTtkA5XoDAFuqQK4bDvJ%2B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        RecyclerView firstRecyclerview = findViewById(R.id.first_recyclerview);
        firstRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter firstAdapter = new CustomAdapter();
        firstRecyclerview.setAdapter(firstAdapter);
        firstRecyclerview.getLayoutParams().height = Utils.getScreenHeight(this);

        RecyclerView recyclerView = findViewById(R.id.my_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ColorAdapter mAdapter = new ColorAdapter(generateRandomColors(30));
        recyclerView.setAdapter(mAdapter);
        recyclerView.getLayoutParams().height = Utils.getScreenHeight(this);

        initWebview();
    }

    private void initWebview() {
        webView = findViewById(R.id.my_webview);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
//        webView.loadUrl("https://www.cnblogs.com/xxxxxx.html");
        webView.loadUrl(url);
        webView.getLayoutParams().height = Utils.getScreenHeight(this);
    }

    // 自定义Adapter
    class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        // 定义 ViewType
        private static final int TYPE_TEXT = 0;

        @Override
        public int getItemViewType(int position) {
            return TYPE_TEXT;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
            return new TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.textView.setText("Item " + (position + 1));  // 设置普通项的文本
        }

        @Override
        public int getItemCount() {
            return 50;  // 总共 20 个Item
        }

        // ViewHolder for Text Items
        class TextViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public TextViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
            }
        }
    }

    // 生成随机颜色的列表
    private List<Integer> generateRandomColors(int itemCount) {
        List<Integer> colors = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < itemCount; i++) {
            // 生成随机颜色
            int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colors.add(color);
        }
        return colors;
    }

}