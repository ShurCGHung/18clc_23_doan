package com.example.progallery.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.progallery.model.entities.Media;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Converter {
    public static LinkedHashMap<String, List<Media>> toHashMap(List<Media> mediaList) {
        LinkedHashMap<String, List<Media>> groupedData = new LinkedHashMap<>();
        for (Media media : mediaList) {
            String hashMapKey = media.getMediaDateAdded();
            if (groupedData.containsKey(hashMapKey)) {
                Objects.requireNonNull(groupedData.get(hashMapKey)).add(media);
            } else {
                List<Media> list = new ArrayList<>();
                list.add(media);
                groupedData.put(hashMapKey, list);
            }
        }
        return groupedData;
    }

    public static String toPath(Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
