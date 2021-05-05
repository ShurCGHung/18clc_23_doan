package com.example.progallery.model;

public class Media {
    private String mediaPath;
    private String mediaName;
    private String mediaDateAdded;
    private String mediaWidth;
    private String mediaHeight;
    private String mediaType;

    public Media(String mediaPath, String mediaName, String mediaDateAdded, String mediaHeight, String mediaWidth, String mediaType) {
        this.mediaPath = mediaPath;
        this.mediaName = mediaName;
        this.mediaDateAdded = mediaDateAdded;
        this.mediaHeight = mediaHeight;
        this.mediaWidth = mediaWidth;
        this.mediaType = mediaType;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaDateAdded() {
        return mediaDateAdded;
    }

    public void setMediaDateAdded(String mediaDateAdded) {
        this.mediaDateAdded = mediaDateAdded;
    }

    public String getMediaWidth() {
        return mediaWidth;
    }

    public void setMediaWidth(String mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    public String getMediaHeight() {
        return mediaHeight;
    }

    public void setMediaHeight(String mediaHeight) {
        this.mediaHeight = mediaHeight;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
