package com.example.progallery.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.progallery.model.entities.Album;

import java.util.List;

@Dao
public interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Album album);

    @Delete
    void delete(Album album);

    @Query("SELECT * FROM album_table ORDER BY dateCreated DESC")
    LiveData<List<Album>> getAllAlbums();
}
