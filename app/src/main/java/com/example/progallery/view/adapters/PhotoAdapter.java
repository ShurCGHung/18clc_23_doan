package com.example.progallery.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.progallery.R;
import com.example.progallery.model.entities.Image;
import com.example.progallery.view.fragments.PhotosFragment;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    List<Image> imageList = new ArrayList<>();

    public PhotoAdapter() {
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (PhotosFragment.displayOption == PhotosFragment.GRID) {
            return new PhotoViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_grid_item, parent, false));
        } else if (PhotosFragment.displayOption == PhotosFragment.LIST) {
            return new PhotoViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_list_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
                .load(imageList.get(position).getImagePath())
                .placeholder(R.color.black)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.imageView);

        if (PhotosFragment.displayOption == PhotosFragment.LIST) {
            holder.imagePath.setText(imageList.get(position).getImageName());
            holder.imageDate.setText(imageList.get(position).getImageDateAdded());
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void setImageList(List<Image> images) {
        this.imageList = images;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imagePath;
        TextView imageDate;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            if (PhotosFragment.displayOption == PhotosFragment.LIST) {
                imagePath = itemView.findViewById(R.id.file_name);
                imageDate = itemView.findViewById(R.id.file_date);
            }
        }
    }

}