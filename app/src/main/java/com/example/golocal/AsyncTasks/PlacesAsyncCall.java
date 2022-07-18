package com.example.golocal.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.golocal.R;
import com.example.golocal.fragments.MapFragment;
import com.example.golocal.models.BusinessDataModel;
import com.google.android.gms.maps.CameraUpdate;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlacesAsyncCall extends AsyncTask<String, Void, String> {

    private final String PLACES_URL = "https://api.foursquare.com/v3/places/";
    public static final String FROM_MAP_FRAGMENT = "fromMapFragment";
    public static final String FROM_DETAIL_FRAGMENT = "fromDetailFragment";

    private OkHttpClient client = new OkHttpClient();
    private String foursquareId;
    private String apiKey;
    private String requestType;
    private MapFragment mapFragment;
    private TextView tvBusinessDescription;
    private ImageView ivBusinessImage;
    private int screenWidth;
    private Context context;

    @Override
    protected String doInBackground(String... params) {

        foursquareId = params[0];
        apiKey = params[1];
        requestType = params[2];

        String requestUrl;
        if (requestType.equals(FROM_DETAIL_FRAGMENT)) {
            requestUrl = PLACES_URL + foursquareId + "?fields=description,photos";
        } else {
            requestUrl = PLACES_URL + foursquareId;
        }
        Request request = new Request.Builder()
                .url(requestUrl)
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

    protected void onPostExecute(String results) {
        try {
            if (requestType.equals(FROM_MAP_FRAGMENT)) {
                postExecuteFromMapFragment(results);
            } else if (requestType.equals(FROM_DETAIL_FRAGMENT)) {
                setFields(results);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void postExecuteFromMapFragment(String results) throws JSONException {
        JSONObject jsonResults = new JSONObject(results);
        String name = jsonResults.getString("name");
        String address = jsonResults.getJSONObject("location").getString("address");
        Double latitude = Double.parseDouble(jsonResults.getJSONObject("geocodes").getJSONObject("main").getString("latitude"));
        Double longitude = Double.parseDouble(jsonResults.getJSONObject("geocodes").getJSONObject("main").getString("longitude"));
        LatLng businessLocation = new LatLng(latitude, longitude);
        BusinessDataModel businessDataModel = new BusinessDataModel();
        businessDataModel.setName(name);
        businessDataModel.setAddress(address);
        mapFragment.addMarker(businessLocation, businessDataModel);
    }

    public void setMapFragment(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public void setViewFields(TextView tvBusinessDescription, ImageView ivBusinessImage, int screenWidth, Context context) {
        this.tvBusinessDescription = tvBusinessDescription;
        this.ivBusinessImage = ivBusinessImage;
        this.screenWidth = screenWidth;
        this.context = context;
    }

    private void setFields(String data) throws JSONException {
        JSONObject queryResponse = new JSONObject(data);
        String businessDescription = queryResponse.optString("description");
        if (businessDescription != null) {
            tvBusinessDescription.setText(businessDescription);
        } else {
            tvBusinessDescription.setVisibility(View.GONE);
        }
        JSONArray photos = queryResponse.getJSONArray("photos");
        if (photos.getJSONObject(0) != null) {
            JSONObject businessPhoto = photos.getJSONObject(0);
            String prefix = businessPhoto.getString("prefix");
            String suffix = businessPhoto.getString("suffix");
            String dimensions = Integer.valueOf(screenWidth) + "x" + Integer.valueOf(screenWidth);
            String imageUrl = prefix + dimensions + suffix;
            Glide.with(context).load(imageUrl).override(screenWidth, screenWidth).into(ivBusinessImage);
        } else {
            ivBusinessImage.setVisibility(View.GONE);
        }
    }
}
