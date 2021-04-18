package com.example.progallery.helpers;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.progallery.model.entities.Image;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FetchStorage {
    public static List<Image> getAllImages(Context context) {
        List<Image> images = new ArrayList<>();
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "DATE_ADDED DESC");
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            String nameOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
            long dateAddedOfImage = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));

            Date date = new java.util.Date(dateAddedOfImage * 1000L);

            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);

            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
            String formattedDate = sdf.format(date);

            Image image = new Image(absolutePathOfImage, nameOfImage, formattedDate);
            images.add(image);
        }
        cursor.close();
        return images;
    }
}
