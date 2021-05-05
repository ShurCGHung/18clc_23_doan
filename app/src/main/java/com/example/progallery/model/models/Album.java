package com.example.progallery.model.models;

import androidx.annotation.NonNull;

public class Album {
    private String albumID;
    private String albumName;
    private String albumThumbnail;
    private String numberOfImages;
    private String thumbnailType;
    private String albumPath;

    public Album(String ID, String albumName, String numberOfImages, String albumThumbnail, String thumbnailType, String albumPath) {
        this.albumID = ID;
        this.albumName = albumName;
        this.numberOfImages = numberOfImages;
        this.albumThumbnail = albumThumbnail;
        this.thumbnailType = thumbnailType;
        this.albumPath = albumPath;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
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

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getThumbnailType() {
        return thumbnailType;
    }

    public void setThumbnailType(String thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    @NonNull
    @Override
    public String toString() {
        return albumID + ": " + albumName + ": " + numberOfImages + ": " + albumThumbnail + ": " + albumPath + "\n";
    }
}
