package com.example.golocal.fragments;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.golocal.AsyncTasks.PlacesAsyncCall;
import com.example.golocal.AsyncTasks.SearchAsyncCall;
import com.example.golocal.R;
import com.example.golocal.activities.MainActivity;
import com.example.golocal.models.BusinessDataModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MapFragment extends Fragment {

    public static final int CAMERA_ZOOM = 17;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Location mCurrentLocation;
    public HashMap<Marker, BusinessDataModel> queryResultBusinesses = new HashMap<>();
    private MainActivity mainActivity;
    private Button btFilterMap;
    private PlacesAsyncCall call = new PlacesAsyncCall();

    public MapFragment(MainActivity main, Location currentLocation) {
        mainActivity = main;
        mCurrentLocation = currentLocation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String businessFoursquareId = intent.getData().getLastPathSegment();
            call.setMapFragment(this);
            call.execute(businessFoursquareId, getString(R.string.foursquare_api_key), PlacesAsyncCall.FROM_MAP_FRAGMENT);
        }
    }

    public void addMarker(LatLng markerPosition, BusinessDataModel businessDataModel) {
        BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        Marker mapMarker = map.addMarker(new MarkerOptions()
                .position(markerPosition)
                .title(businessDataModel.getName())
                .snippet(businessDataModel.getAddress())
                .icon(defaultMarker));
        queryResultBusinesses.put(mapMarker, businessDataModel);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 17);
        map.moveCamera(cameraUpdate);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        SearchManager searchManager = (SearchManager) mainActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(mainActivity.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Double latitude = mCurrentLocation.getLatitude();
                Double longitude = mCurrentLocation.getLongitude();
                String userLocation = df.format(latitude) + "%2C" + df.format(longitude);
                SearchAsyncCall call = new SearchAsyncCall();
                call.setGoogleMap(map);
                call.setMapFragment(MapFragment.this);
                call.execute(query, userLocation, getResources().getString(R.string.foursquare_api_key));
                searchView.clearFocus();
                map.clear();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMapIfNeeded();
        btFilterMap = view.findViewById(R.id.btFilterMap);
        btFilterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement listener for filter button
            }
        });
    }

    public void updateLocation(Location location) {
        mCurrentLocation = location;
        if (this.map != null) {
            updateLocationMarker();
        }
    }

    protected void setUpMapIfNeeded() {
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(mainActivity, "Map is null", Toast.LENGTH_SHORT).show();
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            updateLocationMarker();
        } else {
            Toast.makeText(mainActivity, "Map is null", Toast.LENGTH_SHORT).show();
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                BusinessDataModel clickedBusiness = queryResultBusinesses.get(marker);
                BusinessDetailFragment businessDetailFragment = new BusinessDetailFragment(clickedBusiness);
                FragmentTransaction fragmentTransaction = mainActivity.fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, businessDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return false;
            }
        });
    }


    private void updateLocationMarker() {
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM);
            map.animateCamera(cameraUpdate);
        }
    }
}
