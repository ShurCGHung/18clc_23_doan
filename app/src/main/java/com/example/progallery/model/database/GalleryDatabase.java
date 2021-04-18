package com.example.progallery.model.database;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.progallery.model.dao.AlbumDao;
import com.example.progallery.model.dao.ImageDao;
import com.example.progallery.model.entities.Album;
import com.example.progallery.model.entities.Image;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Database(entities = {Image.class, Album.class}, version = 1, exportSchema = false)
public abstract class GalleryDatabase extends RoomDatabase {
    private static GalleryDatabase instance;

    public static synchronized GalleryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), GalleryDatabase.class, "gallery_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


    public abstract ImageDao imageDao();

    public abstract AlbumDao albumDao();

}
