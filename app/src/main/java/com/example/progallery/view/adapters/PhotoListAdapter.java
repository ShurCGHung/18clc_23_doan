package com.example.progallery.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.progallery.R;

public class PhotoListAdapter extends BaseAdapter {
    Context context;
    int[] photos;
    LayoutInflater inflater;

    public PhotoListAdapter(Context context, int[] photos) {
        this.context = context;
        this.photos = photos;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return photos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listitem_photo, null); // inflate the layout
        ImageView photo = (ImageView) view.findViewById(R.id.grid_image_view); // get the reference of ImageView
        photo.setImageResource(R.drawable.photo); // set logo images
        return view;
    }
}
