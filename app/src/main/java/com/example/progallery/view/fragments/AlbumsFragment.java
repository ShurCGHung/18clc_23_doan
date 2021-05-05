package com.example.progallery.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.model.models.Album;
import com.example.progallery.view.adapters.AlbumAdapter;
import com.example.progallery.view.listeners.AlbumListener;
import com.example.progallery.viewmodel.AlbumViewModel;

import java.util.Objects;

public class AlbumsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private AlbumViewModel albumViewModel;
    private AlbumAdapter albumAdapter;
    private SwipeRefreshLayout layout;

    public AlbumsFragment() {
        albumViewModel = null;
        albumAdapter = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_album) {
            handleAddAlbum();
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleAddAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        final View customDialog = getLayoutInflater().inflate(R.layout.album_name_dialog, null);

        EditText editText = customDialog.findViewById(R.id.album_name_text);

        builder.setView(customDialog);
        builder.setTitle("Create album");
        builder.setPositiveButton("OK", (dialog, which) -> {
            String albumName = editText.getText().toString();
            boolean check = albumViewModel.createAlbum(albumName);
            if (check) {
                loadView();
                Toast.makeText(getContext(), "Album is created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Album is already existed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        setHasOptionsMenu(true);

        layout = view.findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);

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

        layout.post(() -> {
            layout.setRefreshing(true);
            loadView();
        });

        albumAdapter.setAlbumListener(new AlbumListener() {
            @Override
            public void onAlbumClick(Album album) {
                assert getFragmentManager() != null;
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_fragment, new PhotoForAlbumFragment(album.getAlbumName()), "PHOTO_ALBUM");
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }

            @Override
            public void onOptionAlbumClick(Album album) {
                Log.d("MY_APP", "option clicked");
            }
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
        albumViewModel = new ViewModelProvider(this, factory).get(AlbumViewModel.class);
        albumViewModel.getAlbumsObserver().observe(this, albumList -> {
            if (albumList == null) {
                Toast.makeText(getContext(), "Error in fetching data", Toast.LENGTH_SHORT).show();
            } else {
                albumAdapter.setAlbumList(albumList);
            }
        });
        albumViewModel.callService(getContext());
        layout.setRefreshing(false);
    }
}