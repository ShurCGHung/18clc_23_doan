package com.example.progallery.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.helpers.Converter;
import com.example.progallery.helpers.ToolbarAnimator;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.Objects;

public class ViewImageActivity extends AppCompatActivity {
    private Toolbar topToolbar;
    private Toolbar bottomToolbar;
    private PhotoView imageView;
    private String mediaPath;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_image_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_image);

        topToolbar = findViewById(R.id.topBar);
        bottomToolbar = findViewById(R.id.bottomBar);
        imageView = findViewById(R.id.photoViewPlaceHolder);

        setSupportActionBar(topToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;

            @Override
            public void onClick(View v) {
                if (flag) {
                    ToolbarAnimator.hideTopToolbar(topToolbar);
                    ToolbarAnimator.hideBottomToolbar(bottomToolbar);
                } else {
                    ToolbarAnimator.showTopToolbar(topToolbar);
                    ToolbarAnimator.showBottomToolbar(bottomToolbar);
                }

                flag = !flag;
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            Uri mediaUri = intent.getData();
            if (mediaUri != null) {
                mediaPath = Converter.toPath(this, mediaUri);
//                InputStream is = getContentResolver().openInputStream(mediaUri);
//                imageView.setImageBitmap(BitmapFactory.decodeStream(is));
            }
        } else {
            mediaPath = intent.getStringExtra(Constant.EXTRA_PATH);
        }

        File imageFile = new File(mediaPath);
        if (imageFile.exists()) {
            Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(image);
        }


        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit ảnh here please
                Intent intent = new Intent(getBaseContext(), EditImageActivity.class);
                intent.putExtra("IMAGE_PATH", mediaPath);
                startActivity(intent);
            }
        });
    }
}