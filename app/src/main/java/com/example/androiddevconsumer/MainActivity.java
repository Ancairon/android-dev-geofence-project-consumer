package com.example.androiddevconsumer;


import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import com.example.androiddevconsumer.databinding.ActivityMainBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static Context context;

    public static Context getContext() {
        return context;
    }


    private static final String TAG = "MainActivity";

    private GoogleMap mMap;

    private final float GEOFENCE_RADIUS = 100;

    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;


    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        //setContentView(R.layout.activity_main);

        final ContentResolver resolver = this.getContentResolver();


        findViewById(R.id.button).setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "button pressed", Toast.LENGTH_LONG).show();

            mMap.clear();
            Uri uri = Uri.parse("content://com.example.androiddev/coord");
            Cursor cursor = resolver.query(uri, null, null, null, null);

            int markerId = 0;

            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {

                    //TODO here maybe you need a counter with a step of 5.

                    int id = cursor.getInt(0);

                    double lat = cursor.getDouble(1);

                    double lon = cursor.getDouble(2);

                    int action = cursor.getInt(3);

                    long timestamp = cursor.getLong(4);

                    LatLng latLng = new LatLng(lat, lon);
                    handleButtonClick(latLng, action, timestamp, markerId);
                    Log.d("OK", "CURSOR PROCESSED");
                    Toast.makeText(getApplicationContext(), "" + timestamp, Toast.LENGTH_LONG).show();
                    markerId++;
                }
                //Log.d("WTF", "");

            }
            cursor.close();
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //enableUserLocation();

        mMap.setOnMapLongClickListener(this);
    }

    private void handleButtonClick(LatLng latLng, int action, long timestamp, int id) {

        addMarker(latLng, action, timestamp, id);
        //addCircle(latLng, GEOFENCE_RADIUS);
        //addGeofence(latLng, GEOFENCE_RADIUS);
    }

    private void addMarker(LatLng latLng, int action, long timestamp, int id) {

        String geofAction;
        switch (action) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                geofAction = "GEOFENCE_TRANSITION_ENTER";

                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                geofAction = "GEOFENCE_TRANSITION_EXIT";

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }


        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("Action: " + geofAction + "\nTimestamp: " + timestamp);
        Marker m = mMap.addMarker(markerOptions);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(getApplicationContext(), "" + marker.getTitle(), Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}