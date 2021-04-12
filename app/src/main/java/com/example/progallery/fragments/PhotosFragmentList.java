package com.example.progallery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.progallery.R;
import com.example.progallery.adapters.PhotoListAdapter;

import java.util.Objects;

public class PhotosFragmentList extends Fragment {

    GridView gridView;
    int[] photos = {
            R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo,
    };

    public PhotosFragmentList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos_list, container, false);

        gridView = (GridView) view.findViewById(R.id.photo_list_view);

        PhotoListAdapter photoAdapter = new PhotoListAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), photos);
        gridView.setAdapter(photoAdapter);

        return view;
    }
}