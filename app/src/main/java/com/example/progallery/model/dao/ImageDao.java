package com.example.progallery.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.progallery.model.entities.Image;

import java.util.List;

@Dao
public interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Image> imageList);

    @Delete
    void delete(Image image);

    @Query("SELECT * FROM image_table ORDER BY imageDateAdded DESC")
    LiveData<List<Image>> getAllImages();
}
