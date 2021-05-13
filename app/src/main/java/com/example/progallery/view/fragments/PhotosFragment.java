package com.example.progallery.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.helpers.Constant;
import com.example.progallery.view.activities.MainActivity;
import com.example.progallery.view.activities.SettingsActivity;
import com.example.progallery.view.activities.ViewImageActivity;
import com.example.progallery.view.activities.ViewVideoActivity;
import com.example.progallery.view.adapters.PhotoAdapter;
import com.example.progallery.view.adapters.SectionedPhotoAdapter;
import com.example.progallery.viewmodel.MediaViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.progallery.helpers.Constant.FLEX;
import static com.example.progallery.helpers.Constant.GRID;
import static com.example.progallery.helpers.Constant.LIST;
import static com.example.progallery.helpers.Constant.REQUEST_MOVE_VAULT;
import static com.example.progallery.helpers.Constant.REQUEST_REMOVE_MEDIA;
import static com.example.progallery.helpers.Constant.REQUEST_REMOVE_VAULT;
import static com.example.progallery.helpers.Constant.REQUEST_VIEW_MEDIA;

public class PhotosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private MediaViewModel mediaViewModel;
    private SwipeRefreshLayout layout;
    private PhotoAdapter photoAdapter;
    private SectionedPhotoAdapter photoAdapterByDate;

    public PhotosFragment() {
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
        menu.clear();
        inflater.inflate(R.menu.view_photos_menu, menu);
        MenuItem showDates = menu.findItem(R.id.show_dates);
        showDates.setVisible(MainActivity.displayOption == GRID);

        if (MainActivity.showDatesBool) {
            showDates.setTitle(R.string.hide_dates);
        } else {
            showDates.setTitle(R.string.show_dates);
        }
        super.onCreateOptionsMenu(menu, inflater);
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
            MainActivity.showDatesBool = false;
            recreateFragment();
        } else if (id == R.id.flex && MainActivity.displayOption != FLEX) {
            MainActivity.displayOption = FLEX;
            MainActivity.showDatesBool = false;
            recreateFragment();
        } else if (id == R.id.show_dates) {
            MainActivity.showDatesBool = !MainActivity.showDatesBool;
            recreateFragment();
        } else if (id == R.id.take_media) {
            dispatchTakePictureIntent();
            return true;
        } else if (id == R.id.take_video) {
            dispatchTakeVideoIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        setHasOptionsMenu(true);

        layout = view.findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.photo_grid_view);
        recyclerView.setHasFixedSize(true);

        if (MainActivity.showDatesBool) {
            photoAdapterByDate = new SectionedPhotoAdapter();
            recyclerView.setAdapter(photoAdapterByDate);

            photoAdapterByDate.setMediaListener(media -> {
                if (Integer.parseInt(media.getMediaType()) == 1) {
                    Intent intent = new Intent(PhotosFragment.this.getContext(), ViewImageActivity.class);
                    intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                    intent.putExtra(Constant.EXTRA_VAULT, false);
                    startActivityForResult(intent, Constant.REQUEST_VIEW_MEDIA);
                } else if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    Intent intent = new Intent(PhotosFragment.this.getContext(), ViewVideoActivity.class);
                    intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                    intent.putExtra(Constant.EXTRA_VAULT, false);
                    startActivityForResult(intent, Constant.REQUEST_VIEW_MEDIA);
                }
            });

        } else {
            photoAdapter = new PhotoAdapter();
            recyclerView.setAdapter(photoAdapter);

            photoAdapter.setMediaListener(media -> {
                if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    Intent intent = new Intent(PhotosFragment.this.getContext(), ViewImageActivity.class);
                    intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                    intent.putExtra(Constant.EXTRA_VAULT, false);
                    startActivityForResult(intent, Constant.REQUEST_VIEW_MEDIA);
                } else if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    Intent intent = new Intent(PhotosFragment.this.getContext(), ViewVideoActivity.class);
                    intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                    intent.putExtra(Constant.EXTRA_VAULT, false);
                    startActivityForResult(intent, Constant.REQUEST_VIEW_MEDIA);
                }
            });
        }

        if (MainActivity.displayOption == GRID) {
            View tempView = inflater.inflate(R.layout.photo_grid_item, container, false);
            ImageView tempImage = tempView.findViewById(R.id.imageView);
            int columnWidth = tempImage.getLayoutParams().width;
            int numColumn = ColumnCalculator.calculateNoOfColumns(requireContext(), columnWidth);

            GridLayoutManager glm = new GridLayoutManager(getContext(), numColumn);
            if (MainActivity.showDatesBool) {
                photoAdapterByDate.setLayoutManager(glm);
            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (requestCode == REQUEST_VIEW_MEDIA) {
            int requestCodeFromIntent = data.getIntExtra(Constant.EXTRA_REQUEST, -1);
            Log.d("MY_APP", String.valueOf(requestCodeFromIntent));
            if (requestCodeFromIntent == REQUEST_REMOVE_MEDIA) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), "Media is deleted successfully", Toast.LENGTH_SHORT).show();
                    loadView();
                } else {
                    Toast.makeText(getContext(), "Failed to delete media", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCodeFromIntent == REQUEST_MOVE_VAULT) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), "Moved to vault", Toast.LENGTH_SHORT).show();
                    loadView();
                } else {
                    Toast.makeText(getContext(), "Failed to move to vault", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCodeFromIntent == REQUEST_REMOVE_VAULT) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), "Removed from vault", Toast.LENGTH_SHORT).show();
                    loadView();
                } else {
                    Toast.makeText(getContext(), "Failed to remove from vault", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void loadView() {
        layout.setRefreshing(true);

        // THIS LINE CAUSES BUG, IT DIRECTS THE APPLICATION TO NON ARGUMENT CONSTRUCTOR
        // mediaViewModel = new ViewModelProvider(getActivity()).get(MediaViewModel.class);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this.requireActivity().getApplication());
        mediaViewModel = new ViewModelProvider(this, factory).get(MediaViewModel.class);
        mediaViewModel.getMediasObserver().observe(getViewLifecycleOwner(), mediaList -> {
            if (mediaList == null) {
                Toast.makeText(getContext(), "Error in fetching data", Toast.LENGTH_SHORT).show();
            } else {
                if (MainActivity.showDatesBool) {
                    photoAdapterByDate.setImageList(mediaList);
                } else {
                    photoAdapter.setMediaList(mediaList);
                }
            }
        });
        mediaViewModel.callService(getContext());
        layout.setRefreshing(false);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(takePictureIntent);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        if (takeVideoIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(takeVideoIntent);
        }
    }
}