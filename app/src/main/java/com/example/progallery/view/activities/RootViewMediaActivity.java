package com.example.progallery.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.services.MediaFetchService;
import com.example.progallery.view.fragments.ImageInfoFragment;

public class RootViewMediaActivity extends AppCompatActivity {
    protected Toolbar topToolbar;
    protected Toolbar bottomToolbar;
    protected String mediaPath;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_image_menu, menu);
        return true;
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
        } else if (id == R.id.add_to_album) {
            addToAlbum();
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
            MediaFetchService service = MediaFetchService.getInstance();
            boolean delRes = service.deleteMedia(getApplicationContext(), mediaPath);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constant.EXTRA_REQUEST, Constant.REQUEST_REMOVE_MEDIA);
            if (delRes) {
                setResult(RESULT_OK, returnIntent);
            } else {
                setResult(RESULT_CANCELED, returnIntent);
            }
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void addToAlbum() {
        // Show alert box with list view

        // get album ID (name)

        // Show alert box with copy or move

        // Copy or move media
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}