package com.example.progallery.helpers;


import android.content.Context;
import android.widget.Toast;

import com.ethanco.lib.abs.ICheckPasswordFilter;

public class CountCheckFilter implements ICheckPasswordFilter {
    @Override
    public boolean onCheckPassword(Context context, CharSequence password) {
        if (password.length() != 4) {
            Toast.makeText(context, "Length 4 only", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}