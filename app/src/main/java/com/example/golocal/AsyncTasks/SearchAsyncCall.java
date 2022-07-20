package com.example.golocal.AsyncTasks;

import android.os.AsyncTask;

import com.example.golocal.MapAutocompleteProvider;
import com.example.golocal.fragments.MapFragment;
import com.example.golocal.models.AutocompleteResultDataModel;
import com.example.golocal.models.BusinessDataModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchAsyncCall extends AsyncTask<String, Void, String> {
    private static final String SEARCH_URL = "https://api.foursquare.com/v3/places/search?query=";
    private static final int CAMERA_ZOOM = 17;
    private final BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

    private OkHttpClient client = new OkHttpClient();
    private String searchText;
    private String userLocation;
    private String apiKey;
    private GoogleMap map;
    private HashMap<Marker, BusinessDataModel> queryResultBusinesses = new HashMap<>();
    private MapFragment mapFragment;
    private Function<String, Void> postExecuteMethod;

    public SearchAsyncCall(Function<String, Void> postExecuteMethod) {
        this.postExecuteMethod = postExecuteMethod;
    }

    @Override
    protected String doInBackground(String... params) {
        searchText = params[0];
        userLocation = params[1];
        apiKey = params[2];

        String results = makeApiRequest(SEARCH_URL + params[0] + "&ll=" + userLocation + "&exclude_all_chains=true");
        return results;
    }

    protected void onPostExecute(String results) {
        try {
            queryResultsFromJson(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String makeApiRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", apiKey)
                .build();

        String results;

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return results;
    }

    public void queryResultsFromJson(String data) throws JSONException {
        JSONObject queryResponse = new JSONObject(data);
        LatLng mapCenter = null;
        JSONArray results = queryResponse.getJSONArray("results");
        queryResultBusinesses.clear();
        for (int i = 0; i < results.length(); i++) {
            JSONObject business = results.getJSONObject(i);
            LatLng markerPosition = createBusinessModelFromJson(business);
            if (i == 0) {
                mapCenter = markerPosition;
            }
        }
        if (mapCenter != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, CAMERA_ZOOM));
        }

        // set the results hashmap back in the mapFragment
        mapFragment.queryResultBusinesses.clear();
        mapFragment.queryResultBusinesses.putAll(this.queryResultBusinesses);
    }

    public LatLng createBusinessModelFromJson(JSONObject business) throws JSONException {
        String address = business.getJSONObject("location").getString("address");
        String foursquareId = business.getString("fsq_id");
        String latitude = business.getJSONObject("geocodes").getJSONObject("main").getString("latitude");
        String longitude = business.getJSONObject("geocodes").getJSONObject("main").getString("longitude");
        LatLng markerPosition = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        String businessName = business.getString("name");
        BusinessDataModel currentBusiness = new BusinessDataModel();
        currentBusiness.setName(businessName);
        currentBusiness.setAddress(address);
        currentBusiness.setFoursquareId(foursquareId);
        addMapMarker(markerPosition, businessName, address, currentBusiness);
        return markerPosition;
    }

    public void addMapMarker(LatLng markerPosition, String businessName, String address, BusinessDataModel currentBusiness) {
        Marker mapMarker = map.addMarker(new MarkerOptions()
                .position(markerPosition)
                .title(businessName)
                .snippet(address)
                .icon(defaultMarker));
        queryResultBusinesses.put(mapMarker, currentBusiness);
    }

    public void setMapFragment(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public void setGoogleMap(GoogleMap map) {
        this.map = map;
    }
}
