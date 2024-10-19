package com.example.androidstudy.scroll;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidstudy.R;

public class ScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scroll);
        Button buttonYPositive = findViewById(R.id.y_positive);
        Button buttonYNegative = findViewById(R.id.y_negative);
        View view = findViewById(R.id.target);

        buttonYPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.scrollTo(0, 100);
            }
        });

        buttonYNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.scrollBy(0, -100);
            }
        });

    }
}