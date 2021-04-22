package com.example.progallery.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.view.adapters.AlbumAdapter;
import com.example.progallery.viewmodel.AlbumViewModel;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.Objects;

public class AlbumsFragment extends Fragment {
    private AlbumViewModel albumViewModel;
    private AlbumAdapter albumAdapter;

    public AlbumsFragment() {
        albumViewModel = null;
        albumAdapter = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.album_grid_view);
        recyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter();
        recyclerView.setAdapter(albumAdapter);

        View tempView = inflater.inflate(R.layout.album_grid_item, container, false);
        ImageView tempImage = tempView.findViewById(R.id.album_thumbnail);
        int columnWidth = tempImage.getLayoutParams().width;
        int numColumn = ColumnCalculator.calculateNoOfColumns(Objects.requireNonNull(getContext()), columnWidth);

        GridLayoutManager glm = new GridLayoutManager(getContext(), numColumn);
        recyclerView.setLayoutManager(glm);
        recyclerView.addItemDecoration(new LayoutMarginDecoration(numColumn, getResources().getDimensionPixelSize(R.dimen._10sdp)));

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(Objects.requireNonNull(this.getActivity()).getApplication());
        albumViewModel = new ViewModelProvider(this, factory).get(AlbumViewModel.class);
        albumViewModel.getAllAlbums().observe(this, albumAdapter::setAlbumList);

        return view;
    }

}