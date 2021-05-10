package com.example.progallery.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.model.models.Album;
import com.example.progallery.model.services.MediaFetchService;
import com.example.progallery.view.adapters.AlbumAdapter;
import com.example.progallery.view.fragments.ImageInfoFragment;
import com.example.progallery.view.listeners.AlbumListener;
import com.example.progallery.viewmodel.AlbumViewModel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RootViewMediaActivity extends AppCompatActivity {
    protected Toolbar topToolbar;
    protected Toolbar bottomToolbar;
    protected String mediaPath;
    protected boolean isFavorite;
    AlbumViewModel albumViewModel;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_media_menu, menu);
        MediaFetchService service = MediaFetchService.getInstance();
        isFavorite = service.checkFavorite(RootViewMediaActivity.this, mediaPath);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        if (isFavorite) {
            menu.findItem(R.id.btnFavorite).setTitle("Unfavorite");
        } else {
            menu.findItem(R.id.btnFavorite).setTitle("Favorite");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constant.EXTRA_REQUEST, Constant.REQUEST_RETURN);
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteImage) {
            deleteMedia();
        } else if (id == R.id.btnShowInfo) {
            showImageInfo();
        } else if (id == R.id.btnAddAlbum) {
            addToAlbum();
        } else if (id == R.id.btnFavorite) {
            MediaFetchService service = MediaFetchService.getInstance();
            if (isFavorite) {
                service.removeFavorite(RootViewMediaActivity.this, mediaPath);
                Toast.makeText(RootViewMediaActivity.this, "Unfavorited", Toast.LENGTH_SHORT).show();
            } else {
                service.addFavorite(RootViewMediaActivity.this, mediaPath);
                Toast.makeText(RootViewMediaActivity.this, "Favorited", Toast.LENGTH_SHORT).show();
            }
            isFavorite = !isFavorite;
        } else if (id == R.id.btnSlideshow) {
            Intent intent = new Intent(RootViewMediaActivity.this, SlideshowActivity.class);
            intent.putExtra(Constant.EXTRA_PATH, mediaPath);
            startActivity(intent);
        } else if (id == R.id.btnLocation) {
            Intent intent = new Intent(RootViewMediaActivity.this, LocationPickerActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showImageInfo() {
        ImageInfoFragment fragment = new ImageInfoFragment(mediaPath);
        fragment.show(getSupportFragmentManager(), "Image Info");
    }

    protected void deleteMedia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RootViewMediaActivity.this);

        final View customDialog = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);

        builder.setView(customDialog);
        builder.setTitle("Delete media");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constant.EXTRA_REQUEST, Constant.REQUEST_REMOVE_MEDIA);

            File file = new File(mediaPath);
            if (file.exists()) {
                if (file.delete()) {
                    MediaFetchService service = MediaFetchService.getInstance();
                    boolean delRes = service.deleteMedia(getApplicationContext(), mediaPath);
                    if (delRes) {
                        setResult(RESULT_OK, returnIntent);
                    } else {
                        setResult(RESULT_CANCELED, returnIntent);
                    }
                } else {
                    setResult(RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void addToAlbum() {
        // Show alert box with list view
        AlertDialog.Builder builder = new AlertDialog.Builder(RootViewMediaActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.choose_album_dialog, null);
        builder.setView(customDialog);
        builder.setTitle("Choose an album");

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        RecyclerView recyclerView = customDialog.findViewById(R.id.album_pick_view);
        recyclerView.setHasFixedSize(true);

        AlbumAdapter albumAdapter = new AlbumAdapter(false);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(RootViewMediaActivity.this));

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        albumViewModel = new ViewModelProvider(this, factory).get(AlbumViewModel.class);
        albumViewModel.getAlbumsObserver().observe(this, albumList -> {
            if (albumList == null) {
                Toast.makeText(RootViewMediaActivity.this, "Error in fetching data", Toast.LENGTH_SHORT).show();
            } else {
                albumAdapter.setAlbumList(albumList);
            }
        });
        albumViewModel.callService(RootViewMediaActivity.this);

        AlertDialog dialog = builder.create();
        dialog.show();

        albumAdapter.setAlbumListener(new AlbumListener() {
            @Override
            public void onAlbumClick(Album album) {
                dialog.dismiss();

                // Show alert box with copy or move
                AlertDialog.Builder builder = new AlertDialog.Builder(RootViewMediaActivity.this);
                builder.setTitle("Choose a method");

                String[] options = {"Move to album", "Copy to album"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                moveMedia(album);
                                break;
                            case 1:
                                copyMedia(album);
                                break;
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onOptionAlbumClick(Album album) {
            }
        });
    }

    private void moveMedia(Album targetAlbum) {
        String imageName = mediaPath.substring(mediaPath.lastIndexOf('/'));
        String newPath = targetAlbum.getAlbumPath() + imageName;

        boolean fileMoved = true;
        try {
            Files.move(Paths.get(mediaPath), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            fileMoved = false;
            e.printStackTrace();
        }

        if (fileMoved) {
            MediaFetchService service = MediaFetchService.getInstance();
            boolean updateMediaStore = service.updateMediaAlbum(RootViewMediaActivity.this, mediaPath, newPath);
            if (updateMediaStore) {
                Toast.makeText(RootViewMediaActivity.this, "Moved to album", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RootViewMediaActivity.this, "Failed to move to media", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyMedia(Album targetAlbum) {
        String newTitle = mediaPath.substring(mediaPath.lastIndexOf('/'), mediaPath.lastIndexOf('.')) + "_1";
        String fileType = mediaPath.substring(mediaPath.lastIndexOf('.'));
        String newPath = targetAlbum.getAlbumPath() + newTitle + fileType;

        boolean fileCopied = true;
        try {
            Files.copy(Paths.get(mediaPath), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            fileCopied = false;
            e.printStackTrace();
        }

        if (fileCopied) {
            MediaFetchService service = MediaFetchService.getInstance();
            boolean addToMediaStore = service.addNewMedia(RootViewMediaActivity.this, newPath, newTitle);
            if (addToMediaStore) {
                Toast.makeText(RootViewMediaActivity.this, "Copied to album", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RootViewMediaActivity.this, "Failed to copy to media", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}