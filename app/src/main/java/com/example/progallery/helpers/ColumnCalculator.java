package com.example.progallery.helpers;

import android.content.Context;
import android.util.DisplayMetrics;

public class ColumnCalculator {
    public static int calculateNoOfColumns(Context context, int columnWidthPixels) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenPixels = displayMetrics.widthPixels;
        return  screenPixels / columnWidthPixels;
    }
}
