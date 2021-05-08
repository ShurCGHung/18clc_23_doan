package com.example.progallery.model.services;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import com.example.progallery.model.models.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
                String albumPath = root + File.pathSeparator + albumName;

                Album album = new Album(null, albumName, Integer.toString(0), null, null, albumPath);
                mapAlbumThumbnail.put(albumName, null);
                albums.add(album);
            }
        }

        return Observable.just(albums);
    }

    private static class SingletonHelper {
        private static final AlbumFetchService INSTANCE = new AlbumFetchService();
    }
}
