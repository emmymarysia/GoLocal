package com.example.golocal.fragments;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MapFragment extends Fragment {

    private static final int CAMERA_ZOOM = 17;
    private static final String AUTOCOMPLETE_URL = "https://api.foursquare.com/v3/autocomplete?query=";
    private static final String SEARCH_URL = "https://api.foursquare.com/v3/places/search?query=";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Location mCurrentLocation;
    private OkHttpClient client = new OkHttpClient();
    private HashMap<Marker, BusinessDataModel> queryResultBusinesses = new HashMap<>();
    private MainActivity mainActivity;
    private Button btFilterMap;

    public MapFragment(MainActivity main, Location currentLocation) {
        mainActivity = main;
        mCurrentLocation = currentLocation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchQueryCall call = new SearchQueryCall();
                call.execute(query);
                searchView.clearFocus();
                map.clear();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3) {
                    AutocompleteCall call = new AutocompleteCall();
                    call.execute(newText);
                }
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
                String title = marker.getTitle();
                String address = marker.getSnippet();
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

    private class AutocompleteCall extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Double latitude = mCurrentLocation.getLatitude();
            Double longitude = mCurrentLocation.getLongitude();
            String latlng = df.format(latitude) + "%2C" + df.format(longitude);
            Request request = new Request.Builder()
                    .url(AUTOCOMPLETE_URL + params[0] + "&ll=" + latlng)
                    .get()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", getResources().getString(R.string.foursquare_api_key))
                    .build();

            Response response;
            String results;
            try {
                response = client.newCall(request).execute();
                results = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return results;
        }
    }

    private class SearchQueryCall extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Double latitude = mCurrentLocation.getLatitude();
            Double longitude = mCurrentLocation.getLongitude();
            String latlng = df.format(latitude) + "%2C" + df.format(longitude);
            Request request = new Request.Builder()
                    .url(SEARCH_URL + params[0] + "&ll=" + latlng + "&exclude_all_chains=true")
                    .get()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", getResources().getString(R.string.foursquare_api_key))
                    .build();


            Response response;
            String results;
            try {
                response = client.newCall(request).execute();
                results = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return results;
        }

        protected void onPostExecute(String results) {
            try {
                queryResultsFromJson(results);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void queryResultsFromJson(String data) throws JSONException {
        BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        JSONObject queryResponse = new JSONObject(data);
        LatLng mapCenter = null;
        JSONArray results = queryResponse.getJSONArray("results");
        queryResultBusinesses.clear();
        for (int i = 0; i < results.length(); i++) {
            JSONObject business = results.getJSONObject(i);
            String address = business.getJSONObject("location").getString("address");
            String foursquareId = business.getString("fsq_id");
            String latitude = business.getJSONObject("geocodes").getJSONObject("main").getString("latitude");
            String longitude = business.getJSONObject("geocodes").getJSONObject("main").getString("longitude");
            LatLng markerPosition = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            if (i == 0) {
                mapCenter = markerPosition;
            }
            String businessName = business.getString("name");
            BusinessDataModel currentBusiness = new BusinessDataModel();
            currentBusiness.setName(businessName);
            currentBusiness.setAddress(address);
            currentBusiness.setFoursquareId(foursquareId);
            Marker mapMarker = map.addMarker(new MarkerOptions()
                    .position(markerPosition)
                    .title(businessName)
                    .snippet(address)
                    .icon(defaultMarker));
            queryResultBusinesses.put(mapMarker, currentBusiness);
        }
        if (mapCenter != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, CAMERA_ZOOM));
        }
    }
}
