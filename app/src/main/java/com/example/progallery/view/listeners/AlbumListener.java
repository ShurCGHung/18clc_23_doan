package com.example.progallery.view.listeners;

import com.example.progallery.model.models.Album;

public interface AlbumListener {
    void onAlbumClick(Album album);

    void onOptionAlbumClick(Album album, int option);
}
