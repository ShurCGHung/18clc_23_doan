package com.example.progallery.model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.progallery.helpers.FetchStorage;
import com.example.progallery.model.dao.ImageDao;
import com.example.progallery.model.database.GalleryDatabase;
import com.example.progallery.model.entities.Image;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageRepository {
    private ImageDao imageDao;
    private LiveData<List<Image>> allImages;

    public ImageRepository(Application application) {
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        imageDao = galleryDatabase.imageDao();
        allImages = imageDao.getAllImages();
    }

    public void insert(Image image) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            imageDao.insert(image);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void insert(List<Image> imageList) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            imageDao.insert(imageList);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void update(Image image) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            imageDao.insert(image);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public void delete(Image image) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            imageDao.delete(image);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }
}
