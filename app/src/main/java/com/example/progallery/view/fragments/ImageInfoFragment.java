package com.example.progallery.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;

import com.example.progallery.R;

import java.io.IOException;

public class ImageInfoFragment extends DialogFragment {

    TextView imgTitle, imgSource, imgLength, imgWidth, imgOrientation, imgDateTime, imgLongtitude, imgLatitude;
    ExifInterface exif;
    private String mediaPath;

    public ImageInfoFragment(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.image_details, null);

        builder.setView(view)
                .setTitle("Image Information")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        imgTitle = view.findViewById(R.id.imgTitle);
        imgSource = view.findViewById(R.id.imgSource);
        imgLength = view.findViewById(R.id.imgLength);
        imgWidth = view.findViewById(R.id.imgWidth);
        imgDateTime = view.findViewById(R.id.imgDateTime);
        imgOrientation = view.findViewById(R.id.imgOrientation);
        imgLongtitude = view.findViewById(R.id.imgLongtitude);
        imgLatitude = view.findViewById(R.id.imgLatitude);
        try {
            exif = new ExifInterface(mediaPath);

            imgTitle.setText(mediaPath.substring(mediaPath.lastIndexOf("/") + 1, mediaPath.lastIndexOf('.')));
            imgLength.setText(exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
            imgWidth.setText(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
            imgDateTime.setText(exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED));
            imgOrientation.setText(exif.getAttribute(ExifInterface.TAG_ORIENTATION));

            imgLongtitude.setText(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            imgLatitude.setText(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));

            imgSource.setText(mediaPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.create();
    }
}
