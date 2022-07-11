package com.example.golocal;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.golocal.AsyncTasks.SearchAndAutocompleteAPICall;
import com.example.golocal.models.BusinessDataModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapAutocompleteProvider extends ContentProvider {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String searchText = selectionArgs[0];
        if (searchText.length() < 3) {
            return null;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AutocompleteResults");
        query.whereEqualTo("queryText", searchText);
        ArrayList<BusinessDataModel> resultBusinesses = new ArrayList<>();
        SearchAndAutocompleteAPICall call = new SearchAndAutocompleteAPICall();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        List<String> providers = locationManager.getProviders(true);
        String userLocation = "";
        for (int i = providers.size()-1; i >= 0; i--) {
            Location currentLocation = locationManager.getLastKnownLocation(providers.get(i));
            if (currentLocation != null) {
                Double latitude = currentLocation.getLatitude();
                Double longitude = currentLocation.getLongitude();
                userLocation = df.format(latitude) + "%2C" + df.format(longitude);
            }
        }
        call.execute(searchText, userLocation, getContext().getResources().getString(R.string.foursquare_api_key), "autocomplete");
        MatrixCursor cursor = new MatrixCursor(new String[] { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID});
        while (call.autocompleteResults.getResultBusinesses() == null) {
            continue;
        }
        resultBusinesses.addAll(call.autocompleteResults.getResultBusinesses());
        if (resultBusinesses.size() > 0) {
            for (int i = 0; i < resultBusinesses.size(); i++) {
                BusinessDataModel searchSuggestion = resultBusinesses.get(i);
                String id = searchSuggestion.getFoursquareId();
                cursor.newRow()
                        .add(BaseColumns._ID, i)
                        .add(SearchManager.SUGGEST_COLUMN_TEXT_1, searchSuggestion.getName())
                        .add(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, id);
            }
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
