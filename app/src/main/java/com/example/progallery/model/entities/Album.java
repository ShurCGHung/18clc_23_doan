package com.example.progallery.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Album {
    @PrimaryKey
    @NonNull
    private String albumName;

    public Album(@NonNull String albumName) {
        this.albumName = albumName;
    }

    @NonNull
    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(@NonNull String albumName) {
        this.albumName = albumName;
    }
}
