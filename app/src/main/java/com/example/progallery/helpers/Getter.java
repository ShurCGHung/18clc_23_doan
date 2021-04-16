package com.example.progallery.helpers;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.progallery.entities.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class Getter {
    public static List<ImageModel> getAllImages(Context context) {
        List<ImageModel> imageModels = new ArrayList<>();
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            String nameOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
            String dateAddedOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
            ImageModel imageModel = new ImageModel();
            imageModel.setImagePath(absolutePathOfImage);
            imageModel.setImageName(nameOfImage);
            imageModel.setImageDateAdded(dateAddedOfImage);
            imageModels.add(imageModel);
        }
        cursor.close();
        return imageModels;
    }

}
