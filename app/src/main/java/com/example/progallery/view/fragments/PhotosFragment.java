package com.example.progallery.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.helpers.FetchStorage;
import com.example.progallery.model.entities.Image;
import com.example.progallery.view.adapters.PhotoAdapter;
import com.example.progallery.view.adapters.SectionedPhotoAdapter;
import com.example.progallery.viewmodel.ImageViewModel;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.List;
import java.util.Objects;

public class PhotosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final int GRID = 0;
    public static final int LIST = 1;
    public static final int FLEX = 2;

    public static int displayOption;
    private boolean showDatesBool;

    private ImageViewModel imageViewModel;
    private SwipeRefreshLayout layout;
    private PhotoAdapter photoAdapter;
    private SectionedPhotoAdapter photoAdapterByDate;

    public PhotosFragment() {
        displayOption = GRID;
        imageViewModel = null;
        showDatesBool = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_image_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem showDates = menu.findItem(R.id.show_dates);
        showDates.setVisible(displayOption == GRID);

        if (showDatesBool) {
            showDates.setTitle(R.string.hide_dates);
        } else {
            showDates.setTitle(R.string.show_dates);
        }

        if (displayOption == GRID) {
            MenuItem item = menu.findItem(R.id.grid);
            item.setChecked(true);
        } else if (displayOption == LIST) {
            MenuItem item = menu.findItem(R.id.list);
            item.setChecked(true);
        } else if (displayOption == FLEX) {
            MenuItem item = menu.findItem(R.id.flex);
            item.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.grid && displayOption != GRID) {
            displayOption = GRID;
            recreateFragment();
        } else if (id == R.id.list && displayOption != LIST) {
            displayOption = LIST;
            recreateFragment();
        } else if (id == R.id.flex && displayOption != FLEX) {
            displayOption = FLEX;
            recreateFragment();
        } else if (id == R.id.show_dates) {
            showDatesBool = !showDatesBool;
            recreateFragment();
        }
        return true;
    }

    private void recreateFragment() {
        assert this.getFragmentManager() != null;
        this.getFragmentManager().beginTransaction()
                .detach(PhotosFragment.this)
                .attach(PhotosFragment.this)
                .commit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        layout = view.findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.photo_grid_view);
        recyclerView.setHasFixedSize(true);

        if (showDatesBool) {
            photoAdapterByDate = new SectionedPhotoAdapter();
            recyclerView.setAdapter(photoAdapterByDate);
        } else {
            photoAdapter = new PhotoAdapter();
            recyclerView.setAdapter(photoAdapter);
        }

        if (displayOption == GRID) {
            View tempView = inflater.inflate(R.layout.photo_grid_item, container, false);
            ImageView tempImage = tempView.findViewById(R.id.imageView);
            int columnWidth = tempImage.getLayoutParams().width;
            int numColumn = ColumnCalculator.calculateNoOfColumns(Objects.requireNonNull(getContext()), columnWidth);

            GridLayoutManager glm = new GridLayoutManager(getContext(), numColumn);
            if (showDatesBool) {
                photoAdapterByDate.setLayoutManager(glm);
            }
            recyclerView.setLayoutManager(glm);
            recyclerView.addItemDecoration(new LayoutMarginDecoration(numColumn, getResources().getDimensionPixelSize(R.dimen._10sdp)));
        } else if (displayOption == LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new LayoutMarginDecoration(1, getResources().getDimensionPixelSize(R.dimen._10sdp)));
        }

        // THIS LINE CAUSES BUG, IT DIRECTS THE APPLICATION TO NON ARGUMENT CONSTRUCTOR
        // imageViewModel = new ViewModelProvider(getActivity()).get(ImageViewModel.class);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication());
        imageViewModel = new ViewModelProvider(this, factory).get(ImageViewModel.class);
        imageViewModel.getAllImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> imageList) {
                if (showDatesBool) {
                    photoAdapterByDate.setImageList(imageList);
                } else {
                    photoAdapter.setImageList(imageList);
                }
            }
        });

        layout.post(new Runnable() {
            @Override
            public void run() {
                layout.setRefreshing(true);
                loadView();
            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        loadView();
    }

    public void loadView() {
        layout.setRefreshing(true);
        List<Image> refetch = FetchStorage.getAllImages(Objects.requireNonNull(getContext()));
        imageViewModel.getAll();
        imageViewModel.insert(refetch);
        layout.setRefreshing(false);
    }
}