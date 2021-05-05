package com.example.progallery.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.helpers.ToolbarAnimator;
import com.example.progallery.services.MediaFetchService;

import java.io.File;
import java.util.Objects;

public class ViewVideoActivity extends AppCompatActivity {
    private Toolbar topToolbar;
    private Toolbar bottomToolbar;
    private ImageView videoThumbnailView;
    private String mediaPath;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_video_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constant.EXTRA_REQUEST, Constant.REQUEST_RETURN);
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteVideo) {
            boolean delRes = deleteMedia(mediaPath);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constant.EXTRA_REQUEST, Constant.REQUEST_REMOVE_MEDIA);
            if (delRes) {
                setResult(RESULT_OK, returnIntent);
            } else {
                setResult(RESULT_CANCELED, returnIntent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_video);

        topToolbar = findViewById(R.id.topBar);
        bottomToolbar = findViewById(R.id.bottomBar);
        videoThumbnailView = findViewById(R.id.thumbnailView);

        setSupportActionBar(topToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        videoThumbnailView.setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVideoActivity.this, DisplayVideoActivity.class);
                intent.putExtra(Constant.EXTRA_PATH, mediaPath);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        mediaPath = intent.getStringExtra(Constant.EXTRA_PATH);

        File videoFile = new File(mediaPath);
        if (videoFile.exists()) {
            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mediaPath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

            Glide.with(this)
                    .load(bmThumbnail)
                    .placeholder(R.color.black)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(videoThumbnailView);
        }
    }

    private boolean deleteMedia(String mediaPath) {
        MediaFetchService service = MediaFetchService.getInstance();
        return service.deleteMedia(getApplicationContext(), mediaPath);
    }
}