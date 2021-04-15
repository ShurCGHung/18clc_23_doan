package com.example.progallery.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.progallery.R;
import com.example.progallery.activities.MainActivity;
import com.example.progallery.entities.ImageModel;

import java.util.ArrayList;

public class PhotoGridAdapter extends BaseAdapter {
    Context context;
    ArrayList<ImageModel> imageList;

    public PhotoGridAdapter(Context context, Activity activity) {
        this.context = context;
        imageList = new ArrayList<>();
        getAllImages(activity);
    }

    public void getAllImages(Activity activity){
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            ImageModel imageModel = new ImageModel();
            imageModel.setImage(absolutePathOfImage);
            Log.d("MY_APP", imageModel.getImage());
            imageList.add(imageModel);
        }
        cursor.close();
    }
    @Override
    public int getCount() {
        return imageList.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.griditem_photo, null); // inflate the layout
        ImageView photo = view.findViewById(R.id.imageView); // get the reference of ImageView
        Glide.with(context)
                .load(imageList.get(i).getImage())
                .placeholder(R.color.black)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(photo);
        return view;
    }
}
