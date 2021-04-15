package com.example.progallery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.progallery.R;
import com.example.progallery.adapters.PhotoGridAdapter;

import java.util.Objects;

public class PhotosFragmentGrid extends Fragment {
    GridView gridView;

    public PhotosFragmentGrid() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos_grid, container, false);

        gridView = view.findViewById(R.id.photo_grid_view);
        PhotoGridAdapter photoGridAdapter = new PhotoGridAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), getActivity());
        gridView.setAdapter(photoGridAdapter);

        return view;
    }
}