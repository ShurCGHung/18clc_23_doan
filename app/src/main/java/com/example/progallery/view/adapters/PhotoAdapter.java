package com.example.progallery.view.adapters;

import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.progallery.R;
import com.example.progallery.view.listeners.MediaListener;
import com.example.progallery.model.Media;
import com.example.progallery.view.fragments.PhotosFragment;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Media> mediaList = new ArrayList<>();
    private MediaListener mediaListener;

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
            if (Integer.parseInt(mediaList.get(position).getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                String temp = "IMAGE";
                holder.imageType.setText(temp);

            } else if (Integer.parseInt(mediaList.get(position).getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                String temp = "VIDEO";
                holder.imageType.setText(temp);
            }
        } else if (PhotosFragment.displayOption == PhotosFragment.FLEX) {
            ViewGroup.LayoutParams lp = holder.imageView.getLayoutParams();
            if (lp instanceof FlexboxLayoutManager.LayoutParams) {
                FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
                flexboxLp.setFlexGrow(1.0f);
            }
        }

        if (Integer.parseInt(mediaList.get(position).getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            Drawable drawable = ContextCompat.getDrawable(holder.imageView.getContext(), R.drawable.video_overlay);
            holder.imageView.setForeground(drawable);
        } else {
            holder.imageView.setForeground(null);
        }

        File f = new File(mediaList.get(position).getMediaPath());

        if (PhotosFragment.displayOption != PhotosFragment.FLEX) {
            Glide.with(holder.imageView.getContext())
                    .load(mediaList.get(position).getMediaPath())
                    .placeholder(R.color.black)
                    .centerCrop()
                    .signature(new MediaStoreSignature("", f.lastModified(), 0))
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(holder.imageView);
        } else {
            Glide.with(holder.imageView.getContext())
                    .load(mediaList.get(position).getMediaPath())
                    .placeholder(R.color.black)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .signature(new MediaStoreSignature("", f.lastModified(), 0))
                    .override(Integer.parseInt(mediaList.get(position).getMediaWidth()) / 4, Integer.parseInt(mediaList.get(position).getMediaHeight()) / 4)
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

    public void setMediaListener(MediaListener mediaListener) {
        this.mediaListener = mediaListener;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageName;
        TextView imageDate;
        TextView imageType;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            if (PhotosFragment.displayOption == PhotosFragment.LIST) {
                imageName = itemView.findViewById(R.id.file_name);
                imageDate = itemView.findViewById(R.id.file_date);
                imageType = itemView.findViewById(R.id.file_type);
            }

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (mediaListener != null && position != RecyclerView.NO_POSITION)
                    mediaListener.onMediaClick(mediaList.get(position));
            });
        }
    }
}