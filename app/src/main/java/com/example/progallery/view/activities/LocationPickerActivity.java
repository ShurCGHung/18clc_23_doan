package com.example.progallery.view.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.progallery.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.progallery.helpers.Constant.LOCATION_REQUEST_CODE;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in HCM and move the camera
        LatLng vn = new LatLng(10.762622, 106.660172);

        MarkerOptions markerOptions = new MarkerOptions().position(vn).title("Marker in HCM, VN").draggable(true);

        marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vn));

        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnMapClickListener(latLng -> {
            marker.setPosition(latLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        });

        googleMap.setOnMapLongClickListener(latLng -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LocationPickerActivity.this);

            builder.setTitle(R.string.pick_this_location);
            builder.setPositiveButton("OK", (dialog, which) -> {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat", latLng.latitude);
                returnIntent.putExtra("long", latLng.longitude);
                setResult(RESULT_OK, returnIntent);
                finish();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
