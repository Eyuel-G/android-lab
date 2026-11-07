package com.example.lab1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.lab1.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPoiClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this,
                    "Map fragment not found!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        UiSettings mUiSettings = mMap.getUiSettings();

        LatLng defaultLocation = new LatLng(7.673645, 36.836937);

        mMap.addMarker(
                new MarkerOptions()
                        .position(defaultLocation)
                        .title("Jimma, Ethiopia")
        );

        mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(defaultLocation, 13f)
        );

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);

        mUiSettings.setCompassEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setIndoorLevelPickerEnabled(true);

        mMap.setOnPoiClickListener(this);

        checkLocationPermission();
    }

    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {

            enableMyLocation();

        } else {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    private void enableMyLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults
        );

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            (grantResults.length > 1 &&
                                    grantResults[1] == PackageManager.PERMISSION_GRANTED))) {

                enableMyLocation();

                Toast.makeText(
                        this,
                        "Location permission granted",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    @Override
    public void onPoiClick(@NonNull PointOfInterest poi) {

        String poiInfo = "POI: " + poi.name +
                "\nLatitude: " + poi.latLng.latitude +
                "\nLongitude: " + poi.latLng.longitude;

        binding.textv.setText(poiInfo);

        Toast.makeText(
                this,
                "Clicked: " + poi.name,
                Toast.LENGTH_SHORT
        ).show();
    }

    public void searchLocation(View view) {

        if (mMap == null) {
            Toast.makeText(
                    this,
                    "Map is not ready yet",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String inputLocation = binding.editTextSearch.getText().toString().trim();
        final String locationName = inputLocation.isEmpty() ? "Jimma" : inputLocation;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        new Thread(() -> {

            try {

                List<Address> addressList =
                        geocoder.getFromLocationName(locationName, 1);

                runOnUiThread(() -> {

                    if (addressList != null && !addressList.isEmpty()) {

                        Address address = addressList.get(0);

                        LatLng latLng = new LatLng(
                                address.getLatitude(),
                                address.getLongitude()
                        );

                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(latLng)
                                        .title(locationName)
                        );

                        mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        );

                        binding.textv.setText(
                                "Location: " + locationName +
                                        "\nLatitude: " + latLng.latitude +
                                        "\nLongitude: " + latLng.longitude
                        );

                    } else {

                        Toast.makeText(
                                MapsActivity.this,
                                "Location not found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

            } catch (IOException e) {

                runOnUiThread(() -> Toast.makeText(
                        MapsActivity.this,
                        "Geocoder error: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show());
            }

        }).start();
    }
}
