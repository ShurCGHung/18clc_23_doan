package com.example.progallery.services;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.loader.content.CursorLoader;

import com.example.progallery.model.Media;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.core.Observable;

public class MediaFetchService {
    public static MediaFetchService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Observable<List<Media>> getMediaList(Context context) {
        List<Media> medias = new ArrayList<>();
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.HEIGHT,
                MediaStore.Files.FileColumns.WIDTH,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                context,
                queryUri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        Cursor cursor = cursorLoader.loadInBackground();

        // Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "DATE_ADDED DESC");
        while (true) {
            assert cursor != null;
            if (!cursor.moveToNext()) break;
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            String nameOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
            long dateAddedOfImage = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
            String typeOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
            String heightOfImage;
            String widthOfImage;
            if (typeOfImage.equals("1")) {
                heightOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));
                widthOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));
            } else {
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(context, Uri.parse(absolutePathOfImage));
                heightOfImage = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                widthOfImage = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                metaRetriever.release();
            }

            Date date = new java.util.Date(dateAddedOfImage * 1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
            String formattedDate = sdf.format(date);

            Media media = new Media(absolutePathOfImage, nameOfImage, formattedDate, heightOfImage, widthOfImage, typeOfImage);
            medias.add(media);
        }
        cursor.close();

        return Observable.just(medias);
    }

    public Observable<List<Media>> getMediaListForAlbum(Context context, String albumName) {
        List<Media> medias = new ArrayList<>();
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.HEIGHT,
                MediaStore.Files.FileColumns.WIDTH,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };

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
                projection,
                selection,
                new String[]{albumName},
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        Cursor cursor = cursorLoader.loadInBackground();

        // Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "DATE_ADDED DESC");
        while (true) {
            assert cursor != null;
            if (!cursor.moveToNext()) break;
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            String nameOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
            long dateAddedOfImage = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
            String typeOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
            String heightOfImage;
            String widthOfImage;
            if (typeOfImage.equals("1")) {
                heightOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));
                widthOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));
            } else {
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(context, Uri.parse(absolutePathOfImage));
                heightOfImage = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                widthOfImage = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                metaRetriever.release();
            }

            Date date = new java.util.Date(dateAddedOfImage * 1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
            String formattedDate = sdf.format(date);

            Media media = new Media(absolutePathOfImage, nameOfImage, formattedDate, heightOfImage, widthOfImage, typeOfImage);
            medias.add(media);
        }

        Log.d("MY_APP", String.valueOf(medias));
        cursor.close();

        return Observable.just(medias);
    }

    private static class SingletonHelper {
        private static final MediaFetchService INSTANCE = new MediaFetchService();
    }
}
