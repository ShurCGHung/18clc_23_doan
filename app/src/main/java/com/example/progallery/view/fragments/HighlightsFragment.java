package com.example.progallery.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.progallery.helpers.ExpandableHeightGridView;
import com.example.progallery.R;
import com.example.progallery.view.adapters.AlbumAdapter;

import java.util.Objects;

public class HighlightsFragment extends Fragment {

    ExpandableHeightGridView peopleGView;
    ExpandableHeightGridView locationGView;
    ExpandableHeightGridView favoriteGView;

    int[] photosPeople = {
            R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo
    };

    int[] photos_Location = {
            R.drawable.photo, R.drawable.photo, R.drawable.photo
    };

    int[] photos_Favorite = {
            R.drawable.photo
    };

    public HighlightsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_highlights, container, false);

        peopleGView = (ExpandableHeightGridView) view.findViewById(R.id.people_grid);
        locationGView = (ExpandableHeightGridView) view.findViewById(R.id.location_grid);
        favoriteGView = (ExpandableHeightGridView) view.findViewById(R.id.favorite_grid);

        AlbumAdapter albumAdapter = new AlbumAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), photosPeople);
        peopleGView.setAdapter(albumAdapter);

        albumAdapter = new AlbumAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), photos_Location);
        locationGView.setAdapter(albumAdapter);

        albumAdapter = new AlbumAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), photos_Favorite);
        favoriteGView.setAdapter(albumAdapter);

        return view;

    }
}