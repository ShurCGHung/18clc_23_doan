package com.example.progallery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.R;
import com.example.progallery.adapters.PhotoGridAdapter;
import com.example.progallery.helpers.ColumnCalculator;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

public class PhotosFragmentGrid extends Fragment {
    RecyclerView recyclerView;

    public PhotosFragmentGrid() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_grid, container, false);

        View tempView = inflater.inflate(R.layout.griditem_photo, container, false);
        ImageView tempImage = tempView.findViewById(R.id.imageView);
        int columnWidth = tempImage.getLayoutParams().width;
        int numColumn = ColumnCalculator.calculateNoOfColumns(getContext(), columnWidth);
        recyclerView = view.findViewById(R.id.photo_grid_view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumn));
        recyclerView.addItemDecoration(new LayoutMarginDecoration(numColumn, getResources().getDimensionPixelSize(R.dimen._10sdp)));

        PhotoGridAdapter photoGridAdapter = new PhotoGridAdapter(getContext());
        recyclerView.setAdapter(photoGridAdapter);

        return view;
    }
}