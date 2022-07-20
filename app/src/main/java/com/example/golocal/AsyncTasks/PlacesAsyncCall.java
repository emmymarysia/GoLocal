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
import java.util.function.Function;

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
    private Function<String, Void> postExecuteMethod;
    private String requestUrl;

    public PlacesAsyncCall(Function<String, Void> postExecuteMethod) {
        this.postExecuteMethod = postExecuteMethod;
    }

    @Override
    protected String doInBackground(String... params) {
        requestUrl = params[0];
        apiKey = params[1];

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
        postExecuteMethod.apply(results);
    }
}
