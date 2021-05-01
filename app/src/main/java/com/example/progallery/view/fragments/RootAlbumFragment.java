package com.example.progallery.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.progallery.R;
import com.example.progallery.view.fragments.AlbumsFragment;

public class RootAlbumFragment extends Fragment {
    public RootAlbumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_album, container, false);

        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.root_fragment, new AlbumsFragment());

        transaction.commit();

        return view;
    }
}