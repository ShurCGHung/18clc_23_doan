package com.example.progallery.model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.progallery.model.dao.AlbumDao;
import com.example.progallery.model.database.GalleryDatabase;
import com.example.progallery.model.entities.Album;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public Future<Boolean> isExists(String name) {
        CompletableFuture<Boolean> cf = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            cf.complete(albumDao.checkExist(name));
            return null;
        });

        return cf;
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }
}
