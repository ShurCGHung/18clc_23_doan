package com.example.progallery.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.progallery.R;
import com.example.progallery.helpers.Converter;
import com.example.progallery.model.entities.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SectionedPhotoAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {
    ArrayList<String> dates = new ArrayList<>();
    HashMap<String, List<Image>> hashMap = new HashMap<>();

    public SectionedPhotoAdapter() {
    }

    public void setImageList(List<Image> images) {
        dates.clear();

        hashMap = Converter.toHashMap(images);
        for (Map.Entry<String, List<Image>> pair : hashMap.entrySet()) {
            dates.add((String) pair.getKey());
        }
        notifyDataSetChanged();
    }


    @Override
    public int getSectionCount() {
        return dates.size();
    }

    @Override
    public int getItemCount(int section) {
        return hashMap.get(dates.get(section)).size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        SectionViewHolder sectionViewHolder = (SectionViewHolder) viewHolder;
        sectionViewHolder.textView.setText(dates.get(i));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i1, int i2) {
        ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;

        Glide.with(contentViewHolder.imageView.getContext())
                .load(Objects.requireNonNull(hashMap.get(dates.get(i))).  get(i1).getImagePath())
                .placeholder(R.color.black)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(contentViewHolder.imageView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_header, parent, false);
            return new SectionViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_grid_item, parent, false);
            return new ContentViewHolder(v);
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.date_header);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
