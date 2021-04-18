package com.example.progallery.Interface;

public interface EditImageFragmentListener {
    void onBrightnessChanged(int brightnessValue);
    void onContrastChanged(float contrastValue);
    void onSaturationChanged(float saturationValue);
    void onEditStarted();
    void onEditCompleted();
}
