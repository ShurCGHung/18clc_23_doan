package com.example.progallery.model.services;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import com.example.progallery.helpers.Constant;
import com.example.progallery.model.models.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;

public class AlbumFetchService {
    public static AlbumFetchService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private int getCount(final Context context, final Uri contentUri, final String bucketName) {
        try (final Cursor cursor = context.getContentResolver().query(contentUri,
                null,
                "("
                        + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                        + " OR "
                        + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                        + ")"
                        + " AND "
                        + MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME + "=?",
                new String[]{bucketName},
                null)) {
            return ((cursor == null) || (!cursor.moveToFirst())) ? 0 : cursor.getCount();
        }
    }

    public Observable<List<Album>> getAlbumList(Context context) {
        LinkedHashMap<String, String> mapAlbumThumbnail = new LinkedHashMap<>();

        List<Album> albums = new ArrayList<>();


        // Query from medias
        Uri queryUri = MediaStore.Files.getContentUri("external");

        String[] projection = new String[]{
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.BUCKET_ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                queryUri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );

        Cursor cursor = cursorLoader.loadInBackground();

        while (true) {
            assert cursor != null;
            if (!cursor.moveToNext()) break;
            String albumID = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID));
            String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME));

            if (!mapAlbumThumbnail.containsKey(albumName) && albumName.charAt(0) != '.') {
                String albumThumbnail = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                String thumbnailType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));

                File f = new File(albumThumbnail);
                String folderPath = Objects.requireNonNull(f.getParentFile()).getPath();

                int count = getCount(context, queryUri, albumName);
                Album album = new Album(albumID, albumName, Integer.toString(count), albumThumbnail, thumbnailType, folderPath);
                mapAlbumThumbnail.put(albumName, albumThumbnail);
                albums.add(album);
            }
        }

        cursor.close();

        // Query from folder
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File file = new File(root);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

        for (int i = 0; i < Objects.requireNonNull(directories).length; i++) {
            if (!mapAlbumThumbnail.containsKey(directories[i]) && directories[i].charAt(0) != '.') {
                String albumName = directories[i];
                String albumPath = root + File.separator + albumName;

                Album album = new Album(null, albumName, Integer.toString(0), null, null, albumPath);
                mapAlbumThumbnail.put(albumName, null);
                albums.add(album);
            }
        }

        return Observable.just(albums);
    }

    public boolean changeMediaPath(Context context, String oldName, String newName) {
        String selection = "("
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                + ")"
                + " AND "
                + MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME + "=?";

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                context,
                queryUri,
                null,
                selection,
                new String[]{oldName},
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        Cursor cursor = cursorLoader.loadInBackground();
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();

        // Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "DATE_ADDED DESC");
        while (true) {
            assert cursor != null;
            if (!cursor.moveToNext()) break;
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            ContentValues values = new ContentValues();
            values.put(MediaStore.Files.FileColumns.DATA, absolutePathOfImage.replace(oldName, newName));
            boolean check = contentResolver.update(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values,
                    MediaStore.Files.FileColumns.DATA + "='" + absolutePathOfImage + "'", null)
                    == 1;
            if (!check) {
                return false;
            }
        }

        cursor.close();

        return true;
    }

    public boolean deleteAlbum(Context context, String albumName) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID
        };

        String selection = MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME + "=?";

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                context,
                queryUri,
                projection,
                selection,
                new String[]{albumName},
                null
        );

        Cursor cursor = cursorLoader.loadInBackground();
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                long mediaID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                Uri deleteUri = ContentUris.withAppendedId(queryUri, mediaID);
                contentResolver.delete(deleteUri, null, null);
            }
            cursor.close();
        }
        return true;
    }

    public Observable<Album> getFavoriteAlbum(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.FAVORITE_PREF, Context.MODE_PRIVATE);
        Map<String, String> res = new LinkedHashMap<>();

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            res.put(entry.getKey(), entry.getValue().toString());
        }

        List<Map.Entry<String, String>> entryList = new ArrayList<>(res.entrySet());

        Album favoriteAlbum;
        if (entryList.size() == 0) {
            favoriteAlbum = new Album(null, null, Integer.toString(0), null, null, null);
        } else {
            Map.Entry<String, String> lastEntry = entryList.get(entryList.size() - 1);
            favoriteAlbum = new Album(null, null, Integer.toString(entryList.size()), lastEntry.getKey(), lastEntry.getValue(), null);
        }

        return Observable.just(favoriteAlbum);
    }

    private static class SingletonHelper {
        private static final AlbumFetchService INSTANCE = new AlbumFetchService();
    }
}
