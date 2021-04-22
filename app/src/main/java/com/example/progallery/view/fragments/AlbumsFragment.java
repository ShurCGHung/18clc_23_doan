package com.example.progallery.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.model.entities.Album;
import com.example.progallery.view.adapters.AlbumAdapter;
import com.example.progallery.viewmodel.AlbumViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class AlbumsFragment extends Fragment {
    private AlbumViewModel albumViewModel;
    private AlbumAdapter albumAdapter;
    private Timer timer;

    public AlbumsFragment() {
        albumViewModel = null;
        albumAdapter = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_album_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_album) {
            handleAddAlbum();
        }

        return true;
    }

    private void handleAddAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        final View customDialog = getLayoutInflater().inflate(R.layout.album_name_dialog, null);

        EditText editText = customDialog.findViewById(R.id.album_name_text);
        TextView warningText = customDialog.findViewById(R.id.warning_text);

        builder.setView(customDialog);
        builder.setTitle("Create album");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = editText.getText().toString();
                try {
                    boolean isExist = albumViewModel.isExist(albumName);
                    if (isExist) {
                        Toast.makeText(getContext(), "Existed album name", Toast.LENGTH_SHORT).show();
                    } else {
                        addAlbum(albumName);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();


        dialog.show();
    }

    private void addAlbum(String albumName) {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
        String formattedDate = sdf.format(new Date());
        Album newAlbum = new Album(albumName, formattedDate);
        albumViewModel.insert(newAlbum);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

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

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(Objects.requireNonNull(this.getActivity()).getApplication());
        albumViewModel = new ViewModelProvider(this, factory).get(AlbumViewModel.class);
        albumViewModel.getAllAlbums().observe(this, albumAdapter::setAlbumList);

        return view;
    }


}