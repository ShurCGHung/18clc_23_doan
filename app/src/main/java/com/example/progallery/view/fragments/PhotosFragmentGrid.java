package com.example.progallery.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.model.entities.Image;
import com.example.progallery.view.adapters.PhotoGridAdapter;
import com.example.progallery.viewmodel.ImageViewModel;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.List;

public class PhotosFragmentGrid extends Fragment {
    private ImageViewModel imageViewModel;

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

        RecyclerView recyclerView = view.findViewById(R.id.photo_grid_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumn));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LayoutMarginDecoration(numColumn, getResources().getDimensionPixelSize(R.dimen._10sdp)));

        PhotoGridAdapter photoGridAdapter = new PhotoGridAdapter();
        recyclerView.setAdapter(photoGridAdapter);

        // THIS LINE CAUSES BUG, IT DIRECTS THE APPLICATION TO NON ARGUMENT CONSTRUCTOR
        // imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication());
        imageViewModel = new ViewModelProvider(this, factory).get(ImageViewModel.class);
        imageViewModel.getAllImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> imageList) {
                photoGridAdapter.setImageList(imageList);
            }
        });

        return view;
    }
}