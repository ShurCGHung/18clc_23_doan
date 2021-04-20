package com.example.progallery.helpers;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.widget.Toolbar;

public class ToolbarAnimator {
    public static void hideTopToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
    }

    public static void showTopToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    public static void hideBottomToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(toolbar.getTop()).setInterpolator(new AccelerateInterpolator()).start();
    }

    public static void showBottomToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

}
