package com.example.progallery.model.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.progallery.helpers.FetchStorage;
import com.example.progallery.model.dao.ImageDao;
import com.example.progallery.model.database.GalleryDatabase;
import com.example.progallery.model.entities.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageRepository {
    private ImageDao imageDao;
    private LiveData<List<Image>> allImages;

    public ImageRepository(Application application) {
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        imageDao = galleryDatabase.imageDao();
        getAll();
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

    public void getAll() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Image> temp = imageDao.getAllImagesSync();
            List<String> notExistFile = new ArrayList<>();
            for (Image image : temp) {
                String path = image.getImagePath();
                if (!FetchStorage.isExist(path)) {
                    notExistFile.add(path);
                }
            }
            delete(notExistFile);
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

    public void delete(List<String> imageList) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            imageDao.delete(imageList);

//            new Handler(Looper.getMainLooper()).post(() -> {
//            });
        });
    }


    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }
}
