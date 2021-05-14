package com.example.progallery.view.fragments;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.progallery.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class SetWallpaperFragment extends BottomSheetDialogFragment {

    private static final int REQUEST_CROP_IMAGE = 2501;
    public SetWallpaperFragment() { }


    Button btnSetHomeScreen, btnSetLockScreen;
    private String mediaPath;

    public SetWallpaperFragment(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.set_wallpaper_fragment, container, false);
        btnSetHomeScreen = view.findViewById(R.id.setWallpaperAsHomeScreen);
        btnSetLockScreen = view.findViewById(R.id.setWallpaperAsLockScreen);

        btnSetLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imgFile = new File(mediaPath);
                if (imgFile.exists()) {
                    Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                    try {
                        wallpaperManager.setBitmap(imgBitmap, null, false, WallpaperManager.FLAG_LOCK);
                        Toast.makeText(getActivity(), getResources().getString(R.string.lockscreen_set), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.lockscreen_not_set), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSetHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imgFile = new File(mediaPath);
                if (imgFile.exists()) {
                    Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                    try {
                        wallpaperManager.setBitmap(imgBitmap, null, false, WallpaperManager.FLAG_SYSTEM);
                        Toast.makeText(getActivity(), getResources().getString(R.string.homescreen_set), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.homescreen_not_set), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
