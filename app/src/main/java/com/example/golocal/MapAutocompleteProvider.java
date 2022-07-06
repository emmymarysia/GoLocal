package com.example.golocal;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.golocal.models.AutocompleteResultDataModel;
import com.example.golocal.models.BusinessDataModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class MapAutocompleteProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.golocal.MapAutocompleteProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/id/";
    static final Uri CONTENT_URI = Uri.parse(URL);
    private final String TAG = "MapAutocompleteProvider";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // get parse object associated with that object id
        String objectId = uri.getLastPathSegment();
        Log.e("MapAutocomplete", objectId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AutocompleteResults");
        query.whereEqualTo("queryText", objectId);
        ArrayList<BusinessDataModel> resultBusinesses = new ArrayList<>();

        query.findInBackground((objects, e) -> {
            if(e == null){
                if (objects.size() > 0) {
                    AutocompleteResultDataModel results = (AutocompleteResultDataModel) objects.get(0);
                    resultBusinesses.addAll(results.getResultBusinesses());
                }
            }else{
                Log.e(TAG, "error retrieving parse object", e);
            }
        });
        // somehow set the array as the search results
        if (resultBusinesses != null) {
            MatrixCursor cursor = new MatrixCursor(new String[] { "_ID", "SUGGEST_COLUMN_TEXT_1" });
            for (int i = 0; i < resultBusinesses.size(); i++) {
                BusinessDataModel searchSuggestion = resultBusinesses.get(i);
                cursor.newRow()
                        .add("_ID", searchSuggestion.getObjectId())
                        .add("SUGGEST_COLUMN_TEXT_1", searchSuggestion.getName());
            }
            return cursor;
        }
        return null;
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
