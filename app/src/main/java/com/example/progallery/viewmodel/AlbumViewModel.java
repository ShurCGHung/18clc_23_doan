package com.example.progallery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.progallery.model.entities.Album;
import com.example.progallery.model.repository.AlbumRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AlbumViewModel extends AndroidViewModel {
    private final AlbumRepository repository;
    private final LiveData<List<Album>> allAlbums;

    public AlbumViewModel(@NonNull Application application) {
        super(application);
        repository = new AlbumRepository(application);
        allAlbums = repository.getAllAlbums();
    }

    public void insert(Album album) {
        repository.insert(album);
    }

    public void update(Album album) {
        repository.insert(album);
    }

    public void delete(Album album) {
        repository.delete(album);
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }

    public boolean isExist(String name) throws ExecutionException, InterruptedException {
        Future<Boolean> fb = repository.isExists(name);
        return fb.get();
    }

}
