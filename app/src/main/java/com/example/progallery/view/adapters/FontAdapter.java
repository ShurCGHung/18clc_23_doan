package com.example.progallery.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.R;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    public FontAdapter(Context context, FontAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        fontList = loadFontList();
    }

    int fontSelectedRow = -1;

    private List<String> loadFontList() {
        List<String> fontsResult = new ArrayList<>();

        fontsResult.add("cheque_black.otf");
        fontsResult.add("cheque_regular.otf");

        return fontsResult;
    }

    Context context;
    FontAdapterListener listener;
    List<String> fontList;

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.font_item, parent, false);
        return new FontViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if (fontSelectedRow == position) {
            holder.img_check.setVisibility(View.VISIBLE);
        } else {
            holder.img_check.setVisibility(View.INVISIBLE);
        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), new StringBuilder("fonts/")
        .append(fontList.get(position)).toString());

        holder.textFont.setTypeface(typeface);
        holder.fontName.setText(fontList.get(position));
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        TextView textFont, fontName;
        ImageView img_check;

        public FontViewHolder(@NonNull View itemView) {
            super(itemView);
            textFont = (TextView) itemView.findViewById(R.id.fontForText);
            fontName = (TextView) itemView.findViewById(R.id.fontNameForText);

            img_check = (ImageView) itemView.findViewById(R.id.img_check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFontSelected(fontList.get(getAdapterPosition()));
                    fontSelectedRow = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface FontAdapterListener {
        void onFontSelected(String fontName);
    }
}
