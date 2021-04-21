package com.example.progallery.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.helpers.ToolbarAnimator;

import java.io.File;
import java.util.Objects;

public class DisplayVideoActivity extends AppCompatActivity {
    VideoView videoView;
    Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_video);

        videoView = findViewById(R.id.videoView);
        toolbar = findViewById(R.id.topBar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String mediaPath = intent.getStringExtra(Constant.EXTRA_PATH);

        MediaController mediaController = new MediaController(this);
        File videoFile = new File(mediaPath);
        if (videoFile.exists()) {
            videoView.setOnTouchListener(new View.OnTouchListener() {
                boolean flag = true;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (flag) {
                            mediaController.hide();
                            ToolbarAnimator.hideTopToolbar(toolbar);
                        } else {
                            mediaController.show(0);
                            ToolbarAnimator.showTopToolbar(toolbar);
                        }
                        flag = !flag;
                        return true;
                    }

                    return false;
                }
            });

            videoView.setVideoPath(mediaPath);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
        }
    }
}