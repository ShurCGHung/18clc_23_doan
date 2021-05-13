package com.example.progallery.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.helpers.LocaleHelpers;
import com.example.progallery.view.adapters.PageAdapter;
import com.example.progallery.view.fragments.PhotosFragment;
import com.example.progallery.view.fragments.RootAlbumFragment;
import com.example.progallery.view.fragments.RootHighlightFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_READWRITE_PERMISSION_CODE = 101;

    public static int displayOption;
    public static boolean showDatesBool;
    public static TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    private String initialLocale;

    PageAdapter pageAdapter;
    private List<String> permission = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayOption = Constant.GRID;
        showDatesBool = false;

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.RECORD_AUDIO);
        }

        if (permission.size() != 0) {
            ActivityCompat.requestPermissions(MainActivity.this, permission.toArray(new String[0]), MY_READWRITE_PERMISSION_CODE);
        } else {
            init();
        }
        sharedPreferences = getSharedPreferences(Constant.KEY_PREF_LANGUAGE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Constant.language)) {
            initialLocale = sharedPreferences.getString(Constant.language, "");
        }
    }

    public void init() {
        pageAdapter = new PageAdapter(getSupportFragmentManager(), 0);
        pageAdapter.addFragment(new PhotosFragment(), getResources().getString(R.string.photo_tab));
        pageAdapter.addFragment(new RootAlbumFragment(), getResources().getString(R.string.album_tab));
        pageAdapter.addFragment(new RootHighlightFragment(), getResources().getString(R.string.highlight_tab));

        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSmoothScrollingEnabled(true);

        viewPager.addOnPageChangeListener((new TabLayout.TabLayoutOnPageChangeListener(tabLayout)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean approveAll = true;
        if (requestCode == MY_READWRITE_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        approveAll = false;
                    }
                }
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        approveAll = false;
                    }
                }
            }

            if (approveAll) {
                init();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelpers.onAttach(base));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (initialLocale != null && !initialLocale.equals(LocaleHelpers.getPersistedLocale(this))) {
            recreate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}