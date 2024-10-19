package com.example.androidstudy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BitmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bitmap);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test, options);
//        Bitmap bitmap1 = Bitmap.createBitmap(10000, 5000, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap1);
//        canvas.drawBitmap(bitmap, 0, 0, null);
//        System.out.println("clllll" + bitmap1.getByteCount());
//        ImageView imageView = findViewById(R.id.image);
//        imageView.setImageDrawable(getResources().getDrawable(R.drawable.test));
    }
}