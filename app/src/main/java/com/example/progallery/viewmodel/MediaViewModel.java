package com.example.progallery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.progallery.model.entities.Media;
import com.example.progallery.model.repository.MediaRepository;

import java.util.List;

public class MediaViewModel extends AndroidViewModel {
    private final MediaRepository repository;
    private final LiveData<List<Media>> allMedias;

    public MediaViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaRepository(application);
        allMedias = repository.getAllMedias();
    }

    public void insert(Media media) {
        repository.insert(media);
    }

    public void insert(List<Media> mediaList) {
        repository.insert(mediaList);
    }

    public void update(Media media) {
        repository.update(media);
    }

    public void delete(Media media) {
        repository.delete(media);
    }

    public void delete(List<String> mediaList) {
        repository.delete(mediaList);
    }

    public void getAll() {
        repository.getAll();
    }

    public LiveData<List<Media>> getAllImages() {
        return allMedias;
    }
}
