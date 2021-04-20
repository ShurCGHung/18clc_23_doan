package com.example.progallery.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.progallery.model.dao.AlbumDao;
import com.example.progallery.model.dao.MediaDao;
import com.example.progallery.model.entities.Album;
import com.example.progallery.model.entities.Media;

@Database(entities = {Media.class, Album.class}, version = 1, exportSchema = false)
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


    public abstract MediaDao mediaDao();

    public abstract AlbumDao albumDao();

}
