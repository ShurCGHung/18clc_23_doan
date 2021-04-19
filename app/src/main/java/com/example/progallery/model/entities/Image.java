package com.example.progallery.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_table")
public class Image {
    @PrimaryKey
    @NonNull
    private String imagePath;

    private String imageName;
    private String imageDateAdded;
    private String imageWidth;
    private String imageHeight;

    public Image(@NonNull String imagePath, String imageName, String imageDateAdded, String imageHeight, String imageWidth) {
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.imageDateAdded = imageDateAdded;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    @NonNull
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageDateAdded() {
        return imageDateAdded;
    }

    public void setImageDateAdded(String imageDateAdded) {
        this.imageDateAdded = imageDateAdded;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }
}
