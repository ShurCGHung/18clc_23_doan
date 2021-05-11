package com.example.progallery.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;

import com.example.progallery.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class VideoInfoFragment extends DialogFragment {

    TextView imgTitle, imgSource, imgDateTime, imgLongtitude, imgLatitude;
    ExifInterface exif;
    private String mediaPath;

    public VideoInfoFragment(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.video_details, null);

        builder.setView(view)
                .setTitle("Image Information")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        imgTitle = view.findViewById(R.id.imgTitle);
        imgSource = view.findViewById(R.id.imgSource);
        imgDateTime = view.findViewById(R.id.imgDateTime);
        imgLongtitude = view.findViewById(R.id.imgLongtitude);
        imgLatitude = view.findViewById(R.id.imgLatitude);
        try {
            Uri uri = Uri.fromFile(new File(mediaPath));
            InputStream in = Objects.requireNonNull(getContext()).getApplicationContext().getContentResolver().openInputStream(uri);
            exif = new ExifInterface(in);

            imgTitle.setText(mediaPath.substring(mediaPath.lastIndexOf("/") + 1));
            imgDateTime.setText(exif.getAttribute(ExifInterface.TAG_DATETIME));
            imgLongtitude.setText(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            imgLatitude.setText(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            imgSource.setText(mediaPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.create();
    }
}
