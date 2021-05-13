package com.example.progallery.model.services;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;
import androidx.loader.content.CursorLoader;

import com.example.progallery.helpers.Constant;
import com.example.progallery.model.models.Media;
import com.example.progallery.view.activities.RootViewMediaActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        cursor.close();

        return Observable.just(medias);
    }

    public boolean deleteMedia(Context context, String path) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.FAVORITE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(path);
        editor.apply();

        String[] projection = {
                MediaStore.Files.FileColumns._ID
        };

        String selection = MediaStore.Files.FileColumns.DATA + "=?";

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                context,
                queryUri,
                projection,
                selection,
                new String[]{path},
                null
        );

        Cursor cursor = cursorLoader.loadInBackground();
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        boolean check = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                long mediaID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                Uri deleteUri = ContentUris.withAppendedId(queryUri, mediaID);
                contentResolver.delete(deleteUri, null, null);
                check = true;
            }
            cursor.close();
        }
        return check;
    }

    public boolean updateMediaAlbum(Context context, String oldPath, String newPath) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.FAVORITE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String type = null;
        if (sharedPreferences.contains(oldPath)) {
            type = sharedPreferences.getString(oldPath, "");
        }
        editor.remove(oldPath);
        editor.putString(newPath, type);
        editor.apply();

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Files.FileColumns.DATA, newPath);
        return contentResolver.update(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values,
                MediaStore.Files.FileColumns.DATA + "='" + oldPath + "'", null)
                == 1;
    }

    public boolean addNewMedia(Context context, String newPath, String title) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Files.FileColumns.DATA, newPath);
        values.put(MediaStore.Files.FileColumns.TITLE, title);
        values.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000);

        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return uri != null;
    }

    public boolean checkFavorite(Context context, String mediaPath) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.FAVORITE_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.contains(mediaPath);
    }

    public void addFavorite(Context context, String mediaPath) {
        String[] projection = {
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };

        String selection = MediaStore.Files.FileColumns.DATA + "=?";

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                context,
                queryUri,
                projection,
                selection,
                new String[]{mediaPath},
                null
        );

        Cursor cursor = cursorLoader.loadInBackground();
        String mediaType = "";

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mediaType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));

            }
            cursor.close();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.FAVORITE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mediaPath, mediaType);
        editor.apply();
    }

    public void removeFavorite(Context context, String mediaPath) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.FAVORITE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(mediaPath);
        editor.apply();
    }

    public Observable<List<Media>> getMediaListForFavoriteAlbum(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.FAVORITE_PREF, Context.MODE_PRIVATE);
        Map<String, String> res = new LinkedHashMap<>();

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            res.put(entry.getKey(), entry.getValue().toString());
        }

        List<Map.Entry<String, String>> entryList = new ArrayList<>(res.entrySet());

        ArrayList<String> listPath = new ArrayList<>();
        for (Map.Entry<String, String> entry : entryList) {
            listPath.add(entry.getKey());
        }

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
                + MediaStore.Files.FileColumns.DATA + "=?";

        Uri queryUri = MediaStore.Files.getContentUri("external");

        String[] paths = listPath.toArray(new String[0]);
        for (String path : paths) {
            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    queryUri,
                    projection,
                    selection,
                    new String[]{path},
                    null // Sort order.
            );
            Cursor cursor = cursorLoader.loadInBackground();

            // Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "DATE_ADDED DESC");
            if (cursor != null) {
                if (cursor.moveToFirst()) {
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

                    Date date = new Date(dateAddedOfImage * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
                    String formattedDate = sdf.format(date);

                    Media media = new Media(absolutePathOfImage, nameOfImage, formattedDate, heightOfImage, widthOfImage, typeOfImage);
                    medias.add(media);
                }
            }
            assert cursor != null;
            cursor.close();
        }


        return Observable.just(medias);
    }

    public Observable<List<Media>> getMediaListForVaultAlbum(Context context) throws IOException {
        File folder = new File(context.getExternalFilesDir(".hidden").getAbsolutePath());

        File[] listOfFiles = folder.listFiles();
        List<Media> medias = new ArrayList<>();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String path = file.getAbsolutePath();
                String name = file.getName();

                ExifInterface exif = new ExifInterface(path);

                String height = null;
                String width = null;
                String type = null;

                if (RootViewMediaActivity.isImageFile(path)) {
                    height = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
                    width = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
                    type = Integer.toString(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
                } else if (RootViewMediaActivity.isVideoFile(path)) {
                    Uri uri = Uri.fromFile(new File(path));
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(context, uri);
                    width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                    height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                    type = Integer.toString(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
                }

                Media media = new Media(path, name, null, height, width, type);
                medias.add(media);
            }
        }

        return Observable.just(medias);
    }


    private static class SingletonHelper {
        private static final MediaFetchService INSTANCE = new MediaFetchService();
    }
}
