package com.example.progallery.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener adapterListener;

    public ColorAdapter(Context context, ColorAdapterListener adapterListener) {
        this.context = context;
        this.colorList = getColorList();
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

    public List<Integer> getColorList() {
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#f9bed6"));
        colorList.add(Color.parseColor("#ffebf6"));
        colorList.add(Color.parseColor("#ffd3e6"));
        colorList.add(Color.parseColor("#f5cee5"));
        colorList.add(Color.parseColor("#fed2ea"));
        colorList.add(Color.parseColor("#cff1f8"));
        colorList.add(Color.parseColor("#93c4f6"));
        colorList.add(Color.parseColor("#e91e63"));
        colorList.add(Color.parseColor("#00ebc2"));
        colorList.add(Color.parseColor("#cab8b0"));
        colorList.add(Color.parseColor("#c5a89d"));
        colorList.add(Color.parseColor("#b19d97"));
        colorList.add(Color.parseColor("#660000"));
        colorList.add(Color.parseColor("#9467f9"));
        colorList.add(Color.parseColor("#ffd9ea"));
        colorList.add(Color.parseColor("#ffb6c1"));
        colorList.add(Color.parseColor("#00ebc2"));
        colorList.add(Color.parseColor("#b7f7ff"));

        return colorList;
    }

    public interface ColorAdapterListener {
        void onColorSelected(int color);
    }
}
