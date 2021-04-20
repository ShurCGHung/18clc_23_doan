package com.example.progallery.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.progallery.model.entities.Media;

import java.util.List;

@Dao
public interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Media media);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Media> mediaList);

    @Delete
    void delete(Media media);

    @Query("DELETE FROM media_table WHERE mediaPath IN (:imageList)")
    void delete(List<String> imageList);

    @Query("SELECT * FROM media_table ORDER BY mediaDateAdded DESC")
    LiveData<List<Media>> getAllMedias();

    @Query("SELECT * FROM media_table ORDER BY mediaDateAdded DESC")
    List<Media> getAllMediasSync();
}
