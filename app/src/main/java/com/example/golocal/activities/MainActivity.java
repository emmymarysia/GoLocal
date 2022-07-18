package com.example.golocal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.golocal.fragments.GuidesFragment;
import com.example.golocal.fragments.MapFragment;
import com.example.golocal.fragments.ProfileFragment;
import com.example.golocal.R;
import com.example.golocal.models.BusinessDataModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final static String KEY_LOCATION = "location";
    private final static int MIN_TIME_INTERVAL = 10;
    private final static int MIN_DISTANCE = 1;

    private BottomNavigationView bottomNavigationView;
    private LocationManager locationManager;
    private Location currentLocation;
    private ParseUser currentUser;
    public MapFragment mapFragment;
    public GuidesFragment guidesFragment;
    public ProfileFragment profileFragment;
    public final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.keySet().contains(KEY_LOCATION)) {
            currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, MIN_TIME_INTERVAL, MIN_DISTANCE, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
                mapFragment.updateLocation(location);
            }


        });

        currentUser = ParseUser.getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_guides:
                        if (guidesFragment == null) {
                            guidesFragment = new GuidesFragment(MainActivity.this);
                        }
                        fragment = guidesFragment;
                        break;
                    case R.id.action_profile:
                        if (profileFragment == null) {
                            profileFragment = new ProfileFragment(ParseUser.getCurrentUser());
                        }
                        fragment = profileFragment;
                        break;
                    case R.id.action_map:
                    default:
                        if (mapFragment == null ){
                            mapFragment = new MapFragment(MainActivity.this, currentLocation);
                        }
                        fragment = mapFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_map);
    }
}