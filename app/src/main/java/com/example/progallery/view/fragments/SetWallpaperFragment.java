package com.example.progallery.view.fragments;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.progallery.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;

public class SetWallpaperFragment extends BottomSheetDialogFragment {

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
                        Toast.makeText(getActivity(), "Lock Screen set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Lock Screen not set", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Home Screen set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Home Screen not set", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
}
