package com.example.progallery.model.database;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.progallery.model.dao.AlbumDao;
import com.example.progallery.model.dao.ImageDao;
import com.example.progallery.model.entities.Album;
import com.example.progallery.model.entities.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Image.class, Album.class}, version = 1)
public abstract class GalleryDatabase extends RoomDatabase {
    private static GalleryDatabase instance;
    private static List<Image> imageList;
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            ImageDao imageDao = instance.imageDao();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                imageDao.insert(imageList);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
            });
        }
    };

    public static synchronized GalleryDatabase getInstance(Context context) {
        imageList = new ArrayList<>();
        imageList = getAllImages(context);
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), GalleryDatabase.class, "gallery_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static List<Image> getAllImages(Context context) {
        List<Image> images = new ArrayList<>();
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "DATE_ADDED DESC");
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            String nameOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
            String dateAddedOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
            Image image = new Image(absolutePathOfImage, nameOfImage, dateAddedOfImage);
            images.add(image);
        }
        cursor.close();
        return images;
    }

    public abstract ImageDao imageDao();

    public abstract AlbumDao albumDao();

}
