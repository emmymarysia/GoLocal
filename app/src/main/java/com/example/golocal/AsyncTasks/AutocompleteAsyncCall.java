package com.example.golocal.AsyncTasks;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.golocal.MapAutocompleteProvider;
import com.example.golocal.R;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AutocompleteAsyncCall extends AsyncTask<String, Void, String> {

    private static final String AUTOCOMPLETE_URL = "https://api.foursquare.com/v3/autocomplete?query=";

    private OkHttpClient client = new OkHttpClient();
    private String searchText;
    private String userLocation;
    private String apiKey;
    private String requestType;
    private GoogleMap map;
    private HashMap<Marker, BusinessDataModel> queryResultBusinesses = new HashMap<>();
    private MapFragment mapFragment;
    private MapAutocompleteProvider provider = new MapAutocompleteProvider();
    public AutocompleteResultDataModel autocompleteResults = new AutocompleteResultDataModel();

    @Override
    protected String doInBackground(String... params) {
        searchText = params[0];
        userLocation = params[1];
        apiKey = params[2];

        String results = makeApiRequest(AUTOCOMPLETE_URL + searchText + "&ll=" + userLocation);
        return results;
    }

    protected void onPostExecute(String results) {
        try {
            queryAutocompleteResultsFromJson(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String makeApiRequest(String url) {
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

    public HashMap<Marker, BusinessDataModel> getQueryResultBusinesses() {
        return queryResultBusinesses;
    }

    public void queryAutocompleteResultsFromJson(String data) throws JSONException {
        if (data == null) {
            return;
        }
        JSONObject autocompleteResponse = new JSONObject(data);
        JSONArray results = autocompleteResponse.getJSONArray("results");
        ArrayList<BusinessDataModel> resultBusinesses = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            BusinessDataModel business = new BusinessDataModel();
            JSONObject recommendation = results.getJSONObject(i);
            JSONObject text = recommendation.getJSONObject("text");
            String primaryText = text.getString("primary");
            business.setName(primaryText);
            resultBusinesses.add(business);
        }
        autocompleteResults.setResultBusinesses(resultBusinesses);
        autocompleteResults.setQueryText(searchText);
    }
}