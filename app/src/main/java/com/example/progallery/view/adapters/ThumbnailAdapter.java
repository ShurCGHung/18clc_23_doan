package com.example.progallery.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.view.listeners.FilterFragmentListener;
import com.example.progallery.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {

    private List<ThumbnailItem> itemsList;
    private FilterFragmentListener filterListener;
    private Context context;
    private int selectedFilter;

    public ThumbnailAdapter(List<ThumbnailItem> itemsList, FilterFragmentListener filterListener, Context context) {
        this.itemsList = itemsList;
        this.filterListener = filterListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.thumbnail_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ThumbnailItem thumbnailItem = itemsList.get(position);

        holder.thumbnail.setImageBitmap(thumbnailItem.image);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                filterListener.onFilterSelected(thumbnailItem.filter);
                selectedFilter = position;
                notifyDataSetChanged();
            }
        });

        holder.filter_name.setText(thumbnailItem.filterName);

        if (selectedFilter == position) {
            holder.filter_name.setTextColor(ContextCompat.getColor(context, R.color.selected_filter));
        } else {
            holder.filter_name.setTextColor(ContextCompat.getColor(context, R.color.unselected_filter));
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView filter_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            filter_name = (TextView) itemView.findViewById(R.id.filter_name);
        }
    }
}
