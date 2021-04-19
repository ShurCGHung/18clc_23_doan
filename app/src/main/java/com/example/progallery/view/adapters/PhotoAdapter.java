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
import com.example.progallery.model.entities.Media;
import com.example.progallery.view.fragments.PhotosFragment;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    List<Media> mediaList = new ArrayList<>();

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
        } else {
            return new PhotoViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_flex_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        if (PhotosFragment.displayOption == PhotosFragment.LIST) {
            holder.imageName.setText(mediaList.get(position).getMediaName());
            holder.imageDate.setText(mediaList.get(position).getMediaDateAdded());
        } else if (PhotosFragment.displayOption == PhotosFragment.FLEX) {
            ViewGroup.LayoutParams lp = holder.imageView.getLayoutParams();
            if (lp instanceof FlexboxLayoutManager.LayoutParams) {
                FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
                flexboxLp.setFlexGrow(1.0f);
            }
        }

        if (PhotosFragment.displayOption != PhotosFragment.FLEX) {
            Glide.with(holder.imageView.getContext())
                    .load(mediaList.get(position).getMediaPath())
                    .placeholder(R.color.black)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(holder.imageView);
        } else {
            Glide.with(holder.imageView.getContext())
                    .load(mediaList.get(position).getMediaPath())
                    .placeholder(R.color.black)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .override(Integer.parseInt(mediaList.get(position).getMediaWidth()) / 5, Integer.parseInt(mediaList.get(position).getMediaHeight()) / 5)
                    .into(holder.imageView);

        }
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void setMediaList(List<Media> media) {
        this.mediaList = media;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageName;
        TextView imageDate;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            if (PhotosFragment.displayOption == PhotosFragment.LIST) {
                imageName = itemView.findViewById(R.id.file_name);
                imageDate = itemView.findViewById(R.id.file_date);
            }

        }
    }
}