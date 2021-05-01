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
import android.widget.Toast;

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
import com.example.progallery.view.activities.MainActivity;
import com.example.progallery.view.activities.ViewImageActivity;
import com.example.progallery.view.activities.ViewVideoActivity;
import com.example.progallery.view.adapters.PhotoAdapter;
import com.example.progallery.viewmodel.MediaViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.Objects;

import static com.example.progallery.helpers.Constant.FLEX;
import static com.example.progallery.helpers.Constant.GRID;
import static com.example.progallery.helpers.Constant.LIST;

public class PhotoForAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String albumName;
    private MediaViewModel mediaViewModel;
    private SwipeRefreshLayout layout;
    private PhotoAdapter photoAdapter;

    public PhotoForAlbumFragment(String albumName) {
        this.albumName = albumName;
        mediaViewModel = null;
        MainActivity.showDatesBool = false;
        MainActivity.displayOption = GRID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_photo_for_album_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if (MainActivity.displayOption == GRID) {
            MenuItem item = menu.findItem(R.id.grid);
            item.setChecked(true);
        } else if (MainActivity.displayOption == LIST) {
            MenuItem item = menu.findItem(R.id.list);
            item.setChecked(true);
        } else if (MainActivity.displayOption == FLEX) {
            MenuItem item = menu.findItem(R.id.flex);
            item.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.grid && MainActivity.displayOption != GRID) {
            MainActivity.displayOption = GRID;
            recreateFragment();
        } else if (id == R.id.list && MainActivity.displayOption != LIST) {
            MainActivity.displayOption = LIST;
            recreateFragment();
        } else if (id == R.id.flex && MainActivity.displayOption != FLEX) {
            MainActivity.displayOption = FLEX;
            recreateFragment();
        }
        return true;
    }

    private void recreateFragment() {
        assert this.getFragmentManager() != null;
        this.getFragmentManager().beginTransaction()
                .detach(PhotoForAlbumFragment.this)
                .attach(PhotoForAlbumFragment.this)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        layout = view.findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.photo_grid_view);
        recyclerView.setHasFixedSize(true);

        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        photoAdapter.setMediaListener(media -> {
            if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                Intent intent = new Intent(PhotoForAlbumFragment.this.getContext(), ViewImageActivity.class);
                intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                startActivity(intent);
            } else if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                Intent intent = new Intent(PhotoForAlbumFragment.this.getContext(), ViewVideoActivity.class);
                intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                startActivity(intent);
            }
        });

        if (MainActivity.displayOption == GRID) {
            View tempView = inflater.inflate(R.layout.photo_grid_item, container, false);
            ImageView tempImage = tempView.findViewById(R.id.imageView);
            int columnWidth = tempImage.getLayoutParams().width;
            int numColumn = ColumnCalculator.calculateNoOfColumns(Objects.requireNonNull(getContext()), columnWidth);

            GridLayoutManager glm = new GridLayoutManager(getContext(), numColumn);
            recyclerView.setLayoutManager(glm);
        } else if (MainActivity.displayOption == LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (MainActivity.displayOption == FLEX) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setAlignItems(AlignItems.STRETCH);
            recyclerView.setLayoutManager(layoutManager);
        }

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

        // THIS LINE CAUSES BUG, IT DIRECTS THE APPLICATION TO NON ARGUMENT CONSTRUCTOR
        // mediaViewModel = new ViewModelProvider(getActivity()).get(MediaViewModel.class);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(Objects.requireNonNull(this.getActivity()).getApplication());
        mediaViewModel = new ViewModelProvider(this, factory).get(MediaViewModel.class);
        mediaViewModel.getMediasObserver().observe(this, mediaList -> {
            if (mediaList == null) {
                Toast.makeText(getContext(), "Error in fetching data", Toast.LENGTH_SHORT).show();
            } else {
                photoAdapter.setMediaList(mediaList);
            }
        });
        mediaViewModel.callServiceForAlbum(getContext(), albumName);
        layout.setRefreshing(false);
    }
}