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

    public Image(@NonNull String imagePath, String imageName, String imageDateAdded) {
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.imageDateAdded = imageDateAdded;
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
}
