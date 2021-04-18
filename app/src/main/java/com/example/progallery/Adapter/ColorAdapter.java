package com.example.progallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener adapterListener;

    public ColorAdapter(Context context, List<Integer> colorList, ColorAdapterListener adapterListener) {
        this.context = context;
        this.colorList = colorList;
        this.adapterListener = adapterListener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_items, parent, false);
        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.color_section.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        public CardView color_section;

        public ColorViewHolder(View itemView) {
            super(itemView);
            color_section = (CardView) itemView.findViewById(R.id.colorSection);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ColorAdapterListener {
        void onColorSelected(int color);
    }
}
