package com.example.progallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.hardware.Camera.CameraInfo;

public class Camera extends Activity {

    private SurfaceHolder mHolder;
    private ImageButton capture, modeChange, backToPhoto;
    private SurfaceView surfaceView;
    private int cameraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        cameraID = CameraInfo.CAMERA_FACING_BACK;
        capture = (ImageButton) findViewById(R.id.btnCapture);
        modeChange = (ImageButton) findViewById(R.id.btnChangeMode);
        backToPhoto = (ImageButton) findViewById(R.id.btnBackToPhoto);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
    }
}
