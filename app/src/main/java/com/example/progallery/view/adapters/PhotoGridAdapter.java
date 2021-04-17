package com.example.progallery.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.progallery.R;
import com.example.progallery.model.entities.Image;

import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder> {
    List<Image> imageList = new ArrayList<>();

    public PhotoGridAdapter() {
    }

    @NonNull
    @Override
    public PhotoGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoGridViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.griditem_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoGridViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
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

    public void setImageList(List<Image> images) {
        this.imageList = images;
        notifyDataSetChanged();
    }

    static class PhotoGridViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoGridViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}