package com.example.progallery.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "album_table")
public class Album {
    @PrimaryKey(autoGenerate = true)
    private int albumID;
    private String albumName;
    private String dateCreated;
    private String albumThumbnail;
    private String numberOfImages;

    public Album(String albumName, String dateCreated, String albumThumbnail, String numberOfImages) {
        this.albumName = albumName;
        this.dateCreated = dateCreated;
        this.albumThumbnail = albumThumbnail;
        this.numberOfImages = numberOfImages;
    }

    public String getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(String numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    public String getAlbumThumbnail() {
        return albumThumbnail;
    }

    public void setAlbumThumbnail(String albumThumbnail) {
        this.albumThumbnail = albumThumbnail;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(@NonNull String albumName) {
        this.albumName = albumName;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
