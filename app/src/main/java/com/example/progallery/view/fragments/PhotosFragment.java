package com.example.progallery.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.helpers.Constant;
import com.example.progallery.helpers.FetchStorage;
import com.example.progallery.model.entities.Media;
import com.example.progallery.view.activities.ViewImageActivity;
import com.example.progallery.view.activities.ViewVideoActivity;
import com.example.progallery.view.adapters.PhotoAdapter;
import com.example.progallery.view.adapters.SectionedPhotoAdapter;
import com.example.progallery.view.listeners.MediaListener;
import com.example.progallery.viewmodel.MediaViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;
import java.util.Objects;

public class PhotosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final int GRID = 0;
    public static final int LIST = 1;
    public static final int FLEX = 2;

    public static int displayOption;
    public static boolean showDatesBool;

    private MediaViewModel mediaViewModel;
    private SwipeRefreshLayout layout;
    private PhotoAdapter photoAdapter;
    private SectionedPhotoAdapter photoAdapterByDate;

    public PhotosFragment() {
        displayOption = GRID;
        mediaViewModel = null;
        showDatesBool = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_photos_menu, menu);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("displayOption", String.valueOf(displayOption));
        outState.putString("showDatesBool", String.valueOf(showDatesBool));
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
            showDatesBool = false;
            recreateFragment();
        } else if (id == R.id.flex && displayOption != FLEX) {
            displayOption = FLEX;
            showDatesBool = false;
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
        if (savedInstanceState != null) {
            // Restore last state
            displayOption = Integer.parseInt(savedInstanceState.getString("displayOption"));
            showDatesBool = Boolean.parseBoolean(savedInstanceState.getString("showDatesBool"));
        }

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        layout = view.findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.photo_grid_view);
        recyclerView.setHasFixedSize(true);

        if (showDatesBool) {
            photoAdapterByDate = new SectionedPhotoAdapter();
            recyclerView.setAdapter(photoAdapterByDate);

            photoAdapterByDate.setMediaListener(new MediaListener() {
                @Override
                public void onMediaClick(Media media) {
                    if (Integer.parseInt(media.getMediaType()) == 1) {
                        Intent intent = new Intent(PhotosFragment.this.getContext(), ViewImageActivity.class);
                        intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                        startActivity(intent);
                    } else if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                        Intent intent = new Intent(PhotosFragment.this.getContext(), ViewVideoActivity.class);
                        intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                        startActivity(intent);
                    }
                }
            });

        } else {
            photoAdapter = new PhotoAdapter();
            recyclerView.setAdapter(photoAdapter);

            photoAdapter.setMediaListener(media -> {
                if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    Intent intent = new Intent(PhotosFragment.this.getContext(), ViewImageActivity.class);
                    intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                    startActivity(intent);
                } else if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    Intent intent = new Intent(PhotosFragment.this.getContext(), ViewVideoActivity.class);
                    intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                    startActivity(intent);
                }
            });
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
        } else if (displayOption == LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (displayOption == FLEX) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setAlignItems(AlignItems.STRETCH);
            recyclerView.setLayoutManager(layoutManager);
        }

        // THIS LINE CAUSES BUG, IT DIRECTS THE APPLICATION TO NON ARGUMENT CONSTRUCTOR
        // mediaViewModel = new ViewModelProvider(getActivity()).get(MediaViewModel.class);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(Objects.requireNonNull(this.getActivity()).getApplication());
        mediaViewModel = new ViewModelProvider(this, factory).get(MediaViewModel.class);
        mediaViewModel.getAllImages().observe(this, mediaList -> {
            if (showDatesBool) {
                photoAdapterByDate.setImageList(mediaList);
            } else {
                photoAdapter.setMediaList(mediaList);
            }
        });

        layout.post(() -> {
            layout.setRefreshing(true);
            loadView();
        });

        return view;
    }

    @Override
    public void onRefresh() {
        loadView();
    }

    private void loadView() {
        layout.setRefreshing(true);
        List<Media> refetch = FetchStorage.getAllMedias(Objects.requireNonNull(getContext()));

        // Delete not exist files
        mediaViewModel.getAll();

        // Insert new files if needed
        mediaViewModel.insert(refetch);
        layout.setRefreshing(false);
    }
}