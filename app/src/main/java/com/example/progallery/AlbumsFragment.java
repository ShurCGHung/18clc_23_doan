package com.example.progallery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Objects;

public class AlbumsFragment extends Fragment {
    GridView gridView;

    int[] photos = {
            R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo, R.drawable.photo,
    };

    public AlbumsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        gridView = (GridView) view.findViewById(R.id.album_grid_view);

        AlbumAdapter albumAdapter = new AlbumAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), photos);
        gridView.setAdapter(albumAdapter);

        return view;

    }
}