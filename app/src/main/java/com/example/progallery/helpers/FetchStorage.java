package com.example.progallery.helpers;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.progallery.model.entities.Media;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FetchStorage {
    public static List<Media> getAllMedias(Context context) {
        List<Media> medias = new ArrayList<>();
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED, MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.WIDTH};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "DATE_ADDED DESC");
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            String nameOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
            long dateAddedOfImage = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
            String heightOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT));
            String widthOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH));

            Date date = new java.util.Date(dateAddedOfImage * 1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
            String formattedDate = sdf.format(date);

            Media media = new Media(absolutePathOfImage, nameOfImage, formattedDate, heightOfImage, widthOfImage);
            medias.add(media);
        }
        cursor.close();
        return medias;
    }

    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}
