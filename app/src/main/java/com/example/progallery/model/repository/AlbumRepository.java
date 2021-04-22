package com.example.progallery.model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.progallery.model.dao.AlbumDao;
import com.example.progallery.model.database.GalleryDatabase;
import com.example.progallery.model.entities.Album;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumRepository {
    private AlbumDao albumDao;
    private LiveData<List<Album>> allAlbums;

    public AlbumRepository(Application application) {
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        albumDao = galleryDatabase.albumDao();
        allAlbums = albumDao.getAllAlbums();
    }

    public void insert(Album album) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            albumDao.insert(album);
        });
    }

    public void delete(Album album) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            albumDao.delete(album);
        });
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }
}
