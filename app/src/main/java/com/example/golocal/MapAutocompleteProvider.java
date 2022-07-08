package com.example.golocal;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.golocal.models.AutocompleteResultDataModel;
import com.example.golocal.models.BusinessDataModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MapAutocompleteProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.golocal.MapAutocompleteProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/id/";
    static final Uri CONTENT_URI = Uri.parse(URL);
    private final String TAG = "MapAutocompleteProvider";

    @Override
    public boolean onCreate() {
        Log.e("mapautocomplete", "create");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // get parse object associated with that object id
        // String objectId = uri.getLastPathSegment();
        String objectId = selectionArgs[0];
        Log.e("MapAutocomplete", objectId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AutocompleteResults");
        query.whereEqualTo("queryText", objectId);
        ArrayList<BusinessDataModel> resultBusinesses = new ArrayList<>();

        MatrixCursor cursor = new MatrixCursor(new String[] { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1 });
        /*
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if (objects.size() > 0) {
                        AutocompleteResultDataModel results = (AutocompleteResultDataModel) objects.get(0);
                        resultBusinesses.addAll(results.getResultBusinesses());
                        if (resultBusinesses.size() > 0) {
                            for (int i = 0; i < resultBusinesses.size(); i++) {
                                BusinessDataModel searchSuggestion = resultBusinesses.get(i);
                                cursor.newRow()
                                        .add(BaseColumns._ID, searchSuggestion.getObjectId())
                                        .add(SearchManager.SUGGEST_COLUMN_TEXT_1, searchSuggestion.getName());
                                Log.e("MapAutocomplete", searchSuggestion.getName());
                            }
                        }
                    }
                } else{
                    Log.e(TAG, "error retrieving parse object", e);
                }
            }
        }); */
        Log.e("cursor", String.valueOf(cursor.getCount()));
        AutocompleteResultDataModel results = null;
        while (results == null) {
            try {
                Log.e("hello", "hello");
                results = (AutocompleteResultDataModel) query.find().get(0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        resultBusinesses.addAll(results.getResultBusinesses());
        if (resultBusinesses.size() > 0) {
            for (int i = 0; i < resultBusinesses.size(); i++) {
                BusinessDataModel searchSuggestion = resultBusinesses.get(i);
                cursor.newRow()
                        .add(BaseColumns._ID, i)
                        .add(SearchManager.SUGGEST_COLUMN_TEXT_1, searchSuggestion.getName());
                Log.e("MapAutocomplete", searchSuggestion.getName());
            }
        }
        Log.e("mapautocomplete", "returning");
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
