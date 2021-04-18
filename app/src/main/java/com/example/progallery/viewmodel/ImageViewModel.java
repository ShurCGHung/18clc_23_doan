package com.example.progallery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.progallery.model.entities.Image;
import com.example.progallery.model.repository.ImageRepository;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository repository;
    private LiveData<List<Image>> allImages;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        repository = new ImageRepository(application);
        allImages = repository.getAllImages();
    }

    public void insert(Image image) {
        repository.insert(image);
    }

    public void insert(List<Image> imageList) {
        repository.insert(imageList);
    }

    public void update(Image image) {
        repository.update(image);
    }

    public void delete(Image image) {
        repository.delete(image);
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }
}
