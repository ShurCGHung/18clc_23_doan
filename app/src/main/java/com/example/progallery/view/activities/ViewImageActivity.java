package com.example.progallery.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.helpers.Converter;
import com.example.progallery.helpers.ToolbarAnimator;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.Objects;

import iamutkarshtiwari.github.io.ananas.editimage.EditImageActivity;
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder;

public class ViewImageActivity extends AppCompatActivity {
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
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
                // Intent intent = new Intent(getBaseContext(), EditImageActivity.class);
                // intent.putExtra("IMAGE_PATH", mediaPath);
                // startActivity(intent);
                EditImage();
            }
        });
    }

    private void EditImage() {
        try {
            Intent intent = new ImageEditorIntentBuilder(this, mediaPath, mediaPath)
                    .withAddText()
                    .withPaintFeature()
                    .withFilterFeature()
                    .withRotateFeature()
                    .withCropFeature()
                    .withBrightnessFeature()
                    .withSaturationFeature()
                    .withBeautyFeature()
                    .withStickerFeature()
                    .forcePortrait(true)
                    .build();

            EditImageActivity.start(this, intent, ACTION_REQUEST_EDITIMAGE);
        } catch (Exception e) {
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
            Log.e("Demo App", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_REQUEST_EDITIMAGE && resultCode == RESULT_OK) {
            String newFilePath = data.getStringExtra(ImageEditorIntentBuilder.OUTPUT_PATH);
            boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IS_IMAGE_EDITED, false);

            if (isImageEdit) {
                mediaPath = newFilePath;
                File imageFile = new File(mediaPath);
                if (imageFile.exists()) {
                    Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    imageView.setImageBitmap(image);
                }
            }
        }
    }
}