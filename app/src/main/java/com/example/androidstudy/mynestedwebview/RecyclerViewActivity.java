package com.example.androidstudy.mynestedwebview;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudy.R;
import com.example.androidstudy.utils.Utils;

public class RecyclerViewActivity extends AppCompatActivity {

    private CustomAdapter mAdapter;
    private MyWebview webView;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomAdapter();
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
        webView.loadUrl("https://www.cnblogs.com/xxxxxx.html");
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

}