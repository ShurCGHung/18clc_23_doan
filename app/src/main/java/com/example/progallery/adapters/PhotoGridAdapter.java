package com.example.progallery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.progallery.R;
import com.example.progallery.entities.ImageModel;
import com.example.progallery.helpers.Getter;

import java.util.List;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder> {
    Context context;
    List<ImageModel> imageList;

    public PhotoGridAdapter(Context context) {
        this.imageList = Getter.getAllImages(context);
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoGridViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.griditem_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoGridViewHolder holder, int position) {
        Glide.with(context)
                .load(imageList.get(position).getImagePath())
                .placeholder(R.color.black)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    
    static class PhotoGridViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoGridViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}