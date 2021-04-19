package com.example.progallery.model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.progallery.helpers.FetchStorage;
import com.example.progallery.model.dao.MediaDao;
import com.example.progallery.model.database.GalleryDatabase;
import com.example.progallery.model.entities.Media;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaRepository {
    private MediaDao mediaDao;
    private LiveData<List<Media>> allMedias;

    public MediaRepository(Application application) {
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        mediaDao = galleryDatabase.mediaDao();
        getAll();
        allMedias = mediaDao.getAllMedias();
    }

    public void insert(Media media) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mediaDao.insert(media);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void insert(List<Media> mediaList) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mediaDao.insert(mediaList);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void getAll() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Media> temp = mediaDao.getAllMediasSync();
            List<String> notExistFile = new ArrayList<>();
            for (Media media : temp) {
                String path = media.getMediaPath();
                if (!FetchStorage.isExist(path)) {
                    notExistFile.add(path);
                }
            }
            delete(notExistFile);
//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void update(Media media) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mediaDao.insert(media);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void delete(Media media) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mediaDao.delete(media);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void delete(List<String> mediaList) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mediaDao.delete(mediaList);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }


    public LiveData<List<Media>> getAllMedias() {
        return allMedias;
    }
}
