package com.example.progallery.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.progallery.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class ViewImageActivity extends AppCompatActivity {
    public static final String EXTRA_PATH = "com.example.progallery.EXTRA_PATH";

    private Toolbar topToolbar;
    private Toolbar bottomToolbar;
    private PhotoView imageView;
    private String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        topToolbar = findViewById(R.id.topBar);
        bottomToolbar = findViewById(R.id.bottomBar);
        imageView = findViewById(R.id.photoViewPlaceHolder);

        setSupportActionBar(topToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        imageView.setOnClickListener(v -> {
            if (topToolbar.getVisibility() == View.VISIBLE) {
                topToolbar.setVisibility(View.GONE);
                bottomToolbar.setVisibility(View.GONE);
            } else {
                topToolbar.setVisibility(View.VISIBLE);
                bottomToolbar.setVisibility(View.VISIBLE);
            }
        });

        Intent intent = getIntent();
        mediaPath = intent.getStringExtra(EXTRA_PATH);

        File imageFile = new File(mediaPath);
        if (imageFile.exists()) {
            Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(image);
        }

        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit ảnh here please
            }
        });
    }
}