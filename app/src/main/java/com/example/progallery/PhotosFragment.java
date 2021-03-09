package com.example.progallery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class PhotosFragment extends Fragment {

    GridView gridView;
    int[] photos = {
            R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo,
    };

    public PhotosFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        gridView = (GridView) view.findViewById(R.id.photo_grid_view);

        PhotoAdapter photoAdapter = new PhotoAdapter(getActivity().getApplicationContext(), photos);
        gridView.setAdapter(photoAdapter);

        return view;
    }
}