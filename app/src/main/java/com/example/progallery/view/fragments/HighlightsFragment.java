package com.example.progallery.view.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.progallery.R;
import com.example.progallery.model.models.Album;
import com.example.progallery.viewmodel.AlbumViewModel;

import java.util.Objects;

public class HighlightsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;
    private SwipeRefreshLayout layout;
    private AlbumViewModel albumViewModel;

    public HighlightsFragment() {
        albumViewModel = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_highlights, container, false);
        layout = view.findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);

        layout.post(() -> {
            layout.setRefreshing(true);
            loadView(view);
        });

        return view;
    }

    private void setView(View view, Album favoriteAlbum) {
        View includedFavorite = view.findViewById(R.id.favorite_album);
        TextView title = includedFavorite.findViewById(R.id.album_name);
        TextView countMedias = includedFavorite.findViewById(R.id.count_images);
        ImageView thumbnail = includedFavorite.findViewById(R.id.album_thumbnail);
        ImageView optionButton = includedFavorite.findViewById(R.id.album_options);
        optionButton.setVisibility(View.GONE);
        title.setText(getResources().getString(R.string.favorite));
        countMedias.setText(favoriteAlbum.getNumberOfImages());

        if (favoriteAlbum.getThumbnailType() != null) {
            if (Integer.parseInt(favoriteAlbum.getThumbnailType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                Drawable drawable = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.video_overlay);
                thumbnail.setForeground(drawable);
            } else {
                thumbnail.setForeground(null);
            }
        } else {
            thumbnail.setForeground(null);
        }

        Glide.with(Objects.requireNonNull(getContext()))
                .load(favoriteAlbum.getAlbumThumbnail())
                .placeholder(R.color.gray)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(thumbnail);

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_highlight_fragment, new PhotoForAlbumFragment(null), "PHOTO_FAVORITE");
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }

    private void loadView(View view) {
        layout.setRefreshing(true);

        // THIS LINE CAUSES BUG, IT DIRECTS THE APPLICATION TO NON ARGUMENT CONSTRUCTOR
        // mediaViewModel = new ViewModelProvider(getActivity()).get(MediaViewModel.class);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(Objects.requireNonNull(this.getActivity()).getApplication());
        albumViewModel = new ViewModelProvider(this, factory).get(AlbumViewModel.class);
        albumViewModel.getFavoriteObserver().observe(this, album -> {
            if (album == null) {
                Toast.makeText(getContext(), "Error in fetching data", Toast.LENGTH_SHORT).show();
            } else {
                setView(view, album);
            }
        });
        albumViewModel.callFavoriteService(getContext());
        layout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadView(view);
    }
}