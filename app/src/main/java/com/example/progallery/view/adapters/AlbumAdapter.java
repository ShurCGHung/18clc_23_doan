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
import com.example.progallery.model.entities.Album;
import com.example.progallery.view.listeners.AlbumListener;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<Album> albumList = new ArrayList<>();
    private AlbumListener albumListener;

    public AlbumAdapter() {
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.albumName.setText(albumList.get(position).getAlbumName());
        holder.imageCount.setText(albumList.get(position).getNumberOfImages());
        if (Integer.parseInt(albumList.get(position).getNumberOfImages()) == 0) {
            Glide.with(holder.imageView.getContext())
                    .load(R.color.gray)
                    .placeholder(R.color.gray)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(holder.imageView);
        } else {
            Glide.with(holder.imageView.getContext())
                    .load(albumList.get(position).getAlbumThumbnail())
                    .placeholder(R.color.black)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }


    public void setAlbumListener(AlbumListener albumListener) {
        this.albumListener = albumListener;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView albumName;
        TextView imageCount;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.album_thumbnail);
            albumName = itemView.findViewById(R.id.album_name);
            imageCount = itemView.findViewById(R.id.count_images);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (albumListener != null && position != RecyclerView.NO_POSITION)
                    albumListener.onAlbumClick(albumList.get(position));
            });
        }
    }
}

