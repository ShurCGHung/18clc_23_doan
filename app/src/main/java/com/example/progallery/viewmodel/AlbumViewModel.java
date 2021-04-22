package com.example.progallery.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.progallery.model.entities.Album;
import com.example.progallery.model.repository.AlbumRepository;

import java.util.List;

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

}
